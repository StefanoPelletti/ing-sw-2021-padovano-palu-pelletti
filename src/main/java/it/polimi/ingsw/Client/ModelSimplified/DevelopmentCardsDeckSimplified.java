package it.polimi.ingsw.Client.ModelSimplified;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_DevDeck;
import it.polimi.ingsw.Server.Model.DevelopmentCard;
import it.polimi.ingsw.Server.Utils.Displayer;

public class DevelopmentCardsDeckSimplified {
    private DevelopmentCard[][] cards;

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
        return Displayer.developmentCardsDeckToString(this.cards);
    }
}