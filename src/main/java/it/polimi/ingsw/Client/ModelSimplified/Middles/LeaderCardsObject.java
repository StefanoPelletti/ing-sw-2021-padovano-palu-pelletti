package it.polimi.ingsw.Client.ModelSimplified.Middles;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.MSG_UPD_LeaderCardsObject;
import it.polimi.ingsw.Server.Model.LeaderCard;

import java.util.ArrayList;

public class LeaderCardsObject
{
    private boolean enabled;
    ArrayList<LeaderCard> cards;

    public boolean isEnabled() {
        return enabled;
    }

    public void update(MSG_UPD_LeaderCardsObject message)
    {
        boolean newEnabled = message.getEnabled();
        ArrayList<LeaderCard> newCards = message.getCards();

        this.enabled=newEnabled;
        this.cards = new ArrayList<>(newCards);
    }

}
