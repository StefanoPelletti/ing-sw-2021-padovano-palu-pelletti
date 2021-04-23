package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_ACTION_ACTIVATE_LEADERCARD extends Message implements Serializable {

    private final int cardNumber;

    //cardNumber must be 0 or 1
    public MSG_ACTION_ACTIVATE_LEADERCARD(int cardNumber)
    {
        super(MessageType.MSG_ACTION_ACTIVATE_LEADERCARD);

        if(cardNumber!=0 && cardNumber!=1) throw new IllegalArgumentException();

        this.cardNumber=cardNumber;
    }

    public int getCardNumber() { return cardNumber;}

    public MessageType getMessageType() { return super.getMessageType();}
}
