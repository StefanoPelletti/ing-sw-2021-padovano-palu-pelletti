package it.polimi.ingsw.Server.Model.Middles;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.MSG_UPD_LeaderCardsObject;
import it.polimi.ingsw.Server.Model.LeaderCard;
import it.polimi.ingsw.Server.Utils.A;
import it.polimi.ingsw.Server.Utils.ModelObservable;

import java.util.ArrayList;

public class LeaderCardsObject extends ModelObservable {
    boolean enabled;
    ArrayList<LeaderCard> cards;

    public LeaderCardsObject() {
        this.enabled = false;
        this.cards = null;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        notifyObservers();
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setCards(ArrayList<LeaderCard> newCards) {
        cards = new ArrayList<>(newCards);
        if (enabled) {
            notifyObservers();
        }
    }

    public ArrayList<LeaderCard> getCards() {
        return new ArrayList<>(this.cards);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\n");
        result.append(A.CYAN + " LEADERCARD PICKER IS HERE TO HELP! " + A.RESET).append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        if (enabled) {
            result.append(" These are the cards: ").append("\n");
            for (int i = 0; i < cards.size(); i++) {
                result.append("\n\n").append(" Card number #").append(i + 1).append("\n");
                result.append(cards.get(i).toString());
            }
        } else
            result.append("  LeaderCardsObject is not enabled.");
        return result.toString();
    }

    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    public MSG_UPD_LeaderCardsObject generateMessage() {
        return new MSG_UPD_LeaderCardsObject(
                this.enabled,
                this.cards
        );
    }
}
