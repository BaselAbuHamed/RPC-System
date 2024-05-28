# Distributed Prime Number Service with Load Balancing

## Overview

This project implements a distributed system for prime number calculations using a multi-client and multi-server architecture. The system comprises three main components: the Client, the Server, and the Dictionary (Load Balancer). The system is designed to handle multiple client requests simultaneously, distribute the computational load among several servers, and ensure robustness and scalability.

## Components

### 1. Client Process
- Prompts the user for an integer.
- Connects to the Dictionary to obtain server details.
- Sends RPC requests to the server to check if the number is prime and to find the next highest prime number.
- Displays the results to the user.

### 2. Server Process
- Registers itself with the Dictionary upon startup.
- Listens for client connections.
- Handles RPC calls for `isPrime(int x)` to check if a number is prime and `nextPrime(int x)` to find the next highest prime number.
- Manages multiple client connections concurrently using threads.

### 3. Dictionary (Load Balancer) Process
- Manages a list of available servers.
- Uses a round-robin algorithm to distribute client requests evenly across servers.
- Provides the IP and port of an available server to the client.

## How to Run

### Prerequisites
- Java Development Kit (JDK) installed
- A terminal or command prompt

### Compilation
Compile all Java files:
```sh
javac Client.java Server.java Dictionary.java
