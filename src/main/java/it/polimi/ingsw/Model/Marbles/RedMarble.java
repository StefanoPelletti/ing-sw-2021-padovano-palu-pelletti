package it.polimi.ingsw.Model.Marbles;

import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Game;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Resource;

import java.util.ArrayList;

public class RedMarble implements MarketMarble {
    private final Color color;

    public RedMarble() { color = Color.RED; }

    public boolean doAction(ArrayList<Resource> ResourceList, Player player) {
        Game.
        FaithTrack.advance(player);
        return true;
    }

    @Override
    public String toString() { return " " + color + "Marble  "; }

    public String toAbbreviation() { return color.toAbbreviation(); }
}
