package it.polimi.ingsw.Server.Model.Marbles;

import it.polimi.ingsw.Server.Model.Enumerators.Color;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;

import java.io.Serializable;
import java.util.ArrayList;

public class PurpleMarble implements MarketMarble, Serializable {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    private final Color color;

    public PurpleMarble() {
        color = Color.PURPLE;
    }

    public void addResource(ArrayList<Resource> ResourceList) {
        ResourceList.add(Resource.SERVANT);
    }

    @Override
    public String toString() {
        return " " + ANSI_PURPLE + color + ANSI_RESET + " Marble  ";
    }

    public String toAbbreviation() {
        return ANSI_PURPLE + color.toAbbreviation() + ANSI_RESET;
    }
}