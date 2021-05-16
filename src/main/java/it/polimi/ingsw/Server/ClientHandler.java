package it.polimi.ingsw.Server;

import it.polimi.ingsw.Networking.*;
import it.polimi.ingsw.Networking.Message.*;
import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_End;

import java.util.*;

import java.net.*;
import java.io.*;

enum Phase { GiveModel, Proceed, YourTurn, Disconnected, ReceiveUpdates, GameOver, TimeOut}

public class ClientHandler implements Runnable {

    private Socket clientSocket;
    private final Object outputLock;
    private final Object pendingLock;
    private int playerNumber;

    private Lobby lobby;
    private String nickname;
    private OutputStream outputStream; // = socket.getOutputStream();
    private ObjectOutputStream objectOutputStream;// = new ObjectOutputStream(outputStream);
    private InputStream inputStream;// = socket.getInputStream();
    private ObjectInputStream objectInputStream;// = new ObjectInputStream(inputStream);

    private Boolean pendingConnection;
    private Phase phase;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.lobby = null;
        this.nickname = null;

        this.outputLock = new Object();
        this.pendingLock = new Object();
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
                this.nickname = nickname;

                this.lobby = Lobby.getLobby(lobbyNumber);

                if (this.lobby == null) {
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
                System.out.println("["+Thread.currentThread().getName()+"] - "+this.nickname+" reconnected to lobby, giving up ");
                return;
            }
//LOBBY CREATION-------------------------
            else if (message.getMessageType() == MessageType.MSG_CREATE_LOBBY) {
                MSG_CREATE_LOBBY msg = (MSG_CREATE_LOBBY) message;
                String nickname = msg.getNickname();
                this.nickname = nickname;
                int numOfPlayers = msg.getNumOfPlayers();

                Random random = new Random();
                boolean found = false;
                int i;
                int lobbyMaxSize = 1;

                if (numOfPlayers < 1 || numOfPlayers > 4) {
                    System.out.println("["+Thread.currentThread().getName()+"] - "+this.nickname+" tried to create a lobby with a wrong number of player");
                    send(new MSG_ERROR("The number of players is not correct!"));
                    closeStreams();
                    return;
                }
                if (Lobby.getLobbies().size() >= lobbyMaxSize) {
                    System.out.println("["+Thread.currentThread().getName()+"] - "+this.nickname+" tried to create a lobby, but the too many lobbies");
                    send(new MSG_ERROR("Too many lobbies!"));
                    closeStreams();
                    return;
                }

                do {
                    i = random.nextInt(lobbyMaxSize);
                    found = Lobby.checkLobbies(i);
                } while (found);

                this.lobby = new Lobby(i, numOfPlayers);
                synchronized (lobby) {
                    this.lobby.onJoin(nickname, this.clientSocket, this);

                    Lobby.addLobby(this.lobby);

                    System.out.println(nickname + " created lobby " + i);
                    this.send(new MSG_OK_CREATE(i));

                    if (msg.getNumOfPlayers() == 1) {
                        System.out.println("["+Thread.currentThread().getName()+"] - "+this.nickname+" Solo lobby detected and init "+i);
                        lobby.init();
                    }
                    else
                    {
                        System.out.println("["+Thread.currentThread().getName()+"] - "+this.nickname+" Created a lobby, waiting on lobby "+i);
                        lobby.wait();
                        System.out.println("["+Thread.currentThread().getName()+"] - "+this.nickname+" awoken on lobby "+i);
                    }

                }
            }
//LOBBY JOIN---------------------------
            else if (message.getMessageType() == MessageType.MSG_JOIN_LOBBY) {
                MSG_JOIN_LOBBY msg = (MSG_JOIN_LOBBY) message;
                int lobbyNumber = msg.getLobbyNumber();
                String nickname = msg.getNickname();

                this.lobby = Lobby.getLobby(lobbyNumber);
                if(this.lobby == null)
                {
                    send(new MSG_ERROR("Lobby not found"));
                    closeStreams();
                    return;
                }
                synchronized (lobby) {
                    this.nickname = this.lobby.onJoin(nickname, this.clientSocket, this);
                    if (this.nickname != null) {
                        send(new MSG_OK_JOIN(this.nickname));
                        System.out.println(this.nickname + " joined lobby " + lobbyNumber);
                    } else {
                        System.out.println("["+Thread.currentThread().getName()+"] - "+this.nickname+" looked for a non existing lobby");
                        send(new MSG_ERROR("Lobby full!"));
                        closeStreams();
                        return;
                    }

                    if (lobby.getNumberOfPresentPlayers() == lobby.getLobbyMaxPlayers()) {
                        System.out.println("["+Thread.currentThread().getName()+"] - "+this.nickname+" reached full lobby, lobby init and notify() "+lobbyNumber);
                        lobby.init();
                        lobby.notifyAll();
                    } else
                    {
                        System.out.println("["+Thread.currentThread().getName()+"] - "+this.nickname+" waiting on lobby "+lobbyNumber);
                        lobby.wait();
                        System.out.println("["+Thread.currentThread().getName()+"] - "+this.nickname+" awoken on lobby "+lobbyNumber);
                    }

                }
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


        if(lobby.isDeleted()) //if timeout occurred
        {
            phase = Phase.TimeOut;
        }
        else
        {
            phase = Phase.GiveModel;
            this.playerNumber = lobby.whoIs(this.nickname);
        }

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
                case TimeOut:
                    timeOut();
                    return;
            }
        }

    }

    private Phase giveModel() {
        try {
            System.out.println("[T "+Thread.currentThread().getName()+"] - "+this.nickname+" - sent full model ");
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
        System.out.println("[T "+Thread.currentThread().getName()+"] - "+this.nickname+" - yourTurn ");
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
        System.out.println("[T "+Thread.currentThread().getName()+"] - "+this.nickname+" - waiting for update messages ");
        Message message;
        try {
            message = lobby.messagePlatform.waitForLatestMessage(this.playerNumber);
            System.out.println("[T "+Thread.currentThread().getName()+"] - "+this.nickname+" - grabbed  "+message.getMessageType());
            switch(message.getMessageType())
            {
                case MSG_UPD_End:
                case MSG_ERROR:
                    send(message);
                    return Phase.Proceed;
                case MSG_UPD_LeaderBoard:
                    send(message);
                    closeStreams();
                    lobby.setDeleted(true);
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



    private void timeOut() {
        System.out.println("[T "+Thread.currentThread().getName()+"] - "+" timeOut of the Lobby. Disconnection.");
        try {
            send(new MSG_ERROR("Not all Players have joined the lobby in time."));
            closeStreams();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public Boolean isPendingConnection() { return pendingConnection; }

    private Phase disconnect() {
        System.out.println("[T "+Thread.currentThread().getName()+"] - "+"An error has occurred and the ClientHandler will be disconnected");
        closeStreams();

        //check if last player -> kill other threads (how? see below [275], same situation)
        //in this case he should win? or he should continue alone? must discuss!

        this.pendingConnection=true;
        lobby.addIdlePlayer(this.playerNumber);
        lobby.messagePlatform.decrementActivePlayers();
        lobby.disconnectPlayer(this.nickname);
        try {
            synchronized (this.pendingLock) {
                while (this.pendingConnection) this.pendingLock.wait();
                // while (status!= GAMEOVER && this.pendingConnection ) this.pendingconnection.wait()
                // {    if (status == GAMEOVER) return GameOver
                // else ...   HOW TO UNLOCK THIS?
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
// if the game is over when he wakes up
        if(lobby.isDeleted()) {
            closeStreams();
            return Phase.GameOver;
        }

        lobby.messagePlatform.incrementActivePlayers();
        lobby.removeIdlePlayer(this.playerNumber);
        lobby.messagePlatform.update(new MSG_NOTIFICATION(this.nickname+" reconnected to lobby!"));
        return Phase.GiveModel;
    }

    public void substituteStreams(Socket socket, InputStream inputStream, ObjectInputStream objectInputStream,
                                  OutputStream outputStream, ObjectOutputStream objectOutputStream)
    {
        this.clientSocket = socket;
        this.inputStream = inputStream;
        this.objectInputStream = objectInputStream;
        this.outputStream = outputStream;
        this.objectOutputStream = objectOutputStream;

        System.out.println("["+Thread.currentThread().getName()+"] - "+this.nickname+" exploded, going into dead state ");
        wakeUp();
        /*
        synchronized (this.pendingConnection) {
            this.pendingConnection=false;
            this.pendingConnection.notify();
        }

         */

        System.out.println("["+Thread.currentThread().getName()+"] - "+this.nickname+" reconnected to lobby ");
    }

    public void wakeUp() {
        synchronized (this.pendingLock) {
            this.pendingConnection = false;
            this.pendingLock.notify(); //or notifyAll
        }
    }
}
