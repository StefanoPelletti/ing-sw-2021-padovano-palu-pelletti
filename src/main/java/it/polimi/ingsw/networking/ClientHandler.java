package it.polimi.ingsw.networking;

import it.polimi.ingsw.networking.message.*;
import it.polimi.ingsw.networking.message.actionMessages.ActionMessage;
import it.polimi.ingsw.networking.message.initMessages.InitMessage;
import it.polimi.ingsw.server.utils.ModelObserver;

import java.io.*;
import java.net.Socket;
import java.util.Random;

enum Phase {GIVE_MODEL, DISCONNECTED, GAME_OVER, TIME_OUT, LISTEN_CLIENT}

public class ClientHandler implements Runnable, ModelObserver {
    /**
     * reference to the casual number generator for Lobby numbers
     */
    Random random = new Random();

    /**
     * an object on which any entity syncs on when using the send() method of this ClientHandler
     */
    private final Object outputLock;
    /**
     * an object on which this ClientHandler waits when it goes in a Disconnected/Pending status
     */
    private final Object pendingLock;

    /**
     * the reference to the Socket used for network communication
     */
    private Socket clientSocket;
    /**
     * the reference to the Lobby, shared between many ClientHandlers
     */
    private Lobby lobby;
    /**
     * the number of the Player that this ClientHandler manages
     */
    private int playerNumber;
    /**
     * the nickname of the Player that this ClientHandler manages
     */
    private String nickname;
    /**
     * The standard output Stream generated by the Socket
     */
    private OutputStream outputStream;
    /**
     * The object output Stream constructed on the outputStream and generated by the Socket
     */
    private ObjectOutputStream objectOutputStream;
    /**
     * The standard input Stream generated by the Socket
     */
    private InputStream inputStream;
    /**
     * The object input Stream constructed on the inputStream and generated by the Socket
     */
    private ObjectInputStream objectInputStream;
    /**
     * True if this ClientHandler is in a Pending status, False otherwise
     */
    private Boolean pendingConnection;
    /**
     * the actual Phase the FSA behavior of this ClientHandler rounds on
     */
    private Phase phase;

    /**
     * Construct a new ClientHandler which manages a specified socket
     *
     * @param clientSocket the socket corresponding to a Client to handle
     */
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.lobby = null;
        this.nickname = null;

