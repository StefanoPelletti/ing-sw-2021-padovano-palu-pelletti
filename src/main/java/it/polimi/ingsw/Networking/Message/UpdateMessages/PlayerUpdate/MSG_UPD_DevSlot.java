package it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;

import java.io.Serializable;

public class MSG_UPD_DevSlot extends Message implements Serializable {

    public MSG_UPD_DevSlot()
    {
        super(MessageType.MSG_UPD_DevSlot);
    }

    public MessageType getMessageType() { return super.getMessageType();}
}
