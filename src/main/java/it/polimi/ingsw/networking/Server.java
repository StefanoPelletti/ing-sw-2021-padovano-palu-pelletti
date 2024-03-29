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
     * specifies the networking port that will be used for starting the server. <p>
     * An incorrect value will cause an IOException and the program will terminate.
     * Not necessarily a private attribute, since it's final. Still, not static since it's referred to this specific Server.
     */
    final int port;

    /**
     * Default constructor, that sets the port number to the default 43210.
     */
    public Server() {
        this.port = 43210;
    }

    /**
     * Custom constructor, that sets the port number to a custom port number.
     */
    public Server(int port) {
        this.port = port;
    }

    /**
     * Testing purposes only.
     *
     * @param args argument list
     */
    public static void main(String[] args) {
        (new Server(43210)).run();
    }

    /**
     * Accepts the incoming connections and creates a new ClientHandler that handles that newly creates Socket. <p>
     * Implements the Runnable interface.
     */
    @Override
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
