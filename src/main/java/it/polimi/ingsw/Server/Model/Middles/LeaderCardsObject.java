package it.polimi.ingsw.Server.Model.Middles;

import it.polimi.ingsw.Server.Model.LeaderCard;
import it.polimi.ingsw.Server.Utils.ModelObservable;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class LeaderCardsObject extends ModelObservable {
    boolean enabled;
    ArrayList<LeaderCard> cards;

    public LeaderCardsObject()
    {
        this.enabled=false;
        this.cards = null;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        //notify();
    }

    public boolean isEnabled() { return this.enabled; }

    public void setCards(ArrayList<LeaderCard> newCards)
    {
        cards = new ArrayList<>(newCards);
        if(enabled) {
            //notify()
        }
    }

    public ArrayList<LeaderCard> getCards() { return new ArrayList<LeaderCard>(this.cards); }
}
