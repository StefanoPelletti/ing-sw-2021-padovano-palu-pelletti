package it.polimi.ingsw.Model.Marbles;

import it.polimi.ingsw.Model.Enumerators.Color;
import it.polimi.ingsw.Model.Enumerators.Resource;

import java.util.ArrayList;

public class WhiteMarble implements MarketMarble{
    private final Color color;

    public WhiteMarble() { color = Color.WHITE; }

    public void addResource(ArrayList<Resource> ResourceList) {
        ResourceList.add(Resource.EXTRA);
    }

    @Override
    public String toString() {
        return " " + color + " Marble  ";
    }
    public String toAbbreviation() { return color.toAbbreviation(); }
}
