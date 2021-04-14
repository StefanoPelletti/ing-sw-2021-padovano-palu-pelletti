package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_GAME_YOUR_TURN extends Message implements Serializable {
    private MessageType messageType;

    public MSG_GAME_YOUR_TURN()
    {
        super(MessageType.MSG_GAME_YOUR_TURN);
    }

    public MessageType getMessageType() { return messageType; }
}
