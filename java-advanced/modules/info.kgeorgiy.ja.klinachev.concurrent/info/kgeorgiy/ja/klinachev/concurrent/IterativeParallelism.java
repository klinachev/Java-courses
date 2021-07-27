package info.kgeorgiy.ja.klinachev.concurrent;

import info.kgeorgiy.java.advanced.concurrent.AdvancedIP;
import info.kgeorgiy.java.advanced.concurrent.ListIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IterativeParallelism implements AdvancedIP {
    private final ParallelMapper parallelMapper;

    public IterativeParallelism() {
        parallelMapper = null;
    }

    public IterativeParallelism(final ParallelMapper mapper) {
        parallelMapper = mapper;
    }

    public IterativeParallelism(final int count) {
        parallelMapper = new ParallelMapperImpl(count);
    }

    private static <T> List<Stream<? extends T>> split(
            final int threads,
            final List<? extends T> values) {
        final List<Stream<? extends T>> lists = new ArrayList<>(threads);
        final int size = values.size() / threads;
        final int remain = values.size() % threads;
        int position = 0;
        for (int i = 0; i < threads; ++i) {
            lists.add(values
                    .subList(position, position += size + (remain > i ? 1 : 0))
                    .stream());
        }
        return lists;
    }

    private <T, R, A> R evaluate(
            final Function<? super Stream<? extends T>, A> parallelFunction,
            final Function<? super Stream<A>, ? extends R> resultFunction,
            final List<? extends T> values,
            final int threadsCount
    ) throws InterruptedException {
        final List<Stream<? extends T>> args = split(Math.min(threadsCount, values.size()), values);
        final Stream<A> stream = parallelMapper != null
                        ? parallelMapper.map(parallelFunction, args).stream()
                        : defaultEvaluate(parallelFunction, args).stream();
        return resultFunction.apply(stream);
    }

    private static <T, R> List<R> defaultEvaluate(
            final Function<? super Stream<? extends T>, ? extends R> function1,
            final List<Stream<? extends T>> values
    ) throws InterruptedException {
        final List<R> results = new ArrayList<>(Collections.nCopies(values.size(), null));
        final List<Thread> threads = new ArrayList<>(values.size());
        for (int i = 0; i < values.size(); ++i) {
            final int pos = i;
            final Thread thread = new Thread(() -> results.set(pos, function1.apply(values.get(pos))));
            threads.add(thread);
            thread.start();
        }
        for (final Thread thread : threads) {
            try {
                thread.join();
            } catch (final InterruptedException exception) {
                // :NOTE: you still need to join them all
                try {
                    close(threads);
                } catch (final InterruptedException e) {
                    exception.addSuppressed(e);
                    throw exception;
                }
            }
        }
        return results;
    }

    static void close(final List<Thread> threads) throws InterruptedException {
        threads.forEach(Thread::interrupt);
        InterruptedException exception = null;
        for (final Thread thread : threads) {
            while (thread.isAlive()) {
                try {
                    thread.join();
                } catch (final InterruptedException e) {
                    if (exception == null) {
                        exception = e;
                    } else {
                        exception.addSuppressed(e);
                    }
                }
            }
        }
        if (exception != null) {
            throw exception;
        }
    }

    // :NOTE: try to make everything into one expression
    @Override
    public <T> T maximum(
            final int threads,
            final List<? extends T> values,
            final Comparator<? super T> comparator
    ) throws InterruptedException {
        final Function<Stream<? extends T>, T> function = stream -> stream.max(comparator).orElse(null);
        return evaluate(function, function, values, threads);
    }

    @Override
    public <T> T minimum(
            final int threads,
            final List<? extends T> values,
            final Comparator<? super T> comparator
    ) throws InterruptedException {
        return maximum(threads, values, comparator.reversed());
    }

    @Override
    public <T> boolean all(
            final int threads,
            final List<? extends T> values,
            final Predicate<? super T> predicate
    ) throws InterruptedException {
        return evaluate(stream -> stream.allMatch(predicate),
                stream -> stream.allMatch(Boolean::booleanValue),
                values, threads);
    }

    @Override
    public <T> boolean any(
            final int threads,
            final List<? extends T> values,
            final Predicate<? super T> predicate
    ) throws InterruptedException {
        return !all(threads, values, predicate.negate());
    }

    @Override
    public String join(final int threads, final List<?> values) throws InterruptedException {
        final Function<Stream<?>, String> function = stream -> stream.map(Object::toString)
                .collect(Collectors.joining());
        return evaluate(function, function, values, threads);
    }

    private static <T> Function<Stream<? extends Stream<? extends T>>, List<T>> collectToListFunction() {
        return stream -> stream.flatMap(Function.identity()).collect(Collectors.toList());
    }

    @Override
    public <T> List<T> filter(
            final int threads,
            final List<? extends T> values,
            final Predicate<? super T> predicate
    ) throws InterruptedException {
        // :NOTE-2: intermediate lists, get rid of them
        return evaluate(stream -> stream.filter(predicate),
                collectToListFunction(),
                values, threads);
    }

    @Override
    public <T, U> List<U> map(
            final int threads,
            final List<? extends T> values,
            final Function<? super T, ? extends U> function
    ) throws InterruptedException {
        return evaluate(stream -> stream.map(function),
                collectToListFunction(),
                values,
                threads);
    }

    @Override
    public <T> T reduce(
            final int threads,
            final List<T> values,
            final Monoid<T> monoid
    ) throws InterruptedException {
        return mapReduce(threads, values, Function.identity(), monoid);
    }


    private static <T, R> Function<Stream<? extends T>, R> monoidFunction(
            final Function<T, R> lift,
            final Monoid<R> monoid) {
        return stream -> stream.map(lift).reduce(monoid.getIdentity(), monoid.getOperator());
    }

    @Override
    public <T, R> R mapReduce(
            final int threads,
            final List<T> values,
            final Function<T, R> lift,
            final Monoid<R> monoid
    ) throws InterruptedException {
        return evaluate(monoidFunction(lift, monoid), monoidFunction(Function.identity(), monoid),
                values,
                threads);
    }

}
