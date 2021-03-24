package it.polimi.ingsw.Model.Marbles;

import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Player;
import it.polimi.ingsw.Model.Resource;

import java.util.ArrayList;

public interface MarketMarble
{

    boolean doAction(ArrayList<Resource> List, Player player);

    String toString();
    String toAbbreviation();
}
