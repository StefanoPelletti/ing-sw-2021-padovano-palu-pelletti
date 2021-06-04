package it.polimi.ingsw.client.modelSimplified.middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_ResourcePicker;
import it.polimi.ingsw.server.model.middles.ResourcePicker;
import it.polimi.ingsw.server.utils.A;

import static it.polimi.ingsw.server.model.enumerators.Resource.*;
import static it.polimi.ingsw.server.model.enumerators.Resource.STONE;

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

    /**
     * Returns the amount of Resource and a related list the user should choose from.
     * @return A String representing the current state of a ResourcePicker.
     */
    @Override
    public String toString() {
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
}