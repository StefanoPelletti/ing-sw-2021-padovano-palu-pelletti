package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Game {
    static Status status;
    private ArrayList<Player> playerList;

    public Game()
    {
        status = Status.START;
        playerList = new ArrayList<Player>();
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

}
