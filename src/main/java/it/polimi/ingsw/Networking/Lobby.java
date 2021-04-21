package it.polimi.ingsw.Networking;

import it.polimi.ingsw.Networking.Message.*;
import it.polimi.ingsw.Server.ClientHandler;
import it.polimi.ingsw.Server.Controller.ActionManager;
import it.polimi.ingsw.Server.Controller.FaithTrackManager;
import it.polimi.ingsw.Server.Controller.GameManager;
import it.polimi.ingsw.Server.Model.*;

import java.net.*;
import java.util.*;


public class Lobby {
    List<Socket> socketList;
    List<String> nicknameList;
    List<ClientHandler> threadsList;


    private GameManager gameManager;
    private ActionManager actionManager;
    private final static ArrayList<Lobby> lobbies = new ArrayList<>();
    int lobbyNumber;
    int lobbyMaxPlayers;



    public Lobby(String nickname, Socket socket,  ClientHandler clientHandler, int lobbyNumber, int lobbyMaxPlayers)
    {
        this.socketList = new LinkedList<>();
        this.nicknameList = new LinkedList<>();
        this.threadsList = new LinkedList<>();
        this.nicknameList.add(nickname);
        this.threadsList.add(clientHandler);
        this.socketList.add(socket);
        this.lobbyNumber = lobbyNumber;
        this.lobbyMaxPlayers = lobbyMaxPlayers;
    }

    public void init() {
        this.gameManager = new GameManager(this.lobbyMaxPlayers);
        this.actionManager = gameManager.getActionManager();
        List<Integer> playerNumbers = new LinkedList<>();

        for(int i = 0; i<lobbyMaxPlayers; i++) {
            playerNumbers.add(i);
        }

        Collections.shuffle(playerNumbers);
        Game game = gameManager.getGame();

        for(int i = 0; i<lobbyMaxPlayers; i++) {
            game.addPlayer(nicknameList.get(i), playerNumbers.get(i));
        }

        for(ClientHandler clientHandler : threadsList){
            game.addAllObservers(clientHandler);
        }
        game.getLeaderCardsObject().setEnabled(true);
        game.getLeaderCardsObject().setCards(game.getLeaderCardsDeck().pickFourCards());
    }

    public boolean onMessage(Message message, String nickname) throws IllegalArgumentException{
        gameManager.resetErrorObject();
        Player player = gameManager.currentPlayer();
        if(player.getNickname().equals(nickname)) {
            switch (message.getMessageType()) {
                case MSG_INIT_CHOOSE_RESOURCE:
                    return actionManager.chooseResource(player, (MSG_INIT_CHOOSE_RESOURCE) message);
                case MSG_INIT_CHOOSE_LEADERCARDS:
                    return actionManager.chooseLeaderCard(player, (MSG_INIT_CHOOSE_LEADERCARDS) message);
                case MSG_ACTION_ACTIVATE_LEADERCARD:
                    return actionManager.activateLeaderCard(player, (MSG_ACTION_ACTIVATE_LEADERCARD) message);
                case MSG_ACTION_DISCARD_LEADERCARD:
                    return actionManager.discardLeaderCard(player, (MSG_ACTION_DISCARD_LEADERCARD) message);
                case MSG_ACTION_CHANGE_DEPOT_CONFIG:
                    return actionManager.changeDepotConfig(player, (MSG_ACTION_CHANGE_DEPOT_CONFIG) message);
                case MSG_ACTION_ACTIVATE_PRODUCTION:
                    return actionManager.activateProduction(player, (MSG_ACTION_ACTIVATE_PRODUCTION) message);
                case MSG_ACTION_BUY_DEVELOPMENT_CARD:
                    return actionManager.buyDevelopmentCard(player);
                case MSG_ACTION_GET_MARKET_RESOURCES:
                    return actionManager.getMarketResources(player, (MSG_ACTION_GET_MARKET_RESOURCES) message);
                case MSG_ACTION_MARKET_CHOICE:
                    return actionManager.newChoiceMarket(player, (MSG_ACTION_MARKET_CHOICE) message);
                case MSG_ACTION_CHOOSE_DEVELOPMENT_CARD:
                    return actionManager.chooseDevelopmentCard(player, (MSG_ACTION_CHOOSE_DEVELOPMENT_CARD) message);
                case MSG_ACTION_ENDTURN:
                    gameManager.endTurn();
                    return true;
            }
        }
        throw new IllegalArgumentException();
    }

    public String onJoin(MSG_JOIN_LOBBY message, Socket socket ,ClientHandler clientHandler){
        if(this.lobbyMaxPlayers > nicknameList.size()) {
            threadsList.add(clientHandler);
            socketList.add(socket);
            final String nickname = message.getNickname();
            String newNickname = message.getNickname();

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
            if(this.lobbyMaxPlayers == nicknameList.size()) init();
            return newNickname;
        }
        else return null;
    }

    public int getLobbyMaxPlayers() { return this.lobbyMaxPlayers; }

    public int getLobbyNumber() { return this.lobbyNumber; }

    public int getNumberOfPresentPlayers() {
        assert ( this.socketList.size() == this.nicknameList.size());
        return this.socketList.size();
    }

    public static boolean checkLobbies(int i){
        for(Lobby l : lobbies){
            if(l.getLobbyNumber()==i) return false;
        }
        return true;
    }

    public static Lobby getLobby(int n){
        for(Lobby lobby : lobbies){
            if(lobby.getLobbyNumber()== n) return lobby;
        }
        return null;
    }

    public static void addLobby(Lobby lobby){
        Lobby.lobbies.add(lobby);
    }

    public static ArrayList<Lobby> getLobbies(){
        return Lobby.lobbies;
    }
}
