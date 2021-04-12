package it.polimi.ingsw.Server.Model.Marbles;

import it.polimi.ingsw.Server.Model.Enumerators.Color;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;

import java.util.ArrayList;

public class YellowMarble implements MarketMarble {
    private final Color color;

    public YellowMarble() { color = Color.YELLOW; }

    public void addResource(ArrayList<Resource> ResourceList) {
        ResourceList.add(Resource.COIN);
    }

    @Override
    public String toString() { return " " + color + " Marble  "; }
    public String toAbbreviation() { return color.toAbbreviation(); }
}
