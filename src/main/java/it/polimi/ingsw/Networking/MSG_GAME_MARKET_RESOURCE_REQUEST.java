package it.polimi.ingsw.Networking;

import java.io.Serializable;

public class MSG_GAME_MARKET_RESOURCE_REQUEST extends Message implements Serializable {
    private MessageType messageType;

    public MSG_GAME_MARKET_RESOURCE_REQUEST()
    {
        super(MessageType.MSG_GAME_MARKET_RESOURCE_REQUEST);
    }

    public MessageType getMessageType() { return messageType; }
}
