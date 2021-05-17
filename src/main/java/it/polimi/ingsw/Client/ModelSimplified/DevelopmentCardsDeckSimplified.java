package it.polimi.ingsw.Client.ModelSimplified;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_DevDeck;
import it.polimi.ingsw.Server.Model.DevelopmentCard;

public class DevelopmentCardsDeckSimplified {
    DevelopmentCard[][] cards;

    public DevelopmentCardsDeckSimplified() {
        cards = new DevelopmentCard[3][4];
    }

    public void update(MSG_UPD_DevDeck message) {
        DevelopmentCard[][] cards = message.getCards();
        for (int i = 0; i < 3; i++) {
            System.arraycopy(cards[i], 0, this.cards[i], 0, 4);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(" Development Deck: ");
        for (int i = 0; i < 3; i++) {
            result.append(" Row ").append(i);
            for (int j = 0; j < 4; j++) {
                result.append(" Column ").append(j);
                if (this.cards[i][j] == null)
                    result.append(" XX Empty! XX");
                else
                    result.append(cards[i][j].toString());
            }
        }
        return result.toString();
    }
}