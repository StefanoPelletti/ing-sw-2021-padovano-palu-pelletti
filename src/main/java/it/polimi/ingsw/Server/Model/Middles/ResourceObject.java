package it.polimi.ingsw.Server.Model.Middles;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.MSG_UPD_ResourceObject;
import it.polimi.ingsw.Server.Utils.ModelObservable;


public class ResourceObject extends ModelObservable  {
    private boolean enabled;
    private int numOfResources;

    public ResourceObject()
    {
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

    public void decNumOfResources() {
        this.numOfResources--;
        if(enabled) {
            notifyObservers();
        }
    }
    public void setNumOfResources(int numOfResources) {
        this.numOfResources = numOfResources;
        if(enabled) {
            notifyObservers();
        }
    }

    private void notifyObservers(){
        this.notifyObservers(new MSG_UPD_ResourceObject(this.enabled, this.numOfResources));
    }
}
