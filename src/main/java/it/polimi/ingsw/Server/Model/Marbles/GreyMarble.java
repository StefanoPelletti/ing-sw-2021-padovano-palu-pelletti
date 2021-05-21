package it.polimi.ingsw.Server.Model.Marbles;

import it.polimi.ingsw.Server.Model.Enumerators.Color;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;

import java.io.Serializable;
import java.util.ArrayList;

public class GreyMarble implements MarketMarble, Serializable {
    public static final String ANSI_RESET = "\u001B[0m";
    private final Color color;


    public GreyMarble() {
        color = Color.GREY;
    }

    public void addResource(ArrayList<Resource> ResourceList) {
        ResourceList.add(Resource.STONE);
    }

    @Override
    public String toString() {
        return " " + color + " Marble  ";
    }

    public String toAbbreviation() {
        return "" + color.toAbbreviation();
    }
}