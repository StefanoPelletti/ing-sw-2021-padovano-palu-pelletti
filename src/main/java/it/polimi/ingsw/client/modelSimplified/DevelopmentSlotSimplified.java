package it.polimi.ingsw.client.modelSimplified;

import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_DevSlot;
import it.polimi.ingsw.server.model.DevelopmentCard;
import it.polimi.ingsw.server.utils.A;

public class DevelopmentSlotSimplified {
    private DevelopmentCard[][] cards;

    public DevelopmentSlotSimplified() {
        cards = null;
    }

    /**
     * Updates this Development Slot to reflect the same status inside the server model.
     *
     * @param message the UpdateMessage that will update this Object internal status.
     */
    public void update(MSG_UPD_DevSlot message) {
        DevelopmentCard[][] newCards = message.getCards();
        cards = new DevelopmentCard[3][3];

        for (int i = 0; i < 3; i++) {
            System.arraycopy(newCards[i], 0, this.cards[i], 0, 3);
        }
    }


    public DevelopmentCard[][] getCards() {
        return this.cards;
    }

    /**
     * Returns a DevelopmentCard[] array containing the top cards of this development slot.
     * Some card may be null, reflecting the internal status.
     *
     * @return a 3-cell DevelopmentCard[] array
     */

    public DevelopmentCard[] getTopCards() {
        DevelopmentCard[] result = new DevelopmentCard[3];

        for (int deck = 0; deck < 3; deck++) {
            DevelopmentCard t = null;
            for (int height = 0; height < 3; height++) {
                if (this.cards[deck][height] != null)
                    t = this.cards[deck][height];
                else
                    break;
            }
            result[deck] = t;
        }
        return result;
    }

    /**
     * Returns a representation of the current state of a given DevelopmentSlot.
     *
     * @return A String that represents the DevSlot.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        DevelopmentCard[] onTop = new DevelopmentCard[3];
        for (int i = 0; i < 3; i++) {
            for (int j = 2; j >= 0; j--) {
                if (cards[i][j] != null) {
                    onTop[i] = cards[i][j];
                    break;
                }
            }
        }

        result.append("                  DEVELOPMENT SLOT:    \n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append(A.CYAN + "Slot 1: \n" + A.RESET);
        if (onTop[0] != null) {
            result.append(onTop[0]);
        } else {
            result.append(" No card in slot number 1. \n");
        }
        for (int i = 0; i < 3; i++) {
            if (cards[0][i] != onTop[0]) {
                assert cards[0][i] != null; //getVP was signaling possible nullPointerException, please check //FIXME
                result.append("VP of underneath cards: ").append(cards[0][i].getVp()).append("\n");
            } else {
                break;
            }
        }

        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append(A.CYAN + "Slot 2: \n" + A.RESET);
        if (onTop[1] != null) {
            result.append(onTop[1]);
        } else {
            result.append(" No card in slot number 2. \n");
        }
        for (int i = 0; i < 3; i++) {
            if (cards[1][i] != onTop[1]) {
                result.append("VP of underneath cards: ").append(cards[1][i].getVp()).append("\n");
            } else {
                break;
            }
        }

        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append(A.CYAN + "Slot 3: \n" + A.RESET);
        if (onTop[2] != null) {
            result.append(onTop[2]);
        } else {
            result.append(" No card in slot number 3. \n");
        }
        for (int i = 0; i < 3; i++) {
            if (cards[2][i] != onTop[2]) {
                result.append("VP of underneath cards: ").append(cards[2][i].getVp()).append("\n");
            } else {
                break;
            }
        }
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");

        return result.toString();
    }
}