package it.polimi.ingsw.server.model.middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_ResourcePicker;
import it.polimi.ingsw.server.utils.A;
import it.polimi.ingsw.server.utils.ModelObservable;

import static it.polimi.ingsw.server.model.enumerators.Resource.*;


public class ResourcePicker extends ModelObservable {
    private boolean enabled;
    private int numOfResources;

    /**
     * CONSTRUCTOR
     */
    public ResourcePicker() {
        this.enabled = false;
        this.numOfResources = 0;
    }

    /**
     * Returns the amount of Resource and a related list the user should choose from.
     * A ResourcePicker or a ResourcePickerSimplified may use this shared method by passing their internal values.
     * @param enabled True if the ResourcePicker is enabled.
     * @param numOfResources The number of resources available for the player.
     * @return A String representing the current state of a ResourcePicker.
     */
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

    /**
     * Sets the ResourcePicker active or disabled.
     * Also notifies observers.
     * This method should be used only after the usage of setNumOfResources().
     * @param enabled The boolean value to set.
     * @see #setNumOfResources(int)
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        notifyObservers();
    }

    public int getNumOfResources() {
        return numOfResources;
    }

    /**
     * Sets the ResourcePicker amount of Resources.
     * To notify, the ResourcePicker must be set to enabled.
     * @param numOfResources The amount of available numOfResources for a player.
     * @see #setEnabled(boolean)
     */
    public void setNumOfResources(int numOfResources) {
        this.numOfResources = numOfResources;
        if (enabled) {
            notifyObservers();
        }
    }

    /**
     * Decrements the number of resources that a player must choose.
     * Notifies the observers if the ResourcePicker is still enabled and the amount of resource it not zero.
     */
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

    /**
     * Creates a message using generateMessage() and notifies observers.
     * @see #generateMessage()
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     * Returns a MSG_UPD_ResourcePicker representing the current state of the ResourcePicker.
     * @return A MSG_UPD_ResourcePicker representing the current state of the ResourcePicker.
     */
    public MSG_UPD_ResourcePicker generateMessage() {
        return new MSG_UPD_ResourcePicker(
                this.enabled,
                this.numOfResources
        );
    }
}