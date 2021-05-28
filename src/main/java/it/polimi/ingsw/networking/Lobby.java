package it.polimi.ingsw.networking;

import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_Full;
import it.polimi.ingsw.server.controller.ActionManager;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;

import java.net.Socket;
import java.util.*;


public class Lobby {
    private static final List<Lobby> lobbies = new ArrayList<>();
    private final int lobbyNumber;
    private final int lobbyMaxPlayers;
    List<Socket> socketList;
    List<String> nicknameList;
    List<ClientHandler> clientHandlers;
    boolean solo;
    private GameManager gameManager;
    private ActionManager actionManager;
    private boolean started;
    private boolean deleted;

    /**
     * Constructs a new Lobby with the specified capacity and number.
     * Initializes the lobby as not deleted and not started.
     * @param lobbyNumber the lobby number
     * @param lobbyMaxPlayers the capacity of the lobby
     */
    public Lobby(int lobbyNumber, int lobbyMaxPlayers) {
        solo = (lobbyMaxPlayers == 1);
        this.socketList = new LinkedList<>();
        this.nicknameList = new LinkedList<>();
        this.clientHandlers = new LinkedList<>();
        this.lobbyNumber = lobbyNumber;
        this.lobbyMaxPlayers = lobbyMaxPlayers;

        this.started = false;
        this.deleted = false;
    }

    /**
     * Checks if there's already a Lobby with the specified lobbyNumber
     * @param lobbyNumber the number of the lobby to search
     * @return true if there's already a Lobby with the same lobby number, false if there's none
     */
    public static synchronized boolean checkLobbies(int lobbyNumber) {
        for (Lobby l : lobbies) {
            if (l.getLobbyNumber() == lobbyNumber) return true;
        }
        return false;
    }

    /**
     * Searches for the Lobby which has the specified lobbyNumber
     * @param lobbyNumber the number of the lobby to search
     * @return the Lobby reference with such lobbyNumber, or null if there's none
     */
    public static synchronized Lobby getLobby(int lobbyNumber) {
        for (Lobby lobby : lobbies) {
            if (lobby.getLobbyNumber() == lobbyNumber) return lobby;
        }
        return null;
    }

    /**
     * Adds the specified Lobby to the common Lobby list
     * Starts a CountDownThread which will eliminate the Lobby after some time, if the Lobby has not been started yet
     * @param lobby the lobby reference to be added
     */
    public static synchronized void addLobby(Lobby lobby) {
        Lobby.lobbies.add(lobby);
        Lobby.createCountDownThread(lobby);
    }

    /**
     * Creates the CountDownThread thread specified in the method above (addLobby)
     * The interval of time to deletion can be set here
     * @param lobby the lobby reference
     */
    public static void createCountDownThread(Lobby lobby) {
        new Thread(new CountDownThread(lobby, 30)).start();
    }

    /**
     * @return the common list of Lobbies
     */
    public static synchronized List<Lobby> getLobbies() {
        return Lobby.lobbies;
    }

    /**
     * removes the specified lobby from the common list of Lobbies
     * @param lobby the lobby to remove
     */
    public static synchronized void removeLobby(Lobby lobby) {
        Lobby.lobbies.remove(lobby);
    }

    /**
     * Initialization of a specific Lobby object:
     *   The started variable is set to true, which affects the CountDownThread behavior
     *   The Controller and the Model are instantiated
     *   The Players are shuffled, given a random number, and added to the model
     *   The LeaderCardPicker is set up
     *   Sets up the observer pattern (ClientHandler -> Model)
     */
    public synchronized void init() {
        this.started = true;
        this.gameManager = new GameManager(this.lobbyMaxPlayers);
        this.actionManager = gameManager.getActionManager();
        List<Integer> playerNumbers = new LinkedList<>();

        for (int i = 1; i <= lobbyMaxPlayers; i++) {
            playerNumbers.add(i);
        }

        Collections.shuffle(playerNumbers);
        Game game = gameManager.getGame();

        for (int i = 0; i < lobbyMaxPlayers; i++) {
            game.addPlayer(nicknameList.get(i), playerNumbers.get(i));
        }

        game.setLeaderCardsPickerCards(game.getCurrentPlayerStartingCards());
        game.setLeaderCardsPickerEnabled(true);

        for (ClientHandler c : clientHandlers) {
            gameManager.addAllObserver(c);
        }
    }

    /**
     * Passes the specified Message from the ClientHandler to the ActionManager
     * A message can only be passed if the receiving ClientHandler handles the currentPlayer
     * @param message the message received from the client
     */
    public synchronized void onMessage(Message message) {
        synchronized (actionManager) {
            actionManager.onMessage(message);
        }
    }

