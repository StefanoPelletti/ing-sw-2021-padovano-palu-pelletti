package it.polimi.ingsw.ModelTest;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.Server.Model.ActionTokens.ActionToken;
import org.junit.jupiter.api.*;
import it.polimi.ingsw.Server.Model.ActionTokenStack;


public class ActionTokenStackTest {

    ActionTokenStack a= new ActionTokenStack();

    //shuffles the actionTokenStack before each test
    @BeforeEach
    public void shuffle(){
        a.shuffle();
    }

    //Test that verifies the method pickFirst() does not return null
    @Test
    public void notNullTokens()
    {
        for(int i=0; i<6; i++){
            assertNotNull(a.pickFirst());
        }
    }

    @Test
    public void testReturns()
    {
        int r = 0;
        int ff = 0;
        int fs = 0;
        ActionToken s;
        for( int i = 0; i < 6; i++)
        {
            s = a.pickFirst();
            if(s.isRemover()) r++;
            if(s.isForward2()) ff++;
            if(s.isForwardAndShuffle()) fs++;
        }
        assertEquals(4, r);
        assertEquals(1, ff);
        assertEquals(1, fs);

    }
}
