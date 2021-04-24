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

        for(ClientHandler clientHandler : threadsList){
            game.addAllObservers(clientHandler);
            gameManager.getFaithTrackManager().addObserver(clientHandler);
        }

        gameManager.setStatus(Status.INIT);
        for(int i = 0; i<lobbyMaxPlayers; i++) {
            game.addPlayer(nicknameList.get(i), playerNumbers.get(i));
        }

        //


        game.getLeaderCardsObject().setEnabled(true);
        game.getLeaderCardsObject().setCards(game.getLeaderCardsDeck().pickFourCards());
    }

    public void onMessage(Message message, String nickname) throws IllegalArgumentException{
        gameManager.resetErrorObject();
        Player player = gameManager.currentPlayer();
        boolean order66 = false;

        boolean result = false;
        if(player.getNickname().equals(nickname) && gameManager.getStatus()!= Status.GAME_OVER) {
            switch (message.getMessageType()) {
                case MSG_INIT_CHOOSE_RESOURCE:
                    result = actionManager.chooseResource(player, (MSG_INIT_CHOOSE_RESOURCE) message);
                    break;
                case MSG_INIT_CHOOSE_LEADERCARDS:
                    result = actionManager.chooseLeaderCard(player, (MSG_INIT_CHOOSE_LEADERCARDS) message);
                    break;
                case MSG_ACTION_ACTIVATE_LEADERCARD:
                    result = actionManager.activateLeaderCard(player, (MSG_ACTION_ACTIVATE_LEADERCARD) message);
                    break;
                case MSG_ACTION_DISCARD_LEADERCARD:
                    result = actionManager.discardLeaderCard(player, (MSG_ACTION_DISCARD_LEADERCARD) message);
                    break;
                case MSG_ACTION_CHANGE_DEPOT_CONFIG:
                    result = actionManager.changeDepotConfig(player, (MSG_ACTION_CHANGE_DEPOT_CONFIG) message);
                    break;
                case MSG_ACTION_ACTIVATE_PRODUCTION:
                    result = actionManager.activateProduction(player, (MSG_ACTION_ACTIVATE_PRODUCTION) message);
                    break;
                case MSG_ACTION_BUY_DEVELOPMENT_CARD:
                    result = actionManager.buyDevelopmentCard(player);
                    break;
                case MSG_ACTION_GET_MARKET_RESOURCES:
                    result = actionManager.getMarketResources(player, (MSG_ACTION_GET_MARKET_RESOURCES) message);
                    break;
                case MSG_ACTION_MARKET_CHOICE:
                    result = actionManager.newChoiceMarket(player, (MSG_ACTION_MARKET_CHOICE) message);
                    break;
                case MSG_ACTION_CHOOSE_DEVELOPMENT_CARD:
                    result = actionManager.chooseDevelopmentCard(player, (MSG_ACTION_CHOOSE_DEVELOPMENT_CARD) message);
                    break;
                case MSG_ACTION_ENDTURN:
                    if(solo) {
                        result = actionManager.lorenzoMove();
                    }
                    order66 = gameManager.endTurn();
                    break;
                default: System.out.println(" SRV: non so cosa mi abbiano inviato aiuto.");
            }

            if(result) {  //if Result is true, then WE MUST END THE "UPDATE MODEL" PHASE!!!!!!!!!!!!!!!!!!!!
                for (ClientHandler c : threadsList) {
                    c.send(new MSG_UPD_End());
                }
            }
        }

        //if(order66) threadsList.stream().forEach();
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

    public int getLobbyMaxPlayers() { return this.lobbyMaxPlayers; }

    public int getLobbyNumber() { return this.lobbyNumber; }

    public synchronized int getNumOfPlayers() { return this.nicknameList.size(); }

    public int getNumberOfPresentPlayers() {
        assert ( this.socketList.size() == this.nicknameList.size());
        return this.socketList.size();
    }

    public static synchronized boolean checkLobbies(int i){
        for(Lobby l : lobbies){
            if(l.getLobbyNumber()==i) return false;
        }
        return true;
    }

    public static synchronized Lobby getLobby(int n){
        for(Lobby lobby : lobbies){
            if(lobby.getLobbyNumber()== n) return lobby;
        }
        return null;
    }

    public static synchronized void addLobby(Lobby lobby){
        Lobby.lobbies.add(lobby);
    }

    public static synchronized ArrayList<Lobby> getLobbies(){
        return Lobby.lobbies;
    }

    public static synchronized void remove(Lobby lobby){Lobby.lobbies.remove(lobby);}
}
