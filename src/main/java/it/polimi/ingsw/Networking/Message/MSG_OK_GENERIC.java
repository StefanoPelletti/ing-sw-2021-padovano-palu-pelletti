package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_OK_GENERIC extends Message implements Serializable {

    private final String message;

    public MSG_OK_GENERIC(String message)
    {
        super(MessageType.MSG_OK_GENERIC);
        this.message=message;
    }

    public MessageType getMessageType() { return super.getMessageType();}
    public String getMessage() { return this.message; }
}
