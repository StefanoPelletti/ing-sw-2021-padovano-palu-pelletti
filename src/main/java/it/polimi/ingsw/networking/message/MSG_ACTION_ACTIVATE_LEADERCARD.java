package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_ACTION_ACTIVATE_LEADERCARD extends Message implements Serializable {

    private final int cardNumber;

    /**
     * MSG_ACTION_ACTIVATE_LEADERCARD is sent by the Client to the ClientHandler.
     * It requests the ClientHandler to perform the Controller activateLeaderCard() method.
     * Contains a number representing the card to discard.
     * @param cardNumber The desired card number.
     * @throws IllegalArgumentException If cardNumber is not 0 or 1.
     */
    public MSG_ACTION_ACTIVATE_LEADERCARD(int cardNumber) {
        super(MessageType.MSG_ACTION_ACTIVATE_LEADERCARD);

        if (cardNumber != 0 && cardNumber != 1)
            throw new IllegalArgumentException();

        this.cardNumber = cardNumber;
    }

    public int getCardNumber() {
        return cardNumber;
    }

}