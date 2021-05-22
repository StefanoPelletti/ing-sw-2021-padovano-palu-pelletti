package it.polimi.ingsw.server.model.middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_DevCardsVendor;
import it.polimi.ingsw.server.model.DevelopmentCard;
import it.polimi.ingsw.server.utils.A;
import it.polimi.ingsw.server.utils.ModelObservable;

import java.util.Map;

public class DevelopmentCardsVendor extends ModelObservable {

    private boolean enabled;
    private Map<DevelopmentCard, boolean[]> cards;

    public DevelopmentCardsVendor() {
        this.enabled = false;
        cards = null;
    }

    public static String toString(boolean enabled, Map<DevelopmentCard, boolean[]> cards) {
        StringBuilder result = new StringBuilder();
        if (!enabled) return result.append(A.RED + " VENDOR IS NOT ENABLED!" + A.RESET).toString();
        int i = 1;
        result.append(A.CYAN + " THE VENDOR IS HERE TO HELP! " + A.RESET).append("\n").append("\n");
        if (cards != null) {
            for (DevelopmentCard dc : cards.keySet()) {
                result.append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
                result.append("  The card number: ").append(i).append("\n");
                result.append(dc).append("\n");
                int k = 1;
                for (boolean b : cards.get(dc)) {
                    if (b) {
                        result.append("  Can be placed in slot number: ").append(k).append("\n");
                    }
                    k++;
                }
                i++;
            }
        } else
            result.append(" No cards present");
        return result.toString();
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
        return DevelopmentCardsVendor.toString(this.enabled, this.cards);
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