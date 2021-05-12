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

    public LeaderCard getCard(int position)
    {
        if (cards == null) return null;
        return cards.get(position);
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        if(enabled) {
            result.append(" These are the cards: ").append("\n");
            for (int i = 0; i < cards.size(); i++) {
                result.append(" Card number # ").append(i+1).append("\n");
                result.append(cards.get(i).toString());
            }
        }
        else
            result.append(" LeaderCardsObject is not enabled.");
        return result.toString();
    }
}
