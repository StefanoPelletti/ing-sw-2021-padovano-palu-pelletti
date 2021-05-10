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
    List<ClientHandler> threadsList;

    public MessagePlatform messagePlatform;
    private GameManager gameManager;
    private ActionManager actionManager;
    private final static ArrayList<Lobby> lobbies = new ArrayList<>();
    int lobbyNumber;
    int lobbyMaxPlayers;

    boolean solo;

    public Lobby(int lobbyNumber, int lobbyMaxPlayers)
    {
        solo = (lobbyMaxPlayers == 1);
        this.socketList = new LinkedList<>();
        this.nicknameList = new LinkedList<>();
        this.threadsList = new LinkedList<>();
        this.lobbyNumber = lobbyNumber;
        this.lobbyMaxPlayers = lobbyMaxPlayers;

        this.messagePlatform = new MessagePlatform(lobbyMaxPlayers);
    }

    public void init() {
        this.gameManager = new GameManager(this.lobbyMaxPlayers);
        this.actionManager = gameManager.getActionManager();
        List<Integer> playerNumbers = new LinkedList<>();

        for(int i = 1; i<=lobbyMaxPlayers; i++) {
            playerNumbers.add(i);
        }

        Collections.shuffle(playerNumbers);
        Game game = gameManager.getGame();

        for(int i = 0; i<lobbyMaxPlayers; i++) {
            game.addPlayer(nicknameList.get(i), playerNumbers.get(i));
        }

        /*
        for(ClientHandler clientHandler : threadsList){
            game.addAllObservers(clientHandler);
            gameManager.getFaithTrackManager().addObserver(clientHandler);
        }
        */

        if(lobbyMaxPlayers == 1) gameManager.setStatus(Status.SOLO);
        else gameManager.setStatus(Status.INIT);

        game.getLeaderCardsObject().setCards(game.getLeaderCardsDeck().pickFourCards());
        game.getLeaderCardsObject().setEnabled(true);

        gameManager.addAllObserver(messagePlatform);
    }

    public boolean onMessage(Message message, String nickname) throws IllegalArgumentException{
        gameManager.resetErrorObject();
        Player player = gameManager.currentPlayer();

        if(player.getNickname().equals(nickname) && gameManager.getStatus()!= Status.GAME_OVER) {
            switch (message.getMessageType()) {
                case MSG_INIT_CHOOSE_LEADERCARDS:
                    return actionManager.chooseLeaderCard(player, (MSG_INIT_CHOOSE_LEADERCARDS) message);
                case MSG_INIT_CHOOSE_RESOURCE:
                    return actionManager.chooseResource(player, (MSG_INIT_CHOOSE_RESOURCE) message);
                case MSG_ACTION_ACTIVATE_LEADERCARD:
                    return actionManager.activateLeaderCard(player, (MSG_ACTION_ACTIVATE_LEADERCARD) message);
                case MSG_ACTION_DISCARD_LEADERCARD:
                    return actionManager.discardLeaderCard(player, (MSG_ACTION_DISCARD_LEADERCARD) message);
                case MSG_ACTION_CHANGE_DEPOT_CONFIG:
                    return actionManager.changeDepotConfig(player, (MSG_ACTION_CHANGE_DEPOT_CONFIG) message);
                case MSG_ACTION_ACTIVATE_PRODUCTION:
                    return actionManager.activateProduction(player, (MSG_ACTION_ACTIVATE_PRODUCTION) message);
                case MSG_ACTION_GET_MARKET_RESOURCES:
                    return actionManager.getMarketResources(player, (MSG_ACTION_GET_MARKET_RESOURCES) message);
                case MSG_ACTION_MARKET_CHOICE:
                    return actionManager.newChoiceMarket(player, (MSG_ACTION_MARKET_CHOICE) message);
                case MSG_ACTION_BUY_DEVELOPMENT_CARD:
                    return actionManager.buyDevelopmentCard(player);
                case MSG_ACTION_CHOOSE_DEVELOPMENT_CARD:
                    return actionManager.chooseDevelopmentCard(player, (MSG_ACTION_CHOOSE_DEVELOPMENT_CARD) message);
                case MSG_ACTION_ENDTURN:
                    return actionManager.endTurn(player);
                default:
                    System.out.println(" SRV: help I don't know what they sent me.");
            }
        }
        return false;
    }

    public synchronized String onJoin(String nickname, Socket socket ,ClientHandler clientHandler){
        if(this.lobbyMaxPlayers > nicknameList.size()) {
            threadsList.add(clientHandler);
            socketList.add(socket);
            String newNickname = nickname;

            if(nicknameList.stream().anyMatch(n-> n.equals(nickname))){
                if(nicknameList.stream().anyMatch(n-> n.equals(nickname+" (1)"))){
                    if(nicknameList.stream().anyMatch(n-> n.equals(nickname+" (2)"))){
                        newNickname= nickname +" (3)";
                    }
                    else{
                        newNickname= nickname +" (2)";
                    }
                }
                else{
                    newNickname= nickname +" (1)";
                }
            }
            nicknameList.add(newNickname);
            return newNickname;
        }
        else return null;
    }


    public void addIdlePlayer(Integer playerNumber)
    {
        gameManager.addIdlePlayer(playerNumber);
    }

    public void removeIdlePlayer(Integer playerNumber)
    {
        gameManager.removeIdlePlayer(playerNumber);
    }

    public void disconnectPlayer(String nickname)
    {
        Player player = gameManager.getGame().getPlayer(nickname);
        if(player.equals( gameManager.currentPlayer() ))
            actionManager.disconnectPlayer(player, true);
        else
            actionManager.disconnectPlayer(player, false);
    }


    public synchronized ClientHandler findPendingClientHandler(String nickname)
    {
        return this.threadsList.stream().filter(ClientHandler::getPendingConnection).sorted(
                (a,b) -> {
                    return Integer.compare(b.getNickname().length(), a.getNickname().length());
                }).filter( x -> x.getNickname().startsWith(nickname)).findFirst().get();
    }

    public int getLobbyMaxPlayers() { return this.lobbyMaxPlayers; }

    public int getLobbyNumber() { return this.lobbyNumber; }

    public int getNumberOfPresentPlayers() {
        assert ( this.socketList.size() == this.nicknameList.size());
        return this.socketList.size();
    }

    public synchronized int getNumOfPlayers() { return this.nicknameList.size(); }

    public static synchronized boolean checkLobbies(int i){
        for(Lobby l : lobbies){
            if(l.getLobbyNumber()==i) return true;
        }
        return false;
    }

    public synchronized int whoIs(String nickname)
    {
        return gameManager.getGame().getPlayer(nickname).getPlayerNumber();
    }

    public static synchronized Lobby getLobby(int n){
        for(Lobby lobby : lobbies){
            if(lobby.getLobbyNumber()== n) return lobby;
        }
        return null;
    }

    public synchronized int currentPlayer()
    {
        return gameManager.getGame().getCurrentPlayerInt();
    }

    public synchronized MSG_UPD_Full getFullModel()
    {
        return gameManager.getFullModel();
    }

    public static synchronized void addLobby(Lobby lobby){
        Lobby.lobbies.add(lobby);
    }

    public static synchronized ArrayList<Lobby> getLobbies(){
        return Lobby.lobbies;
    }

    public static synchronized void removeLobby(Lobby lobby){Lobby.lobbies.remove(lobby);}
}
