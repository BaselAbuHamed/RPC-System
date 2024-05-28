package edu.comp438;

import java.io.*;
import java.net.*;

public class Server {
    /*
     * The main method starts the server process.
     * It gets local host address and registers server with dictionary process.
     * It then creates a ServerSocket that listens for incoming connections on a specified port.
     * For each incoming connection, it creates a new ClientHandler to handle connection.
     */
    public static void main(String[] args) {
        int port = 5001;
        try {
            //get local host address
            String hostAddress = InetAddress.getLocalHost().getHostAddress();

            //connect to dictionary process to register
            Socket dictSocket = new Socket("localhost", 5000);
            PrintWriter dictOutput = new PrintWriter(dictSocket.getOutputStream(), true);
            dictOutput.println("REGISTER:" + hostAddress + ":" + port);
            dictSocket.close();

            //start server to listen for client connections
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * The ClientHandler class is responsible for handling individual client connections.
     * It reads requests from client and sends responses back to client.
     * It supports two types of requests: IS_PRIME and NEXT_PRIME.
     */
    static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        /*
         * The ClientHandler constructor initializes a new ClientHandler with the specified client socket.
         */
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        /*
         * The run method is called when the thread for this ClientHandler is started.
         * It reads requests from the client and sends responses back to the client.
         * If an IS_PRIME request is received it checks if the number is prime and send the result back to the client.
         * If a NEXT_PRIME request is received it find next prime number and sends it back to the client.
         */
        @Override
        public void run() {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
                String request;

                while ((request = input.readLine()) != null) {
                    if (request.startsWith("IS_PRIME:")) {
                        int num = Integer.parseInt(request.substring(9));
                        output.println("IS_PRIME:" + isPrime(num));
                    } else if (request.startsWith("NEXT_PRIME:")) {
                        int num = Integer.parseInt(request.substring(11));
                        output.println("NEXT_PRIME:" + nextPrime(num));
                    }
                }
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*
         * The isPrime method checks if a number is prime.
         * It returns true if the number is prime, and false otherwise.
         */
        private boolean isPrime(int num) {
            if (num <= 1) return false;
            for (int i = 2; i <= Math.sqrt(num); i++) {
                if (num % i == 0) return false;
            }
            return true;
        }

        /*
         * The nextPrime method find the next prime number after a given number.
         * It returns the next prime number.
         */
        private int nextPrime(int num) {
            int next = num + 1;
            while (!isPrime(next)) {
                next++;
            }
            return next;
        }
    }
}