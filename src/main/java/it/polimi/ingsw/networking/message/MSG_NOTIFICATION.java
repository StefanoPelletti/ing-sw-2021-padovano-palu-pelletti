package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_NOTIFICATION extends Message implements Serializable {

    private final String message;

    /**
     * MSG_NOTIFICATION is sent by the ClientHandler to the Client.
     * It indicates that some event has happened.
     * @param message A String representing the event.
     */
    public MSG_NOTIFICATION(String message) {
        super(MessageType.MSG_NOTIFICATION);

        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}