package it.polimi.ingsw.networking;

import it.polimi.ingsw.networking.message.*;
import it.polimi.ingsw.server.utils.ModelObserver;

import java.io.*;
import java.net.Socket;

enum Phase {GIVE_MODEL, DISCONNECTED, GAME_OVER, TIME_OUT, LISTEN_CLIENT}

public class ClientHandler implements Runnable, ModelObserver {
    // Random random = new Random();

    private final Object outputLock;
    private final Object pendingLock;

    private Socket clientSocket;
    private int playerNumber;

    private Lobby lobby;
    private String nickname;
    private OutputStream outputStream;
    private ObjectOutputStream objectOutputStream;
    private InputStream inputStream;
    private ObjectInputStream objectInputStream;

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

                String inputNickname = msg.getNickname();
                this.nickname = inputNickname;

                this.lobby = Lobby.getLobby(lobbyNumber);

                if (this.lobby == null) {
                    send(new MSG_ERROR("Lobby not found"));
                    closeStreams();
                    return;
                }

                ClientHandler handler = lobby.findPendingClientHandler(inputNickname);
                if (handler == null) {
                    send(new MSG_ERROR("Found no player that needs to reconnect with that nickname"));
                    closeStreams();
                    return;
                }

                send(new MSG_OK_REJOIN(handler.getNickname()));
                handler.substituteStreams(this.clientSocket, this.inputStream, this.objectInputStream, this.outputStream, this.objectOutputStream);
                System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " reconnected to lobby, giving up ");
                return;
            }
//LOBBY CREATION-------------------------
            else if (message.getMessageType() == MessageType.MSG_CREATE_LOBBY) {
                MSG_CREATE_LOBBY msg = (MSG_CREATE_LOBBY) message;
                String inputNickname = msg.getNickname();
                this.nickname = inputNickname;
                int numOfPlayers = msg.getNumOfPlayers();

                boolean found = false;
                int i;
                int lobbyMaxSize = 50;

                if (numOfPlayers < 1 || numOfPlayers > 4) {
                    System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " tried to create a lobby with a wrong number of player");
                    send(new MSG_ERROR("The number of players is not correct!"));
                    closeStreams();
                    return;
                }
                if (Lobby.getLobbies().size() >= lobbyMaxSize) {
                    System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " tried to create a lobby, but the too many lobbies");
                    send(new MSG_ERROR("Too many lobbies!"));
                    closeStreams();
                    return;
                }

                do {
                    //i = random.nextInt(lobbyMaxSize);
                    i = 0; //delete this when OK
                    found = Lobby.checkLobbies(i);
                } while (found);

                this.lobby = new Lobby(i, numOfPlayers);
                synchronized (lobby) {
                    this.lobby.onJoin(inputNickname, this.clientSocket, this);

                    Lobby.addLobby(this.lobby);

                    System.out.println(inputNickname + " created lobby " + i);
                    this.send(new MSG_OK_CREATE(i));

                    if (msg.getNumOfPlayers() == 1) {
                        System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " Solo lobby detected and init " + i);
                        lobby.init();
                    } else {
                        System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " Created a lobby, waiting on lobby " + i);
                        while (!lobby.isStarted()) lobby.wait();
                        System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " awoken on lobby " + i);
                    }
                }
            }
//LOBBY JOIN---------------------------
            else if (message.getMessageType() == MessageType.MSG_JOIN_LOBBY) {
                MSG_JOIN_LOBBY msg = (MSG_JOIN_LOBBY) message;
                int lobbyNumber = msg.getLobbyNumber();
                String inputNickname = msg.getNickname();

                this.lobby = Lobby.getLobby(lobbyNumber);
                if (this.lobby == null) {
                    send(new MSG_ERROR("Lobby not found"));
                    closeStreams();
                    return;
                }
                synchronized (lobby) {
                    this.nickname = this.lobby.onJoin(inputNickname, this.clientSocket, this);
                    if (this.nickname != null) {
                        send(new MSG_OK_JOIN(this.nickname));
                        System.out.println(this.nickname + " joined lobby " + lobbyNumber);
                    } else {
                        System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " looked for a non existing lobby");
                        send(new MSG_ERROR("Lobby full!"));
                        closeStreams();
                        return;
                    }

                    if (lobby.getNumberOfPresentPlayers() == lobby.getLobbyMaxPlayers()) {
                        System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " reached full lobby, lobby init and notify() " + lobbyNumber);
                        lobby.init();
                        lobby.notifyAll();
                    } else {
                        System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " waiting on lobby " + lobbyNumber);
                        while (!lobby.isStarted()) lobby.wait();
                        System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " awoken on lobby " + lobbyNumber);
                    }

                }
            }
