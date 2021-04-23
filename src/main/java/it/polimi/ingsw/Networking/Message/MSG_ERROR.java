package it.polimi.ingsw.Networking.Message;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;

import java.io.Serializable;

public class MSG_ERROR extends Message implements Serializable {

    private final String errorMessage;

    public MSG_ERROR(String errorMessage)
    {
        super(MessageType.MSG_ERROR);

        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() { return this.errorMessage; }

    public MessageType getMessageType() { return super.getMessageType();}
}
