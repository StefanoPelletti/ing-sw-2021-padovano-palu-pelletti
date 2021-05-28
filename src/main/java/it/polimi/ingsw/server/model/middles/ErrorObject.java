package it.polimi.ingsw.server.model.middles;


import it.polimi.ingsw.networking.message.MSG_ERROR;
import it.polimi.ingsw.server.utils.ModelObservable;

public class ErrorObject extends ModelObservable {
    private boolean enabled;
    private String errorMessage;

    /**
     * CONSTRUCTOR
     */
    public ErrorObject() {
        this.enabled = false;
        this.errorMessage = "";
    }

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the ErrorObject active or disabled
     * Also notifies observers
     * @param enabled the boolean value to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled)
            errorMessage = "";
        if (enabled)
            notifyObservers();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Creates a message and notifies observers
     * @see #generateMessage()
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     *
     * @return a MSG_UPD_DevCardsVendor representing the current state of the DevelopmentCardsVendor
     */
    public MSG_ERROR generateMessage() {
        return new MSG_ERROR(this.errorMessage);
    }
}