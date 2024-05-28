package edu.comp438;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            /*
             * Establishes connection to  dictionary process.
             * Creates socket connection to dictionary server running on localhost at port 5000.
             * Sets up input and output streams for communication with dictionary server.
             * Scanner object is also created to read user input from console.
             */
            Socket dictSocket = new Socket("localhost", 5000);
            BufferedReader dictInput = new BufferedReader(new InputStreamReader(dictSocket.getInputStream()));
            PrintWriter dictOutput = new PrintWriter(dictSocket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter an integer: ");
            int num = scanner.nextInt();

            /*
             * Requests server info from dictionary.
             * Sends GET_SERVER command to dictionary server and reads response.
             * Closes connection to dictionary server.
             */
            dictOutput.println("GET_SERVER");
            String serverInfo = dictInput.readLine();
            dictSocket.close();

            /*
             * Processes server info received from dictionary.
             * If valid server info is received, establishes a connection to server.
             * Sets up input and output streams for communication with server.
             */
            if (serverInfo != null && !serverInfo.isEmpty()) {
                String[] serverParts = serverInfo.split(":");
                if (serverParts.length == 2) {
                    String serverIP = serverParts[0];
                    int serverPort = Integer.parseInt(serverParts[1]);

                    Socket serverSocket = new Socket(serverIP, serverPort);
                    BufferedReader serverInput = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
                    PrintWriter serverOutput = new PrintWriter(serverSocket.getOutputStream(), true);

                    /*
                     * Sends RPC requests to server to check if the entered number is prime and to find the next prime number.
                     * Reads responses from the server and prints them to the console.
                     * Closes the connection to server.
                     */
                    serverOutput.println("IS_PRIME:" + num);
                    String isPrimeResponse = serverInput.readLine();
                    System.out.println("Is " + num + " prime? " + isPrimeResponse);

                    serverOutput.println("NEXT_PRIME:" + num);
                    String nextPrimeResponse = serverInput.readLine();
                    System.out.println("Next prime number after " + num + " is: " + nextPrimeResponse);

                    serverSocket.close();
                } else {
                    System.err.println("Invalid server information received: " + serverInfo);
                }
            } else {
                System.err.println("No available servers or empty response received from dictionary.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}