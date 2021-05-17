package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_NOTIFICATION extends Message implements Serializable {

    private final String message;

    public MSG_NOTIFICATION(String message) {
        super(MessageType.MSG_NOTIFICATION);

        this.message = message;
    }

    public MessageType getMessageType() {
        return super.getMessageType();
    }

    public String getMessage() {
        return this.message;
    }
}