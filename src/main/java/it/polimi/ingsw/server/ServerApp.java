package it.polimi.ingsw.server;

import it.polimi.ingsw.networking.ClientHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {
    public static void main(String[] args) {
        final int port = 43210;
        long openedConnections = 0;
        Socket socket;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server online, listening on: " + InetAddress.getLocalHost().getHostAddress() + ":" + serverSocket.getLocalPort());
            while (openedConnections <= 1000000L) {
                socket = serverSocket.accept();
                new Thread(new ClientHandler(socket)).start();

                openedConnections++;
                if (openedConnections == 1000000L)
                    openedConnections = 0;
            }
        } catch (IOException e) {
            System.out.println("[SRV] ERROR: " + e.getMessage());
        }
    }
}