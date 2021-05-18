package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_ACTION_CHOOSE_DEVELOPMENT_CARD extends Message implements Serializable {

    private final int cardNumber;
    private final int slotNumber;

    public MSG_ACTION_CHOOSE_DEVELOPMENT_CARD(int cardNumber, int slotNumber) {
        super(MessageType.MSG_ACTION_CHOOSE_DEVELOPMENT_CARD);

        if(cardNumber==-1 && slotNumber==-1){
            this.cardNumber=-1;
            this.slotNumber=-1;
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

    public MessageType getMessageType() {
        return super.getMessageType();
    }
}