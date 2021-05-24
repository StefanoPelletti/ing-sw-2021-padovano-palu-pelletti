package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.model.LeaderCardsDeck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LeaderCardsDeckTest {

    LeaderCardsDeck deck;

    @BeforeEach
    public void reset() {
        deck = new LeaderCardsDeck();
    }

    @Test
    public void deckCorrectnessTest() {
        List<LeaderCard> l = deck.pickFourCards();

        assertSame(l.size(), 4);

        //check if cards are different
        Set<LeaderCard> s = new HashSet<>(l);
        assertSame(s.size(), l.size());
    }

    @Test
    public void cycleLength() {
        assertSame(deck.pickFourCards().size(), 4);
        assertSame(deck.pickFourCards().size(), 4);
        assertSame(deck.pickFourCards().size(), 4);
        assertSame(deck.pickFourCards().size(), 4);
        assertSame(deck.pickFourCards().size(), 4);
        assertSame(deck.pickFourCards().size(), 4);
        assertSame(deck.pickFourCards().size(), 4);
        assertSame(deck.pickFourCards().size(), 4);
    }

    @Test
    public void cycleDifferent() {
        Set<LeaderCard> s;

        List<LeaderCard> l1 = deck.pickFourCards();
        List<LeaderCard> l2 = deck.pickFourCards();
        List<LeaderCard> l3 = deck.pickFourCards();
        List<LeaderCard> l4 = deck.pickFourCards();

        List<LeaderCard> lloop = deck.pickFourCards();

        s = new HashSet<>(l1);
        s.addAll(l2);
        assertSame(s.size(), l1.size() + l2.size());

        s = new HashSet<>(l1);
        s.addAll(l3);
        assertSame(s.size(), l1.size() + l3.size());

        s = new HashSet<>(l1);
        s.addAll(l4);
        assertSame(s.size(), l1.size() + l4.size());

        s = new HashSet<>(l2);
        s.addAll(l3);
        assertSame(s.size(), l2.size() + l3.size());

        s = new HashSet<>(l2);
        s.addAll(l4);
        assertSame(s.size(), l2.size() + l4.size());

        s = new HashSet<>(l3);
        s.addAll(l4);
        assertSame(s.size(), l3.size() + l4.size());

        s = new HashSet<>(l1);
        s.addAll(lloop);
        assertSame(s.size(), 4);
    }

    @Test
    public void equalsTest() {

    }

    @Test
    public void toStringFormat() {
        System.out.println(deck.getCards().get(0));
        assertTrue(true);
    }
}