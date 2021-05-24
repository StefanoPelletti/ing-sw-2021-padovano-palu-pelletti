package it.polimi.ingsw.client.modelSimplified.middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_DevCardsVendor;
import it.polimi.ingsw.server.model.middles.DevelopmentCardsVendor;

import java.util.ArrayList;
import java.util.List;
import it.polimi.ingsw.server.model.middles.VendorCard;

public class DevelopmentCardsVendorSimplified {
    private boolean enabled;

    private List<VendorCard> cards;

    public boolean isEnabled() {
        return enabled;
    }

    public void update(MSG_UPD_DevCardsVendor message) {
        List<VendorCard> newMap = message.getCards();
        boolean newEnable = message.getEnabled();
        if (newMap != null)
            cards = new ArrayList<>(newMap);
        else
            cards = null;
        this.enabled = newEnable;
    }

    public List<VendorCard> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return DevelopmentCardsVendor.toString(this.enabled, this.cards);
    }
}