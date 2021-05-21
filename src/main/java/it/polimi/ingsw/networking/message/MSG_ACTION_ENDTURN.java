package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_ACTION_ENDTURN extends Message implements Serializable {

    public MSG_ACTION_ENDTURN() {
        super(MessageType.MSG_ACTION_ENDTURN);
    }
}