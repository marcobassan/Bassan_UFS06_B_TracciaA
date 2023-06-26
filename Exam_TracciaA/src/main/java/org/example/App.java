package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class App {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);

            System.out.println("Server avviato. Comandi disponibili:");
            System.out.println(" - all: Visualizza tutti gli alberghi");
            System.out.println(" - all_sorted: Visualizza gli alberghi ordinati");
            System.out.println(" - most_expensive: Visualizza la suite più costosa");
            System.out.println(" - close: Interrompi connessione");
            System.out.println();

            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                String inputLine;
                System.out.println("Nuova connessione client: " + socket.getInetAddress());
                
                writer.write("Comandi disponibili:\n");
                writer.write(" - all: Visualizza tutti gli alberghi\n");
                writer.write(" - all_sorted: Visualizza gli alberghi ordinati\n");
                writer.write(" - most_expensive_suite: Visualizza la suite più costosa\n");
                writer.write(" - close: Interrompi connessione\n");
                writer.newLine();
                writer.flush();

                while ((inputLine = reader.readLine()) != null) {
                    String request = inputLine.trim();

                    if (request.equals("exit")) {
                        break;
                    } else if (request.equals("close")) {
                        writer.write("Connessione chiusa\n");
                        writer.flush();
                        break;
                    }

                    String response = AlbergoProcessor.Request(request);

                    writer.write(response);
                    writer.newLine();
                    writer.newLine();
                    writer.flush();
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
        }
    }
}
