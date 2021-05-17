package it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;

import java.io.Serializable;

public class MSG_UPD_ResourceObject extends Message implements Serializable {

    private final boolean enabled;
    private final int numOfResources;

    public MSG_UPD_ResourceObject(boolean enabled, int numOfResources) {
        super(MessageType.MSG_UPD_ResourceObject);

        this.enabled = enabled;
        this.numOfResources = numOfResources;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public int getNumOfResources() {
        return this.numOfResources;
    }

    public MessageType getMessageType() {
        return super.getMessageType();
    }
}