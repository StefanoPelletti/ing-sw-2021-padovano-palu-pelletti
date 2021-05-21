package it.polimi.ingsw.server.model.marbles;

import it.polimi.ingsw.server.model.enumerators.Color;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.A;

import java.io.Serializable;
import java.util.List;

public class PurpleMarble implements MarketMarble, Serializable {

    private final Color color;

    public PurpleMarble() {
        color = Color.PURPLE;
    }

    public void addResource(List<Resource> resourceList) {
        resourceList.add(Resource.SERVANT);
    }

    @Override
    public String toString() {
        return " " + A.PURPLE + color + A.RESET + " Marble  ";
    }

    public String toAbbreviation() {
        return A.PURPLE + color.toAbbreviation() + A.RESET;
    }
}