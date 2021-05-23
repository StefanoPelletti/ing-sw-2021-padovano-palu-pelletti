package it.polimi.ingsw.client.modelSimplified.middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_ResourcePicker;
import it.polimi.ingsw.server.model.middles.ResourcePicker;

public class ResourcePickerSimplified {
    private boolean enabled;
    private int numOfResources;

    public boolean isEnabled() {
        return enabled;
    }

    public void update(MSG_UPD_ResourcePicker message) {
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
        return ResourcePicker.toString(this.enabled, this.numOfResources);
    }
}