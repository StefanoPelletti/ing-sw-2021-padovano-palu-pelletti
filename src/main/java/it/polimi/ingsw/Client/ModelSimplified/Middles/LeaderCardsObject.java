package it.polimi.ingsw.Client.ModelSimplified.Middles;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.MSG_UPD_LeaderCardsObject;
import it.polimi.ingsw.Server.Model.LeaderCard;
import it.polimi.ingsw.Server.Utils.Displayer;

import java.util.ArrayList;

public class LeaderCardsObject {
    private boolean enabled;
    private ArrayList<LeaderCard> cards;

    public boolean isEnabled() {
        return enabled;
    }

    public void update(MSG_UPD_LeaderCardsObject message) {
        boolean newEnabled = message.getEnabled();
        ArrayList<LeaderCard> newCards = message.getCards();

        this.enabled = newEnabled;
        this.cards = new ArrayList<>(newCards);
    }

    public LeaderCard getCard(int position) {
        if (cards == null) return null;
        return cards.get(position);
    }

    @Override
    public String toString() {
        return Displayer.leaderCardsObjectToString(this.enabled, this.cards);
    }
}