package com.mylsaber.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ReactorMultiThread {
    public static void main(String[] args) throws IOException {
        Reactor reactor = new Reactor(8888);
        reactor.run();
    }

    private static class Reactor implements Runnable {
        private final Selector selector;

        public Reactor(int port) throws IOException {
            selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, new Acceptor());
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    selector.select();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        dispatch(selectionKey);
                        iterator.remove();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void dispatch(SelectionKey selectionKey) throws IOException {
            Handler attachment = (Handler) selectionKey.attachment();
            if (Objects.nonNull(attachment)) {
                attachment.handle(selectionKey.channel());
            }
        }
    }

    private interface Handler {
        void handle(SelectableChannel selectableChannel) throws IOException;
    }

    private static class Acceptor implements Handler {
        private final Handler handler = new ReadHandler();

        @Override
        public void handle(SelectableChannel selectableChannel) throws IOException {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectableChannel;
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (Objects.nonNull(socketChannel)) {
                handler.handle(socketChannel);
            }
        }
    }

    private static class ReadHandler implements Handler {
        private final ThreadPoolExecutor executor = new ThreadPoolExecutor(4,
                4,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        @Override
        public void handle(SelectableChannel selectableChannel) {
            executor.submit(() -> {
                System.out.println(Thread.currentThread().getName() + "：begin");
                try (SocketChannel socketChannel = (SocketChannel) selectableChannel) {
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    StringBuilder sb = new StringBuilder();
                    if (socketChannel.read(buffer) > 0) {
                        buffer.flip();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);
                        sb.append(new String(bytes));
                        buffer.clear();
                    }
                    System.out.println(sb);

                    buffer.clear();
                    buffer.put("over".getBytes(StandardCharsets.UTF_8));
                    buffer.flip();
                    socketChannel.write(buffer);
                    socketChannel.shutdownOutput();
                    System.out.println(Thread.currentThread().getName() + "：end");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
