package it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Model.LeaderCard;

import java.io.Serializable;
import java.util.ArrayList;

public class MSG_UPD_LeaderCardsObject extends Message implements Serializable {

    private final boolean enabled;
    private final ArrayList<LeaderCard> cards;

    public MSG_UPD_LeaderCardsObject(boolean enabled, ArrayList<LeaderCard> cards) {
        super(MessageType.MSG_UPD_LeaderCardsObject);

        this.enabled = enabled;
        this.cards = new ArrayList<>(cards);
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public ArrayList<LeaderCard> getCards() {
        return this.cards;
    }

    public MessageType getMessageType() {
        return super.getMessageType();
    }
}