        this.outputLock = new Object();
        this.pendingLock = new Object();
        this.playerNumber = 0;
        this.pendingConnection = false;
    }

    /**
     * Execution Body of the ClientHandler:
     * FIRST BLOCK: unwraps the request of the newly connected player. Possible requests: <ul>
     * <li> CREATE, JOIN, REJOIN -> the ClientHandler acts as requested
     * <li> Anything else -> the ClientHandler closes the connection. </ul>
     * SECOND BLOCK: checks if the Lobby has been destroyed by a CountDownTimer.
     * THIRD BLOCK: MAIN RUN, a Finite State Machine: <ul>
     * <li> Starting State: GIVE_MODEL: updates the Client with a current-state Full Model
     * <li> LISTEN_CLIENT: listens for a Client Request. The Request is then passed to the Lobby
     * <li> DISCONNECTED: the ClientHandler executes the disconnected procedure
     * <li> TIME_OUT: enters in this state if a TimeOut condition has been met
     * <li> Ending State: GAME_OVER: executes the termination procedure
     */
    public void run() {
        try {
            outputStream = clientSocket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            inputStream = clientSocket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);

            InitMessage message;
            message = (InitMessage) objectInputStream.readObject();
            if (!message.execute(this)) return;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
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

    /**
     * Sends the Client a message containing the current-state Full Model.
     *
     * @return the next Phase: <ul>
     * <li> LISTEN_CLIENT if the message has been sent without errors,
     * <li> DISCONNECTED if an error occurred.
     */
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

    /**
     * Waits for a request from the Client.
     * In case the player handled is the currentPlayer, the request is passed to the lobby and the ClientHandler listens for a new request.
     * There may be exceptions on the socket, which determine the next State.
     *
     * @return GAME_OVER is the Game is ended, or DISCONNECTED if a network error occurred.
     */
    private Phase listenClient() {
        System.out.println("[T " + Thread.currentThread().getName() + "] - " + this.nickname + " - Listening player ");
        while (true) {
            try {
                ActionMessage message;
                message = (ActionMessage) objectInputStream.readObject();

                if (playerNumber == lobby.currentPlayer()) {
                    lobby.onMessage(message);
                } else {
                    send(new MSG_ERROR("not your Turn!"));
                }
            } catch (ClassNotFoundException | IOException exception) {
                if (lobby.isGameOver())
                    return Phase.GAME_OVER;
                return Phase.DISCONNECTED;
            }
        }
    }

    /**
     * This method is called when not all players joined the lobby in time.
     * Notifies the handled player of such event. Correctly interrupts the waiting Client.
     */
    private void timeOut() {
        System.out.println("[T " + Thread.currentThread().getName() + "] - " + " timeOut of the Lobby. Disconnection.");
        try {
            send(new MSG_ERROR("Not all Players have joined the lobby in time."));
            closeStreams();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the streams handled, if any error occurred or the Game is terminated.
     */
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

    /**
     * Sends a specified message to the handled Client.
     * If the ClientHandler is in pending-connection state, the message will not be forwarded.
     * note: uses reset() and flush() to send the message.
     *
     * @param message The message that will be sent to the client.
     * @throws IOException If any error occurred during the message send.
     */
    public void send(Message message) throws IOException {
        if (!pendingConnection) {
            synchronized (outputLock) {
                objectOutputStream.reset();
                objectOutputStream.writeObject(message);
                objectOutputStream.flush();
            }
        }
    }

    /**
     * Returns the nickname of the player handler by the ClientHandler.
     *
     * @return The nickname of the player handler by the ClientHandler.
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * Returns the pending-connection status.
     *
     * @return True if the ClientHandler is in a pending-connection state, False otherwise.
     */
    public Boolean isPendingConnection() {
        return pendingConnection;
    }

    /**
     * Disconnection procedure executed by the ClientHandler when a network error has occurred.
     * Sets the ClientHandler in a pending-connection state.
     * Inserts the player handled in the IdlePlayer list.
     * IF the lobby has no more active players, destroy the lobby,
     * ELSE Waits for a WakeUp.
     * After a WakeUp, the ClientHandler sends a current-state of the Game Model.
     * Removes the player from the IdlePlayer list, and sets to false the pending-connection state.
     *
     * @return the next Phase: <ul>
     * <li> GAME_OVER if the Game has ended
     * <li> DISCONNECTED if a network error has occurred while sending the Full Model
     * <li> LISTEN_CLIENT if everything went well.
     */
    private Phase disconnect() {
        System.out.println("[T " + Thread.currentThread().getName() + "] - " + "An error has occurred and the ClientHandler will be disconnected");
        closeStreams();

        //check if last player -> kill other threads (how? see below [275], same situation)
        //in this case he should win? or he should continue alone? must discuss!

        this.pendingConnection = true;
        lobby.addIdlePlayer(this.playerNumber);
        if (lobby.areAllPlayersIdle())
            new Thread(new AllDisconnectedThread(this.lobby)).start();
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

    /**
     * This method should be called by an external ClientHandler to a pending-connection ClientHandler.
     * The external ClientHandler gives all its streams and socket to the pending-connection ClientHandler.
     * Consequentially Wakes up the pending-connection ClientHandler.
     *
     * @param socket             The socket managed by the external ClientHandler.
     * @param inputStream        The input stream associated with the above socket.
     * @param objectInputStream  The object stream associated with the above input stream.
     * @param outputStream       The output stream associated with the above socket.
     * @param objectOutputStream The object stream associated with the the above output stream.
     * @see #wakeUp()
     */
    public void substituteStreams(Socket socket, InputStream inputStream, ObjectInputStream objectInputStream,
                                  OutputStream outputStream, ObjectOutputStream objectOutputStream) {
        this.clientSocket = socket;
        this.inputStream = inputStream;
        this.objectInputStream = objectInputStream;
        this.outputStream = outputStream;
        this.objectOutputStream = objectOutputStream;

        System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " exploded, going into dead state ");
        wakeUp();
        System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " reconnected to lobby ");
    }

    /**
     * Wakes up a pending-connection ClientHandler.
     */
    public void wakeUp() {
        synchronized (this.pendingLock) {
            this.pendingConnection = false;
            this.pendingLock.notifyAll();
        }
    }


    /**
     * Used in the Observer Pattern, forwards a specified message to the Client.
     * If the specified message is a MSG_Stop, interrupts the ClientHandler which will then go into a GAME_OVER condition.
     *
     * @param message The message to send to the handled Client.
     */
    @Override
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

    public boolean rejoinLobby(int lobbyNumber, String inputNickname) {
        this.nickname = inputNickname;
        this.lobby = Lobby.getLobby(lobbyNumber);
        try {
            if (this.lobby == null) {
                send(new MSG_ERROR("Lobby not found"));
                closeStreams();
                return false;
            }

            ClientHandler handler = lobby.findPendingClientHandler(inputNickname);
            if (handler == null) {
                send(new MSG_ERROR("Found no player that needs to reconnect with that nickname"));
                closeStreams();
                return false;
            }

            send(new MSG_OK_REJOIN(handler.getNickname()));
            handler.substituteStreams(this.clientSocket, this.inputStream, this.objectInputStream, this.outputStream, this.objectOutputStream);
            System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " reconnected to lobby, giving up ");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean joinLobby(int lobbyNumber, String inputNickname) {
        try {
            this.lobby = Lobby.getLobby(lobbyNumber);
            if (this.lobby == null) {
                send(new MSG_ERROR("Lobby not found"));
                closeStreams();
                return false;
            }
            synchronized (lobby) {
                this.nickname = this.lobby.onJoin(inputNickname, this);
                if (this.nickname != null) {
                    send(new MSG_OK_JOIN(this.nickname));
                    System.out.println(this.nickname + " joined lobby " + lobbyNumber);
                } else {
                    System.out.println("[" + Thread.currentThread().getName() + "] - " + inputNickname + " looked for a non existing lobby");
                    send(new MSG_ERROR("Lobby full!"));
                    closeStreams();
                    return false;
                }

                if (lobby.getNumberOfPresentPlayers() == lobby.getLobbyMaxPlayers()) {
                    System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " reached full lobby, lobby init and notify() " + lobbyNumber);
                    lobby.init();
                    lobby.notifyAll();
                } else {
                    System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " waiting on lobby " + lobbyNumber);
                    while (!lobby.isDeleted() && !lobby.isStarted()) lobby.wait();
                    System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " awoken on lobby " + lobbyNumber);
                }

            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public boolean createLobby(String inputNickname, int numOfPlayers) {
        this.nickname = inputNickname;

        boolean found = false;
        int i;
        int lobbyMaxSize = 500;

        try {
            if (numOfPlayers < 1 || numOfPlayers > 4) {
                System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " tried to create a lobby with a wrong number of player");
                send(new MSG_ERROR("The number of players is not correct!"));
                closeStreams();
                return false;
            }
            if (Lobby.getLobbies().size() >= lobbyMaxSize) {
                System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " tried to create a lobby, but there are too many lobbies");
                send(new MSG_ERROR("Too many lobbies!"));
                closeStreams();
                return false;
            }

            do {
                i = random.nextInt(lobbyMaxSize);

                found = Lobby.checkLobbies(i);
            } while (found);

            this.lobby = new Lobby(i, numOfPlayers);
            synchronized (lobby) {
                this.nickname = this.lobby.onJoin(inputNickname, this);

                Lobby.addLobby(this.lobby);

                System.out.println(this.nickname + " created lobby " + i);
                this.send(new MSG_OK_CREATE(i, this.nickname));

                if (numOfPlayers == 1) {
                    System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " Solo lobby detected and init " + i);
                    lobby.init();
                } else {
                    System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " Created a lobby, waiting on lobby " + i);
                    while (!lobby.isDeleted() && !lobby.isStarted()) lobby.wait();
                    System.out.println("[" + Thread.currentThread().getName() + "] - " + this.nickname + " awoken on lobby " + i);
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}