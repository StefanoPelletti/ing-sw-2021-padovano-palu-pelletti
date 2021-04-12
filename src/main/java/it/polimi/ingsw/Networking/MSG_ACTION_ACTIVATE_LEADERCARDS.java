package it.polimi.ingsw.Networking;

import java.io.Serializable;

public class MSG_ACTION_ACTIVATE_LEADERCARDS extends Message implements Serializable {
    private MessageType messageType;

    public MSG_ACTION_ACTIVATE_LEADERCARDS()
    {
        super(MessageType.MSG_ACTION_ACTIVATE_LEADERCARDS);
    }

    public MessageType getMessageType() { return messageType; }
}
