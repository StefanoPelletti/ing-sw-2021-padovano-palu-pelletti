package it.polimi.ingsw.networking.message.updateMessages.middlesUpdate;

import it.polimi.ingsw.client.cli.UpdateHandler;
import it.polimi.ingsw.client.gui.UpdateHandlerGUI;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.networking.message.updateMessages.UpdateMessage;
import it.polimi.ingsw.server.model.middles.VendorCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MSG_UPD_DevCardsVendor extends UpdateMessage implements Serializable {

    private final boolean enabled;
    private final List<VendorCard> vendorCards;

    /**
     * MSG_UPD_DevCardsVendor is sent by the ClientHandler to the Client.
     * It is generated by a model.middles.DevelopmentsCardVendor generateMessage().
     * It contains the internal status of the DevelopmentsCardVendor middle object.
     * Its usage is is best described in the Documentation file, Actions.
     *
     * @param enabled     If true, the currentPlayer must resolve this object as a priority.
     * @param vendorCards A list of the possible purchasable cards.
     * @see it.polimi.ingsw.server.model.middles.VendorCard;
     */
    public MSG_UPD_DevCardsVendor(boolean enabled, List<VendorCard> vendorCards) {
        super(MessageType.MSG_UPD_DevCardsVendor);

        if (vendorCards == null) {
            this.vendorCards = null;
        } else {
            this.vendorCards = new ArrayList<>(vendorCards);
        }
        this.enabled = enabled;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public List<VendorCard> getCards() {
        return this.vendorCards;
    }

    @Override
    public void executeCLI(UpdateHandler updateHandler) {
        updateHandler.updateDevCardVendor(this);
    }

    @Override
    public void executeGUI(UpdateHandlerGUI updateHandler) {
        updateHandler.updateDevCardVendor(this);
    }
}