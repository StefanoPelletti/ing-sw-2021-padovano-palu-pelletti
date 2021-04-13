package it.polimi.ingsw.Networking;

import java.io.Serializable;

public class MSG_ACTION_ACTIVATE_LEADERCARDS extends Message implements Serializable {
    private MessageType messageType;
    private int cardNumber;

    public MSG_ACTION_ACTIVATE_LEADERCARDS(int cardNumber)
    {
        super(MessageType.MSG_ACTION_ACTIVATE_LEADERCARDS);
        this.cardNumber=cardNumber;
    }

    public MessageType getMessageType() { return messageType; }
    public int getCardNumber() { return cardNumber;}
}
