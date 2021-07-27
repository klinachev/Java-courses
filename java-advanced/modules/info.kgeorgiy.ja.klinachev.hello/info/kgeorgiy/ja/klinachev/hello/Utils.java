package info.kgeorgiy.ja.klinachev.hello;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Utils {
    private static int firstDigit(final String s, int start) {
        while (s.length() > start && !Character.isDigit(s.charAt(start))) {
            start++;
        }
        return start;
    }

    private static int firstNotDigit(final String s, int start) {
        while (s.length() > start && Character.isDigit(s.charAt(start))) {
            start++;
        }
        return start;
    }

    private static boolean equalsNumbers(final String s, final int start, final int end, int number) {
        if (start == end) {
            return false;
        }
        if (number < 10) {
            return start == end - 1 && s.charAt(start) == '0' + number;
        }
        for (int i = end - 1; i >= start; i--) {
            if (number % 10 != s.charAt(i) - '0' || number == 0) {
                return false;
            }
            number /= 10;
        }
        return true;
    }

    private static int checkNextNumber(final String s, final int begin, final int number) {
        final int start = firstDigit(s, begin);
        final int end = firstNotDigit(s, start);
        if (!equalsNumbers(s, start, end, number)) {
            return -1;
        }
        return end;
    }

    static boolean checkMessage(final String s, final int threadId, final int requestId) {
        int position = checkNextNumber(s, 0, threadId);
        if (position == -1) {
            return false;
        }
        position = checkNextNumber(s, position, requestId);
        return position != -1 && firstDigit(s, position) == s.length();
    }

    static String createMessage(final String prefix, final int threadId, final int request) {
        return prefix + threadId + "_" + request;
    }

    static String packetString(final DatagramPacket packet) {
        return new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
    }

    static byte[] responseData(final DatagramSocket socket) throws SocketException {
        return new byte[socket.getReceiveBufferSize()];
    }

    static void setMessage(final DatagramPacket packet, final String message) {
        packet.setData(message.getBytes(StandardCharsets.UTF_8));
    }

    // :NOTE: Не дождались
    static void shutdownAndAwait(final ExecutorService pool) {
        if (pool == null) {
            return;
        }
        pool.shutdown();
        try {
            pool.awaitTermination(60, TimeUnit.SECONDS);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        pool.shutdownNow();
        while (!pool.isTerminated()) {
            try {
                System.err.println("Pool do not terminate");
                pool.awaitTermination(1, TimeUnit.HOURS);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static void close(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (final IOException e) {
                System.err.println("Close failed: " + e.getMessage());
            }
        }
    }

    static void clear(final Collection<?> collection) {
        if (collection != null) {
            collection.clear();
        }
    }


    static SocketAddress read(final DatagramChannel channel, final ByteBuffer buffer) {
        buffer.clear();
        try {
            final SocketAddress address = channel.receive(buffer);
            buffer.flip();
            return address;
        } catch (final IOException e) {
            return null;
        }

    }

    static boolean send(final DatagramChannel channel, final ByteBuffer buffer, final SocketAddress address) {
        buffer.flip();
        try {
            return channel.send(buffer, address) > 0;
        } catch (final IOException e) {
            return false;
        }
    }

    static DatagramChannel createChannel(final SocketAddress address) throws IOException {
        final DatagramChannel channel = DatagramChannel.open();
        channel.configureBlocking(false);
        channel.bind(address);
        return channel;
    }

    static boolean isReadable(final SelectionKey selectionKey) {
        return selectionKey.isValid() && selectionKey.isReadable()
                && (selectionKey.interestOps() & SelectionKey.OP_READ) != 0;
    }

    static boolean isWritable(final SelectionKey selectionKey) {
        return selectionKey.isValid() && selectionKey.isWritable()
                && (selectionKey.interestOps() & SelectionKey.OP_WRITE) != 0;
    }
}
