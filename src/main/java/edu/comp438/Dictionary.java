package edu.comp438;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Dictionary {
    private static List<String> servers = new ArrayList<>();
    private static AtomicInteger index = new AtomicInteger(0);

    /*
     * main method starts dictionary process.
     * It creates ServerSocket that listens for incoming connections on specified port.
     * For each incoming connection it creates new DictionaryHandler to handle connection.
     */
    public static void main(String[] args) {
        int port = 5000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Dictionary process listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new DictionaryHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The DictionaryHandler class is responsible for handling individual client connections.
     * It reads requests from client and sends responses back to client.
     * It supports two types of requests: REGISTER and GET_SERVER.
     */
    static class DictionaryHandler implements Runnable {
        private Socket socket;

        /*
         * The DictionaryHandler constructor initializes a new DictionaryHandler with specified client socket.
         */
        public DictionaryHandler(Socket socket) {
            this.socket = socket;
        }

        /*
         * The run method called when thread for this DictionaryHandler started.
         * It reads requests from client and sends responses back to client.
         * If a REGISTER request is received it adds server information to list of servers.
         * If a GET_SERVER request is received it sends the information of a server to client.
         */
        @Override
        public void run() {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                String request;

                while ((request = input.readLine()) != null) {
                    if (request.startsWith("REGISTER:")) {
                        String serverInfo = request.substring(9);
                        synchronized (servers) {
                            servers.add(serverInfo);
                        }
                        System.out.println("Registered server: " + serverInfo);
                    } else if (request.equals("GET_SERVER")) {
                        synchronized (servers) {
                            if (servers.isEmpty()) {
                                output.println("NO_SERVER");
                            } else {
                                int currentIndex = index.getAndUpdate(i -> (i + 1) % servers.size());
                                String server = servers.get(currentIndex);
                                System.out.println("Providing server to client: " + server);
                                output.println(server);
                            }
                        }
                    }
                }
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}