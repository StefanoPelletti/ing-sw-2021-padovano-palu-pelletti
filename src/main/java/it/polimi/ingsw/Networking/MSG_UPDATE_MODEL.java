package it.polimi.ingsw.Networking;

import java.io.Serializable;

public class MSG_UPDATE_MODEL extends Message implements Serializable {
    private MessageType messageType;

    public MSG_UPDATE_MODEL()
    {
        super(MessageType.MSG_UPDATE_MODEL);
    }

    public MessageType getMessageType() { return messageType; }
}
