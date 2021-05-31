package it.polimi.ingsw.server.model.marbles;

import it.polimi.ingsw.server.model.enumerators.Color;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.A;

import java.io.Serializable;
import java.util.List;

public class YellowMarble implements MarketMarble, Serializable {
    private final Color color;

    public YellowMarble() {
        color = Color.YELLOW;
    }

    /**
     * Adds a COIN to the given Resource List.
     * @param resourceList The Resource List.
     */
    public void addResource(List<Resource> resourceList) {
        resourceList.add(Resource.COIN);
    }

    @Override
    public String toString() {
        return "" + A.YELLOW + color + A.RESET + " Marble  ";
    }

    /**
     * Returns the Color of this Marble in an abbreviated form, colored in a proper manner.
     * @return A Colored String containing the Initial of the Color of this Marble.
     */
    public String toAbbreviation() {
        return A.YELLOW + color.toAbbreviation() + A.RESET;
    }
}