package it.polimi.ingsw.client.modelSimplified;

import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_DevDeck;
import it.polimi.ingsw.server.model.DevelopmentCard;
import it.polimi.ingsw.server.model.DevelopmentCardsDeck;

public class DevelopmentCardsDeckSimplified {
    private final DevelopmentCard[][] cards;

    public DevelopmentCardsDeckSimplified() {
        cards = new DevelopmentCard[3][4];
    }

    public void update(MSG_UPD_DevDeck message) {
        DevelopmentCard[][] newCards = message.getCards();
        for (int i = 0; i < 3; i++) {
            System.arraycopy(newCards[i], 0, this.cards[i], 0, 4);
        }
    }

    @Override
    public String toString() {
        return DevelopmentCardsDeck.toString(this.cards);
    }
}