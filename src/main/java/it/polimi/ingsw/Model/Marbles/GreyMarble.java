package it.polimi.ingsw.Model.Marbles;

import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Resource;

import java.util.ArrayList;

public class GreyMarble implements MarketMarble {
    private final Color color;

    public GreyMarble() { color = Color.GREY; }

    public boolean doAction(ArrayList<Resource> ResourceList, Player player) {
        ResourceList.add(Resource.STONE);
        return true;
    }

    @Override
    public String toString() { return " " + color + "Marble  "; }

    public String toAbbreviation() { return ""+color.toString(); }
}
