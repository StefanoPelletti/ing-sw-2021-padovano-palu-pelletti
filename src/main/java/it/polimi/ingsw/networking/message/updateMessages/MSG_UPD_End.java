package it.polimi.ingsw.networking.message.updateMessages;

import it.polimi.ingsw.client.cli.UpdateHandler;
import it.polimi.ingsw.client.gui.UpdateHandlerGUI;
import it.polimi.ingsw.networking.message.MessageType;

import java.io.Serializable;

public class MSG_UPD_End extends UpdateMessage implements Serializable {

    /**
     * MSG_UPD_End is sent by the ClientHandler to the Client.
     * It is sent to the Clients by the Thread who's using the Controller.
     * Its usage is is best described in the Documentation file, Network Protocol.
     */
    public MSG_UPD_End() {
        super(MessageType.MSG_UPD_End);
    }

    @Override
    public void executeCLI(UpdateHandler updateHandler) {
        updateHandler.updateEnd(this);
    }

    @Override
    public void executeGUI(UpdateHandlerGUI updateHandler) {
        updateHandler.updateEnd(this);
    }
}
