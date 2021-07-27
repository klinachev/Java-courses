package info.kgeorgiy.ja.klinachev.concurrent;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;

public class ParallelMapperImpl implements ParallelMapper {
    private final List<Thread> threads;
    private final SynchronizedQueue<Task<?, ?>> tasks = new SynchronizedQueue<>();

    public ParallelMapperImpl(final int threadsCount) {
        threads = new ArrayList<>(threadsCount);
        final Runnable treadTask = () -> {
            try {
                while (!Thread.interrupted()) {
                    final Runnable runnable;
                    final Task<?, ?> task;
                    synchronized (tasks) {
                        task = tasks.peek();
                        runnable = task.nextTask();
                    }
                    runnable.run();
                    task.finish();
                }
            } catch (final InterruptedException e) {
//                    e.printStackTrace();
            } finally {
                Thread.currentThread().interrupt();
            }
        };
        for (int i = 0; i < threadsCount; ++i) {
            // :NOTE: no need to create a runnable every time
            threads.add(new Thread(treadTask));
        }
        threads.forEach(Thread::start);
    }

    private static class SynchronizedQueue<T> {
        private final Queue<T> data = new ArrayDeque<>();

        public synchronized void add(final T val) {
            data.add(val);
            notifyAll();
        }

        public synchronized T peek() throws InterruptedException {
            while (data.isEmpty()) {
                wait();
            }
            return data.peek();
        }

        public synchronized void poll() {
            data.poll();
        }
    }

    @Override
    public <T, R> List<R> map(
            final Function<? super T, ? extends R> f,
            final List<? extends T> args
    ) throws InterruptedException {
        final Task<T, R> task = new Task<>(f, args);
        tasks.add(task);
        return task.getResults();
    }

    @Override
    public void close() {
        // :NOTE: actually close them
        try {
            IterativeParallelism.close(threads);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class Task<T, S> {
        private final Queue<Runnable> smallTasks = new ArrayDeque<>();
        private final List<S> results;
        private int finished = 0;

        public Task(final Function<? super T, ? extends S> function,
                    final List<? extends T> values) {
            results = new ArrayList<>(Collections.nCopies(values.size(), null));
            for (int i = 0; i < values.size(); ++i) {
                final int pos = i;
                smallTasks.add(() -> results.set(pos, function.apply(values.get(pos))));
            }
        }

        public synchronized Runnable nextTask() {
            final Runnable runnable = smallTasks.poll();
            if (smallTasks.isEmpty()) {
                tasks.poll();
            }
            return runnable;
        }

        public synchronized void finish() {
            finished++;
            if (finished == results.size()) {
                notify();
            }
        }

        public synchronized List<S> getResults() throws InterruptedException {
            while (finished != results.size()) {
                wait();
            }
            return results;
        }
    }

}
