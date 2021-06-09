package it.polimi.ingsw.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * The Server class starts the Server service for the game, accepting incoming connections and instating new ClientHandlers per connection.
 * By design the Server runs indefinitely, and can be invoked externally, because it implements the Runnable interface.
 * An external object can also instantiate the Server and specify a different port number, rather than using the default port number;
 */
public class Server implements Runnable {

    /**
     * specifies the networking port that will be used for starting the server.
     * An incorrect value will cause an IOException and the program will terminate.
     */
    final int port;

    public Server() { this.port = 43210; }
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
