package it.polimi.ingsw.networking.message;

import it.polimi.ingsw.client.cli.UpdateHandler;
import it.polimi.ingsw.client.gui.UpdateHandlerGUI;
import it.polimi.ingsw.networking.message.updateMessages.UpdateMessage;

import java.io.Serializable;

public class MSG_ERROR extends UpdateMessage implements Serializable {

    private final String errorMessage;

    /**
     * MSG_ERROR is sent by the ClientHandler to the Client.
     * It communicates that an error has occurred.
     * Its usage depends on the phase in which the message is received (game phase or opening phases).
     * Contains a String explaining the error cause.
     *
     * @param errorMessage The error cause.
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

    @Override
    public void executeCLI(UpdateHandler updateHandler) {
        updateHandler.printError(this);
    }

    @Override
    public void executeGUI(UpdateHandlerGUI updateHandler) {
        updateHandler.printError(this);
    }
}