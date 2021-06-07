package it.polimi.ingsw.client.modelSimplified.middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_DevCardsVendor;
import it.polimi.ingsw.server.model.middles.VendorCard;
import it.polimi.ingsw.server.utils.A;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Returns the representation of the current state of a given DevelopmentCardVendor.
     *
     * @return A String representing the current state of the DevelopmentCardsVendor.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        boolean enabled = this.enabled;
        List<VendorCard> cards = this.cards;
        if (!enabled) return result.append(A.RED + " VENDOR IS NOT ENABLED!" + A.RESET).toString();
        result.append(A.CYAN + " THE VENDOR IS HERE TO HELP! " + A.RESET).append("\n").append("\n");
        for (int i = 0; i < cards.size(); i++) {
            result.append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
            result.append("  The card number: ").append(i + 1).append("\n");
            result.append(cards.get(i).getCard()).append("\n");
            if (cards.get(i).isSlot1()) result.append("  Can be placed in slot number 1").append("\n");
            if (cards.get(i).isSlot2()) result.append("  Can be placed in slot number 2").append("\n");
            if (cards.get(i).isSlot3()) result.append("  Can be placed in slot number 3").append("\n");
        }
        return result.toString();
    }
}