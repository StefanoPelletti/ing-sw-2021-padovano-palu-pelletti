package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Game {
    private Status status;
    private ArrayList<Player> playerList;
    private LeaderCardsDeck leaderCardsDeck;
    private Market market;

    public Game()
    {
        status = Status.INIT;
        playerList = new ArrayList<Player>();
        leaderCardsDeck = new LeaderCardsDeck();
        market = new Market();
    }

    public void changeStatus(Status status)
    {
        this.status = status;
    }
    public Status getStatus() { return this.status; }

    public ArrayList<Player> getPlayerList() {
        ArrayList<Player> newList = new ArrayList<Player>(playerList);
        return newList;
    }

    public void advance( Player p )
    {

    }

}
