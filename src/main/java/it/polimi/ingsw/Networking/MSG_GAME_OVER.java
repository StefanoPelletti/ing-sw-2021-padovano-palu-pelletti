package it.polimi.ingsw.Networking;

import java.io.Serializable;

public class MSG_GAME_OVER extends Message implements Serializable {
    private MessageType messageType;

    public MSG_GAME_OVER()
    {
        super(MessageType.MSG_GAME_OVER);
    }

    public MessageType getMessageType() { return messageType; }
}
