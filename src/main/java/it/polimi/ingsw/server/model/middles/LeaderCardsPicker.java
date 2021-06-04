package it.polimi.ingsw.server.model.middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_LeaderCardsPicker;
import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.utils.A;
import it.polimi.ingsw.server.utils.ModelObservable;

import java.util.ArrayList;
import java.util.List;

public class LeaderCardsPicker extends ModelObservable {
    private boolean enabled;
    private List<LeaderCard> cards;

    /**
     * CONSTRUCTOR
     */
    public LeaderCardsPicker() {
        this.enabled = false;
        this.cards = null;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Sets the LeaderCardsPicker active or disabled.
     * Also notifies observers.
     * @param enabled The boolean value to set.
     */
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


    /**
     * Creates a message generateMessage() and notifies observers.
     * @see #generateMessage()
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     * Returns a MSG_UPD_LeaderCardsPicker representing the current state of the LeaderCardsPicker.
     * @return A MSG_UPD_LeaderCardsPicker representing the current state of the LeaderCardsPicker.
     */
    public MSG_UPD_LeaderCardsPicker generateMessage() {
        return new MSG_UPD_LeaderCardsPicker(
                this.enabled,
                this.cards
        );
    }
}
