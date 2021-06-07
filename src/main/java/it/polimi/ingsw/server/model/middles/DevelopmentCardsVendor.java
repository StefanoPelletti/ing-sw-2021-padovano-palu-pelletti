package it.polimi.ingsw.server.model.middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_DevCardsVendor;
import it.polimi.ingsw.server.utils.ModelObservable;

import java.util.List;

public class DevelopmentCardsVendor extends ModelObservable {

    private boolean enabled;
    private List<VendorCard> vendorCards;

    /**
     * CONSTRUCTOR
     */
    public DevelopmentCardsVendor() {
        this.enabled = false;
        vendorCards = null;
    }

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the DevelopmentCardsVendor enabled or disabled.
     * If the DevelopmentCardsVendor is being set to enabled, it will notify its observers.
     * If the DevelopmentCardsVendor is being set to disabled, it will disband the internal List of VendorCards and notify its observers.
     * This method should be used only after the usage of setCards().
     *
     * @param enabled The boolean value to set.
     * @see #setCards(List)
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled)
            vendorCards = null;
        notifyObservers();
    }

    public List<VendorCard> getCards() {
        return vendorCards;
    }

    /**
     * Sets the VendorCards that will be shown to the user.
     * To notify, the DevelopmentCardsVendor must be set to enabled.
     *
     * @param cards The List of VendorCards.
     * @see #setEnabled(boolean)
     */
    public void setCards(List<VendorCard> cards) {
        this.vendorCards = cards;
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
     * Returns a MSG_UPD_DevCardsVendor representing the current state of the DevelopmentCardsVendor.
     *
     * @return A MSG_UPD_DevCardsVendor representing the current state of the DevelopmentCardsVendor.
     */
    public MSG_UPD_DevCardsVendor generateMessage() {
        return new MSG_UPD_DevCardsVendor(
                this.enabled,
                this.vendorCards
        );
    }
}