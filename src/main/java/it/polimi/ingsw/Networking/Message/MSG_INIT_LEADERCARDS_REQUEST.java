package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_INIT_LEADERCARDS_REQUEST extends Message implements Serializable {


    public MSG_INIT_LEADERCARDS_REQUEST()
    {
        super(MessageType.MSG_INIT_LEADERCARDS_REQUEST);
    }

    public MessageType getMessageType() { return super.getMessageType();}
}
