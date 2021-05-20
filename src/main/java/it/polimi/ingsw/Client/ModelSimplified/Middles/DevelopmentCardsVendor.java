package it.polimi.ingsw.Client.ModelSimplified.Middles;

import it.polimi.ingsw.Server.Model.DevelopmentCard;
import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.*;
import it.polimi.ingsw.Server.Utils.Displayer;

import java.util.*;

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