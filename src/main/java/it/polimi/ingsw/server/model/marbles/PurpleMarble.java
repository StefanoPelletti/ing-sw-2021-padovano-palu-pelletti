package it.polimi.ingsw.server.model.marbles;

import it.polimi.ingsw.server.model.enumerators.Color;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.A;

import java.io.Serializable;
import java.util.List;

public class PurpleMarble implements MarketMarble, Serializable {

    private final Color color;
    private final String path;

    public PurpleMarble() {
        path = "/punchboard/purple_marble.png";
        color = Color.PURPLE;
    }

    /**
     * Adds a SERVANT to the given Resource List.
     *
     * @param resourceList The Resource List.
     */
    public void addResource(List<Resource> resourceList) {
        resourceList.add(Resource.SERVANT);
    }

    /**
     * Returns the Color of this Marble, colored in a proper manner.
     *
     * @return A Colored String representing this Marble.
     */
    public String toStringColored() {
        return "" + A.PURPLE + color + A.RESET + " Marble  ";
    }

    @Override
    public String toString() {
        return "" + color + " Marble";
    }

    /**
     * Returns the Color of this Marble in an abbreviated form, colored in a proper manner.
     *
     * @return A Colored String containing the Initial of the Color of this Marble.
     */
    public String toAbbreviation() {
        return A.PURPLE + color.toAbbreviation() + A.RESET;
    }

    public String getPath() {
        return path;
    }
}