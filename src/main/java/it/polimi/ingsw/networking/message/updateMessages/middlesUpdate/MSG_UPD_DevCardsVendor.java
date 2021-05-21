package it.polimi.ingsw.networking.message.updateMessages.middlesUpdate;

import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.model.DevelopmentCard;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MSG_UPD_DevCardsVendor extends Message implements Serializable {

    private final boolean enabled;
    private final Map<DevelopmentCard, boolean[]> cards;

    public MSG_UPD_DevCardsVendor(boolean enabled, Map<DevelopmentCard, boolean[]> map) {
        super(MessageType.MSG_UPD_DevCardsVendor);

        if (map == null) {
            cards = null;
        } else {
            cards = new HashMap<>(map);
        }
        this.enabled = enabled;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public Map<DevelopmentCard, boolean[]> getCards() {
        return this.cards;
    }
}