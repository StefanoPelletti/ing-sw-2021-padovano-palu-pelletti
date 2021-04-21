package it.polimi.ingsw.Client.ModelSimplified.Middles;

import it.polimi.ingsw.Server.Model.DevelopmentCard;
import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.*;

import java.util.*;

public class DevelopmentCardsVendor
{
    private boolean enabled;
    private Map<DevelopmentCard, boolean[]> cards;

    public boolean isEnabled() {
        return enabled;
    }

    public void update(MSG_UPD_DevCardsVendor message)
    {
        Map<DevelopmentCard, boolean[]> newMap = message.getCards();
        boolean newEnable = message.getEnabled();
        cards = new HashMap<DevelopmentCard, boolean[]>(newMap);
        this.enabled = newEnable;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("");
        int i = 1;

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
