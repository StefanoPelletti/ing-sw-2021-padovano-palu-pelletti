package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class BlueMarble implements MarketMarble{
    private Color color;

    public BlueMarble() { color = Color.BLUE; }

    public boolean doAction(ArrayList<Resource> ResourceList, Player player) {
        ResourceList.add(Resource.SHIELD);
        return true;
    }
}
