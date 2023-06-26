package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void handleClient() {
        new Thread(() -> {
            try {
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();

                byte[] buffer = new byte[1024];

                System.out.println("Nuova connessione client: " + socket.getInetAddress());

                while (true) {
                    int bytesRead = inputStream.read(buffer);
                    String request = new String(buffer, 0, bytesRead).trim();

                    if (request.equals("exit")) {
                        break;
                    } else if (request.equals("close")) {
                        outputStream.write("Connessione chiusa\n".getBytes());
                        outputStream.flush();
                        break;
                    }

                    String response = AlbergoProcessor.Request(request);

                    outputStream.write(response.getBytes());
                    outputStream.write("\n Connessione chiusa".getBytes());
                    outputStream.flush();
                }

                socket.close();
                System.out.println("Connessione client chiusa: " + socket.getInetAddress());
            } catch (IOException e) {
                if (e instanceof SocketException) {
                    System.out.println("Connessione client interrotta: " + socket.getInetAddress());
                } else {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
