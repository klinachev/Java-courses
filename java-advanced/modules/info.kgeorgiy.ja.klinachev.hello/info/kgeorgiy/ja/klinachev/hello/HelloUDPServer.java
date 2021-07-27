package info.kgeorgiy.ja.klinachev.hello;

import info.kgeorgiy.java.advanced.hello.HelloServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class HelloUDPServer implements HelloServer {
    private ExecutorService executor;
    private DatagramSocket socket;


    static String answer(final String s) {
        return "Hello, " + s;
    }

    private void threadTask() {
        final byte[] data;
        try {
            data = Utils.responseData(socket);
        } catch (final SocketException e) {
            System.err.println("Failed to create packet: " + e.getMessage());
            return;
        }
        final DatagramPacket packet = new DatagramPacket(data, data.length);
        while (!socket.isClosed()) {
            try {
                packet.setData(data);
                socket.receive(packet);
                    final String answer = answer(Utils.packetString(packet));
                    Utils.setMessage(packet, answer);
                    try {
                        socket.send(packet);
                    } catch (final IOException e) {
                        System.err.println("Failed to send message: " + e.getMessage());
                    }
            } catch (final IOException e) {
                System.err.println("Failed to receive message: " + e.getMessage());
            }
        }
    }

    @Override
    public void start(final int port, final int threads) {
        try {
            socket = new DatagramSocket(port);
        } catch (final SocketException e) {
            System.err.println("Failed to create socket: " + e.getMessage());
            return;
        }
        executor = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; ++i) {
            executor.submit(this::threadTask);
        }
    }

    @Override
    public void close() {
        Utils.close(socket);
        Utils.shutdownAndAwait(executor);
    }

    static void main(final Supplier<HelloServer> serverSupplier, final String[] args) {
        if (args == null || args.length != 2) {
            System.out.println("Usage: java HelloUDPServer <port> <threads>");
            return;
        }
        try {
            final int port = Integer.parseInt(args[0]);
            final int threads = Integer.parseInt(args[1]);
            try (final HelloServer server = serverSupplier.get()) {
                server.start(port, threads);
                TimeUnit.MINUTES.sleep(60);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("Failed to parse argument: " + e.getMessage());
        }
    }

    public static void main(final String[] args) {
        main(HelloUDPServer::new, args);
    }
}
