package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_INIT_RESOURCE_REQUEST extends Message implements Serializable {


    public MSG_INIT_RESOURCE_REQUEST()
    {
        super(MessageType.MSG_INIT_RESOURCE_REQUEST);
    }

    public MessageType getMessageType() { return super.getMessageType();}
}
