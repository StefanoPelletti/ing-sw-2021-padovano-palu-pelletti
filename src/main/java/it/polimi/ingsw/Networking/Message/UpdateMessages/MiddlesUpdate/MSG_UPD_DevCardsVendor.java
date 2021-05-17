package it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Model.DevelopmentCard;

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

    public MessageType getMessageType() {
        return super.getMessageType();
    }
}