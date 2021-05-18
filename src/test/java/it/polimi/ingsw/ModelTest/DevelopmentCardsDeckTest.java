package it.polimi.ingsw.ModelTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import it.polimi.ingsw.Server.Model.DevelopmentCard;
import it.polimi.ingsw.Server.Model.DevelopmentCardsDeck;



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
    public void toStringFormat()
    {
        System.out.println(d.toString());
        assertTrue(true);
    }

    @Test
    public void toStringCardFormat()
    {
        DevelopmentCard c = d.removeCard(2,2);
        System.out.println(c);
    }
}
