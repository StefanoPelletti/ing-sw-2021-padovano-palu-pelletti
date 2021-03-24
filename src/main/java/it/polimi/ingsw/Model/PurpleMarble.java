package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class PurpleMarble implements MarketMarble{
    private Color color;

    public PurpleMarble() { color = Color.PURPLE; }

    public boolean doAction(ArrayList<Resource> ResourceList, Player player) {
        ResourceList.add(Resource.SERVANT);
        return true;
    }
}
