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
     * Returns a representation of the current state of a given DevelopmentSlot.
     * A DevelopmentSlot or a DevelopmentSlotSimplified may use this shared method by passing their internal values.
     * @param cards The grid of all the cards in the DevSlot.
     * @return A String that represents the DevSlot.
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
     * Tries to add a DevelopmentCard in this DevelopmentSlot.
     * @param newCard The card that is being placed.
     * @param selectedSlot The slot where the user wants to put the card.
     * @return True if the card is added correctly, False if:
     * <ul>
     * <li> the stack is full
     * <li> the level is not right
     * <li> parameters are not correct
     */
    public boolean addCard(DevelopmentCard newCard, int selectedSlot) {
        if(selectedSlot < 0 || selectedSlot > 2) return false;
        if(newCard == null) return false;

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

    /**
     * Checks if a DevelopmentCard is addable in this DevelopmentSlot, without adding it.
     * @param newCard A card that is being tested.
     * @param selectedSlot A slot where the card is being tried at.
     * @return True if the card is addable, False otherwise.
     */
    public boolean validateNewCard(DevelopmentCard newCard, int selectedSlot) {
        if(selectedSlot < 0 || selectedSlot > 2) return false;
        if (newCard == null) { return false; }

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
     * Returns a new DevelopmentCard matrix of all the DevelopmentCards present in the DevSlot.
     * @return A new DevelopmentCard matrix of all the DevelopmentCards present in the DevSlot.
     */
    public DevelopmentCard[][] getAllCards() {
        DevelopmentCard[][] tempDeck = new DevelopmentCard[3][3];

        for (int n = 0; n < 3; n++) {
            System.arraycopy(cards[n], 0, tempDeck[n], 0, 3);
        }
        return tempDeck;
    }

    /**
     * Returns a new List containing all the DevelopmentCards present in the DevSlot.
     * @return A new List containing all the DevelopmentCards present in the DevSlot.
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
     * Return a new List containing all the DevelopmentCards that are on top of the stacks.
     * The card on top are the ones usable in the Production Action.
     * @return A new List containing all the DevelopmentCards that are on top of the stacks.
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
     * Returns an array of the top cards in the DevSlot (the cards that the user can actually use and see).
     * @return An array of the top cards in the DevSlot (the cards that the user can actually use and see).
     */
    public DevelopmentCard[] getOnTop() {
        return onTop.clone();
    }

    /**
     * Returns the amount of the cards in the DevSlot.
     * @return The amount of the cards in the DevSlot.
     */
    public int getNumOfCards() {
        return numOfCards;
    }

    @Override
    /**
     * Calls the static toString() passing its internal values.
     * @see #toString(DevelopmentCard[][])
     */
    public String toString() {
        return DevelopmentSlot.toString(this.cards);
    }

    /**
     * Creates a message using generateMessage() and notifies observers.
     * @see #generateMessage()
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     * Returns a MSG_UPD_DevSlot representing the current state of the DevelopmentSlot.
     * @return A MSG_UPD_DevSlot representing the current state of the DevelopmentSlot.
     */
    public MSG_UPD_DevSlot generateMessage() {
        return new MSG_UPD_DevSlot(cards);
    }
}