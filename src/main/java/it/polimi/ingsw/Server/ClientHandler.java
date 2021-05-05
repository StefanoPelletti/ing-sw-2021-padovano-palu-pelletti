package it.polimi.ingsw.Server;

import it.polimi.ingsw.Networking.*;
import it.polimi.ingsw.Networking.Message.*;
import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_End;

import java.util.*;

import java.net.*;
import java.io.*;

enum Phase { GiveModel, Proceed, YourTurn, Disconnected, ReceiveUpdates, GameOver}

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private final Object outputLock;
    private int playerNumber;

    private Lobby lobby;
    private final String nickname;
    private OutputStream outputStream; // = socket.getOutputStream();
    private ObjectOutputStream objectOutputStream;// = new ObjectOutputStream(outputStream);
    private InputStream inputStream;// = socket.getInputStream();
    private ObjectInputStream objectInputStream;// = new ObjectInputStream(inputStream);

    private Boolean pendingConnection;
    private Phase phase;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = null;
        this.lobby = null;
        this.nickname = null;

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
            if (message.getMessageType() == MessageType.MSG_REJOIN_LOBBY) {
                MSG_REJOIN_LOBBY msg = (MSG_REJOIN_LOBBY) message;
                int lobbyNumber = msg.getLobbyNumber();
                String nickname = msg.getNickname();

                lobby = Lobby.getLobby(lobbyNumber);

                if (lobby == null) {
                    send(new MSG_ERROR("Lobby not found"));
                    closeStreams();
                    return;
                }

                ClientHandler handler = lobby.findPendingClientHandler(nickname);
                if (handler == null) {
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

                System.out.println(nickname + " created lobby " + i);
                this.send(new MSG_OK_CREATE(i));

                if (msg.getNumOfPlayers() == 1)
                    lobby.init();
                else
                    lobby.wait();
//LOBBY JOINING-------------------------
            }
//LOBBY JOIN---------------------------
            else if (message.getMessageType() == MessageType.MSG_JOIN_LOBBY) {
                MSG_JOIN_LOBBY msg = (MSG_JOIN_LOBBY) message;
                int lobbyNumber = msg.getLobbyNumber();
                String nickname = msg.getNickname();

                this.lobby = Lobby.getLobby(lobbyNumber);
                if (this.lobby != null) {
                    nickname = this.lobby.onJoin(nickname, this.clientSocket, this);
                    if (nickname != null) {
                        send(new MSG_OK_JOIN(nickname));
                        System.out.println(nickname + " joined lobby " + lobbyNumber);
                    } else {
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
                } else
                    lobby.wait();
            } else {
                this.send(new MSG_ERROR("Huh?"));
                closeStreams();
                return;
            }
        }
        catch (IOException | InterruptedException | ClassNotFoundException e)
        {
            e.printStackTrace();
            return;
        }
        phase = Phase.GiveModel;

        //send Complete ModelUpdate?
        //send(lobby.getFullModel());
        //send(new MSG_UPD_End());

        this.playerNumber = lobby.whoIs(this.nickname);

//MAIN RUN(), loops

        while (true) {
            switch (phase) {
                case GiveModel: //updates the Client with the FULL Model
                    phase = giveModel();
                    break;
                case Proceed: //basically chooses if the player is the currentPlayer. If true, YourTurn, else, receive updates
                    phase = proceed();
                    break;
                case YourTurn: //creates a new thread which will perform the action. Then goes to receive updates
                    phase = yourTurn();
                    break;
                case ReceiveUpdates: //loops in this phase till a certain message pops up
                    phase = receiveUpdates();
                    break;
                case Disconnected: //gets in this phase if some exception was thrown by the streams
                    phase = disconnect();
                    break;
                case GameOver: //basically kills the thread
                    return;
            }
        }

    }

    private Phase giveModel() {
        try {
            send(lobby.getFullModel());
            return Phase.Proceed;
        } catch (IOException e) {
            e.printStackTrace();
            return Phase.Disconnected;
        }
    }

    private Phase proceed() {
        if(playerNumber == lobby.currentPlayer()) //never happens for a reconnecting player
            return Phase.YourTurn; // if this is true, then the Thread HAS TO listen to the client
        else
            return Phase.ReceiveUpdates; //if playerNumber is not the currentPlayer, this thread is one of the "Inactive" players. THEN: send just the messages.
    }

    private Phase yourTurn() {
        try {
            Message message;
            message = (Message) objectInputStream.readObject();
            Message finalMessage = message;

            new Thread(() -> {
                boolean result = lobby.onMessage(finalMessage, nickname);
                if (result) lobby.messagePlatform.update(new MSG_UPD_End());
            }).start();
            return Phase.ReceiveUpdates;
        }
        catch (ClassNotFoundException | IOException e)
        {
            e.printStackTrace();
            return Phase.Disconnected;
        }
    }

    private Phase receiveUpdates() {
        Message message;
        try {
            message = lobby.messagePlatform.waitForLatestMessage();
            switch(message.getMessageType())
            {
                case MSG_UPD_End:
                case MSG_ERROR:
                    send(message);
                    return Phase.Proceed;
                case MSG_UPD_LeaderBoard:
                    send(message);
                    closeStreams();
                    Lobby.removeLobby(this.lobby);
                    return Phase.GameOver;
                default:
                    /*
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
                    */

                    send(message);
                    return Phase.ReceiveUpdates;
            }

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            return Phase.Disconnected;
        }
    }

    private Phase disconnect() {
        System.out.println("An error has occurred and the ClientHandler will be disconnected");
        closeStreams();

        //check if last player -> kill other threads (how? see below [275], same situation)
        //in this case he should win? or he should continue alone? must discuss!

        this.pendingConnection=true;
        lobby.addIdlePlayer(this.playerNumber);
        lobby.messagePlatform.decrementActivePlayers();
        lobby.disconnectPlayer(this.nickname);
        try {
            while (this.pendingConnection) this.pendingConnection.wait();
            // while (status!= GAMEOVER && this.pendingConnection ) this.pendingconnection.wait()
            // {    if (status == GAMEOVER) return GameOver
            // else ...   HOW TO UNLOCK THIS?
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        lobby.messagePlatform.incrementActivePlayers();
        lobby.removeIdlePlayer(this.playerNumber);
        return Phase.GiveModel;
    }


    private void closeStreams() {
        try {
            this.clientSocket.close();
            this.inputStream.close();
            this.objectInputStream.close();
            this.outputStream.close();
            this.objectOutputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void send(Message message) throws IOException {
        objectOutputStream.writeObject(message);
        objectOutputStream.reset();
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
