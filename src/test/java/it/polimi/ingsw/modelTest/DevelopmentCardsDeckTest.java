package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.server.model.DevelopmentCard;
import it.polimi.ingsw.server.model.DevelopmentCardsDeck;
import it.polimi.ingsw.server.model.Power;
import it.polimi.ingsw.server.model.enumerators.Color;
import it.polimi.ingsw.server.model.enumerators.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class DevelopmentCardsDeckTest {

    DevelopmentCardsDeck d;

    @BeforeEach
    public void reset() {
        d = new DevelopmentCardsDeck();
    }

    @Test
    public void getStackNotNull() {
        DevelopmentCard[] x = d.getStack(-1, 0);
        assertNull(x);

        x = d.getStack(0, 5);
        assertNull(x);

        x = d.getStack(0, 0);
        assertNotNull(x);
    }

    @Test
    public void StackNotNull() {
        DevelopmentCard[] x = d.getStack(0, 0);

        for (int i = 0; i < 4; i++)
            assertNotNull(x[i]);

    }

    @Test
    public void testOfRemove() {
        DevelopmentCard x;

        x = d.removeCard(0, 0);
        assertNotNull(x);

        x = d.removeCard(0, 0);
        assertNotNull(x);

        x = d.removeCard(0, 0);
        assertNotNull(x);

        x = d.removeCard(0, 0);
        assertNotNull(x);

        x = d.removeCard(0, 0);
        assertNull(x);

    }

    @Test
    public void testOfAStack() {
        DevelopmentCard[] x;

        x = d.getStack(0, 0);
        d.removeCard(0, 0);
        DevelopmentCard[][] y = d.getVisible();

        assertEquals(x[2], y[0][0]);

        d.removeCard(0, 0);
        y = d.getVisible();

        assertEquals(x[1], y[0][0]);

        d.removeCard(0, 0);
        y = d.getVisible();

        assertEquals(x[0], y[0][0]);

        d.removeCard(0, 0);
        y = d.getVisible();

        assertNull(y[0][0]);
    }

    @Test
    public void shuffleTest() {
        DevelopmentCard[] x = d.getStack(0, 0);
        d.shuffle();
        DevelopmentCard[] y = d.getStack(0, 0);

        assertTrue(x[0].equals(y[0]) ||
                x[0].equals(y[1]) ||
                x[0].equals(y[2]) ||
                x[0].equals(y[3]));
        assertTrue(x[1].equals(y[0]) ||
                x[1].equals(y[1]) ||
                x[1].equals(y[2]) ||
                x[1].equals(y[3]));
        assertTrue(x[2].equals(y[0]) ||
                x[2].equals(y[1]) ||
                x[2].equals(y[2]) ||
                x[2].equals(y[3]));
        assertTrue(x[3].equals(y[0]) ||
                x[3].equals(y[1]) ||
                x[3].equals(y[2]) ||
                x[3].equals(y[3]));
    }

    @Test
    public void correctCards() {
        DevelopmentCard x;

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                x = d.removeCard(r, c);
                assertNotNull(x);
                assertTrue(x.internalCheck());
            }
        }
    }

    @Test
    public void removeColumnInvalidParameters() {
        assertFalse(d.removeCard(-1));
        assertFalse(d.removeCard(6));
    }

    @Test
    public void removingAllColumns() {
        assertTrue(d.removeCard(0));
        assertTrue(d.removeCard(0));
        DevelopmentCard[][] x = d.getVisible();
        assertNotNull(x[0][0]);
        assertNotNull(x[1][0]);
        assertNull(x[2][0]);

        assertTrue(d.removeCard(0));
        assertTrue(d.removeCard(0));
        x = d.getVisible();
        assertNotNull(x[0][0]);
        assertNull(x[1][0]);
        assertNull(x[2][0]);

        assertTrue(d.removeCard(0));
        assertTrue(d.removeCard(0));
        x = d.getVisible();
        assertNull(x[0][0]);
        assertNull(x[1][0]);
        assertNull(x[2][0]);

        assertTrue(d.removeCard(1));
        assertTrue(d.removeCard(1));
        assertTrue(d.removeCard(1));
        assertTrue(d.removeCard(1));
        assertTrue(d.removeCard(1));
        assertTrue(d.removeCard(1));

        assertTrue(d.removeCard(2));
        assertTrue(d.removeCard(2));
        assertTrue(d.removeCard(2));
        assertTrue(d.removeCard(2));
        assertTrue(d.removeCard(2));
        assertTrue(d.removeCard(2));

        assertTrue(d.removeCard(0));
        assertTrue(d.removeCard(1));
        assertTrue(d.removeCard(2));

        assertTrue(d.removeCard(3));
        assertTrue(d.removeCard(3));
        assertTrue(d.removeCard(3));
        assertTrue(d.removeCard(3));
        assertTrue(d.removeCard(3));
        assertTrue(d.removeCard(3));
        x = d.getVisible();

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                assertNull(x[r][c]);
            }
        }
    }

    @Test
    public void getStackTest() {
        assertNull(d.getStack(-1, 0));
        assertNull(d.getStack(0, -1));

        DevelopmentCard[] stack = d.getStack(0, 0);
        assertNotNull(stack);

        assertNotNull(stack[0]);
        assertNotNull(stack[1]);
        assertNotNull(stack[2]);
        assertNotNull(stack[3]);

        d.removeCard(0, 0);
        assertNull(d.getStack(0, 0)[3]);
        d.removeCard(0, 0);
        assertNull(d.getStack(0, 0)[2]);
        d.removeCard(0, 0);
        assertNull(d.getStack(0, 0)[1]);
        d.removeCard(0, 0);
        assertNotNull(d.getStack(0, 0));
        assertNull(d.getStack(0, 0)[0]);
    }

    @Test
    public void isOneColumnDestroyedTest() {
        assertFalse(d.isOneColumnDestroyed());
        d.removeCard(1);
        d.removeCard(1);
        d.removeCard(1);
        d.removeCard(1);
        d.removeCard(1);
        assertFalse(d.isOneColumnDestroyed());
        d.removeCard(1);
        assertTrue(d.isOneColumnDestroyed());
    }

    @Test
    public void toStringCardFormat() {
        DevelopmentCard c = d.removeCard(2, 2);
        System.out.println(c);
    }

    @Test
    public void equalsTest() {
        DevelopmentCard card1 = new DevelopmentCard(3, Color.GREEN, 12,
                Map.of(Resource.SHIELD, 4, Resource.COIN, 4),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.COIN, 3, Resource.SHIELD, 1))
        );

        DevelopmentCard card2 = new DevelopmentCard(3, Color.GREEN, 12,
                Map.of(Resource.SHIELD, 4, Resource.COIN, 4),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.COIN, 3, Resource.SHIELD, 1))
        );

        DevelopmentCard card3 = new DevelopmentCard(3, Color.GREEN, 12,
                Map.of(Resource.SHIELD, 4, Resource.COIN, 4),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.COIN, 3, Resource.SHIELD, 2))
        );

        assertEquals(card1, card2);
        assertNotEquals(card1, card3);
    }

    @Test
    public void hashEqualsTest() {
        DevelopmentCard card1 = new DevelopmentCard(3, Color.GREEN, 12,
                Map.of(Resource.SHIELD, 4, Resource.COIN, 4),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.COIN, 3, Resource.SHIELD, 1))
        );

        DevelopmentCard card2 = new DevelopmentCard(3, Color.GREEN, 12,
                Map.of(Resource.SHIELD, 4, Resource.COIN, 4),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.COIN, 3, Resource.SHIELD, 1))
        );

        DevelopmentCard card3 = new DevelopmentCard(3, Color.GREEN, 12,
                Map.of(Resource.SHIELD, 4, Resource.COIN, 4),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.COIN, 3, Resource.SHIELD, 2))
        );

        assertEquals(card1.hashCode(), card2.hashCode());
        assertNotEquals(card1.hashCode(), card3.hashCode());
    }
}
