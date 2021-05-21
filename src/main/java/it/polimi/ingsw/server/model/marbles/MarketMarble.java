package it.polimi.ingsw.server.model.marbles;

import it.polimi.ingsw.server.model.enumerators.Resource;

import java.io.Serializable;
import java.util.List;

public interface MarketMarble extends Serializable {
    //note that the Red Marble throws a RedMarbleException (unchecked) if called
    void addResource(List<Resource> list);

    String toString();

    String toAbbreviation();
}