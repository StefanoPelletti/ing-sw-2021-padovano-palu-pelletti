package it.polimi.ingsw.server.model;

import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_DevSlot;
import it.polimi.ingsw.server.utils.Displayer;
import it.polimi.ingsw.server.utils.ModelObservable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DevelopmentSlot extends ModelObservable {
    private final DevelopmentCard[][] cards;
    private final DevelopmentCard[] onTop;
    private int numOfCards;

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

    public boolean addCard(DevelopmentCard newCard, int selectedDeck) {
        int newCardLevel = newCard.getLevel();
        int cardLevel;

        if (onTop[selectedDeck] == null)
            cardLevel = 0;
        else
            cardLevel = onTop[selectedDeck].getLevel();

        if (cardLevel == 3) //then this stack is full
        {
            return false;
        } else {
            if (newCardLevel != cardLevel + 1) //then level is not ok!
            {
                return false;
            } else // ( newCardLevel == cardLevel +1 ) aka: the level is ok and you can add!
            {
                cards[selectedDeck][cardLevel] = newCard;
                onTop[selectedDeck] = newCard;
                numOfCards++;
                notifyObservers();
                return true;
            }
        }
    }

    public boolean validateNewCard(DevelopmentCard newCard, int selectedDeck) {
        if (newCard == null) {
            return false;
        }
        int newCardLevel = newCard.getLevel();
        int cardLevel;


        if (onTop[selectedDeck] == null)
            cardLevel = 0;
        else
            cardLevel = onTop[selectedDeck].getLevel();

        if (cardLevel == 3) //then this stack is full
        {
            return false;
        } else {
            return newCardLevel == cardLevel + 1;
        }
    }

    public DevelopmentCard[][] getAllCards() {
        DevelopmentCard[][] tempDeck = new DevelopmentCard[3][3];

        for (int n = 0; n < 3; n++) {
            System.arraycopy(cards[n], 0, tempDeck[n], 0, 3);
        }
        return tempDeck;
    }

    public List<DevelopmentCard> getCards() {
        List<DevelopmentCard> result = new ArrayList<>();
        for (int n = 0; n < 3; n++) {
            for (int h = 0; h < 3; h++) {
                if (cards[n][h] != null) result.add(cards[n][h]);
            }
        }
        return result;
    }

    public List<DevelopmentCard> getTopCards() {
        List<DevelopmentCard> list = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (onTop[i] != null) {
                list.add(onTop[i]);
            }
        }
        return list;
    }

    public DevelopmentCard[] getOnTop() {
        return onTop.clone();
    }

    public int getNumOfCards() {
        return numOfCards;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof DevelopmentSlot)) return false;
        DevelopmentSlot o = (DevelopmentSlot) obj;
        return (Arrays.deepEquals(this.cards, (o).cards));
    }

    @Override
    public String toString() {
        return Displayer.developmentSlotToString(this.cards);
    }

    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    public MSG_UPD_DevSlot generateMessage() {
        return new MSG_UPD_DevSlot(cards);
    }
}