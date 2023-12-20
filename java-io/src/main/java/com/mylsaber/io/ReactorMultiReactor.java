package com.mylsaber.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ReactorMultiReactor {
    public static void main(String[] args) throws IOException {
        Reactor reactor = new MainReactor(8888);
        reactor.run();
    }

    private static class Reactor implements Runnable {
        private final Selector selector;

        protected Reactor() throws IOException {
            selector = Selector.open();
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

        protected void dispatch(SelectionKey selectionKey) throws IOException {
            Handler attachment = (Handler) selectionKey.attachment();
            if (Objects.nonNull(attachment)) {
                attachment.handle(selectionKey.channel());
            }
        }

        protected Selector getSelector() {
            return selector;
        }
    }

    private static class MainReactor extends Reactor implements Runnable {
        public MainReactor(int port) throws IOException {
            super();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.register(getSelector(), SelectionKey.OP_ACCEPT, new Acceptor(4));
        }
    }

    private interface Handler {
        void handle(SelectableChannel selectableChannel) throws IOException;
    }

    private static class Acceptor implements Handler {
        private final int threadCount;
        private int index = 0;
        private final List<Reactor> reactors;
        private final Handler handler;

        private Acceptor(int threadCount) throws IOException {
            this.threadCount = threadCount;
            reactors = new ArrayList<>(threadCount);
            handler = new ReadHandler();
            ThreadPoolExecutor executor = new ThreadPoolExecutor(threadCount,
                    threadCount,
                    60,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(1),
                    Executors.defaultThreadFactory(),
                    new ThreadPoolExecutor.AbortPolicy());
            for (int i = 0; i < threadCount; i++) {
                Reactor reactor = new Reactor();
                reactors.add(reactor);
                executor.submit(reactor);
            }
        }

        @Override
        public void handle(SelectableChannel selectableChannel) throws IOException {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectableChannel;
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (Objects.nonNull(socketChannel)) {
                if (index == threadCount) {
                    index = 0;
                }
                Reactor reactor = reactors.get(index++);
                socketChannel.configureBlocking(false);
                socketChannel.register(reactor.getSelector().wakeup(), SelectionKey.OP_READ, handler);
            }
        }
    }

    private static class ReadHandler implements Handler {
        @Override
        public void handle(SelectableChannel selectableChannel) {
            try (SocketChannel socketChannel = (SocketChannel) selectableChannel) {
                System.out.println(Thread.currentThread().getName() + "：begin");
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
        }

        ;
    }
}
