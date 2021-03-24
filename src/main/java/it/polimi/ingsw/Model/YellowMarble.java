package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class YellowMarble implements MarketMarble{
    private Color color;

    public YellowMarble() { color = Color.YELLOW; }

    public boolean doAction(ArrayList<Resource> ResourceList, Player player) {
        ResourceList.add(Resource.COIN);
        return true;
    }
}
