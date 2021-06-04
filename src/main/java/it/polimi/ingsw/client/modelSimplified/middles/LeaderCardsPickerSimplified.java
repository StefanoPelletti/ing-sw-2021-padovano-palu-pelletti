package it.polimi.ingsw.client.modelSimplified.middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_LeaderCardsPicker;
import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.model.middles.LeaderCardsPicker;
import it.polimi.ingsw.server.utils.A;

import java.util.ArrayList;
import java.util.List;

public class LeaderCardsPickerSimplified {
    private boolean enabled;
    private ArrayList<LeaderCard> cards;

    public boolean isEnabled() {
        return enabled;
    }

    public void update(MSG_UPD_LeaderCardsPicker message) {
        boolean newEnabled = message.getEnabled();
        List<LeaderCard> newCards = message.getCards();

        this.enabled = newEnabled;
        this.cards = new ArrayList<>(newCards);
    }

    public LeaderCard getCard(int position) {
        if (cards == null) return null;
        return cards.get(position);
    }

    /**
     * Returns a String containing the 4 Leader Cards that the LeaderCardsPicker has to offer.
     * Note: see Player.getStartingLeaderCards().
     * @return A String containing the 4 Leader Cards that the LeaderCardsPicker has to offer.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (!enabled) return result.append(A.RED + " LEADERCARD PICKER IS NOT ENABLED!" + A.RESET).toString();

        result.append(A.CYAN + " LEADERCARD PICKER IS HERE TO HELP! " + A.RESET).append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append(" These are the cards: ").append("\n");
        for (int i = 0; i < cards.size(); i++) {
            result.append("\n\n").append(" Card number #").append(i + 1).append("\n");
            result.append(cards.get(i).toString());
        }

        return result.toString();
    }
}