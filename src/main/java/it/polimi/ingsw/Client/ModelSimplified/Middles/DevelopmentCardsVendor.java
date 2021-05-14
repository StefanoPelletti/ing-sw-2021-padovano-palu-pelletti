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
        if(newMap != null)
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
        StringBuilder result = new StringBuilder("");
        int i = 1;
        result.append(" THE VENDOR IS HERE TO HELP! ");
        if (cards != null) {
            for (DevelopmentCard dc : cards.keySet()) {
                result.append("  the Card number: ").append(i);
                result.append(dc).append("\n");
                int k = 1;
                for (boolean b : cards.get(dc)) {
                    if (b) {
                        result.append("  can be placed in slot number: ").append(k).append("\n");
                    }
                    k++;
                }
                i++;
            }
        }
        else
            result.append(" No cards present");
        return result.toString();
    }
}
