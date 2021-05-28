package it.polimi.ingsw.server.model;

import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_DevSlot;
import it.polimi.ingsw.server.utils.A;
import it.polimi.ingsw.server.utils.ModelObservable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DevelopmentSlot extends ModelObservable {
    private final DevelopmentCard[][] cards;
    private final DevelopmentCard[] onTop;
    private int numOfCards;

    /**
     * Constructor of the DevelopmentSlot
     */
    public DevelopmentSlot() {
        cards = new DevelopmentCard[3][3];
        onTop = new DevelopmentCard[3];
        numOfCards = 0;

        for (int n = 0; n < 3; n++) {
            onTop[n] = null;
            for (int h = 0; h < 3; h++) {
                cards[n][h] = null;
            }
        }
    }

    /**
     * Receives
     * @param cards the grid of all the cards in the DevSlot
     * @return a string that represents the DevSlot.
     */
    public static String toString(DevelopmentCard[][] cards) {
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

    /**
     * Given:
     * @param newCard the card that the user wants to buy from the DevDeck
     * @param selectedSlot the slot where the user wants to put the card
     * @return true if the card is added correctly, false if:
     *   - the stack is full
     *   - the level is not right
     */
    public boolean addCard(DevelopmentCard newCard, int selectedSlot) {
        int newCardLevel = newCard.getLevel();
        int cardLevel;

        if (onTop[selectedSlot] == null)
            cardLevel = 0;
        else
            cardLevel = onTop[selectedSlot].getLevel();

        if (cardLevel == 3) //then this stack is full
        {
            return false;
        } else {
            if (newCardLevel != cardLevel + 1) //then level is not ok!
            {
                return false;
            } else // ( newCardLevel == cardLevel +1 ) aka: the level is ok and you can add!
            {
                cards[selectedSlot][cardLevel] = newCard;
                onTop[selectedSlot] = newCard;
                numOfCards++;
                notifyObservers();
                return true;
            }
        }
    }

    //decks are 0 1 2

    /**
     * Given:
     * @param newCard a card that the user wants
     * @param selectedSlot a slot where he wants to put the new card
     * @return checks if the level of the new card is equal to that of the last card in the selected slot (if any) + 1.
     * If so returns true, otherwise returns false.
     */
    public boolean validateNewCard(DevelopmentCard newCard, int selectedSlot) {
        if (newCard == null) {
            return false;
        }
        int newCardLevel = newCard.getLevel();
        int cardLevel;

        if (onTop[selectedSlot] == null)
            cardLevel = 0;
        else
            cardLevel = onTop[selectedSlot].getLevel();

        if (cardLevel == 3) //then this stack is full
        {
            return false;
        } else {
            return newCardLevel == cardLevel + 1;
        }
    }

    /**
     * @return a new grid of all the cards in the DevSlot.
     */
    public DevelopmentCard[][] getAllCards() {
        DevelopmentCard[][] tempDeck = new DevelopmentCard[3][3];

        for (int n = 0; n < 3; n++) {
            System.arraycopy(cards[n], 0, tempDeck[n], 0, 3);
        }
        return tempDeck;
    }

    /**
     * @return a new list of the cards in the DevSlot.
     */
    public List<DevelopmentCard> getCards() {
        List<DevelopmentCard> result = new ArrayList<>();
        for (int n = 0; n < 3; n++) {
            for (int h = 0; h < 3; h++) {
                if (cards[n][h] != null) result.add(cards[n][h]);
            }
        }
        return result;
    }

    /**
     * @return a list of the top cards in the DevSlot (the cards that the user can actually use and see).
     */
    public List<DevelopmentCard> getTopCards() {
        List<DevelopmentCard> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (onTop[i] != null) {
                list.add(onTop[i]);
            }
        }
        return list;
    }

    /**
     * @return an array of the top cards in the DevSlot (the cards that the user can actually use and see).
     */
    public DevelopmentCard[] getOnTop() {
        return onTop.clone();
    }

    /**
     * @return the number of all the cards in the DevSlot.
     */
    public int getNumOfCards() {
        return numOfCards;
    }

    @Override
    /**
     * Calls the static toString above passing the grid of cards.
     */
    public String toString() {
        return DevelopmentSlot.toString(this.cards);
    }

    /**
     * notifies the observers by sending a message that contains the actual internal status of the DevSlot.
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     * @return the actual message passed by the notifyObservers() method that contains the status of the DevSlot.
     */
    public MSG_UPD_DevSlot generateMessage() {
        return new MSG_UPD_DevSlot(cards);
    }
}