package it.polimi.ingsw.Model.Marbles;

import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Resource;

import java.util.ArrayList;

public interface MarketMarble
{
    //note that the Red Marble throws a RedMarbleException (unchecked) if called
    void addResource(ArrayList<Resource> List);

    String toString();
    String toAbbreviation();
}
