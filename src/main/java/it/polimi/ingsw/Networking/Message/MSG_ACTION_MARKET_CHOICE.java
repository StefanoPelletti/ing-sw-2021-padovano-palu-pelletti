package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_ACTION_MARKET_CHOICE extends Message implements Serializable {

    private final int choice;

    public MSG_ACTION_MARKET_CHOICE(int choice){
        super(MessageType.MSG_ACTION_MARKET_CHOICE);

        this.choice = choice;
    }

    public int getChoice(){
        return choice;
    }

    public MessageType getMessageType() {
        return super.getMessageType();
    }
}
