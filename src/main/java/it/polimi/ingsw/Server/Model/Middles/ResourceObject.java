package it.polimi.ingsw.Server.Model.Middles;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;

import java.io.Serializable;

public class ResourceObject implements Serializable {
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
        //notify();
    }

    public int getNumOfResources() {
        return numOfResources;
    }

    public void decNumOfResources() {
        this.numOfResources--;
        if(enabled) {
            //notify();
        }
    }
    public void setNumOfResources(int numOfResources) {
        this.numOfResources = numOfResources;
        if(enabled) {
            //notify();
        }
    }

}
