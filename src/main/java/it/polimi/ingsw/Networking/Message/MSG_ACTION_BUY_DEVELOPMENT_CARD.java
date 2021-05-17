package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_ACTION_BUY_DEVELOPMENT_CARD extends Message implements Serializable {

    public MSG_ACTION_BUY_DEVELOPMENT_CARD() {
        super(MessageType.MSG_ACTION_BUY_DEVELOPMENT_CARD);
    }

    public MessageType getMessageType() {
        return super.getMessageType();
    }
}