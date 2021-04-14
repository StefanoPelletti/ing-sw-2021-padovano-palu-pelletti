package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_ACTION_ACTIVATE_LEADERCARDS extends Message implements Serializable {
    private int cardNumber;

    public MSG_ACTION_ACTIVATE_LEADERCARDS(int cardNumber)
    {
        super(MessageType.MSG_ACTION_ACTIVATE_LEADERCARDS);
        this.cardNumber=cardNumber;
    }

    public MessageType getMessageType() { return super.getMessageType(); }
    public int getCardNumber() { return cardNumber;}
}
