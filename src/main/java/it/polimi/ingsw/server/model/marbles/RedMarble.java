package it.polimi.ingsw.server.model.marbles;

import it.polimi.ingsw.server.model.enumerators.Color;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.A;

import java.io.Serializable;
import java.util.List;

public class RedMarble implements MarketMarble, Serializable {

    private final Color color;

    public RedMarble() {
        color = Color.RED;
    }

    public void addResource(List<Resource> ignored) {
        throw new RedMarbleException("Red Marble detected!");
    }

    @Override
    public String toString() {
        return " " + A.RED + color + A.RESET + " Marble  ";
    }

    public String toAbbreviation() {
        return A.RED + color.toAbbreviation() + A.RESET;
    }
}