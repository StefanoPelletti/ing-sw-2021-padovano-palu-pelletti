package it.polimi.ingsw.Server;

import it.polimi.ingsw.Networking.*;
import it.polimi.ingsw.Networking.Message.*;
import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_End;
import it.polimi.ingsw.Server.Controller.GameManager;

import java.util.*;

import java.net.*;
import java.io.*;

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private final Object outputLock;
    private int playerNumber;

    private Lobby lobby;
    private String nickname;
    private OutputStream outputStream; // = socket.getOutputStream();
    private ObjectOutputStream objectOutputStream;// = new ObjectOutputStream(outputStream);
    private InputStream inputStream;// = socket.getInputStream();
    private ObjectInputStream objectInputStream;// = new ObjectInputStream(inputStream);

    private Boolean pendingConnection;

    public ClientHandler( Socket clientSocket)
    {
        this.clientSocket = null;
        this.lobby = null;
        this.nickname=null;

        this.outputLock = new Object();
        this.playerNumber = 0;
        this.pendingConnection = false;
    }

    public void run() {
        try {
            outputStream = clientSocket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            inputStream = clientSocket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);

            Message message;

            message = (Message) objectInputStream.readObject();
//LOBBY REJOIN-------------------------
            if(message.getMessageType() == MessageType.MSG_REJOIN_LOBBY)
            {
                MSG_REJOIN_LOBBY msg = (MSG_REJOIN_LOBBY) message;
                int lobbyNumber = msg.getLobbyNumber();
                String nickname = msg.getNickname();

                lobby = Lobby.getLobby(lobbyNumber);

                if(lobby == null)
                {
                    send(new MSG_ERROR("Lobby not found"));
                    closeStreams();
                    return;
                }

                ClientHandler handler = lobby.findPendingClientHandler(nickname);
                if(handler == null)
                {
                    send(new MSG_ERROR("Found no player that needs to reconnect with that nickname"));
                    closeStreams();
                    return;
                }

                send(new MSG_OK_REJOIN(handler.getNickname()));
                handler.substituteStreams(this.clientSocket, this.inputStream, this.objectInputStream, this.outputStream, this.objectOutputStream);
                return;
            }
//LOBBY CREATION-------------------------
            else if (message.getMessageType() == MessageType.MSG_CREATE_LOBBY) {
                MSG_CREATE_LOBBY msg = (MSG_CREATE_LOBBY) message;
                String nickname = msg.getNickname();
                int numOfPlayers = msg.getNumOfPlayers();

                Random random = new Random();
                boolean found;
                int i;

                if (Lobby.getLobbies().size() > 500) {
                    send(new MSG_ERROR("Ce stanno troppe lobby"));
                    closeStreams();
                    return;
                }
                do {
                    found = false;
                    i = random.nextInt(500);
                    for (Lobby l : Lobby.getLobbies())
                        if (Lobby.checkLobbies(i)) found = true;
                } while (found);

                this.lobby = new Lobby(i, numOfPlayers);
                this.lobby.onJoin(nickname, this.clientSocket, this);

                Lobby.addLobby(this.lobby);

                System.out.println(nickname + " created lobby "+i);
                this.send(new MSG_OK_CREATE(i));

                if (msg.getNumOfPlayers() == 1)
                    lobby.init();
                else
                    lobby.wait();
//LOBBY JOINING-------------------------
            } else if (message.getMessageType() == MessageType.MSG_JOIN_LOBBY) {
                MSG_JOIN_LOBBY msg = (MSG_JOIN_LOBBY) message;
                int lobbyNumber = msg.getLobbyNumber();
                String nickname = msg.getNickname();

                this.lobby = Lobby.getLobby(lobbyNumber);
                if (this.lobby != null) {
                    nickname = this.lobby.onJoin(nickname, this.clientSocket, this);
                    if (nickname != null) {
                        send(new MSG_OK_JOIN(nickname));
                        System.out.println(nickname + " joined lobby "+lobbyNumber);
                    }
                    else {
                        send(new MSG_ERROR("Lobby full!"));
                        closeStreams();
                        return;
                    }
                } else {
                    send(new MSG_ERROR("Lobby not found"));
                    closeStreams();
                    return;
                }
                if (lobby.getNumberOfPresentPlayers() == lobby.getLobbyMaxPlayers()) {
                    lobby.init();
                    lobby.notifyAll();
                }
                else
                    lobby.wait();
            } else {
                this.send(new MSG_ERROR("Ma che mi hai inviato?"));
                closeStreams();
                return;
            }

            //send Complete ModelUpdate?
            send(lobby.getFullModel());
            send(new MSG_UPD_End());

            this.playerNumber = lobby.whoIs(this.nickname);

//MAIN RUN(), loops
            while (true) {
                if (playerNumber == lobby.currentPlayer()) { // if this is true, then the Thread HAS TO listen to the client
                    message = (Message) objectInputStream.readObject();
                    Message finalMessage = message;

                    new Thread(() -> {
                        boolean result = lobby.onMessage(finalMessage, nickname);
                        if (result) lobby.messagePlatform.update(new MSG_UPD_End());
                    }).start();

                    if (looper()) return;
                } else //if playerNumber is not the currentPlayer, this thread is one of the "Inactive" players. THEN: send just the messages.
                {
                    if (looper()) return;
                }
            }

        }
        catch(IOException | ClassNotFoundException | InterruptedException e)
        {

        }

    }


    private boolean looper() throws InterruptedException, IOException {
        Message message;
        loop: while (true) {
            message = lobby.messagePlatform.waitForLatestMessage();
            switch (message.getMessageType()) {
                case MSG_UPD_LeaderBoard:
                    send(message);
                    closeStreams();
                    Lobby.removeLobby(this.lobby);
                    return true;
                case MSG_UPD_End:
                case MSG_ERROR:
                    send(message);
                    break loop;
                case MSG_NOTIFICATION:
                case MSG_UPD_DevCardsVendor:
                case MSG_UPD_LeaderCards:
                case MSG_UPD_Strongbox:
                case MSG_UPD_WarehouseDepot:
                case MSG_UPD_DevDeck:
                case MSG_UPD_Market:
                case MSG_UPD_Player:
                case MSG_UPD_Extradepot:
                case MSG_UPD_Game:
                case MSG_UPD_FaithTrack:
                case MSG_UPD_DevSlot:
                case MSG_UPD_ResourceObject:
                case MSG_UPD_MarketHelper:
                case MSG_UPD_LeaderCardsObject:
                    send(message);
                    break;
            }
        }
        return false;
    }

    private void closeStreams() throws IOException {
        this.clientSocket.close();
        this.inputStream.close();
        this.objectInputStream.close();
        this.outputStream.close();
        this.objectOutputStream.close();
    }


    public void send(Message message) throws InterruptedException, IOException {
        try{
            synchronized (outputLock) {
                objectOutputStream.writeObject(message);
                objectOutputStream.reset();
            }
        }
        catch(IOException e){
            System.out.println("Connection error while sending a message");
            closeStreams();
            this.pendingConnection=true;
            this.pendingConnection.wait();
            //notify update model?
        }
    }

    public String getNickname() { return this.nickname; }

    public Boolean getPendingConnection() { return pendingConnection; }

    public void substituteStreams(Socket socket, InputStream inputStream, ObjectInputStream objectInputStream,
                                  OutputStream outputStream, ObjectOutputStream objectOutputStream)
    {
        this.clientSocket = socket;
        this.inputStream = inputStream;
        this.objectInputStream = objectInputStream;
        this.outputStream = outputStream;
        this.objectOutputStream = objectOutputStream;
        this.pendingConnection=false;
        this.pendingConnection.notify();
    }
}
