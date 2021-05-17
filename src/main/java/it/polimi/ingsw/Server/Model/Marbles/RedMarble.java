package it.polimi.ingsw.Server.Model.Marbles;

import it.polimi.ingsw.Server.Model.Enumerators.Color;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;

import java.io.Serializable;
import java.util.ArrayList;

public class RedMarble implements MarketMarble, Serializable {
    private final Color color;
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    public RedMarble() {
        color = Color.RED;
    }

    public void addResource(ArrayList<Resource> ResourceList) {
        throw new RedMarbleException("Red Marble detected!");
    }

    @Override
    public String toString() {
        return " " + ANSI_RED + color + ANSI_RESET + " Marble  ";
    }

    public String toAbbreviation() {
        return ANSI_RED + color.toAbbreviation() + ANSI_RESET;
    }
}