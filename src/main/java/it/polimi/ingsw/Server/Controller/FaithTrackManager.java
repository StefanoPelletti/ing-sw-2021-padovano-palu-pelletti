package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Model.FaithTrack;
import it.polimi.ingsw.Server.Model.Game;
import it.polimi.ingsw.Server.Model.Player;

import java.util.ArrayList;

public class FaithTrackManager {

    private final FaithTrack faithTrack;
    private final Game game;

    public FaithTrackManager(Game game)
    {
        this.faithTrack=game.getFaithTrack();
        this.game = game;
    }

    public void INIT()
    {

    }

    public FaithTrackInfo getFaithTrackStatus() {
        ArrayList<Player> players = game.getPlayerList();
        FaithTrackInfo result = new FaithTrackInfo();
        return result;
    }

    public boolean advance(Player p)
    {
        return true;
    }

    public boolean advanceAllExcept(Player p)
    {
        return true;
    }
}
