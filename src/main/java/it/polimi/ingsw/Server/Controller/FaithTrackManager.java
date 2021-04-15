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

        for (Player p : players)
            result.setPlayerNumberOffsetPosition(p.getPlayerNumber(), p.getPosition());
        result.setZones(faithTrack.getZones());
        return result;
    }

    public boolean advance(Player p)
    {
        if ( p == null ) return false;
        ArrayList<Player> players = game.getPlayerList();
        if ( players.stream().noneMatch( x -> x.getNickname().equals(p.getNickname()))) return false;

        switch( faithTrack.doesActivateZone(p) )
        {
            case -1: return false;
            case 1:
                faithTrack.advance(p);
                for ( Player player : players )
                {
                    return true;
                }
        }
        return true;
    }

    public boolean advanceAllExcept(Player p)
    {
        return true;
    }
}
