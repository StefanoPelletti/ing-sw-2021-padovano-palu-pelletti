package it.polimi.ingsw.networking.message.updateMessages.middlesUpdate;

import it.polimi.ingsw.client.cli.UpdateHandler;
import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.networking.message.updateMessages.UpdateMessage;

import java.io.Serializable;

public class MSG_UPD_ResourcePicker extends UpdateMessage implements Serializable {

    private final boolean enabled;
    private final int numOfResources;

    /**
     * MSG_UPD_ResourcePicker is sent by the ClientHandler to the Client.
     * It is generated by a model.middles.ResourcePicker generateMessage().
     * It contains the number of resources left that the player can freely choose from.
     * Its usage is best described in the Documentation files, Action and DisconnectionReconnection.
     * @param enabled if true, the currentPlayer must resolve this object as a priority
     * @param numOfResources the number of resources left
     * @see it.polimi.ingsw.server.model.middles.ResourcePicker;
     */
    public MSG_UPD_ResourcePicker(boolean enabled, int numOfResources) {
        super(MessageType.MSG_UPD_ResourcePicker);

        this.enabled = enabled;
        this.numOfResources = numOfResources;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public int getNumOfResources() {
        return this.numOfResources;
    }

    @Override
    public void executeCLI(UpdateHandler updateHandler) {
        updateHandler.updateResourcePicker(this);
    }

    @Override
    public void executeGUI() {

    }
}