package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_ACTION_ACTIVATE_LEADERCARD extends Message implements Serializable {
    private final int cardNumber;

    public MSG_ACTION_ACTIVATE_LEADERCARD(int cardNumber)
    {
        super(MessageType.MSG_ACTION_ACTIVATE_LEADERCARD);
        this.cardNumber=cardNumber;
    }

    public MessageType getMessageType() { return super.getMessageType();}
    public int getCardNumber() { return cardNumber;}
}
