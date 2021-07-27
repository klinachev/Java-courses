package info.kgeorgiy.ja.klinachev.crawler;

import info.kgeorgiy.java.advanced.crawler.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class WebCrawler implements AdvancedCrawler {
    private final Downloader downloader;
    private final ExecutorService downloaderService;
    private final ExecutorService extractorsService;
    private final HostLimiter hostLimiter;

    public WebCrawler(final Downloader downloader, final int downloaders, final int extractors, final int perHost) {
        this.downloader = downloader;
        this.downloaderService = Executors.newFixedThreadPool(downloaders);
        this.extractorsService = Executors.newFixedThreadPool(extractors);
        this.hostLimiter = new HostLimiter(perHost);
    }

    @Override
    public Result download(final String url, final int depth) {
        return new ResultBuilder(depth).evaluateResult(url);
    }

    @Override
    public Result download(final String url, final int depth, final List<String> hosts) {
        return new ResultBuilder(depth, hosts).evaluateResult(url);
    }

    private class ResultBuilder {
        private final Set<String> hosts;
        private final Set<String> visited = ConcurrentHashMap.newKeySet();
        private final Set<String> nextTaskUrls = ConcurrentHashMap.newKeySet();
        private final ConcurrentMap<String, IOException> errors = new ConcurrentHashMap<>();
        private final CountDownLatch currentLevel;
        private final AtomicInteger tasksOnLevel = new AtomicInteger(0);

        private ResultBuilder(final int maxLevel) {
            currentLevel = new CountDownLatch(maxLevel);
            hosts = null;
        }

        private ResultBuilder(final int maxLevel, final List<String> hosts) {
            currentLevel = new CountDownLatch(maxLevel);
            this.hosts = ConcurrentHashMap.newKeySet();
            this.hosts.addAll(hosts);
        }

        private String getHost(final String url) {
            final String host;
            try {
                host = URLUtils.getHost(url);
                if (hosts != null && !hosts.contains(host)) {
                    return null;
                }
            } catch (final MalformedURLException e) {
                errors.put(url, e);
                return null;
            }
            if (!visited.add(url)) {
                return null;
            }
            tasksOnLevel.incrementAndGet();
            return host;
        }

        private List<Runnable> getTasks() {
            final List<Runnable> runnables = new ArrayList<>();
            nextTaskUrls.forEach(url -> {
                final String host = getHost(url);
                if (host != null) {
                    runnables.add(() -> downloaderService.submit(downloaderTask(host, url)));
                }
            });
            return runnables;
        }

        private Runnable downloaderTask(final String host, final String url) {
            return () -> {
                try {
                    hostLimiter.acquire(host);
                    final Document document = downloader.download(url);
                    hostLimiter.release(host);
                    if (currentLevel.getCount() > 1) {
                        extractorsService.submit(extractTask(document, url));
                    } else {
                        afterTask();
                    }
                } catch (final IOException e) {
                    hostLimiter.release(host);
                    errors.put(url, e);
                    afterTask();
                } catch (final InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            };
        }

        private Runnable extractTask(final Document document, final String url) {
            return () -> {
                try {
                    final List<String> links = document.extractLinks();
                    nextTaskUrls.addAll(links);
                } catch (final IOException e) {
                    errors.put(url, e);
                } finally {
                    afterTask();
                }
            };
        }

        private void afterTask() {
            if (tasksOnLevel.decrementAndGet() == 0) {
                goNextLevel();
            }
        }

        private void goNextLevel() {
            currentLevel.countDown();
            final List<Runnable> runnables = getTasks();
            nextTaskUrls.clear();
            if (tasksOnLevel.get() == 0) {
                while (currentLevel.getCount() > 0) {
                    currentLevel.countDown();
                }
            }
            runnables.forEach(Runnable::run);
        }

        private Result getResult() {
            return new Result(visited.stream()
                    .filter(s -> !errors.containsKey(s)).collect(Collectors.toList()),
                    errors);
        }

        private Result evaluateResult(final String url) {
            final String host = getHost(url);
            if (host == null) {
                return getResult();
            }
            downloaderService.submit(downloaderTask(host, url));
            try {
                currentLevel.await();
            } catch (final InterruptedException e) {
                return null;
            }
            return getResult();
        }
    }


    private static class HostLimiter {
        private final int perHost;
        private final ConcurrentMap<String, Semaphore> hostMap = new ConcurrentHashMap<>();

        private HostLimiter(final int available) {
            this.perHost = available;
        }

        private synchronized Semaphore createAndAcquire(final String host) throws InterruptedException {
            final Semaphore semaphore = hostMap.computeIfAbsent(host, s -> new Semaphore(perHost));
            semaphore.acquire();
            return semaphore;
        }

        private void acquire(final String host) throws InterruptedException {
            Semaphore semaphore = createAndAcquire(host);
            while (hostMap.get(host) != semaphore) {
                semaphore = createAndAcquire(host);
            }
        }

        private void release(final String host) {
            hostMap.get(host).release();
            hostMap.computeIfPresent(host, (s, value) -> value.availablePermits() == perHost ? null : value);
        }
    }

    @Override
    public void close() {
        downloaderService.shutdownNow();
        extractorsService.shutdownNow();
        join(downloaderService);
        join(extractorsService);
    }

    private static void join(final ExecutorService executor) {
        while (!executor.isTerminated()) {
            try {
                executor.awaitTermination(10L, TimeUnit.SECONDS);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static int getArg(final String[] args, final int ind, final int defaultValue) {
        return ind < args.length ? Integer.parseInt(args[ind]) : defaultValue;
    }

    public static void main(final String[] args) {
        if (args == null || args.length < 1 || args.length > 5) {
            System.out.println("Usage: WebCrawler url [depth [downloaders [extractors [perHost]]]]");
            return;
        }
        final String url = args[0];
        final int depth = getArg(args, 1, 1);
        final int downloaders = getArg(args, 2, 4);
        final int extractors = getArg(args, 3, 4);
        final int perHost = getArg(args, 4, 8);
        try (final Crawler crawler = new WebCrawler(
                new CachingDownloader(Paths.get(url)), downloaders, extractors, perHost)) {
            final Result result = crawler.download(url, depth);
            System.out.println("Downloaded:");
            result.getDownloaded().forEach(System.out::println);
            System.out.println("Errors:");
            result.getErrors().entrySet().stream()
                    .map(entry -> entry.getValue() + " " + entry.getKey()).forEach(System.out::println);
        } catch (final IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
