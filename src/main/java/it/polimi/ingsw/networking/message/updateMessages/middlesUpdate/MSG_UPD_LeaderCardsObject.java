package it.polimi.ingsw.networking.message.updateMessages.middlesUpdate;

import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.model.LeaderCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MSG_UPD_LeaderCardsObject extends Message implements Serializable {

    private final boolean enabled;
    private final List<LeaderCard> cards;

    public MSG_UPD_LeaderCardsObject(boolean enabled, List<LeaderCard> cards) {
        super(MessageType.MSG_UPD_LeaderCardsObject);

        this.enabled = enabled;
        this.cards = new ArrayList<>(cards);
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public List<LeaderCard> getCards() {
        return this.cards;
    }
}