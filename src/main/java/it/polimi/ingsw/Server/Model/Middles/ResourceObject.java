package it.polimi.ingsw.Server.Model.Middles;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.MSG_UPD_ResourceObject;
import it.polimi.ingsw.Server.Utils.A;
import it.polimi.ingsw.Server.Utils.ModelObservable;

import static it.polimi.ingsw.Server.Model.Enumerators.Resource.*;
import static it.polimi.ingsw.Server.Model.Enumerators.Resource.STONE;


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

    public void decNumOfResources() {
        this.numOfResources--;
        if (enabled) {
            notifyObservers();
        }
    }

    public void setNumOfResources(int numOfResources) {
        this.numOfResources = numOfResources;
        if (enabled) {
            notifyObservers();
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\n");
        result.append(A.CYAN + " RESOURCE OBJECT IS HERE TO HELP! " + A.RESET).append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        if (enabled) {
            result.append("  Number of resource to get: ").append(numOfResources).append("\n");
            result.append("   1:  " + SHIELD);
            result.append("   2:  " + COIN);
            result.append("   3:  " + SERVANT);
            result.append("   4:  " + STONE);
        } else
            result.append("  ResourceObject is not enabled.");
        return result.toString();
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