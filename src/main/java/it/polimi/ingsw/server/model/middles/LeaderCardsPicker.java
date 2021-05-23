package it.polimi.ingsw.server.model.middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_LeaderCardsPicker;
import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.utils.A;
import it.polimi.ingsw.server.utils.ModelObservable;

import java.util.ArrayList;
import java.util.List;

public class LeaderCardsPicker extends ModelObservable {
    private boolean enabled;
    private List<LeaderCard> cards;

    public LeaderCardsPicker() {
        this.enabled = false;
        this.cards = null;
    }

    public static String toString(boolean enabled, List<LeaderCard> cards) {
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

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        notifyObservers();
    }

    public List<LeaderCard> getCards() {
        return new ArrayList<>(this.cards);
    }

    public void setCards(List<LeaderCard> newCards) {
        cards = new ArrayList<>(newCards);
        if (enabled) {
            notifyObservers();
        }
    }

    @Override
    public String toString() {
        return LeaderCardsPicker.toString(this.enabled, this.cards);
    }

    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    public MSG_UPD_LeaderCardsPicker generateMessage() {
        return new MSG_UPD_LeaderCardsPicker(
                this.enabled,
                this.cards
        );
    }
}
