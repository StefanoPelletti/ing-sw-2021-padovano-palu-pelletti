package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_ACTION_ENDTURN extends Message implements Serializable {

    /**
     * MSG_ACTION_ENDTURN is sent by the Client to the ClientHandler
     *  It requests the ClientHandler to perform the Controller endTurn() method
     */
    public MSG_ACTION_ENDTURN() {
        super(MessageType.MSG_ACTION_ENDTURN);
    }
}