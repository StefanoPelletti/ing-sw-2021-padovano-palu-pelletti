package it.polimi.ingsw.Client.ModelSimplified;

import it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate.MSG_UPD_DevSlot;
import it.polimi.ingsw.Server.Model.DevelopmentCard;
import it.polimi.ingsw.Server.Utils.A;

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
                //FIXME check comment below
                assert cards[0][i] != null; //getVP was signaling possible nullPointerException, please check
                result.append("VP of underneath cards: ").append(cards[0][i].getVP()).append("\n");
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
                result.append("VP of underneath cards: ").append(cards[1][i].getVP()).append("\n");
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
                result.append("VP of underneath cards: ").append(cards[2][i].getVP()).append("\n");
            } else {
                break;
            }
        }
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");

        return result.toString();
    }
}