package it.polimi.ingsw.server.model.marbles;

import it.polimi.ingsw.server.model.enumerators.Color;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.A;

import java.io.Serializable;
import java.util.List;

public class BlueMarble implements MarketMarble, Serializable {
    private final Color color;
    private String path;

    public BlueMarble() {
        color = Color.BLUE;
        path = "resources/punchboard/blu_marble.png";
    }

    /**
     * Adds a SHIELD to the given Resource List.
     *
     * @param resourceList The Resource List.
     */
    public void addResource(List<Resource> resourceList) {
        resourceList.add(Resource.SHIELD);
    }

    /**
     * Returns the Color of this Marble, colored in a proper manner.
     * @return A Colored String representing this Marble.
     */
    @Override
    public String toString() {
        return "" + A.BLUE + color + A.RESET + " Marble  ";
    }

    /**
     * Returns the Color of this Marble in an abbreviated form, colored in a proper manner.
     * @return A Colored String containing the Initial of the Color of this Marble.
     */
    public String toAbbreviation() {
        return A.BLUE + color.toAbbreviation() + A.RESET;
    }

    public String getPath() {
        return path;
    }
}
