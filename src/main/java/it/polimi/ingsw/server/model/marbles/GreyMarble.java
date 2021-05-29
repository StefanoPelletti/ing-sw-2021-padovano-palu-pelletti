package it.polimi.ingsw.server.model.marbles;

import it.polimi.ingsw.server.model.enumerators.Color;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.A;

import java.io.Serializable;
import java.util.List;

public class GreyMarble implements MarketMarble, Serializable {
    private final Color color;

    public GreyMarble() {
        color = Color.GREY;
    }

    /**
     * Adds a STONE to the given Resource List.
     * @param resourceList The Resource List.
     */
    public void addResource(List<Resource> resourceList) {
        resourceList.add(Resource.STONE);
    }

    @Override
    public String toString() {
        return A.WHITE + "" + A.UL + color + A.RESET + " Marble  ";
    }

    public String toAbbreviation() {
        return A.WHITE + color.toAbbreviation() + A.RESET;
    }
}