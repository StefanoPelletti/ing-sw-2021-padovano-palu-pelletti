package it.polimi.ingsw.Server.Model.Marbles;

import it.polimi.ingsw.Server.Model.Enumerators.Color;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;

import java.io.Serializable;
import java.util.ArrayList;

public class BlueMarble implements MarketMarble, Serializable {
    private final Color color;
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RESET = "\u001B[0m";

    public BlueMarble() {
        color = Color.BLUE;
    }

    public void addResource(ArrayList<Resource> ResourceList) {
        ResourceList.add(Resource.SHIELD);
    }

    @Override
    public String toString() {
        return " " + ANSI_BLUE + color + ANSI_RESET + " Marble  ";
    }

    public String toAbbreviation() {
        return ANSI_BLUE + color.toAbbreviation() + ANSI_RESET;
    }
}