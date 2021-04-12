package it.polimi.ingsw.Networking;

import java.io.Serializable;

public class MSG_OK_GENERIC extends Message implements Serializable {
    private MessageType messageType;
    private final String message;

    public MSG_OK_GENERIC(String message)
    {
        super(MessageType.MSG_OK_GENERIC);
        this.message=message;
    }

    public MessageType getMessageType() { return messageType; }
    public String getMessage() { return this.message; }
}
