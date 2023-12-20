package com.mylsaber.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class BioClient {
    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            new Thread(new Client("127.0.0.1", 8888, "client " + i)).start();
        }
    }

    public static class Client implements Runnable {
        private final String host;
        private final int port;
        private final String name;

        public Client(String host, int port, String name) {
            this.host = host;
            this.port = port;
            this.name = name;
        }

        @Override
        public void run() {
            try (
                    Socket socket = new Socket(host, port);
                    OutputStream outputStream = socket.getOutputStream();
                    InputStream inputStream = socket.getInputStream();
            ) {
                outputStream.write(("hello from " + name).getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                socket.shutdownOutput();

                byte[] bytes = new byte[1024];
                int read;
                while ((read = inputStream.read(bytes)) != -1) {
                    System.out.println(new String(bytes, 0, read));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
