package it.polimi.ingsw.Client.ModelSimplified;

import it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate.MSG_UPD_DevSlot;
import it.polimi.ingsw.Server.Model.DevelopmentCard;
import it.polimi.ingsw.Server.Model.DevelopmentSlot;

public class DevelopmentSlotSimplified {
    private DevelopmentCard[][] cards;

    public DevelopmentSlotSimplified()
    {
        cards = null;
    }

    public void update(MSG_UPD_DevSlot message){
        DevelopmentCard[][] newCards = message.getCards();
        cards = new DevelopmentCard[3][3];

        for(int i=0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                this.cards[i][j] = newCards[i][j];
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        DevelopmentCard[] onTop = new DevelopmentCard[3];
        for(int i = 0; i < 3; i++) {
            for(int j = 2; j >= 0; j--) {
                if (cards[i][j] != null) {
                    onTop[i] = cards[i][j];
                    break;
                }
            }
        }

        result.append("\u001B[36m" + "_____+_____+_____+_____+_____+_____+_____+_____" + "\u001B[0m").append("\n");
        result.append("                  DEVELOPMENT SLOT:    \n");

        result.append("\u001B[36m" + "Slot 1: \n" + "\u001B[0m");
        if(onTop[0] != null) {
            result.append(onTop[0].toString());
        } else {
            result.append(" No card in slot number 1. \n");
        }
        for(int i = 0; i < 3; i++) {
            if(cards[0][i] != onTop[0]) {
                result.append("VP of underneath cards: " + cards[0][i].getVP() + "\n");
            } else {
                break;
            }
        }

        result.append("\n");
        result.append("\u001B[36m" + "Slot 2: \n" + "\u001B[0m");
        if(onTop[1] != null) {
            result.append(onTop[1].toString());
        } else {
            result.append(" No card in slot number 2. \n");
        }
        for(int i = 0; i < 3; i++) {
            if(cards[1][i] != onTop[1]) {
                result.append("VP of underneath cards: " + cards[1][i].getVP() + "\n");
            } else {
                break;
            }
        }

        result.append("\n");
        result.append("\u001B[36m" + "Slot 3: \n" + "\u001B[0m");
        if(onTop[2] != null) {
            result.append(onTop[2].toString());
        } else {
            result.append(" No card in slot number 3. \n");
        }
        for(int i = 0; i < 3; i++) {
            if(cards[2][i] != onTop[2]) {
                result.append("VP of underneath cards: " + cards[2][i].getVP() + "\n");
            } else {
                break;
            }
        }
        result.append("\u001B[36m" + "_____+_____+_____+_____+_____+_____+_____+_____" + "\u001B[0m").append("\n");

        return result.toString();
    }
}
