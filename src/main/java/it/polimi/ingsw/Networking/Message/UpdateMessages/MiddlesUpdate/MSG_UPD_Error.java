package it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;

import java.io.Serializable;

public class MSG_UPD_Error extends Message implements Serializable {

    public MSG_UPD_Error()
    {
        super(MessageType.MSG_UPD_Error);
    }


    public MessageType getMessageType() { return super.getMessageType();}

}
