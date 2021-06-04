package it.polimi.ingsw.client.modelSimplified;

import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_DevDeck;
import it.polimi.ingsw.server.model.DevelopmentCard;
import it.polimi.ingsw.server.model.DevelopmentCardsDeck;
import it.polimi.ingsw.server.utils.A;

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

    /**
     * Returns a representation of the current state of a given DevelopmentCardsDeck.
     * It only shows the Visible Cards.
     * @return A String the represents the current state of the DevelopmentCardsDeck.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(" DEVELOPMENT DECK, ALL THE VISIBLE CARDS: ");

        for (int i = 0; i < 3; i++) {
            result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET);
            result.append("\n").append(A.CYAN + "     I     I     I     I     I     I     I     " + A.RESET);
            result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
            result.append("\n").append(" Row of Cards level ");
            if(i==0) result.append(3);
            if(i==1) result.append(2);
            if(i==2) result.append(1);
            for (int j = 0; j < 4; j++) {
                result.append("\n").append("  Column of ");
                if(j==0) result.append(A.GREEN+"GREEN"+A.RESET+" cards");
                if(j==1) result.append(A.BLUE+"BLUE"+A.RESET+" cards");
                if(j==2) result.append(A.YELLOW+"YELLOW"+A.RESET+" cards");
                if(j==3) result.append(A.PURPLE +"PURPLE"+A.RESET+" cards");

                if (cards[i][j] == null)
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