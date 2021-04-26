package it.polimi.ingsw.Server.Model.Middles;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.MSG_UPD_LeaderCardsObject;
import it.polimi.ingsw.Server.Model.LeaderCard;
import it.polimi.ingsw.Server.Utils.ModelObservable;

import java.util.ArrayList;

public class LeaderCardsObject extends ModelObservable  {
    boolean enabled;
    ArrayList<LeaderCard> cards;

    public LeaderCardsObject()
    {
        this.enabled=false;
        this.cards = null;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        notifyObservers();
    }

    public boolean isEnabled() { return this.enabled; }

    public void setCards(ArrayList<LeaderCard> newCards)
    {
        cards = new ArrayList<>(newCards);
        if(enabled) {
            notifyObservers();
        }
    }

    public ArrayList<LeaderCard> getCards() { return new ArrayList<LeaderCard>(this.cards); }

    private void notifyObservers(){
        this.notifyObservers(new MSG_UPD_LeaderCardsObject(this.enabled, this.cards));
    }
}
