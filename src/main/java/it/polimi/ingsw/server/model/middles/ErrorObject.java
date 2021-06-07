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
     * Sets the ErrorObject enabled or disabled.
     * If the ErrorObject is being set to enabled, it will notify its observers.
     * If the ErrorObject is being set to disabled, it will reset the error Message and will not notify the observers.
     *
     * @param enabled The boolean value to set.
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

    /**
     * Sets the ErrorObject message that will be forwarded to the observers when a setEnable(true) is invoked.
     *
     * @param errorMessage The error message.
     * @see #setEnabled(boolean)
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Creates a message using generateMessage() and notifies observers.
     *
     * @see #generateMessage()
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     * Returns a MSG_ERROR representing the current state of the ErrorObject.
     *
     * @return A MSG_ERROR representing the current state of the ErrorObject.
     */
    public MSG_ERROR generateMessage() {
        return new MSG_ERROR(this.errorMessage);
    }
}