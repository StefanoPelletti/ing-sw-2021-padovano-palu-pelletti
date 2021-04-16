package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Model.Enumerators.Status;

import java.util.ArrayList;
import java.util.Optional;

public class Game {
    private Status status;
    private Player firstPlayer;
    private int turn;
    private int blackCrossPosition;

    private final ActionTokenStack actionTokenStack;
    private final ArrayList<Player> playerList;
    private final LeaderCardsDeck leaderCardsDeck;
    private final Market market;
    private final FaithTrack faithTrack;
    private final DevelopmentCardsDeck developmentCardsDeck;

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
    }

    public Status getStatus() { return this.status; }
    public Player getFirstPlayer() {
        return firstPlayer;
    }
    public int getTurn() { return this.turn; }
    public int getBlackCrossPosition() { return this.blackCrossPosition; }

    public ArrayList<Player> getPlayerList() {
        return new ArrayList<>(playerList);
    }
    public LeaderCardsDeck getLeaderCardsDeck() { return this.leaderCardsDeck; }
    public Market getMarket() { return this.market; }
    public FaithTrack getFaithTrack() { return this.faithTrack; }
    public DevelopmentCardsDeck getDevelopmentCardsDeck() { return this.developmentCardsDeck; }
    public ActionTokenStack getActionTokenStack(){return this.actionTokenStack;}
    public void setBlackCrossPosition(int blackCrossPosition) {
        this.blackCrossPosition = blackCrossPosition;
    }

    public void changeStatus(Status status) { this.status = status; }
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

    public boolean addPlayer( String nickname , int playerNumber)
    {
        if (getPlayer(nickname) != null) return false;
        Player player = new Player(nickname,playerNumber);
        if (playerNumber == 1)
            firstPlayer = player;
        playerList.add(player);
        return true;
    }

    public boolean removePlayer( String nickname )
    {
        return playerList.removeIf(x -> (x.getNickname()).equals(nickname));
    }
}
