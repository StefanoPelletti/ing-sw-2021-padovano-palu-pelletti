package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class GreyMarble implements MarketMarble{
    private Color color;

    public GreyMarble() { color = Color.GREY; }

    public boolean doAction(ArrayList<Resource> ResourceList, Player player) {
        ResourceList.add(Resource.STONE);
        return true;
    }
}
