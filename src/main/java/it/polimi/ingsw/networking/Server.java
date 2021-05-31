package it.polimi.ingsw.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    final int port;

    public Server(int port) {
        this.port = port;
    }

    public static void main(String[] args) {
        (new Server(43210)).run();
    }

    public void run() {
        Socket socket;

        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            System.out.println("Server online, listening on: " + InetAddress.getLocalHost().getHostAddress() + ":" + serverSocket.getLocalPort());
            while (true) {
                socket = serverSocket.accept();
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            System.out.println("[SRV] ERROR: " + e.getMessage());
        }
    }
}
