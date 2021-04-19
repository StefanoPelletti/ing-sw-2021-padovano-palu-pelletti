package it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;

import java.io.Serializable;

public class MSG_UPD_Strongbox extends Message implements Serializable {

    public MSG_UPD_Strongbox()
    {
        super(MessageType.MSG_UPD_Strongbox);
    }
    public MessageType getMessageType() { return super.getMessageType();}
}
