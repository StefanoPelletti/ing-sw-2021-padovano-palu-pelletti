package it.polimi.ingsw.Model.Marbles;

import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Resource;

import java.util.ArrayList;

public class BlueMarble implements MarketMarble {
    private final Color color;

    public BlueMarble() { color = Color.BLUE; }

    public boolean doAction(ArrayList<Resource> ResourceList, Player player) {
        ResourceList.add(Resource.SHIELD);
        return true;
    }

    @Override
    public String toString() { return " " + color + "Marble  "; }

    public String toAbbreviation() { return color.toAbbreviation(); }
}
