package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_ERROR_GENERIC extends Message implements Serializable {

    private final String message;

    public MSG_ERROR_GENERIC(String message)
    {
        super(MessageType.MSG_ERROR_GENERIC);
        this.message=message;
    }

    public MessageType getMessageType() { return super.getMessageType();}
    public String getMessage() { return this.message; }
}
