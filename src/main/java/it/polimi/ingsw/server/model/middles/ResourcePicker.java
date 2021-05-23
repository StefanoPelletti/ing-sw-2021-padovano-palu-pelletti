package it.polimi.ingsw.server.model.middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_ResourcePicker;
import it.polimi.ingsw.server.utils.A;
import it.polimi.ingsw.server.utils.ModelObservable;

import static it.polimi.ingsw.server.model.enumerators.Resource.*;


public class ResourcePicker extends ModelObservable {
    private boolean enabled;
    private int numOfResources;

    public ResourcePicker() {
        this.enabled = false;
        this.numOfResources = 0;
    }

    public static String toString(boolean enabled, int numOfResources) {
        StringBuilder result = new StringBuilder();
        if (!enabled) return result.append(A.RED + " RESOURCE PICKER IS NOT ENABLED! " + A.RESET).toString();

        result.append(A.CYAN + " RESOURCE PICKER IS HERE TO HELP! " + A.RESET).append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");

        result.append("  Number of resource to get: ").append(numOfResources).append("\n");
        result.append("   1:  " + SHIELD);
        result.append("   2:  " + COIN);
        result.append("   3:  " + SERVANT);
        result.append("   4:  " + STONE);

        return result.toString();
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
        if (enabled && this.numOfResources != 0) {
            notifyObservers();
        }
    }

    @Override
    public String toString() {
        return ResourcePicker.toString(this.enabled, this.numOfResources);
    }

    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    public MSG_UPD_ResourcePicker generateMessage() {
        return new MSG_UPD_ResourcePicker(
                this.enabled,
                this.numOfResources
        );
    }
}