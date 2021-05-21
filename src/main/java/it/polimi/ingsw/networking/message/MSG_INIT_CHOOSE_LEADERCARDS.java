package it.polimi.ingsw.networking.message;

import it.polimi.ingsw.server.model.LeaderCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MSG_INIT_CHOOSE_LEADERCARDS extends Message implements Serializable {

    private final List<LeaderCard> cards;

    public MSG_INIT_CHOOSE_LEADERCARDS(List<LeaderCard> cards) {
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

    public List<LeaderCard> getCards() {
        return this.cards;
    }
}