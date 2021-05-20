package it.polimi.ingsw.Client.ModelSimplified.Middles;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.MSG_UPD_ResourceObject;
import it.polimi.ingsw.Server.Utils.Displayer;

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