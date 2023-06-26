package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class App {

    private static final String COMMANDS_LIST = "Comandi disponibili:\n" +
            " - all: Visualizza tutti gli alberghi\n" +
            " - all_sorted: Visualizza gli alberghi ordinati\n" +
            " - most_expensive_suite: Visualizza la suite piu costosa\n" +
            " - close: Interrompi connessione\n\n";

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);

            System.out.println("Server avviato.");

            while (true) {
                Socket socket = serverSocket.accept();
                Client(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void Client(Socket socket) {
        new Thread(() -> {
            try {
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();

                byte[] buffer = new byte[1024];

                System.out.println("Nuova connessione client: " + socket.getInetAddress());
                outputStream.write(COMMANDS_LIST.getBytes());
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

                    String response = processRequest(request);

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

    private static String processRequest(String request) {
        List<Albergo> alberghi = Albergo.getHotelList();

        if (request.equals("all")) {
            return Albergo.getAllHotelsJSON(alberghi);
        } else if (request.equals("all_sorted")) {
            return Albergo.getSortedHotelsJSON(alberghi);
        } else if (request.equals("most_expensive_suite")) {
            return Albergo.getMostExpensiveSuiteJSON(alberghi);
        } else {
            return "Comando non valido";
        }
    }
}
