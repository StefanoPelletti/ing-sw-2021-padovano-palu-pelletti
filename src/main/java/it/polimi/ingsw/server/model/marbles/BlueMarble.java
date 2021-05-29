package it.polimi.ingsw.server.model.marbles;

import it.polimi.ingsw.server.model.enumerators.Color;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.A;

import java.io.Serializable;
import java.util.List;

public class BlueMarble implements MarketMarble, Serializable {
    private final Color color;

    public BlueMarble() {
        color = Color.BLUE;
    }

    /**
     * Adds a SHIELD to the given Resource List.
     *
     * @param resourceList The Resource List.
     */
    public void addResource(List<Resource> resourceList) {
        resourceList.add(Resource.SHIELD);
    }

    @Override
    public String toString() {
        return "" + A.BLUE + color + A.RESET + " Marble  ";
    }

    public String toAbbreviation() {
        return A.BLUE + color.toAbbreviation() + A.RESET;
    }
}
