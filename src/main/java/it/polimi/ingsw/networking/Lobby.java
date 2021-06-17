package it.polimi.ingsw.networking;

import it.polimi.ingsw.networking.message.actionMessages.ActionMessage;
import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_Full;
import it.polimi.ingsw.server.controller.ActionManager;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;

import java.util.*;

/**
 * The Lobby handles multiple ClientHandlers.
 * <p>
 * It gives the ClientHandler many managing services.
 * It handles the reference to the Sole Controller and Sole Model.
 * By design ClientHandlers are not allowed to invoke directly the Controller, and uses the Lobby to do so.
 */
public class Lobby {
    /**
     * A shared List of the Lobbies currently stored in the memory.
     */
    private static final List<Lobby> lobbies = new ArrayList<>();
    /**
     * the number of this Lobby
     */
    private final int lobbyNumber;
    /**
     * the capacity of this Lobby
     */
    private final int lobbyMaxPlayers;
    /**
     * a List containing the nicknames of all the connected players
     */
    private final List<String> nicknameList;
    /**
     * a List containing the references to the ClientHandlers of all the connected players
     */
    private final List<ClientHandler> clientHandlers;
    /**
     * the Reference to the Sole GameManager
     */
    private GameManager gameManager;
    /**
     * the Reference to the Sole ActionManager
     */
    private ActionManager actionManager;
    /**
     * True after init() and False before
     */
    private boolean started;
    /**
     * False before any entity (for example the CountDownThread) eliminates it. Must be set to True otherwise.
     * Usually is False through all the Game
     */
    private boolean deleted;

    /**
     * Constructs a new Lobby with the specified capacity and number.
     * Initializes the lobby as not deleted and not started.
     *
     * @param lobbyNumber     the Lobby number
     * @param lobbyMaxPlayers the capacity of the Lobby
     */
    public Lobby(int lobbyNumber, int lobbyMaxPlayers) {
        this.nicknameList = new LinkedList<>();
        this.clientHandlers = new LinkedList<>();
        this.lobbyNumber = lobbyNumber;
        this.lobbyMaxPlayers = lobbyMaxPlayers;

        this.started = false;
        this.deleted = false;
    }

    /**
     * Checks if there's already a Lobby with the specified lobbyNumber.
     *
     * @param lobbyNumber The number of the lobby to search.
     * @return True if there's already a Lobby with the same lobby number, False if there's none.
     */
    public static synchronized boolean checkLobbies(int lobbyNumber) {
        for (Lobby l : lobbies) {
            if (l.getLobbyNumber() == lobbyNumber) return true;
        }
        return false;
    }

    /**
     * Searches for the Lobby which has the specified lobbyNumber.
     *
     * @param lobbyNumber The number of the Lobby to search.
     * @return The Lobby reference with such lobbyNumber, or null if there's none.
     */
    public static synchronized Lobby getLobby(int lobbyNumber) {
        for (Lobby lobby : lobbies) {
            if (lobby.getLobbyNumber() == lobbyNumber) return lobby;
        }
        return null;
    }

    /**
     * Adds the specified Lobby to the static Lobby List.
     * Starts a CountDownThread which will eliminate the Lobby after some time, if the Lobby has not been started yet.
     *
     * @param lobby The reference to the Lobby to be added.
     */
    public static synchronized void addLobby(Lobby lobby) {
        Lobby.lobbies.add(lobby);
        Lobby.createCountDownThread(lobby);
    }

    /**
     * Creates the CountDownThread Thread related to a specified Lobby.
     * The interval of time to deletion can be set here.
     *
     * @param lobby the reference to this Lobby
     * @see #addLobby(Lobby) the method that calls this method
     */
    public static void createCountDownThread(Lobby lobby) {
        new Thread(new CountDownThread(lobby, 60)).start();
    }

    /**
     * Returns the static Lobby List.
     *
     * @return the static Lobby List
     */
    public static synchronized List<Lobby> getLobbies() {
        return Lobby.lobbies;
    }

    /**
     * Removes the specified Lobby from the static Lobby List.
     *
     * @param lobby The reference to the Lobby to remove from the static Lobby List.
     */
    public static synchronized void removeLobby(Lobby lobby) {
        Lobby.lobbies.remove(lobby);
    }

    /**
     * Initializes a specific Lobby object.
     * The started variable is set to true, which affects the CountDownThread behavior.
     * The Controller and the Model are instantiated.
     * The Players are shuffled, given a random number, and added to the Model.
     * The LeaderCardPicker is set up.
     * Sets up the observer pattern (ClientHandler -> Model).
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
     * Passes the specified Message from the ClientHandler to the ActionManager.
     * <p>
     * A message can only be passed if the receiving ClientHandler handles the currentPlayer.
     * If this method is called by the non-currentPlayer, unpredictable behaviour is surely gonna happen.
     *
     * @param message the ActionMessage received by the ClientHandler from the Client.
     */
    public synchronized void onMessage(ActionMessage message) {
        synchronized (actionManager) {
            actionManager.onMessage(message);
        }
    }

