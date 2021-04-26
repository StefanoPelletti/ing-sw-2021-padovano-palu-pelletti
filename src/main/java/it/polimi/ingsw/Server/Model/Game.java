package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_Game;
import it.polimi.ingsw.Server.Model.Enumerators.Status;
import it.polimi.ingsw.Server.Model.Middles.*;
import it.polimi.ingsw.Server.Model.SpecialAbilities.ExtraDepot;
import it.polimi.ingsw.Server.Utils.*;
import java.util.ArrayList;
import java.util.Optional;

public class Game extends ModelObservable {
    private Status status;
    private Player firstPlayer;
    private int turn;
    private int blackCrossPosition;

    private int currentPlayer;

    private final ActionTokenStack actionTokenStack;
    private final ArrayList<Player> playerList;
    private final LeaderCardsDeck leaderCardsDeck;
    private final Market market;
    private final FaithTrack faithTrack;
    private final DevelopmentCardsDeck developmentCardsDeck;
    private final MarketHelper marketHelper;
    private final DevelopmentCardsVendor developmentCardsVendor;
    private final ErrorObject errorObject;
    private final LeaderCardsObject leaderCardsObject;
    private final ResourceObject resourceObject;
    private final LeaderBoard leaderBoard;

    public Game()
    {
        status = Status.INIT;
        firstPlayer=null;
        blackCrossPosition = 1;
        playerList = new ArrayList<>();
        leaderCardsDeck = new LeaderCardsDeck();
        market = new Market();
        faithTrack = new FaithTrack(this);
        developmentCardsDeck = new DevelopmentCardsDeck();
        actionTokenStack=new ActionTokenStack();
        marketHelper = new MarketHelper();
        developmentCardsVendor = new DevelopmentCardsVendor();
        errorObject = new ErrorObject();
        leaderCardsObject = new LeaderCardsObject();
        resourceObject = new ResourceObject();
        leaderBoard = new LeaderBoard();
        currentPlayer = 1;
    }

//GETTERS
    public Status getStatus() { return this.status; }
    public Player getFirstPlayer() {
        return firstPlayer;
    }
    public int getTurn() { return this.turn; }
    public int getBlackCrossPosition() { return this.blackCrossPosition; }
    public int getCurrentPlayerInt() {
        return currentPlayer;
    }
    public ArrayList<Player> getPlayerList() {
        return new ArrayList<>(playerList);
    }
    public LeaderCardsDeck getLeaderCardsDeck() { return this.leaderCardsDeck; }
    public Market getMarket() { return this.market; }
    public MarketHelper getMarketHelper(){return this.marketHelper;}
    public ErrorObject getErrorObject() { return this.errorObject;}
    public DevelopmentCardsVendor getDevelopmentCardsVendor() { return this.developmentCardsVendor; }
    public FaithTrack getFaithTrack() { return this.faithTrack; }
    public DevelopmentCardsDeck getDevelopmentCardsDeck() { return this.developmentCardsDeck; }
    public ActionTokenStack getActionTokenStack(){ return this.actionTokenStack; }
    public LeaderCardsObject getLeaderCardsObject() { return this.leaderCardsObject; }
    public ResourceObject getResourceObject() { return this.resourceObject;}
    public LeaderBoard getLeaderBoard() { return this.leaderBoard; }

    public Player getCurrentPlayer()
    {
        return getPlayer(currentPlayer);
    }
    public Player getPlayer( String nickname )
    {
        Optional<Player> result = playerList.stream().filter( p -> p.getNickname().equals(nickname) ).findFirst();
        return result.orElse(null);
    }
    public Player getPlayer( int playerNumber)
    {
        Optional<Player> result = playerList.stream().filter( p -> p.getPlayerNumber()==playerNumber ).findFirst();
        return result.orElse(null);
    }
//SETTERS
    public void setTurn(int turn)
    {
        this.turn = turn;
        notifyObservers();
    }
    public void setBlackCrossPosition(int blackCrossPosition) {
        this.blackCrossPosition = blackCrossPosition;
        notifyObservers();
    }
    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
        notifyObservers();
    }
    public void changeStatus(Status status) {
        this.status = status;
    }

//METHODS
    public boolean addPlayer( String nickname , int playerNumber){
        if (getPlayer(nickname) != null) return false;
        Player player = new Player(nickname,playerNumber);
        if (playerNumber == 1)
            firstPlayer = player;
        playerList.add(player);
        return true;
    }

    public void addAllObservers(ModelObserver observer) {
        this.addObserver(observer);
        faithTrack.addObserver(observer);
        market.addObserver(observer);
        developmentCardsDeck.addObserver(observer);
        developmentCardsVendor.addObserver(observer);
        errorObject.addObserver(observer);
        marketHelper.addObserver(observer);
        resourceObject.addObserver(observer);
        for (LeaderCard l : leaderCardsDeck.getCards()) {
            if (l.getSpecialAbility().isExtraDepot()) {
                ExtraDepot extraDepot = (ExtraDepot) l.getSpecialAbility();
                extraDepot.addObserver(observer);
            }
        }

        for (Player player : this.playerList) {
            player.addObserver(observer);
            player.getWarehouseDepot().addObserver(observer);
            player.getStrongbox().addObserver(observer);
            player.getDevelopmentSlot().addObserver(observer);
        }
    }



    public boolean removePlayer( String nickname ) {
        return playerList.removeIf(x -> (x.getNickname()).equals(nickname));
    }

    private void notifyObservers(){
        this.notifyObservers(new MSG_UPD_Game(this.turn, this.currentPlayer, this.blackCrossPosition));
    }
}
