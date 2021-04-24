package it.polimi.ingsw.ModelTest;

import static org.junit.jupiter.api.Assertions.*;
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
    public void notNullTokens(){
        for(int i=0; i<6; i++){
            assertNotNull(a.pickFirst());
        }
    }
}
