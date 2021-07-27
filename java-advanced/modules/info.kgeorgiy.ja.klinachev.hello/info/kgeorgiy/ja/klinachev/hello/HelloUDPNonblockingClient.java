package info.kgeorgiy.ja.klinachev.hello;

import info.kgeorgiy.java.advanced.hello.HelloClient;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;


public class HelloUDPNonblockingClient implements HelloClient {
    private final static int SO_TIMEOUT = 100;

    private Selector selector;
    private String prefix;
    private int requests;
    private SocketAddress address;

    private static class Context {
        final int threadId;
        int requestId;
        ByteBuffer sendBuffer;
        ByteBuffer readBuffer;

        Context(final int threadId, final int capacity) {
            this.threadId = threadId;
            this.requestId = 0;
            sendBuffer = ByteBuffer.allocate(capacity);
            readBuffer = ByteBuffer.allocate(capacity);
        }
    }

    private void send(final SelectionKey selectionKey, final Context context) {
        final DatagramChannel datagramChannel = (DatagramChannel) selectionKey.channel();
        // :NOTE: Блокирующйи вызов?
        if (Utils.send(datagramChannel, context.sendBuffer, address)) {
            selectionKey.interestOps(SelectionKey.OP_READ);
        } else {
            selectionKey.interestOps(SelectionKey.OP_WRITE);
        }
    }

    private void createMessage(final SelectionKey selectionKey, final Context context) {
        final String request = Utils.createMessage(prefix, context.threadId, context.requestId);
        context.sendBuffer.put(request.getBytes(StandardCharsets.UTF_8));
        send(selectionKey, context);
        System.out.println("Requested: " + request);
    }

    private void write(final SelectionKey selectionKey) {
        final Context context = (Context) selectionKey.attachment();
        if (context.readBuffer.position() != 0) {
            send(selectionKey, context);
            return;
        }
        final String received = StandardCharsets.UTF_8.decode(context.readBuffer).toString();
        if (Utils.checkMessage(received, context.threadId, context.requestId)) {
            context.requestId++;
            context.sendBuffer.clear();
            context.readBuffer.clear();
            System.out.println("Received: " + received);
            if (context.requestId == requests) {
                selectionKey.cancel();
                return;
            }
            createMessage(selectionKey, context);
        } else {
            send(selectionKey, context);
        }
    }

    private void read(final SelectionKey selectionKey) {
        final Context context = (Context) selectionKey.attachment();
        final DatagramChannel datagramChannel = (DatagramChannel) selectionKey.channel();
        if (Utils.read(datagramChannel, context.readBuffer) != null) {
            selectionKey.interestOps(SelectionKey.OP_WRITE);
        }
    }

    private void mainTask() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                final int added = selector.select(SO_TIMEOUT);
                // :NOTE: selectionKeys.isEmpty()
                if (added > 0) {
                    final Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    for (final Iterator<SelectionKey> i = selectionKeys.iterator(); i.hasNext(); ) {
                        final SelectionKey selectionKey = i.next();
                        try {
                            if (Utils.isWritable(selectionKey)) {
                                write(selectionKey);
                            }
                            if (Utils.isReadable(selectionKey)) {
                                read(selectionKey);
                            }
                        } finally {
                            i.remove();
                        }
                    }
                } else {
                    final Set<SelectionKey> keys = selector.keys();
                    if (keys.isEmpty()) {
                        break;
                    }
                    for (final SelectionKey key : keys) {
                        // :NOTE: ???
                        if (key.isValid() && (key.interestOps() & SelectionKey.OP_READ) != 0) {
                            key.interestOps(SelectionKey.OP_WRITE);
                        }
                    }
                }
            } catch (final IOException e) {
                System.err.println("Select failed: " + e.getMessage());
            }
        }
    }

    @Override
    public void run(
            final String host,
            final int port,
            final String prefix,
            final int threads,
            final int requests
    ) {
        try {
            this.address = new InetSocketAddress(InetAddress.getByName(host), port);
            selector = Selector.open();
        } catch (final UnknownHostException e) {
            System.err.println("Unknown host: " + e.getMessage());
            return;
        } catch (final IOException e) {
            System.err.println("Can't open selector: " + e.getMessage());
            return;
        }
        this.requests = requests;
        this.prefix = prefix;

        final List<DatagramChannel> channelList = new ArrayList<>();
        try {
            for (int i = 0; i < threads; i++) {
                final DatagramChannel channel = Utils.createChannel(null);
                final Context context = new Context(i, channel.getOption(StandardSocketOptions.SO_RCVBUF));
                final SelectionKey selectionKey = channel.register(selector, SelectionKey.OP_WRITE, context);
                channelList.add(channel);
                createMessage(selectionKey, context);
            }
            mainTask();
        } catch (final IOException e) {
            System.err.println("Creating channels failed: " + e.getMessage());
        } finally {
            Utils.close(selector);
            channelList.forEach(Utils::close);
        }
    }

    public static void main(final String[] args) {
        HelloUDPClient.main(HelloUDPNonblockingClient::new, args);
    }
}
