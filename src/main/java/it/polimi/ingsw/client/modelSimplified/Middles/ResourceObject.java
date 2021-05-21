package it.polimi.ingsw.client.modelSimplified.Middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_ResourceObject;
import it.polimi.ingsw.server.utils.Displayer;

public class ResourceObject {
    private boolean enabled;
    private int numOfResources;

    public boolean isEnabled() {
        return enabled;
    }

    public void update(MSG_UPD_ResourceObject message) {
        boolean newEnabled = message.getEnabled();
        int newNumOfResource = message.getNumOfResources();

        this.enabled = newEnabled;
        this.numOfResources = newNumOfResource;
    }

    public int getNumOfResources() {
        return this.numOfResources;
    }

    @Override
    public String toString() {
        return Displayer.resourceObjectToString(this.enabled, this.numOfResources);
    }
}