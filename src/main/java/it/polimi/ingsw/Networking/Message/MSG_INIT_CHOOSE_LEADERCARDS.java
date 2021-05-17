package it.polimi.ingsw.Networking.Message;

import it.polimi.ingsw.Server.Model.LeaderCard;

import java.io.Serializable;
import java.util.ArrayList;

public class MSG_INIT_CHOOSE_LEADERCARDS extends Message implements Serializable {

    private final ArrayList<LeaderCard> cards;

    public MSG_INIT_CHOOSE_LEADERCARDS(ArrayList<LeaderCard> cards) {
        super(MessageType.MSG_INIT_CHOOSE_LEADERCARDS);

        if (cards.size() != 2)
            throw new IllegalArgumentException();
        if (cards.get(0).equals(cards.get(1)))
            throw new IllegalArgumentException();

        this.cards = new ArrayList<>(cards);
    }

    public MessageType getMessageType() {
        return super.getMessageType();
    }

    public ArrayList<LeaderCard> getCards() {
        return this.cards;
    }
}