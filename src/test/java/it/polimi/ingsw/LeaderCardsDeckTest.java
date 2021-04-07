package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import it.polimi.ingsw.Model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class LeaderCardsDeckTest {

    @Test
    public void deckCorrectnessTest()
    {
        LeaderCardsDeck deck = new LeaderCardsDeck();
        ArrayList<LeaderCard> l = deck.pickFourCards();

        assert ( l.size() == 4 );

        //check if cards are different
        Set<LeaderCard> s = new HashSet<>(l);
        assert (s.size() == l.size());
    }

    @Test
    public void cycleLength()
    {
        LeaderCardsDeck deck = new LeaderCardsDeck();
        assert (deck.pickFourCards().size()==4);
        assert (deck.pickFourCards().size()==4);
        assert (deck.pickFourCards().size()==4);
        assert (deck.pickFourCards().size()==4);
        assert (deck.pickFourCards().size()==4);
    }

    @Test
    public void cycleDifferent()
    {
        LeaderCardsDeck deck = new LeaderCardsDeck();
        Set<LeaderCard> s;

        ArrayList<LeaderCard> l1 = deck.pickFourCards();
        ArrayList<LeaderCard> l2 = deck.pickFourCards();
        ArrayList<LeaderCard> l3 = deck.pickFourCards();
        ArrayList<LeaderCard> l4 = deck.pickFourCards();

        ArrayList<LeaderCard> lloop = deck.pickFourCards();

        s = new HashSet<>(l1);
        s.addAll(l2);
        assert(s.size() == l1.size() + l2.size());

        s = new HashSet<>(l1);
        s.addAll(l3);
        assert(s.size() == l1.size() + l3.size());

        s = new HashSet<>(l1);
        s.addAll(l4);
        assert(s.size() == l1.size() + l4.size());

        s = new HashSet<>(l2);
        s.addAll(l3);
        assert(s.size() == l2.size() + l3.size());

        s = new HashSet<>(l2);
        s.addAll(l4);
        assert(s.size() == l2.size() + l4.size());

        s = new HashSet<>(l3);
        s.addAll(l4);
        assert(s.size() == l3.size() + l4.size());

        s = new HashSet<>(l1);
        s.addAll(lloop);
        assert(s.size() == 4);
    }
}
