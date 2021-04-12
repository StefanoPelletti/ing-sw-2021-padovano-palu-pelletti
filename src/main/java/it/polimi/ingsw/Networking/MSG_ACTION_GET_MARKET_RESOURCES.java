package it.polimi.ingsw.Networking;

import java.io.Serializable;

public class MSG_ACTION_GET_MARKET_RESOURCES extends Message implements Serializable {
    private MessageType messageType;

    public MSG_ACTION_GET_MARKET_RESOURCES()
    {
        super(MessageType.MSG_ACTION_GET_MARKET_RESOURCES);
    }

    public MessageType getMessageType() { return messageType; }
}
