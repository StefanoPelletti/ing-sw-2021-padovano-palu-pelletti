package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_INIT_START extends Message implements Serializable {


    public MSG_INIT_START()
    {
        super(MessageType.MSG_INIT_START);
    }

    public MessageType getMessageType() { return super.getMessageType();}
}
