package it.polimi.ingsw.networking.message.updateMessages;

import it.polimi.ingsw.client.cli.UpdateHandler;
import it.polimi.ingsw.client.gui.UpdateHandlerGUI;
import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;


/**
 * The UpdateMessage abstract class categorizes the Messages used to perform model updating.
 * <p>
 * This class acts as a linkage between the UpdateHandlerGUI and the correct Update handling method written in the UpdateHandlerGUI.
 */
public abstract class UpdateMessage extends Message {

    public UpdateMessage(MessageType messageType) {
        super(messageType);
    }

    /**
     * Executes the updating routine, specific for the CLI implementation of the Game.
     *
     * @param updateHandler the reference to the UpdateHandlerGUI (CLI implementation)
     */
    public abstract void executeCLI(UpdateHandler updateHandler);

    /**
     * Executes the updating routine, specific for the GUI implementation of the Game.
     *
     *
     */
    public abstract void executeGUI(UpdateHandlerGUI updateHandler);
}
