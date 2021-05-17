package it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate;

import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Model.DevelopmentSlot;

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

    public MessageType getMessageType() {
        return super.getMessageType();
    }
}