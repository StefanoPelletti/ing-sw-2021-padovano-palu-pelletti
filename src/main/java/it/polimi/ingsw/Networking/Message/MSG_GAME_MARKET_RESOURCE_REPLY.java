package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_GAME_MARKET_RESOURCE_REPLY extends Message implements Serializable {


    public MSG_GAME_MARKET_RESOURCE_REPLY()
    {
        super(MessageType.MSG_GAME_MARKET_RESOURCE_REPLY);
    }

    public MessageType getMessageType() { return super.getMessageType();}
}
