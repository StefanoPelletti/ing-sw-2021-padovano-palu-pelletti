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
        /*OutputStream outputStream;
        ObjectOutputStream objectOutputStream;
        InputStream inputStream;
        ObjectInputStream objectInputStream;
        Message message;
        List<Lobby> lobbies = new ArrayList<Lobby>();
        Random random = new Random();
        Thread T;
        int i;*/

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
        while(!Thread.currentThread().isInterrupted())
        {
            try
            {
                socket = serverSocket.accept();
                new Thread(new ClientHandler(socket)).start();
                /*System.out.println("aeaea");
                outputStream = socket.getOutputStream();
                objectOutputStream = new ObjectOutputStream(outputStream);

                inputStream = socket.getInputStream();
                objectInputStream = new ObjectInputStream(inputStream);

                message = (Message) objectInputStream.readObject();

                System.out.println(message.getMessageType());

                System.out.println("second aeae");
                if ( message.getMessageType() == MessageType.MSG_CREATE_LOBBY)
                {
                    MSG_CREATE_LOBBY msg = (MSG_CREATE_LOBBY) message;
                    System.out.println("Received creation message from "+msg.getNickname());
                    boolean found = true;
                    do {
                        found = false;
                        i = random.nextInt(500);
                        for ( Lobby l : lobbies)
                            if ( l.getLobbyNumber() == i) found = true;
                    } while( found );

                    Lobby l = new Lobby(msg.getNickname(), socket, i, msg.getNumOfPlayers());
                    System.out.println(msg.getNickname()+" created a lobby");

                    objectOutputStream.writeObject(new MSG_OK_CREATE(i));

                }
                else if ( message.getMessageType() == MessageType.MSG_JOIN_LOBBY)
                {
                    MSG_JOIN_LOBBY msg = (MSG_JOIN_LOBBY) message;
                    boolean found = false;
                    Lobby lobby = null;
                    for ( Lobby l : lobbies) {
                        if (l.getLobbyNumber() == msg.getLobbyNumber()) {
                            lobby = l;
                            found = true;
                        }
                    }
                    if(found)
                    {
                        if(lobby.getNumberOfPresentPlayers() < lobby.getLobbyMaxPlayers())
                        {
                            String newNickname = lobby.add(msg.getNickname(), socket);
                            objectOutputStream.writeObject(new MSG_OK_JOIN(newNickname));

                            if( lobby.getLobbyMaxPlayers() == lobby.getLobbyNumber()) {
                                //lobby piena, bisogna inizializzarla

                                lobby.init();
                            }
                        }
                        else
                        {
                            objectOutputStream.writeObject(new MSG_ERROR_GENERIC("Error! The lobby is full!"));
                            socket.close();

                        }
                    }
                    else
                    {
                        objectOutputStream.writeObject(new MSG_ERROR_GENERIC("Error! There is no lobby with such number!"));
                        socket.close();
                    }
                }
                else
                {
                    objectOutputStream.writeObject(new MSG_ERROR_GENERIC("Error! I will report you to Ferruccio Resta, you naughty boy"));
                    socket.close();

                }
                outputStream.close();
                inputStream.close();
                objectOutputStream.close();
                objectInputStream.close();*/

            }
            catch (IOException e)
            {
                System.out.println(e.getMessage());
            }
            System.out.println("third aeae");
        }
    }

}
