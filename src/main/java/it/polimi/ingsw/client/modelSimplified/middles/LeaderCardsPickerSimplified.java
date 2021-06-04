package it.polimi.ingsw.client.modelSimplified.middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_LeaderCardsPicker;
import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.model.middles.LeaderCardsPicker;

import java.util.ArrayList;
import java.util.List;

public class LeaderCardsPickerSimplified {
    private boolean enabled;
    private List<LeaderCard> cards;

    public boolean isEnabled() {
        return enabled;
    }

    public void update(MSG_UPD_LeaderCardsPicker message) {
        boolean newEnabled = message.getEnabled();
        List<LeaderCard> newCards = message.getCards();

        this.enabled = newEnabled;
        this.cards = new ArrayList<>(newCards);
    }

    public LeaderCard getCard(int position) {
        if (cards == null) return null;
        return cards.get(position);
    }

    @Override
    public String toString() {
        return LeaderCardsPicker.toString(this.enabled, this.cards);
    }
}