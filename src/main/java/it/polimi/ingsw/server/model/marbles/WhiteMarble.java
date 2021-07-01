package it.polimi.ingsw.server.model.marbles;

import it.polimi.ingsw.server.model.enumerators.Color;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.A;

import java.io.Serializable;
import java.util.List;

public class WhiteMarble implements MarketMarble, Serializable {
    private final Color color;
    private final String path;

    public WhiteMarble() {
        path = "/punchboard/white_marble.png";
        color = Color.WHITE;
    }

    /**
     * Adds a EXTRA Resource to the given Resource List.
     *
     * @param resourceList The Resource List.
     */
    public void addResource(List<Resource> resourceList) {
        resourceList.add(Resource.EXTRA);
    }

    /**
     * Returns the Color of this Marble, colored in a proper manner.
     *
     * @return A Colored String representing this Marble.
     */
    public String toStringColored() {
        return A.RESET + "" + A.UL + "" + color + A.RESET + " Marble  ";
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
        return color.toAbbreviation();
    }

    public String getPath() {
        return path;
    }
}