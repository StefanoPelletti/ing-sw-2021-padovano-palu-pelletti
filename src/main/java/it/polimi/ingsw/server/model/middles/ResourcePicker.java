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