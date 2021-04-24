package it.polimi.ingsw.Server;

import it.polimi.ingsw.Networking.*;
import it.polimi.ingsw.Networking.Message.*;
import it.polimi.ingsw.Server.Model.Player;
import it.polimi.ingsw.Server.Utils.ModelObserver;

import java.util.*;

import java.net.*;
import java.io.*;

public class ClientHandler implements Runnable, ModelObserver {

    private Socket clientSocket;
    private final Object outputLock;

    private Lobby lobby;
    private String nickname;
    private OutputStream outputStream; // = socket.getOutputStream();
    private ObjectOutputStream objectOutputStream;// = new ObjectOutputStream(outputStream);
    private InputStream inputStream;// = socket.getInputStream();
    private ObjectInputStream objectInputStream;// = new ObjectInputStream(inputStream);

    public ClientHandler( Socket clientSocket)
    {
        this.clientSocket = null;
        this.lobby = null;
        this.nickname=null;

        this.outputLock = new Object();
    }

    public void run() {
        try
        {
            outputStream  = clientSocket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            inputStream = clientSocket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);

            while(!Thread.currentThread().isInterrupted()){
                Message message = (Message) objectInputStream.readObject();
                if(message.getMessageType()==MessageType.MSG_CREATE_LOBBY){
                    MSG_CREATE_LOBBY msg = (MSG_CREATE_LOBBY) message;
                    Random random = new Random();
                    boolean found;
                    int i;
                    if(Lobby.getLobbies().size()==500) send(new MSG_ERROR("Too many lobbies!"));
                    do {
                        found = false;
                        i = random.nextInt(500);
                        for ( Lobby l : Lobby.getLobbies())
                            if ( Lobby.checkLobbies(i)) found = true;
                    } while( found );

                    this.lobby = new Lobby(i, msg.getNumOfPlayers());
                    this.lobby.onJoin(msg.getNickname(), this.clientSocket, this);
                    Lobby.addLobby(this.lobby);
                    if(msg.getNumOfPlayers() == 1) lobby.init();
                    System.out.println(msg.getNickname()+" created a lobby");

                    this.send(new MSG_OK_CREATE(i));
                }

                else if(message.getMessageType()==MessageType.MSG_JOIN_LOBBY){
                    MSG_JOIN_LOBBY joinMessage = (MSG_JOIN_LOBBY) message;
                    this.lobby = Lobby.getLobby(joinMessage.getLobbyNumber());
                    if(this.lobby!=null){
                        String nickname = this.lobby.onJoin(joinMessage.getNickname(), this.clientSocket,this);
                        if(nickname!=null) {
                            send(new MSG_OK_JOIN(nickname));
                            if(lobby.getLobbyMaxPlayers() == lobby.getNumOfPlayers()) lobby.init();
                        }
                        else send(new MSG_ERROR("Lobby full!"));
                    }
                    else send(new MSG_ERROR("Lobby not found"));
                }

                else{
                    this.lobby.onMessage(message, nickname);
                }
            }

        }
        catch(IOException | ClassNotFoundException e)
        {
            //??
        }

        catch(Exception e){
            //partita finita
        }
    }

    public void update(Message message){
        send(message);
    }

    public void send(Message message)
    {
        try{
            synchronized (outputLock) {
                objectOutputStream.writeObject(message);
                objectOutputStream.reset();
            }
        }
        catch(IOException e){}
    }
}
