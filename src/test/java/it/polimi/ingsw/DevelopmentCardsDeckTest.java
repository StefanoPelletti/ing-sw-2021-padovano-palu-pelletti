package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import it.polimi.ingsw.Model.DevelopmentCard;
import it.polimi.ingsw.Model.DevelopmentCardsDeck;

import java.util.Arrays;

public class DevelopmentCardsDeckTest {

    DevelopmentCardsDeck d = new DevelopmentCardsDeck();

    @BeforeEach
    public void reset()
    {
        System.out.println("Resetting...");
        d = new DevelopmentCardsDeck();
    }



    @Test
    public void getStackNotNull()
    {
        DevelopmentCard[] x = d.getStack(-1,0);
        assert ( x == null );

        x = d.getStack(0,5);
        assert ( x == null );

        x = d.getStack(0,0);
        assert ( x != null );
    }

    @Test
    public void StackNotNull()
    {
        DevelopmentCard[] x = d.getStack(0,0);
        for(int i=0; i<4; i++)
        {
            assertNotNull(x[i]);
        }
    }

    @Test
    public void testOfRemove()
    {

        DevelopmentCard x = d.removeCard(0,0);
        assert (x != null);
        x = d.removeCard(0,0);
        assert (x != null);
        x = d.removeCard(0,0);
        assert (x != null);
        x = d.removeCard(0,0);
        assert (x != null);
        x = d.removeCard(0,0);
        assert (x == null);

    }

    @Test
    public void testOfAStack()
    {
        DevelopmentCard[] x = d.getStack(0,0);
        d.removeCard(0,0);
        DevelopmentCard[][] y = d.getVisible();

        assert ( x[2].equals(y[0][0]) );

        d.removeCard(0,0);
        y = d.getVisible();

        assert( x[1].equals(y[0][0]));

        d.removeCard(0,0);
        y = d.getVisible();

        assert( x[0].equals(y[0][0]));

        d.removeCard(0,0);
        y = d.getVisible();

        assert (y[0][0] == null);
    }

    @Test
    public void shuffleTest()
    {
        DevelopmentCard[] x = d.getStack(0,0);
        d.shuffle();
        DevelopmentCard[] y = d.getStack(0,0);

        assert ( x[0].equals(y[0]) || x[0].equals(y[1]) ||
                x[0].equals(y[2]) || x[0].equals(y[3]));
        assert ( x[1].equals(y[0]) || x[1].equals(y[1]) ||
                x[1].equals(y[2]) || x[1].equals(y[3]));
        assert ( x[2].equals(y[0]) || x[2].equals(y[1]) ||
                x[2].equals(y[2]) || x[2].equals(y[3]));
        assert ( x[3].equals(y[0]) || x[3].equals(y[1]) ||
                x[3].equals(y[2]) || x[3].equals(y[3]));
    }

    @Test
    public void correctCards()
    {
        DevelopmentCard x;

        for( int r=0; r<3; r++)
        {
            for ( int c=0; c<4; c++)
            {
                x = d.removeCard(r,c);
                assert( x!=null );
                assert( x.internalCheck() );
            }
        }
    }

    @Test
    public void removeColumnInvalidParameters()
    {
        assert(!d.removeCard(-1));
        assert(!d.removeCard(6));
    }

    @Test
    public void removingColumns()
    {
        assert(d.removeCard(0));
        assert(d.removeCard(0));
        DevelopmentCard[][] x = d.getVisible();
        assert ( x[0][0] != null );
        assert ( x[1][0] != null );
        assert ( x[2][0] == null );

        assert(d.removeCard(0));
        assert(d.removeCard(0));
        x = d.getVisible();
        assert ( x[0][0] != null );
        assert ( x[1][0] == null );
        assert ( x[2][0] == null );

        assert(d.removeCard(0));
        assert(d.removeCard(0));
        x = d.getVisible();
        assert ( x[0][0] == null );
        assert ( x[1][0] == null );
        assert ( x[2][0] == null );

        assert(d.removeCard(1));
        assert(d.removeCard(1));
        assert(d.removeCard(1));
        assert(d.removeCard(1));
        assert(d.removeCard(1));
        assert(d.removeCard(1));

        assert(d.removeCard(2));
        assert(d.removeCard(2));
        assert(d.removeCard(2));
        assert(d.removeCard(2));
        assert(d.removeCard(2));
        assert(d.removeCard(2));

        assert(d.removeCard(0));
        assert(d.removeCard(1));
        assert(d.removeCard(2));

        assert(d.removeCard(3));
        assert(d.removeCard(3));
        assert(d.removeCard(3));
        assert(d.removeCard(3));
        assert(d.removeCard(3));
        assert(d.removeCard(3));
        x = d.getVisible();

        for( int r=0; r<3; r++)
        {
            for ( int c=0; c<4; c++)
            {
                assert( x[r][c] == null );
            }
        }
    }

}

