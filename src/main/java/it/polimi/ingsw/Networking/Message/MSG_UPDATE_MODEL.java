package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_UPDATE_MODEL extends Message implements Serializable {


    public MSG_UPDATE_MODEL()
    {
        super(MessageType.MSG_UPDATE_MODEL);
    }

    public MessageType getMessageType() { return super.getMessageType();}
}
