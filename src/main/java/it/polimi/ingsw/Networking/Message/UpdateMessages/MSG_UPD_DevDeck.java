package it.polimi.ingsw.Networking.Message.UpdateMessages;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Model.DevelopmentCard;

import java.io.Serializable;

public class MSG_UPD_DevDeck extends Message implements Serializable {

    private final DevelopmentCard[][] cards;

    public MSG_UPD_DevDeck(DevelopmentCard[][] cards) {
        super(MessageType.MSG_UPD_DevDeck);

        this.cards = new DevelopmentCard[3][4];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(cards[i], 0, this.cards[i], 0, 4);
        }
    }

    public DevelopmentCard[][] getCards() {
        return this.cards;
    }

    public MessageType getMessageType() {
        return super.getMessageType();
    }
}