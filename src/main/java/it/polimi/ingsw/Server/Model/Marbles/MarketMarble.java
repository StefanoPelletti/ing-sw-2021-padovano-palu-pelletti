package it.polimi.ingsw.Server.Model.Marbles;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;

import java.io.Serializable;
import java.util.ArrayList;

public interface MarketMarble extends Serializable
{
    //note that the Red Marble throws a RedMarbleException (unchecked) if called
    void addResource(ArrayList<Resource> List);

    String toString();
    String toAbbreviation();
}
