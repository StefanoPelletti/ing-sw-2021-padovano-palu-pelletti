package it.polimi.ingsw.server.model.middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_DevCardsVendor;
import it.polimi.ingsw.server.model.DevelopmentCard;
import it.polimi.ingsw.server.utils.A;
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

    /**
     * Returns the representation of the current state of a given DevelopmentCardVendor.
     * A DevelopmentCardsVendor or a DevelopmentCardsVendorSimplified may use this shared method by passing their internal values.
     * @param enabled True if the DevelopmentCardsVendor is enabled.
     * @param cards The cards of the DevelopmentCardsVendor.
     * @return A String representing the current state of the DevelopmentCardsVendor.
     */
    public static String toString(boolean enabled, List<VendorCard> cards) {
        StringBuilder result = new StringBuilder();
        if (!enabled) return result.append(A.RED + " VENDOR IS NOT ENABLED!" + A.RESET).toString();
        result.append(A.CYAN + " THE VENDOR IS HERE TO HELP! " + A.RESET).append("\n").append("\n");
        for(int i=0; i<cards.size(); i++) {
            result.append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
            result.append("  The card number: ").append(i + 1).append("\n");
            result.append(cards.get(i).getCard()).append("\n");
            if (cards.get(i).isSlot1()) result.append("  Can be placed in slot number 1").append("\n");
            if (cards.get(i).isSlot2()) result.append("  Can be placed in slot number 2").append("\n");
            if (cards.get(i).isSlot3()) result.append("  Can be placed in slot number 3").append("\n");
        }
        return result.toString();
    }

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the DevelopmentCardsVendor enabled or disabled.
     * If the DevelopmentCardsVendor is being set to enabled, it will notify its observers.
     * If the DevelopmentCardsVendor is being set to disabled, it will disband the internal List of VendorCards and notify its observers.
     * This method should be used only after the usage of setCards().
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
     * @param cards The List of VendorCards.
     * @see #setEnabled(boolean)
     */
    public void setCards(List<VendorCard> cards) {
        this.vendorCards = cards;
    }

    @Override
    public String toString() {
        return DevelopmentCardsVendor.toString(this.enabled, this.vendorCards);
    }

    /**
     * Creates a message using generateMessage() and notifies observers.
     * @see #generateMessage()
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     * Returns a MSG_UPD_DevCardsVendor representing the current state of the DevelopmentCardsVendor.
     * @return A MSG_UPD_DevCardsVendor representing the current state of the DevelopmentCardsVendor.
     */
    public MSG_UPD_DevCardsVendor generateMessage() {
        return new MSG_UPD_DevCardsVendor(
                this.enabled,
                this.vendorCards
        );
    }
}