//NON OPENING MESSAGE RECEIVED
            else {
                this.send(new MSG_ERROR("Huh?"));
                closeStreams();
                return;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (InterruptedException i) {
            Thread.currentThread().interrupt();
        }

//all players connected, lobby init OR timeout
        if (lobby.isDeleted()) //if timeout occurred
        {
            phase = Phase.TIME_OUT;
        } else {
            phase = Phase.GIVE_MODEL;
            this.playerNumber = lobby.whoIs(this.nickname);
        }

//MAIN RUN(), loops

        while (true) {
            switch (phase) {
                case GIVE_MODEL: //updates the Client with the FULL Model
                    phase = giveModel();
                    break;
                case LISTEN_CLIENT:
                    phase = listenClient();
                    break;
                case DISCONNECTED: //gets in this phase if some exception was thrown by the streams
                    phase = disconnect();
                    break;
                case GAME_OVER: //basically kills the thread
                    closeStreams();
                    lobby.setDeleted(true);
                    lobby.wakeUpAllPendingClientHandlers();
                    Lobby.removeLobby(this.lobby);
                    System.out.println("[T " + Thread.currentThread().getName() + "] - " + this.nickname + " - died cause of GameOver condition ");
                    return;
                case TIME_OUT:
                    timeOut();
                    System.out.println("[T " + Thread.currentThread().getName() + "] - " + this.nickname + " - died cause of TimeOut condition ");
                    return;
            }
        }
    }

    private Phase giveModel() {
        try {
            send(lobby.getFullModel());
            System.out.println("[T " + Thread.currentThread().getName() + "] - " + this.nickname + " - sent full model ");
            return Phase.LISTEN_CLIENT;
        } catch (IOException e) {
            e.printStackTrace();
            return Phase.DISCONNECTED;
        }
    }

    private Phase listenClient() {
        System.out.println("[T " + Thread.currentThread().getName() + "] - " + this.nickname + " - Listening player ");
        while (true) {
            try {
                Message message;
                message = (Message) objectInputStream.readObject();

                if (playerNumber == lobby.currentPlayer()) {
                    new Thread(() -> lobby.onMessage(message)).start();
                } else {
                    objectOutputStream.writeObject(new MSG_ERROR("not your Turn!"));
                    objectOutputStream.flush();
                }
            } catch (ClassNotFoundException | IOException exception) {
                if (lobby.isGameOver())
                    return Phase.GAME_OVER;
                return Phase.DISCONNECTED;
            }
        }
    }


    private void timeOut() {
        System.out.println("[T " + Thread.currentThread().getName() + "] - " + " timeOut of the Lobby. Disconnection.");
        try {
            send(new MSG_ERROR("Not all Players have joined the lobby in time."));
            closeStreams();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void closeStreams() {
        try {
            if (!this.clientSocket.isClosed())
                this.clientSocket.close();
            this.inputStream.close();
            this.objectInputStream.close();
            this.outputStream.close();
            this.objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void send(Message message) throws IOException {
        if (!pendingConnection) {
            synchronized (outputLock) {
                objectOutputStream.reset();
                objectOutputStream.writeObject(message);
                objectOutputStream.flush();
            }
        }
    }

    public String getNickname() {
        return this.nickname;
    }

    public Boolean isPendingConnection() {
        return pendingConnection;
    }

    private Phase disconnect() {
        System.out.println("[T " + Thread.currentThread().getName() + "] - " + "An error has occurred and the ClientHandler will be disconnected");
        closeStreams();

        //check if last player -> kill other threads (how? see below [275], same situation)
        //in this case he should win? or he should continue alone? must discuss!

        this.pendingConnection = true;
        lobby.addIdlePlayer(this.playerNumber);
        if (lobby.areAllPlayersIdle())
            new Thread(new allDisconnectedThread(this.lobby)).start();
        else
            lobby.disconnectPlayer(this.nickname);
        try {
            synchronized (this.pendingLock) {
                while (this.pendingConnection) this.pendingLock.wait();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
// if the game is over when he wakes up
        if (lobby.isDeleted()) {
            closeStreams();
            return Phase.GAME_OVER;
        }
//try to send full model
        try {
            send(lobby.getFullModel());
        } catch (IOException e) {
            return Phase.DISCONNECTED;
        }

        lobby.removeIdlePlayer(this.playerNumber);
        System.out.println("[T " + Thread.currentThread().getName() + "] - " + this.nickname + " - sent full model ");
        lobby.notifyReconnection(this.nickname);
        return Phase.LISTEN_CLIENT;
    }

    public void substituteStreams(Socket socket, InputStream inputStream, ObjectInputStream objectInputStream,
                                  OutputStream outputStream, ObjectOutputStream objectOutputStream) {
        this.clientSocket = socket;
        this.inputStream = inputStream;
        this.objectInputStream = objectInputStream;
        this.outputStream = outputStream;
        this.objectOutputStream = objectOutputStream;

        System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " exploded, going into dead state ");
        wakeUp();
        /*
        synchronized (this.pendingConnection) {
            this.pendingConnection=false;
            this.pendingConnection.notify();
        }

         */

        System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " reconnected to lobby ");
    }

    public void wakeUp() {
        synchronized (this.pendingLock) {
            this.pendingConnection = false;
            this.pendingLock.notifyAll();
        }
    }


    public void updateX(Message message) {
        try {
            send(message);
        } catch (IOException e) {
            System.out.println(this.nickname + " failed to send message: " + message.getMessageType());
            e.printStackTrace();
        }
    }

    @Override
    //@experimental
    public void update(Message message) {
        try {
            if (message.getMessageType() == MessageType.MSG_Stop) {
                closeStreams();
                System.out.println(" detected MSG_Stop: closing Thread of " + this.nickname + " streams");
            } else
                send(message);
        } catch (IOException e) {
            System.out.println(this.nickname + " failed to send message: " + message.getMessageType());
            e.printStackTrace();
        }
    }
}