package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.model.LeaderCardsDeck;
import it.polimi.ingsw.server.model.enumerators.Color;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.model.requirements.ReqValue;
import it.polimi.ingsw.server.model.requirements.CardRequirements;
import it.polimi.ingsw.server.model.requirements.ResourceRequirements;
import it.polimi.ingsw.server.model.specialAbilities.DiscountResource;
import it.polimi.ingsw.server.model.specialAbilities.ExtraDepot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
    public void equalsTestCardRequirements() {
        LeaderCard l1=new LeaderCard(2,
                new CardRequirements(Map.of(Color.YELLOW, new ReqValue(1, -1), Color.GREEN, new ReqValue(1, -1))),
                new DiscountResource(Resource.SERVANT));
        LeaderCard l2=new LeaderCard(2,
                new CardRequirements(Map.of(Color.YELLOW, new ReqValue(1, -1), Color.GREEN, new ReqValue(1, -1))),
                new DiscountResource(Resource.SERVANT));
        LeaderCard l3=new LeaderCard(2,
                new CardRequirements(Map.of(Color.YELLOW, new ReqValue(1, -1), Color.GREEN, new ReqValue(1, -1))),
                new DiscountResource(Resource.COIN));

        assertEquals(l1, l2);
        assertNotEquals(l1, l3);

    }

    @Test
    public void equalsTestResourceRequirements() {
        LeaderCard l1=new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.COIN, 5)),
                new ExtraDepot(Resource.STONE));
        LeaderCard l2=new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.COIN, 5)),
                new ExtraDepot(Resource.STONE));
        LeaderCard l3=new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.COIN, 5)),
                new ExtraDepot(Resource.COIN));

        assertEquals(l1, l2);
        assertNotEquals(l1, l3);
    }
    @Test
    public void hashEqualsTestCardRequirements() {
        LeaderCard l1=new LeaderCard(2,
                new CardRequirements(Map.of(Color.YELLOW, new ReqValue(1, -1), Color.GREEN, new ReqValue(1, -1))),
                new DiscountResource(Resource.SERVANT));
        LeaderCard l2=new LeaderCard(2,
                new CardRequirements(Map.of(Color.YELLOW, new ReqValue(1, -1), Color.GREEN, new ReqValue(1, -1))),
                new DiscountResource(Resource.SERVANT));
        LeaderCard l3=new LeaderCard(2,
                new CardRequirements(Map.of(Color.YELLOW, new ReqValue(1, -1), Color.GREEN, new ReqValue(1, -1))),
                new DiscountResource(Resource.COIN));

        assertEquals(l1.hashCode(), l2.hashCode());
        assertNotEquals(l1.hashCode(), l3.hashCode());

    }

    @Test
    public void hashEqualsTestResourceRequirements() {
        LeaderCard l1=new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.COIN, 5)),
                new ExtraDepot(Resource.STONE));
        LeaderCard l2=new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.COIN, 5)),
                new ExtraDepot(Resource.STONE));
        LeaderCard l3=new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.COIN, 5)),
                new ExtraDepot(Resource.COIN));

        assertEquals(l1.hashCode(), l2.hashCode());
        assertNotEquals(l1.hashCode(), l3.hashCode());
    }

    @Test
    public void toStringFormat() {
        System.out.println(deck.getCards().get(0));
        assertTrue(true);
    }
}