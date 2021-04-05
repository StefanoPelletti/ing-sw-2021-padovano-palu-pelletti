package it.polimi.ingsw.Model.Marbles;

import it.polimi.ingsw.Model.Enumerators.Color;
import it.polimi.ingsw.Model.Enumerators.Resource;

import java.util.ArrayList;

public class RedMarble implements MarketMarble {
    private final Color color;

    public RedMarble() { color = Color.RED; }

    public void addResource(ArrayList<Resource> ResourceList)  {
        throw new RedMarbleException("Red Marble detected!");
    }

    @Override
    public String toString() { return " " + color + "Marble  "; }
    public String toAbbreviation() { return color.toAbbreviation(); }
}
