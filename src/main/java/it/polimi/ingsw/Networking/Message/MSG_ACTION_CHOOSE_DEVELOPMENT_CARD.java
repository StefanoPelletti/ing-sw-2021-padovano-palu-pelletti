package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_ACTION_CHOOSE_DEVELOPMENT_CARD extends Message implements Serializable {


    int cardNumber;
    int slotNumber;
    public MSG_ACTION_CHOOSE_DEVELOPMENT_CARD(int cardNumber, int slotNumber)
    {
        super(MessageType.MSG_ACTION_CHOOSE_DEVELOPMENT_CARD);
        this.cardNumber = cardNumber;
        this.slotNumber= slotNumber;
    }

    public int getCardNumber() {
        return cardNumber;
    }
    public int getSlotNumber() {
        return slotNumber;
    }
    public MessageType getMessageType() { return super.getMessageType();}
}
