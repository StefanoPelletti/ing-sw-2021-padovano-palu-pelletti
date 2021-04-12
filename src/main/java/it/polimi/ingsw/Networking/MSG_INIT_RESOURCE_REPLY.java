package it.polimi.ingsw.Networking;

import java.io.Serializable;

public class MSG_INIT_RESOURCE_REPLY extends Message implements Serializable {
    private MessageType messageType;

    public MSG_INIT_RESOURCE_REPLY()
    {
        super(MessageType.MSG_INIT_RESOURCE_REPLY);
    }

    public MessageType getMessageType() { return messageType; }
}
