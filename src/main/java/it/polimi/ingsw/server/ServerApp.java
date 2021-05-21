package it.polimi.ingsw.server;

import it.polimi.ingsw.networking.ClientHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;


public class ServerApp {
    public static void main(String[] args) throws UnknownHostException {
        final int port = 43210;
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("SRV ERROR: " + e.getMessage());
            return;
        }

        System.out.println("Server online, listening on: " + InetAddress.getLocalHost().getHostAddress() + ":" + serverSocket.getLocalPort());

        Socket socket;

        while (true) {
            try {
                socket = serverSocket.accept();
                new Thread(new ClientHandler(socket)).start();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}