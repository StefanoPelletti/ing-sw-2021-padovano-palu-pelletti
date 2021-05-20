package it.polimi.ingsw.Client.ModelSimplified;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_DevDeck;
import it.polimi.ingsw.Server.Model.DevelopmentCard;
import it.polimi.ingsw.Server.Utils.A;

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
        StringBuilder result = new StringBuilder(" DEVELOPMENT DECK, ALL THE VISIBLE CARDS: ");

        for (int i = 0; i < 3; i++) {
            result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET);
            result.append("\n").append(A.CYAN + "     I     I     I     I     I     I     I     " + A.RESET);
            result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
            result.append("\n").append(" Row ").append(i);
            for (int j = 0; j < 4; j++) {
                result.append("\n").append("  Column ").append(j);
                if (this.cards[i][j] == null)
                    result.append("\n").append(" X=====X Empty! X=====X");
                else
                    result.append("\n").append(cards[i][j].toString());
            }
        }
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET);
        result.append("\n").append(A.CYAN + "     I     I     I     I     I     I     I     " + A.RESET);
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        return result.toString();
    }
}