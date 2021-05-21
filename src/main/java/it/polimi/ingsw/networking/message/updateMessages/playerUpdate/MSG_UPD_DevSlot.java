package it.polimi.ingsw.networking.message.updateMessages.playerUpdate;

import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.model.DevelopmentCard;

import java.io.Serializable;

public class MSG_UPD_DevSlot extends Message implements Serializable {

    private final DevelopmentCard[][] cards;

    public MSG_UPD_DevSlot(DevelopmentCard[][] cards) {
        super(MessageType.MSG_UPD_DevSlot);

        this.cards = new DevelopmentCard[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(cards[i], 0, this.cards[i], 0, 3);
        }
    }

    public DevelopmentCard[][] getCards() {
        return this.cards;
    }
}