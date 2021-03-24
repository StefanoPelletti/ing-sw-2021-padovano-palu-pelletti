package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class RedMarble {
    private Color color;

    public RedMarble() { color = Color.RED; }

    public boolean doAction(ArrayList<Resource> ResourceList, Player player) {
        Game.
        FaithTrack.advance(player);
        return true;
    }
}
