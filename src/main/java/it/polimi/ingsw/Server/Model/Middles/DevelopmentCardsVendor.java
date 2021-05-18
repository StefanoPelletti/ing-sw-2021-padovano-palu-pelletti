package it.polimi.ingsw.Server.Model.Middles;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.MSG_UPD_DevCardsVendor;
import it.polimi.ingsw.Server.Model.DevelopmentCard;
import it.polimi.ingsw.Server.Utils.A;
import it.polimi.ingsw.Server.Utils.ModelObservable;

import java.util.Map;

public class DevelopmentCardsVendor extends ModelObservable {

    boolean enabled;
    Map<DevelopmentCard, boolean[]> cards;

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
        StringBuilder result = new StringBuilder();
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