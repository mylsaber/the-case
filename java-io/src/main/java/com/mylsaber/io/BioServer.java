package com.mylsaber.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BioServer {
    public static void main(String[] args) throws IOException {
        Server server = new Server(8888);
        server.run();
    }

    private static class Server implements Runnable {
        private final ServerSocket serverSocket;

        public Server(int port) throws IOException {
            serverSocket = new ServerSocket(port);
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    System.out.println("Server is waiting for connection...");
                    Socket socket = serverSocket.accept();
                    System.out.println("Server is connected with client: " + socket.getInetAddress());
                    new Handler(socket).run();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static class Handler implements Runnable {
        private final Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream()
            ) {
                byte[] bytes = new byte[1024];
                int len;
                StringBuilder stringBuilder = new StringBuilder();
                while ((len = inputStream.read(bytes)) != -1) {
                    stringBuilder.append(new String(bytes, 0, len));
                }
                System.out.println(stringBuilder);

                outputStream.write("Hello, client!".getBytes());
                outputStream.flush();
                socket.shutdownOutput();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
