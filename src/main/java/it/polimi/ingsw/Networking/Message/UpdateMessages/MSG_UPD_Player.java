package it.polimi.ingsw.Networking.Message.UpdateMessages;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;

import java.io.Serializable;

public class MSG_UPD_Player extends Message implements Serializable {
    public MSG_UPD_Player()
    {
        super(MessageType.MSG_UPD_Player);
    }
    public MessageType getMessageType() { return super.getMessageType();}
}
