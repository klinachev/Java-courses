package info.kgeorgiy.ja.klinachev.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;
import info.kgeorgiy.java.advanced.hello.Util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HelloUDPNonblockingServer implements HelloServer {
    private ExecutorService executor;
    private Selector selector;
    private DatagramChannel datagramChannel;
    private Queue<ByteBuffer> freeBuffers;
    private Queue<Data> buffersToSend;

    private static class Data {
        ByteBuffer buffer;
        SocketAddress address;

        public Data(final ByteBuffer buffer, final SocketAddress address) {
            this.buffer = buffer;
            this.address = address;
        }
    }

    private void read() {
        final ByteBuffer byteBuffer = freeBuffers.remove();
        final SocketAddress address = Utils.read(datagramChannel, byteBuffer);
        if (address != null) {
            executor.submit(() -> {
                final String message = HelloUDPServer.answer(
                        StandardCharsets.UTF_8.decode(byteBuffer).toString());
                byteBuffer.clear();
                byteBuffer.put(message.getBytes(StandardCharsets.UTF_8));
                buffersToSend.add(new Data(byteBuffer, address));
                selector.wakeup();
            });
        }
    }

    private void send() {
        final Data data = buffersToSend.remove();
        Utils.send(datagramChannel, data.buffer, data.address);
        freeBuffers.add(data.buffer);
    }

    private void threadTask() {
        final SelectionKey selectionKey = selector.keys().iterator().next();
        while (selector.isOpen()) {
            try {
                final int added = selector.select();
                if (added > 0) {
                    try {
                        if (Utils.isWritable(selectionKey) && !buffersToSend.isEmpty()) {
                            send();
                        }
                        if (Utils.isReadable(selectionKey) && !freeBuffers.isEmpty()) {
                            read();
                        }
                    } finally {
                        selector.selectedKeys().remove(selectionKey);
                    }
                }
            } catch (final IOException e) {
                System.err.println("Select failed: " + e.getMessage());
            }
        }
    }

    @Override
    public void start(final int port, final int threads) {
        final int bufferSize;
        try {
            selector = Selector.open();
            datagramChannel = Utils.createChannel(new InetSocketAddress(port));
            datagramChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            bufferSize = datagramChannel.getOption(StandardSocketOptions.SO_SNDBUF);
        } catch (final IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
            close();
            return;
        }
        freeBuffers = Stream.generate(() -> ByteBuffer.allocate(bufferSize)).limit(2 * threads)
                .collect(Collectors.toCollection(() -> new ArrayBlockingQueue<>(2 * threads)));
        buffersToSend = new ArrayBlockingQueue<>(2 * threads);
        executor = Executors.newFixedThreadPool(threads == 1 ? 2 : threads);

        executor.submit(this::threadTask);
    }

    @Override
    public void close() {
        Utils.close(selector);
        Utils.close(datagramChannel);
        Utils.shutdownAndAwait(executor);
        Utils.clear(freeBuffers);
        Utils.clear(buffersToSend);
    }


    public static void main(final String[] args) {
        HelloUDPServer.main(HelloUDPNonblockingServer::new, args);
    }
}
