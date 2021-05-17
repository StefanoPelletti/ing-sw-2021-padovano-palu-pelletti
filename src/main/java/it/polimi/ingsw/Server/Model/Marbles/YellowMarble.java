package it.polimi.ingsw.Server.Model.Marbles;

import it.polimi.ingsw.Server.Model.Enumerators.Color;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;

import java.io.Serializable;
import java.util.ArrayList;

public class YellowMarble implements MarketMarble, Serializable {
    private final Color color;
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";

    public YellowMarble() {
        color = Color.YELLOW;
    }

    public void addResource(ArrayList<Resource> ResourceList) {
        ResourceList.add(Resource.COIN);
    }

    @Override
    public String toString() {
        return " " + ANSI_YELLOW + color + ANSI_RESET + " Marble  ";
    }

    public String toAbbreviation() {
        return ANSI_YELLOW + color.toAbbreviation() + ANSI_RESET;
    }
}