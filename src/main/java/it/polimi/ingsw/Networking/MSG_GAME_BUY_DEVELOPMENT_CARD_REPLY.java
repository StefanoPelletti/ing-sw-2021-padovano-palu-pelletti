package it.polimi.ingsw.Networking;

import java.io.Serializable;

public class MSG_GAME_BUY_DEVELOPMENT_CARD_REPLY extends Message implements Serializable {
    private MessageType messageType;

    public MSG_GAME_BUY_DEVELOPMENT_CARD_REPLY()
    {
        super(MessageType.MSG_GAME_BUY_DEVELOPMENT_CARD_REPLY);
    }

    public MessageType getMessageType() { return messageType; }
}
