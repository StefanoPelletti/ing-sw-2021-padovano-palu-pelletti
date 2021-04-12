package it.polimi.ingsw.Networking;

import java.io.Serializable;

public class MSG_ERROR_GENERIC extends Message implements Serializable {
    private MessageType messageType;
    private final String message;

    public MSG_ERROR_GENERIC(String message)
    {
        super(MessageType.MSG_ERROR_GENERIC);
        this.message=message;
    }

    public MessageType getMessageType() { return messageType; }
    public String getMessage() { return this.message; }
}
