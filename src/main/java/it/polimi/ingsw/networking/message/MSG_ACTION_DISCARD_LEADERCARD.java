package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_ACTION_DISCARD_LEADERCARD extends Message implements Serializable {

    private final int cardNumber;

    public MSG_ACTION_DISCARD_LEADERCARD(int cardNumber) {
        super(MessageType.MSG_ACTION_DISCARD_LEADERCARD);

        if (cardNumber != 0 && cardNumber != 1)
            throw new IllegalArgumentException();

        this.cardNumber = cardNumber;
    }

    public int getCardNumber() {
        return cardNumber;
    }
}