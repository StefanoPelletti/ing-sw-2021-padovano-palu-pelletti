package it.polimi.ingsw;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.Server.Model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class LeaderCardsDeckTest {

    LeaderCardsDeck deck;

    @BeforeEach
    public void reset()
    {
        deck = new LeaderCardsDeck();
    }

    @Test
    public void deckCorrectnessTest()
    {
        ArrayList<LeaderCard> l = deck.pickFourCards();

        assertSame ( l.size(), 4 );

        //check if cards are different
        Set<LeaderCard> s = new HashSet<>(l);
        assertSame (s.size(), l.size());
    }

    @Test
    public void cycleLength()
    {
        assertSame (deck.pickFourCards().size(),4);
        assertSame (deck.pickFourCards().size(),4);
        assertSame (deck.pickFourCards().size(),4);
        assertSame (deck.pickFourCards().size(),4);
        assertSame (deck.pickFourCards().size(),4);
        assertSame (deck.pickFourCards().size(),4);
        assertSame (deck.pickFourCards().size(),4);
        assertSame (deck.pickFourCards().size(),4);
    }

    @Test
    public void cycleDifferent()
    {
        Set<LeaderCard> s;

        ArrayList<LeaderCard> l1 = deck.pickFourCards();
        ArrayList<LeaderCard> l2 = deck.pickFourCards();
        ArrayList<LeaderCard> l3 = deck.pickFourCards();
        ArrayList<LeaderCard> l4 = deck.pickFourCards();

        ArrayList<LeaderCard> lloop = deck.pickFourCards();

        s = new HashSet<>(l1);
        s.addAll(l2);
        assertSame(s.size() , l1.size() + l2.size());

        s = new HashSet<>(l1);
        s.addAll(l3);
        assertSame(s.size() , l1.size() + l3.size());

        s = new HashSet<>(l1);
        s.addAll(l4);
        assertSame(s.size() , l1.size() + l4.size());

        s = new HashSet<>(l2);
        s.addAll(l3);
        assertSame(s.size() , l2.size() + l3.size());

        s = new HashSet<>(l2);
        s.addAll(l4);
        assertSame(s.size() , l2.size() + l4.size());

        s = new HashSet<>(l3);
        s.addAll(l4);
        assertSame(s.size() , l3.size() + l4.size());

        s = new HashSet<>(l1);
        s.addAll(lloop);
        assertSame(s.size() , 4);
    }
}
