package it.polimi.ingsw.Server.Model.Marbles;

import it.polimi.ingsw.Server.Model.Enumerators.Color;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;

import java.io.Serializable;
import java.util.ArrayList;

public class WhiteMarble implements MarketMarble, Serializable {
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
