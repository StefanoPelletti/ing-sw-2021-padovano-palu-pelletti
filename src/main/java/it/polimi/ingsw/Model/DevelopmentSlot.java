package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class DevelopmentSlot {
    private DevelopmentCard[][] cards;

    public DevelopmentSlot(DevelopmentCard[][] cards) {
        this.cards = cards;
    }

    public boolean addCard(DevelopmentCard card, int position) {
        return true;
    }

    public boolean validateNewCard(DevelopmentCard card, int position) {
        return true;
    }

    public DevelopmentCard[][] getPlayerCards() {
        return cards;
    }

    public ArrayList<DevelopmentCard> getActivableCards() {
        ArrayList<DevelopmentCard> arr = null; //cosi, a caso
        return arr;
    }
}
