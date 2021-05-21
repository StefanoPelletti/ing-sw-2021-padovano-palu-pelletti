package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_ACTION_BUY_DEVELOPMENT_CARD extends Message implements Serializable {

    public MSG_ACTION_BUY_DEVELOPMENT_CARD() {
        super(MessageType.MSG_ACTION_BUY_DEVELOPMENT_CARD);
    }
}