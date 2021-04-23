package it.polimi.ingsw.Server.Model.Middles;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_Market;
import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.MSG_UPD_DevCardsVendor;
import it.polimi.ingsw.Server.Model.DevelopmentCard;
import it.polimi.ingsw.Server.Utils.ModelObservable;

import java.io.Serializable;
import java.util.Map;

public class DevelopmentCardsVendor extends ModelObservable implements Serializable {

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

    private void notifyObservers(){
        this.notifyObservers(new MSG_UPD_DevCardsVendor(this.enabled, this.cards));
    }
}
