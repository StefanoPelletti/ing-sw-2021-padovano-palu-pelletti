package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_ACTION_CHANGE_DEPOT_CONFIG extends Message implements Serializable {


    public MSG_ACTION_CHANGE_DEPOT_CONFIG()
    {
        super(MessageType.MSG_ACTION_CHANGE_DEPOT_CONFIG);
    }

    public MessageType getMessageType() { return super.getMessageType();}

}
