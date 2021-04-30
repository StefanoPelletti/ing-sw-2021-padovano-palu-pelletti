package it.polimi.ingsw;

import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.Model.Game;
import it.polimi.ingsw.Networking.Message.*;
import it.polimi.ingsw.Networking.*;
import java.net.*;
import java.io.*;
import java.util.*;


public class ServerApp
{
    public static void main( String[] args )
    {
        final int port = 43210;
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(port);
        }
        catch (IOException e)
        {
            System.out.println("SRV ERROR: "+e.getMessage());
            return;
        }

        System.out.println("Server online, listening on: " + serverSocket.getLocalPort()+"  "+serverSocket.getInetAddress());

        Socket socket;
        while(true)
        {
            try
            {
                socket = serverSocket.accept();
                new Thread(new ClientHandler(socket)).start();
            }
            catch (IOException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

}