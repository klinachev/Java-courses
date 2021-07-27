package info.kgeorgiy.ja.klinachev.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;
import info.kgeorgiy.java.advanced.hello.Util;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class HelloUDPClient implements HelloClient {

    public static final int SO_TIMEOUT = 100;

    @Override
    public void run(
            final String host,
            final int port,
            final String prefix,
            final int threads,
            final int requests
    ) {
        final ExecutorService executorService = Executors.newFixedThreadPool(threads);
        final InetSocketAddress address;
        try {
            address = new InetSocketAddress(InetAddress.getByName(host), port);
        } catch (final UnknownHostException e) {
            System.err.println("Unknown host: " + e.getMessage());
            return;
        }

        IntStream.range(0, threads).forEach(threadNumber -> executorService.submit(() -> {
            try (final DatagramSocket socket = new DatagramSocket()) {
                socket.setSoTimeout(SO_TIMEOUT);
                final byte[] responseData = Utils.responseData(socket);
                final DatagramPacket packet = new DatagramPacket(responseData, responseData.length, address);
                for (int i = 0; i < requests; i++) {
                    final String message = Utils.createMessage(prefix, threadNumber, i);
                    final byte[] messageData = message.getBytes(StandardCharsets.UTF_8);
                    System.out.println("Request: " + message);
                    while (!Thread.interrupted()) {
                        packet.setData(messageData);
                        socket.send(packet);
                        try {
                            packet.setData(responseData);
                            socket.receive(packet);
                            final String responseString = Utils.packetString(packet);
                            if (Utils.checkMessage(responseString, threadNumber, i)) {
                                System.out.println("Received: " + responseString);
                                break;
                            }
                        } catch (final IOException ignored) {
                        }
                    }
                }
            } catch (final SocketException e) {
                System.err.println("Failed to configure socket: " + e.getMessage());
            } catch (final IOException e) {
                System.err.println("Failed to send request: " + e.getMessage());
            }
        }));
        Utils.shutdownAndAwait(executorService);
    }


    static void main(final Supplier<HelloClient> clientSupplier, final String[] args) {
        if (args == null || args.length != 5) {
            System.out.println("Usage: java HelloUDPClient <host> <port> <prefix> <threads> <perThread>");
            return;
        }
        try {
            final String host = args[0];
            final String prefix = args[2];
            final int port = Integer.parseInt(args[1]);
            final int threadsCount = Integer.parseInt(args[3]);
            final int queriesPerThread = Integer.parseInt(args[4]);
            try {
                final HelloClient client = clientSupplier.get();
                client.run(host, port, prefix, threadsCount, queriesPerThread);
            } catch (final RuntimeException e) {
                System.err.println("Failed to start: " + e.getMessage());
            }
        } catch (final NumberFormatException e) {
            System.err.println("Failed to parse argument: " + e.getMessage());
        }
    }

    public static void main(final String[] args) {
        main(HelloUDPClient::new, args);
    }
}
