package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_Market;
import it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate.MSG_UPD_DevSlot;
import it.polimi.ingsw.Server.Utils.ModelObservable;

import java.util.ArrayList;
import java.util.Arrays;

public class DevelopmentSlot extends ModelObservable {
    private final DevelopmentCard[][] cards;
    private final DevelopmentCard[] onTop;
    private int numOfCards;

    public DevelopmentSlot() {
        cards = new DevelopmentCard[3][3];
        onTop = new DevelopmentCard[3];
        numOfCards = 0;

        for( int n = 0; n < 3; n++ )
        {
            onTop[n] = null;
            for ( int h = 0; h < 3; h++ )
            {
                cards[n][h] = null;
            }
        }
    }

    public boolean addCard(DevelopmentCard newCard, int selectedDeck) {
        int newCardLevel = newCard.getLevel();
        int cardLevel;

        if ( onTop[selectedDeck] == null )
            cardLevel = 0;
        else
            cardLevel = onTop[selectedDeck].getLevel();

        if( cardLevel == 3 ) //then this stack is full
        {
            return false;
        }
        else
        {
            if ( newCardLevel != cardLevel +1 ) //then level is not ok!
            {
                return false;
            }
            else // ( newCardLevel == cardLevel +1 ) aka: the level is ok and you can add!
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
        if(newCard == null) {
            return false;
        }
        int newCardLevel = newCard.getLevel();
        int cardLevel;


        if ( onTop[selectedDeck] == null )
            cardLevel = 0;
        else
            cardLevel = onTop[selectedDeck].getLevel();

        if( cardLevel == 3 ) //then this stack is full
        {
            //System.out.println("Nope, this stack is full!");
            return false;
        }
        else
        {
            if ( newCardLevel != cardLevel +1 ) //then level is not ok!
            {
                //System.out.println("Nope, you cannot add a card on this deck, levels are different!");
                return false;
            }
            else // ( newCardLevel == cardLevel +1 ) aka: the level is ok and you can add!
            {
                //System.out.println("Yes, adding in this position is possible!");
                return true;
            }
        }
    }

    public DevelopmentCard[][] getAllCards() {
        DevelopmentCard[][] tempDeck = new DevelopmentCard[3][3];

        for( int n = 0; n < 3; n++ )
        {
            for ( int h = 0; h < 3; h++ )
            {
                tempDeck[n][h] = cards[n][h];
            }
        }
        return tempDeck;
    }

    public ArrayList<DevelopmentCard> getCards(){
        ArrayList<DevelopmentCard> result= new ArrayList<>();
        for( int n = 0; n < 3; n++ )
        {
            for ( int h = 0; h < 3; h++ )
            {
                if(cards[n][h]!=null) result.add(cards[n][h]);
            }
        }
        return result;
    }

    public ArrayList<DevelopmentCard> getTopCards() {
        ArrayList<DevelopmentCard> List = new ArrayList<>();
        for ( int i = 0; i < 3; i++ )
        {
            if (onTop[i] != null)
            {
                List.add( onTop[i] );
            }
        }
        return List;
    }

    public DevelopmentCard[] getOnTop(){
        return onTop.clone();
    }

    public int getNumOfCards() {
        return numOfCards;
    }

    @Override
    public boolean equals( Object obj )
    {
        if(obj == this) return true;
        if(!(obj instanceof DevelopmentSlot)) return false;
        DevelopmentSlot o = (DevelopmentSlot) obj;
        return (Arrays.deepEquals(this.cards, (o).cards));
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("------ DevelopmentSlot ------ \n");

        result.append("Slot 1: \n");
        if(onTop[0] != null) {
            result.append(onTop[0].toString());
        } else {
            result.append("Nessuna carta nello slot 1. \n");
        }
        for(int i = 0; i < 3; i++) {
            if(cards[0][i] != onTop[0]) {
                result.append("PV carta sottostante: " + cards[0][i].getVP() + "\n");
            } else {
                break;
            }
        }

        result.append("\n");
        result.append("Slot 2: \n");
        if(onTop[1] != null) {
            result.append(onTop[1].toString());
        } else {
            result.append("Nessuna carta nello slot 2. \n");
        }
        for(int i = 0; i < 3; i++) {
            if(cards[1][i] != onTop[1]) {
                result.append("PV carta sottostante: " + cards[1][i].getVP() + "\n");
            } else {
                break;
            }
        }

        result.append("\n");
        result.append("Slot 3: \n");
        if(onTop[2] != null) {
            result.append(onTop[2].toString());
        } else {
            result.append("Nessuna carta nello slot 3. \n");
        }
        for(int i = 0; i < 3; i++) {
            if(cards[2][i] != onTop[2]) {
                result.append("PV carta sottostante: " + cards[2][i].getVP() + "\n");
            } else {
                break;
            }
        }

        return result.toString();
    }

    private void notifyObservers(){
        this.notifyObservers(generateMessage());
    }

    public MSG_UPD_DevSlot generateMessage() {
        return new MSG_UPD_DevSlot(cards);
    }
}
