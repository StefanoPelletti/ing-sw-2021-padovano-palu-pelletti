package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_INIT_RESOURCE_REPLY extends Message implements Serializable {


    public MSG_INIT_RESOURCE_REPLY()
    {
        super(MessageType.MSG_INIT_RESOURCE_REPLY);
    }

    public MessageType getMessageType() { return super.getMessageType();}
}
