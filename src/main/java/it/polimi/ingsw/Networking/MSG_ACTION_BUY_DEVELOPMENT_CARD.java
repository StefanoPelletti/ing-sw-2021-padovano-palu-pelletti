package it.polimi.ingsw.Networking;

import java.io.Serializable;

public class MSG_ACTION_BUY_DEVELOPMENT_CARD extends Message implements Serializable {
    private MessageType messageType;

    public MSG_ACTION_BUY_DEVELOPMENT_CARD()
    {
        super(MessageType.MSG_ACTION_BUY_DEVELOPMENT_CARD);
    }

    public MessageType getMessageType() { return messageType; }
}
