package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_ERROR extends Message implements Serializable {

    private final String errorMessage;

    /**
     * MSG_ERROR is sent by the ClientHandler to the Client
     *  It communicates that an error has occurred.
     *  Its usage depends on the phase in which the message is received (game phase or opening phases)
     *  Contains a String explaining the error cause
     * @param errorMessage the error cause
     */
    public MSG_ERROR(String errorMessage) {
        super(MessageType.MSG_ERROR);

        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public MessageType getMessageType() {
        return super.getMessageType();
    }
}