package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Server.Model.FaithTrack;
import it.polimi.ingsw.Server.Model.Player;

public class FaithTrackManager {

    private final FaithTrack faithTrack;

    public FaithTrackManager(FaithTrack faithTrack)
    {
        this.faithTrack=faithTrack;
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