    /**
     * Links a specified ClientHandler to this Lobby object
     * Checks if another player in this Lobby has the same nickname as the given nickname,
     *  in that case the method will add a progressive number after the specified nickname
     * @param nickname the name of the Player associated with the ClientHandler
     * @param socket the socket operated by the ClientHandler
     * @param clientHandler the ClientHandler reference
     * @return a String containing the new nickname:
     *    - nickname (not modified) if the player is the only one with such nickname
     *    - nickname (1) if there's already a player with such nickname
     *    - nickname (2) if there are already two players with such nickname
     *    - nickname (3) if there are already three players with such nickname
     *    - null if this method gets called while the lobby is at full capacity
     */
    public synchronized String onJoin(String nickname, Socket socket, ClientHandler clientHandler) {
        if (this.lobbyMaxPlayers > nicknameList.size()) {
            clientHandlers.add(clientHandler);
            socketList.add(socket);
            String newNickname = nickname;

            if (nicknameList.stream().anyMatch(n -> n.equals(nickname))) {
                if (nicknameList.stream().anyMatch(n -> n.equals(nickname + " (1)"))) {
                    if (nicknameList.stream().anyMatch(n -> n.equals(nickname + " (2)"))) {
                        newNickname = nickname + " (3)";
                    } else {
                        newNickname = nickname + " (2)";
                    }
                } else {
                    newNickname = nickname + " (1)";
                }
            }
            nicknameList.add(newNickname);
            return newNickname;
        } else return null;
    }

    /**
     * Adds a player, specified by his number, to the IdlePlayer list
     * @param playerNumber the number of the player who's gone Idle/Disconnected
     */
    public synchronized void addIdlePlayer(Integer playerNumber) {
        gameManager.addIdlePlayer(playerNumber);
    }

    /**
     * Removes a player, specified by his number, from the IdlePlayer list
     * @param playerNumber the number of the player who's not Idle/Disconnected anymore
     */
    public void removeIdlePlayer(Integer playerNumber) {
        gameManager.removeIdlePlayer(playerNumber);
    }

    /**
     * Disconnects a player, specified by his nickname, and consequentially
     *  invokes the Controller method to do so
     * @param nickname the nickname of the player who's gone Idle/Disconnected
     */
    public void disconnectPlayer(String nickname) {
        Player player = gameManager.getGame().getPlayer(nickname);
        int currentPlayerNumber = gameManager.getGame().getCurrentPlayerInt();
        synchronized (actionManager) {
            actionManager.disconnectPlayer(player, currentPlayerNumber);
        }

    }

    /**
     * Searches for a ClientHandler which is in a pending-reconnection state and has the same nickname as the given one
     * @param nickname the nickname of the player searched
     * @return the ClientHandler reference if there's one in such state in this Lobby, or null if there's none
     */
    public synchronized ClientHandler findPendingClientHandler(String nickname) {
        Optional<ClientHandler> result = this.clientHandlers.stream().filter(ClientHandler::isPendingConnection).sorted(
                (a, b) -> Integer.compare(b.getNickname().length(), a.getNickname().length())).filter(x -> x.getNickname().startsWith(nickname)).findFirst();
        return result.orElse(null);
    }

    /**
     * @return the capacity of the Lobby
     */
    public int getLobbyMaxPlayers() {
        return this.lobbyMaxPlayers;
    }

    /**
     * @return the number of the Lobby
     */
    public int getLobbyNumber() {
        return this.lobbyNumber;
    }

    /**
     * @return the number of players actually connected to the lobby
     * note: this method does not concerns the Idle/Disconnected players, they are still counted.
     */
    public int getNumberOfPresentPlayers() {
        assert (this.socketList.size() == this.nicknameList.size());
        return this.socketList.size();
    }

    /**
     * Searches the number of a player who has the specified nickname
     * @param nickname the nickname of the specified player
     * @return the number of the player with the specified nickname
     * note: this method is called by the ClientHandler, whose nicknames are surely present.
     */
    public synchronized int whoIs(String nickname) {
        return gameManager.getGame().getPlayer(nickname).getPlayerNumber();
    }

    /**
     * @return the number of the currentPlayer
     */
    public synchronized int currentPlayer() {
        return gameManager.getGame().getCurrentPlayerInt();
    }

    /**
     * Generates a MSG_UPD_Full message containing the entire state of the Game
     * @return the MSG_UPD_Full message
     */
    public synchronized MSG_UPD_Full getFullModel() {
        return gameManager.getFullModel();
    }

    /**
     * @return true if the Lobby is started (after init()), false otherwise
     */
    public synchronized boolean isStarted() {
        return this.started;
    }

    /**
     * @return true if the Lobby has been deleted, false otherwise
     */
    public synchronized boolean isDeleted() {
        return this.deleted;
    }

    /**
     * sets the deleted status of the Lobby
     * @param deleted the value to set
     */
    public synchronized void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Wakes up all the ClientHandlers that are in pending-connection state
     */
    public synchronized void wakeUpAllPendingClientHandlers() {
        for (ClientHandler c : clientHandlers) {
            if (c.isPendingConnection()) {
                c.wakeUp();
            }
        }
        clientHandlers.clear();
    }

    /**
     * @return true if the Game has ended, false if not
     */
    public boolean isGameOver() {
        return gameManager.isGameOver();
    }

    /**
     * Method used by a newly reconnected ClientHandler to notify other players that his player has reconnected to the Game
     * @param nickname the nickname of the player who's reconnected to the Game
     */
    public void notifyReconnection(String nickname) {
        actionManager.notifyReconnection(nickname);
    }

    /**
     * @return true if all players are Idle/Disconnected, false otherwise
     */
    public boolean areAllPlayersIdle() {
        return gameManager.areAllPlayersIdle();
    }
}