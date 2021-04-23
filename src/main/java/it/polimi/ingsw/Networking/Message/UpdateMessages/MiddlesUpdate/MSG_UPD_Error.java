package it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;

import java.io.Serializable;

public class MSG_UPD_Error extends Message implements Serializable {

    private final String errorMessage;

    public MSG_UPD_Error(String errorMessage)
    {
        super(MessageType.MSG_UPD_Error);

        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() { return this.errorMessage; }

    public MessageType getMessageType() { return super.getMessageType();}
}
