package it.polimi.ingsw.client.modelSimplified;

import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_DevSlot;
import it.polimi.ingsw.server.model.DevelopmentCard;
import it.polimi.ingsw.server.utils.Displayer;

public class DevelopmentSlotSimplified {
    private DevelopmentCard[][] cards;

    public DevelopmentSlotSimplified() {
        cards = null;
    }

    public void update(MSG_UPD_DevSlot message) {
        DevelopmentCard[][] newCards = message.getCards();
        cards = new DevelopmentCard[3][3];

        for (int i = 0; i < 3; i++) {
            System.arraycopy(newCards[i], 0, this.cards[i], 0, 3);
        }
    }

    @Override
    public String toString() {
        return Displayer.developmentSlotToString(this.cards);
    }
}