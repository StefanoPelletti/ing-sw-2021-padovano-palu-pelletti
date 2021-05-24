package it.polimi.ingsw.server.model.marbles;

import it.polimi.ingsw.server.model.enumerators.Color;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.A;

import java.io.Serializable;
import java.util.List;

public class WhiteMarble implements MarketMarble, Serializable {
    private final Color color;

    public WhiteMarble() {
        color = Color.WHITE;
    }

    public void addResource(List<Resource> resourceList) {
        resourceList.add(Resource.EXTRA);
    }

    @Override
    public String toString() {
        return A.UL + "" + color + A.RESET + " Marble  ";
    }

    public String toAbbreviation() {
        return A.UL + "" + color.toAbbreviation() + A.RESET;
    }
}