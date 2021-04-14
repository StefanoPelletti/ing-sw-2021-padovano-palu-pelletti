package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_INIT_START extends Message implements Serializable {
    private MessageType messageType;

    public MSG_INIT_START()
    {
        super(MessageType.MSG_INIT_START);
    }

    public MessageType getMessageType() { return messageType; }
}
