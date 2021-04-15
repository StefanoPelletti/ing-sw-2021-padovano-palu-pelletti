package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Model.Enumerators.Status;

import java.util.ArrayList;
import java.util.Optional;

public class Game {
    private Status status;
    private Player firstPlayer;
    private int num_of_players;
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
        num_of_players = 0;
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
    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }
    public int getNum_of_players() { return this.num_of_players; }
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

    public void addPlayer( String nickname )
    {
        if (getPlayer(nickname) == null) {
            if (num_of_players < 4) {
                playerList.add(new Player(nickname, num_of_players));
                num_of_players++;
            } else {
                System.out.println("Max number of player reached!");
            }
        }
        else
        {
            System.out.println("Player already present!");
        }
    }

    public void removePlayer( String nickname )
    {
        boolean result = playerList.removeIf(x -> (x.getNickname()).equals(nickname));
        if ( result )
        {
            num_of_players = num_of_players-1;
            for ( int i = 0; i < playerList.size(); i++)
                playerList.get(i).setPlayerNumber(i);
        }
        else
        {
            System.out.println("No player with such nickname found!");
        }
    }


}
