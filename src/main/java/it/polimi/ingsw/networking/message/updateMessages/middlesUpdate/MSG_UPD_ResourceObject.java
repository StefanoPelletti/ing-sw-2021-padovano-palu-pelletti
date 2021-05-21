package it.polimi.ingsw.networking.message.updateMessages.middlesUpdate;

import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;

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
}