package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_ALERT extends Message implements Serializable {

    private String message;

    public MSG_ALERT(String message)
    {
        super(MessageType.MSG_ALERT);
        this.message = message;
    }

    public MessageType getMessageType() { return super.getMessageType();}
    public String getMessage() { return this.message; }
}
