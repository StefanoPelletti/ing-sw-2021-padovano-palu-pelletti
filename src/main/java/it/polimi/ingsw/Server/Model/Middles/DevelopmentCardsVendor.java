package it.polimi.ingsw.Server.Model.Middles;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.MSG_UPD_DevCardsVendor;
import it.polimi.ingsw.Server.Model.DevelopmentCard;
import it.polimi.ingsw.Server.Utils.Displayer;
import it.polimi.ingsw.Server.Utils.ModelObservable;

import java.util.Map;

public class DevelopmentCardsVendor extends ModelObservable {

    private boolean enabled;
    private Map<DevelopmentCard, boolean[]> cards;

    public DevelopmentCardsVendor() {
        this.enabled = false;
        cards = null;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled)
            cards = null;
        notifyObservers();
    }

    public Map<DevelopmentCard, boolean[]> getCards() {
        return cards;
    }

    public void setCards(Map<DevelopmentCard, boolean[]> cards) {
        this.cards = cards;
    }


    @Override
    public String toString() {
        return Displayer.developmentCardsVendorToString(this.cards);
    }

    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    public MSG_UPD_DevCardsVendor generateMessage() {
        return new MSG_UPD_DevCardsVendor(
                this.enabled,
                this.cards
        );
    }
}