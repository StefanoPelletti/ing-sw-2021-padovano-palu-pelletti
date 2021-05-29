package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_ACTION_CHOOSE_DEVELOPMENT_CARD extends Message implements Serializable {

    private final int cardNumber;
    private final int slotNumber;

    /**
     * MSG_ACTION_CHOOSE_DEVELOPMENT_CARD is sent by the Client to the ClientHandler.
     * It requests the ClientHandler to perform the Controller chooseDevelopmentCard() method.
     * Contains the number of the card chosen, and the number of the slot to place the card in.
     * Note: if cardNumber and slotNumber are both -1, it means the player wants to cancel the buyDevelopmentCard action.
     * @param cardNumber The desired card to buy.
     * @param slotNumber The desired slot to place the above card.
     * @throws IllegalArgumentException If the message is built with: <ul>
     * <li> cardNumber less than -1
     * <li> slotNumber not between 0 and 2 (included).

     */
    public MSG_ACTION_CHOOSE_DEVELOPMENT_CARD(int cardNumber, int slotNumber) {
        super(MessageType.MSG_ACTION_CHOOSE_DEVELOPMENT_CARD);

        if (cardNumber == -1 && slotNumber == -1) {
            this.cardNumber = -1;
            this.slotNumber = -1;
        } else {
            if (cardNumber < 0)
                throw new IllegalArgumentException();
            if (slotNumber != 0 && slotNumber != 1 && slotNumber != 2)
                throw new IllegalArgumentException();

            this.cardNumber = cardNumber;
            this.slotNumber = slotNumber;
        }
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public int getSlotNumber() {
        return slotNumber;
    }
}