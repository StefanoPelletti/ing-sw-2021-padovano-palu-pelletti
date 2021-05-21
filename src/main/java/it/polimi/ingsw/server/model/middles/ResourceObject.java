package it.polimi.ingsw.server.model.middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_ResourceObject;
import it.polimi.ingsw.server.utils.Displayer;
import it.polimi.ingsw.server.utils.ModelObservable;


public class ResourceObject extends ModelObservable {
    private boolean enabled;
    private int numOfResources;

    public ResourceObject() {
        this.enabled = false;
        this.numOfResources = 0;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        notifyObservers();
    }

    public int getNumOfResources() {
        return numOfResources;
    }

    public void setNumOfResources(int numOfResources) {
        this.numOfResources = numOfResources;
        if (enabled) {
            notifyObservers();
        }
    }

    public void decNumOfResources() {
        this.numOfResources--;
        if (enabled) {
            notifyObservers();
        }
    }

    @Override
    public String toString() {
        return Displayer.resourceObjectToString(this.enabled, this.numOfResources);
    }

    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    public MSG_UPD_ResourceObject generateMessage() {
        return new MSG_UPD_ResourceObject(
                this.enabled,
                this.numOfResources
        );
    }
}