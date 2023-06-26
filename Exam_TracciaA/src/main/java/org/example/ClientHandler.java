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

                String commands = "Comandi disponibili:\n" +
                        " - all: Visualizza tutti gli alberghi\n" +
                        " - all_sorted: Visualizza gli alberghi ordinati\n" +
                        " - most_expensive_suite: Visualizza la suite più costosa\n" +
                        " - close: Interrompi connessione\n";
                outputStream.write(commands.getBytes());
                outputStream.flush();


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

                    String response = AlbergoProcessor.processRequest(request);

                    outputStream.write(response.getBytes());
                    outputStream.write("\n".getBytes());
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
