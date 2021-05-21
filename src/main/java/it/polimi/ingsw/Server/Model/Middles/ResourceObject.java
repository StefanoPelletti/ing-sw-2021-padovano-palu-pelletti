package it.polimi.ingsw.Server.Model.Middles;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.MSG_UPD_ResourceObject;
import it.polimi.ingsw.Server.Utils.Displayer;
import it.polimi.ingsw.Server.Utils.ModelObservable;


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