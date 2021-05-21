package it.polimi.ingsw.server.model.middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_LeaderCardsObject;
import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.utils.Displayer;
import it.polimi.ingsw.server.utils.ModelObservable;

import java.util.ArrayList;
import java.util.List;

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

    public List<LeaderCard> getCards() {
        return new ArrayList<>(this.cards);
    }

    public void setCards(List<LeaderCard> newCards) {
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
