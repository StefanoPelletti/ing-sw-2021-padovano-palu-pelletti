package it.polimi.ingsw.server.model.marbles;

import it.polimi.ingsw.server.model.enumerators.Resource;

import java.io.Serializable;
import java.util.List;

public interface MarketMarble extends Serializable {

    //note that the Red Marble throws a RedMarbleException (unchecked) if called

    /**
     * Tries to add a predefined Resource to a given List.
     * Each Marble has a Color which corresponds to a certain Resource.
     * GREY is STONE, BLUE is SHIELD, YELLOW is COIN, PURPLE is SERVANT.
     * RED corresponds to a FAITH, but it does not add in the List: instead it throws a RedMarbleException.
     * WHITE corresponds to EXTRA, and must be managed accordingly to game rules.
     * @param list A List of Resource in which a Resource will be placed.
     * @throws RedMarbleException If this method is called on a RedMarble.
     */
    void addResource(List<Resource> list);

    String toString();

    /**
     * Returns an abbreviation of the selected MarketMarble.
     * Usually is a String made out of the initial character of its Color.
     * @return The abbreviation String.
     */
    String toAbbreviation();

    String getPath();
}