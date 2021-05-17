package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_ACTION_ENDTURN extends Message implements Serializable {

    public MSG_ACTION_ENDTURN() {
        super(MessageType.MSG_ACTION_ENDTURN);
    }

    public MessageType getMessageType() {
        return super.getMessageType();
    }
}