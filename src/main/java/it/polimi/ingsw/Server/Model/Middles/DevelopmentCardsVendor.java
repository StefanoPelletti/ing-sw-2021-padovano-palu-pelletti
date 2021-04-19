package it.polimi.ingsw.Server.Model.Middles;

import it.polimi.ingsw.Server.Model.DevelopmentCard;

import java.io.Serializable;
import java.util.Map;

public class DevelopmentCardsVendor implements Serializable {

    boolean enabled;
    Map<DevelopmentCard, boolean[]> cards;

    public DevelopmentCardsVendor()
    {
        this.enabled=false;
        cards = null;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if(!enabled)
            cards = null;
        //notify();
    }

    public Map<DevelopmentCard, boolean[]> getCards() {
        return cards;
    }

    public void setCards(Map<DevelopmentCard, boolean[]> cards) {
        this.cards = cards;
    }

    public String toString() {
        StringBuilder result = new StringBuilder("");
        int i=1;

        if (cards != null) {
            for (DevelopmentCard dc : cards.keySet()) {
                result.append("La carta numero ").append(i);
                result.append(dc);
                int k = 1;
                for (boolean b : cards.get(dc)) {
                    if (b) {
                        result.append("'\nPuo' essere inserito nello slot: ").append(k);
                    }
                    k++;
                }
                i++;
            }
        }
        return result.toString();
    }
}
