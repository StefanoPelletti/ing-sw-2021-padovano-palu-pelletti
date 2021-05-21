package it.polimi.ingsw.Server.Model.Middles;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.MSG_UPD_LeaderCardsObject;
import it.polimi.ingsw.Server.Model.LeaderCard;
import it.polimi.ingsw.Server.Utils.Displayer;
import it.polimi.ingsw.Server.Utils.ModelObservable;

import java.util.ArrayList;

public class LeaderCardsObject extends ModelObservable {
    private boolean enabled;
    private ArrayList<LeaderCard> cards;

    public LeaderCardsObject() {
        this.enabled = false;
        this.cards = null;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        notifyObservers();
    }

    public ArrayList<LeaderCard> getCards() {
        return new ArrayList<>(this.cards);
    }

    public void setCards(ArrayList<LeaderCard> newCards) {
        cards = new ArrayList<>(newCards);
        if (enabled) {
            notifyObservers();
        }
    }

    @Override
    public String toString() {
        return Displayer.leaderCardsObjectToString(this.enabled, this.cards);
    }

    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    public MSG_UPD_LeaderCardsObject generateMessage() {
        return new MSG_UPD_LeaderCardsObject(
                this.enabled,
                this.cards
        );
    }
}
