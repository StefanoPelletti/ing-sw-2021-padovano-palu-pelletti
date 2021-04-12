package it.polimi.ingsw.Networking;

import java.io.Serializable;

public class MSG_ACTION_CHANGE_DEPOT_CONFIG extends Message implements Serializable {
    private MessageType messageType;

    public MSG_ACTION_CHANGE_DEPOT_CONFIG()
    {
        super(MessageType.MSG_ACTION_CHANGE_DEPOT_CONFIG);
    }

    public MessageType getMessageType() { return messageType; }

}
