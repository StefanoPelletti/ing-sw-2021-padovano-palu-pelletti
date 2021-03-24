package it.polimi.ingsw.Model;

import java.util.ArrayList;
import java.util.Collections;

public class LeaderCardsDeck {
    private ArrayList<LeaderCard> deck;
    int t_pos;

    public LeaderCardsDeck()
    {
        t_pos = 0;
        deck = new ArrayList<LeaderCard>();
        deck.add(new LeaderCard());
    }

    public void shuffle() {
        Collections.shuffle(deck);
        t_pos = 0;
    }


// pickFourCards extracts the next 4 cards from a fixed stack.
// becomes cyclical if called more than 4 times without shuffling
    public ArrayList<LeaderCard> pickFourCards() {
        ArrayList<LeaderCard> cards = new ArrayList<LeaderCard>();
        for (int i=0; i<4; i++) {
            if (t_pos == 16) t_pos = 0;
            cards.add(deck.get(t_pos));
            t_pos++;
        }
        return cards;
    }
}