    /**
     * Links a specified ClientHandler to this Lobby object.
     * Checks if another player in this Lobby has the same nickname as the given nickname,
     * in that case the method will add a progressive number after the specified nickname.
     *
     * @param nickname      The name of the Player associated with the ClientHandler.
     * @param clientHandler The ClientHandler reference.
     * @return a String containing the new nickname:
     * <ul>
     * <li> nickname (not modified) if the player is the only one with such nickname
     * <li> nickname (1) if there's already a player with such nickname
     * <li> nickname (2) if there are already two players with such nickname
     * <li> nickname (3) if there are already three players with such nickname
     * <li> null if this method gets called while the lobby is at full capacity.
     */
    public synchronized String onJoin(String nickname, ClientHandler clientHandler) {
        final int maxPlayerNameSize = 15;
        if (this.lobbyMaxPlayers > nicknameList.size()) {
            if (nickname.length() > maxPlayerNameSize)
                nickname = nickname.substring(0, maxPlayerNameSize - 1);

            clientHandlers.add(clientHandler);
            String newNickname = nickname;

            String finalNickname = nickname;
            if (nicknameList.stream().anyMatch(n -> n.equals(finalNickname))) {
                if (nicknameList.stream().anyMatch(n -> n.equals(finalNickname + " (1)"))) {
                    if (nicknameList.stream().anyMatch(n -> n.equals(finalNickname + " (2)"))) {
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
     * Adds a player, specified by his number, to the IdlePlayer list.
     *
     * @param playerNumber The number of the player who's gone Idle/Disconnected.
     */
    public synchronized void addIdlePlayer(Integer playerNumber) {
        gameManager.addIdlePlayer(playerNumber);
    }

    /**
     * Removes a player, specified by his number, from the IdlePlayer list.
     *
     * @param playerNumber The number of the player who's not Idle/Disconnected anymore.
     */
    public void removeIdlePlayer(Integer playerNumber) {
        gameManager.removeIdlePlayer(playerNumber);
    }

    /**
     * Disconnects a player, specified by his nickname, and consequentially
     * invokes the related Controller method disconnectPlayer().
     *
     * @param nickname The nickname of the player who's gone Idle/Disconnected.
     */
    public void disconnectPlayer(String nickname) {
        Player player = gameManager.getGame().getPlayer(nickname);
        int currentPlayerNumber = gameManager.getGame().getCurrentPlayerInt();
        synchronized (actionManager) {
            actionManager.disconnectPlayer(player, currentPlayerNumber);
        }

    }

    /**
     * Searches for a ClientHandler which is in a pending-reconnection state and has the same nickname as the given one.
     *
     * @param nickname The nickname of the player searched.
     * @return The ClientHandler reference if there's one in such state in this Lobby, or null if there's none.
     */
    public synchronized ClientHandler findPendingClientHandler(String nickname) {
        Optional<ClientHandler> result = this.clientHandlers.stream().filter(ClientHandler::isPendingConnection).sorted(
                (a, b) -> Integer.compare(b.getNickname().length(), a.getNickname().length())).filter(x -> x.getNickname().startsWith(nickname)).findFirst();
        return result.orElse(null);
    }

    /**
     * Returns the capacity of the Lobby.
     *
     * @return The capacity of the Lobby.
     */
    public int getLobbyMaxPlayers() {
        return this.lobbyMaxPlayers;
    }

    /**
     * Returns the number of the Lobby.
     *
     * @return The number of the Lobby.
     */
    public int getLobbyNumber() {
        return this.lobbyNumber;
    }

    /**
     * Returns the number of players that have connected to the Lobby.
     * Note that this method does not concern the Idle/Disconnected players, as they are still counted.
     *
     * @return The number of players that have connected to the Lobby.
     */
    public int getNumberOfPresentPlayers() {
        return this.nicknameList.size();
    }

    /**
     * Searches the number of a player who has the specified nickname.
     * Note: this method is called by the ClientHandler, whose nicknames are surely present.
     *
     * @param nickname The nickname of the specified Player.
     * @return The number of the player with the specified nickname.
     */
    public synchronized int whoIs(String nickname) {
        return gameManager.getGame().getPlayer(nickname).getPlayerNumber();
    }

    /**
     * Return the number of the current Player.
     *
     * @return The number of the current Player.
     */
    public synchronized int currentPlayer() {
        return gameManager.getGame().getCurrentPlayerInt();
    }

    /**
     * Generates a MSG_UPD_Full message containing the entire current-state of the Game.
     *
     * @return A MSG_UPD_Full message.
     */
    public synchronized MSG_UPD_Full getFullModel() {
        return gameManager.getFullModel();
    }

    /**
     * Returns the started status of the Lobby.
     * After a Lobby.init(), this will return True.
     *
     * @return True if the Lobby is started, False otherwise.
     */
    public synchronized boolean isStarted() {
        return this.started;
    }

    /**
     * Returns the deleted status of the Lobby.
     *
     * @return True if the Lobby has been deleted, False otherwise.
     */
    public synchronized boolean isDeleted() {
        return this.deleted;
    }

    /**
     * Sets the deleted status of the Lobby.
     *
     * @param deleted The boolean value to set.
     */
    public synchronized void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * Wakes up all the ClientHandlers that are in pending-connection state.
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
     * Returns the GameOver status of the GameManager.
     *
     * @return True if the Game has ended, False otherwise.
     */
    public boolean isGameOver() {
        return gameManager.isGameOver();
    }

    /**
     * Method used by a newly reconnected ClientHandler to notify other players that his player has reconnected to the Game.
     *
     * @param nickname The nickname of the player who's reconnected to the Game.
     */
    public void notifyReconnection(String nickname) {
        actionManager.notifyReconnection(nickname);
    }

    /**
     * Returns the status of the IdlePlayers List.
     *
     * @return True if all players are Idle/Disconnected, False otherwise.
     */
    public boolean areAllPlayersIdle() {
        return gameManager.areAllPlayersIdle();
    }
}