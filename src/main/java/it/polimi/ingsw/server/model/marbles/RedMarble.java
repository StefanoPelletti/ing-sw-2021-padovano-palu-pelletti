package it.polimi.ingsw.server.model.marbles;

import it.polimi.ingsw.server.model.enumerators.Color;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.A;

import java.io.Serializable;
import java.util.List;

public class RedMarble implements MarketMarble, Serializable {

    private final Color color;
    private final String path;

    public RedMarble() {
        path = "resources/punchboard/red_marble.png";
        color = Color.RED;
    }

    /**
     * This method throws a RedMarbleException. It does not add a FAITH in the given List.
     *
     * @param ignored Formally a List of Resource.
     * @throws RedMarbleException Always throws this exception.
     */
    public void addResource(List<Resource> ignored) {
        throw new RedMarbleException("Red Marble detected!");
    }

    /**
     * Returns the Color of this Marble, colored in a proper manner.
     *
     * @return A Colored String representing this Marble.
     */
    @Override
    public String toString() {
        return "" + A.RED + color + A.RESET + " Marble  ";
    }

    /**
     * Returns the Color of this Marble in an abbreviated form, colored in a proper manner.
     *
     * @return A Colored String containing the Initial of the Color of this Marble.
     */
    public String toAbbreviation() {
        return A.RED + color.toAbbreviation() + A.RESET;
    }

    public String getPath() {
        return path;
    }
}