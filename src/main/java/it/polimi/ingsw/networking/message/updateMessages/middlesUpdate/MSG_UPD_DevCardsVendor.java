package it.polimi.ingsw.networking.message.updateMessages.middlesUpdate;

import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.model.middles.VendorCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MSG_UPD_DevCardsVendor extends Message implements Serializable {

    private final boolean enabled;
    private final List<VendorCard> vendorCards;

    public MSG_UPD_DevCardsVendor(boolean enabled, List<VendorCard> vendorCards) {
        super(MessageType.MSG_UPD_DevCardsVendor);

        if (vendorCards == null) {
            this.vendorCards = null;
        } else {
            this.vendorCards = new ArrayList<VendorCard>(vendorCards);
        }
        this.enabled = enabled;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public List<VendorCard> getCards() {
        return this.vendorCards;
    }
}