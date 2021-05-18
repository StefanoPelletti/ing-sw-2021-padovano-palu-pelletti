package it.polimi.ingsw.Networking;

import it.polimi.ingsw.Networking.Message.*;
import it.polimi.ingsw.Networking.Message.UpdateMessages.*;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.Controller.ActionManager;
import it.polimi.ingsw.Server.Controller.GameManager;
import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.Model.Enumerators.Status;

import java.net.*;
import java.util.*;


public class Lobby {
    List<Socket> socketList;
    List<String> nicknameList;
    List<ClientHandler> clientHandlers;

    private GameManager gameManager;
    private ActionManager actionManager;
    private final static ArrayList<Lobby> lobbies = new ArrayList<>();
    private final int lobbyNumber;
    private final int lobbyMaxPlayers;
    private boolean started;
    private boolean deleted;

    boolean solo;

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

        if (lobbyMaxPlayers == 1) gameManager.setStatus(Status.SOLO);
        else gameManager.setStatus(Status.INIT_1);

        game.getLeaderCardsObject().setCards(game.getCurrentPlayer().getStartingCards());
        game.getLeaderCardsObject().setEnabled(true);

        for (ClientHandler c : clientHandlers) {
            gameManager.addAllObserver(c);
        }
    }

    public synchronized void onMessage(Message message) {
        synchronized (actionManager) {
            actionManager.onMessage(message);
        }
    }

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

    public synchronized void addIdlePlayer(Integer playerNumber) {
        gameManager.addIdlePlayer(playerNumber);
    }

    public void removeIdlePlayer(Integer playerNumber) {
        gameManager.removeIdlePlayer(playerNumber);
    }

    public void disconnectPlayer(String nickname) {
        Player player = gameManager.getGame().getPlayer(nickname);
        int currentPlayerNumber = gameManager.getGame().getCurrentPlayerInt();
        synchronized (actionManager) {
            actionManager.disconnectPlayer(player, currentPlayerNumber);
        }

    }


    public synchronized ClientHandler findPendingClientHandler(String nickname) {
        Optional<ClientHandler> result = this.clientHandlers.stream().filter(ClientHandler::isPendingConnection).sorted(
                (a, b) -> Integer.compare(b.getNickname().length(), a.getNickname().length())).filter(x -> x.getNickname().startsWith(nickname)).findFirst();
        return result.orElse(null);
    }

    public int getLobbyMaxPlayers() {
        return this.lobbyMaxPlayers;
    }

    public int getLobbyNumber() {
        return this.lobbyNumber;
    }

    public int getNumberOfPresentPlayers() {
        assert (this.socketList.size() == this.nicknameList.size());
        return this.socketList.size();
    }

    public synchronized int getNumOfPlayers() {
        return this.nicknameList.size();
    }

    public static synchronized boolean checkLobbies(int i) {
        for (Lobby l : lobbies) {
            if (l.getLobbyNumber() == i) return true;
        }
        return false;
    }

    public synchronized int whoIs(String nickname) {
        return gameManager.getGame().getPlayer(nickname).getPlayerNumber();
    }

    public static synchronized Lobby getLobby(int n) {
        for (Lobby lobby : lobbies) {
            if (lobby.getLobbyNumber() == n) return lobby;
        }
        return null;
    }

    public synchronized int currentPlayer() {
        return gameManager.getGame().getCurrentPlayerInt();
    }

    public synchronized MSG_UPD_Full getFullModel() {
        return gameManager.getFullModel();
    }

    public static synchronized void addLobby(Lobby lobby) {
        Lobby.lobbies.add(lobby);
        Lobby.createCountDownThread(lobby);
    }

    public static synchronized ArrayList<Lobby> getLobbies() {
        return Lobby.lobbies;
    }

    public static synchronized void removeLobby(Lobby lobby) {
        Lobby.lobbies.remove(lobby);
    }

    public static void createCountDownThread(Lobby lobby) {
        new Thread(new CountDownThread(lobby, 30)).start();
    }

    public synchronized boolean isStarted() {
        return this.started;
    }

    public synchronized boolean isDeleted() {
        return this.deleted;
    }

    public synchronized void setStarted(boolean started) {
        this.started = started;
    }

    public synchronized void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public synchronized void wakeUpAllPendingClientHandlers() {
        for (ClientHandler c : clientHandlers) {
            if (c.isPendingConnection()) {
                c.wakeUp();
            }
        }
        clientHandlers.clear(); //not sure if it is safe to use like that, should check
    }

    public boolean isGameOver() {
        return gameManager.isGameOver();
    }

    public void notifyReconnection(String nickname) {
        actionManager.notifyReconnection(nickname);
    }

    public boolean areAllPlayersIdle() {
        return gameManager.areAllPlayersIdle();
    }
}