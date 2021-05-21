package it.polimi.ingsw.client.modelSimplified.Middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_DevCardsVendor;
import it.polimi.ingsw.server.model.DevelopmentCard;
import it.polimi.ingsw.server.utils.Displayer;

import java.util.HashMap;
import java.util.Map;

public class DevelopmentCardsVendor {
    private boolean enabled;

    private Map<DevelopmentCard, boolean[]> cards;

    public boolean isEnabled() {
        return enabled;
    }

    public void update(MSG_UPD_DevCardsVendor message) {
        Map<DevelopmentCard, boolean[]> newMap = message.getCards();
        boolean newEnable = message.getEnabled();
        if (newMap != null)
            cards = new HashMap<>(newMap);
        else
            cards = null;
        this.enabled = newEnable;
    }

    public Map<DevelopmentCard, boolean[]> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return Displayer.developmentCardsVendorToString(this.cards);
    }
}