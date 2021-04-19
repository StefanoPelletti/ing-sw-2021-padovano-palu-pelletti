package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_ACTION_DISCARD_LEADERCARD extends Message implements Serializable {
    private int cardNumber;

    public MSG_ACTION_DISCARD_LEADERCARD(int cardNumber)
    {
        super(MessageType.MSG_ACTION_DISCARD_LEADERCARD);
        this.cardNumber=cardNumber;
    }

    public MessageType getMessageType() { return super.getMessageType();}
    public int getCardNumber() { return cardNumber;}
}