package it.polimi.ingsw.Networking;

import java.io.Serializable;

public class MSG_ACTION_ACTIVATE_PRODUCTION extends Message implements Serializable {
    private MessageType messageType;

    public MSG_ACTION_ACTIVATE_PRODUCTION()
    {
        super(MessageType.MSG_ACTION_ACTIVATE_PRODUCTION);
    }

    public MessageType getMessageType() { return messageType; }
}
