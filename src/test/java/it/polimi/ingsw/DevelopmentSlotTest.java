package it.polimi.ingsw;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import it.polimi.ingsw.Model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DevelopmentSlotTest {
    DevelopmentSlot ds = new DevelopmentSlot();

    @Test
    public void addCardTest() {
        DevelopmentCardsDeck dcd = new DevelopmentCardsDeck();

        //adds cards that are not level 1
        DevelopmentCard dc = dcd.removeCard(1,2); //level 2
        assertFalse(ds.addCard(dc,1));
        dc = dcd.removeCard(0,3); //level 3
        assertFalse(ds.addCard(dc,1));

        //now adds a card that's actually level 1
        dc = dcd.removeCard(2,0);
        assertTrue(ds.addCard(dc,1));

        //tries to add a card that's level 1 on a card that's level 1
        dc = dcd.removeCard(2,1);
        assertFalse(ds.addCard(dc,1));

        //now adds a card that's level 2 on a card that's level 1
        dc = dcd.removeCard(1,1);
        assertTrue(ds.addCard(dc,1));

        //tries to add a card that's level 2 on a card that's level 2
        dc = dcd.removeCard(1,3);
        assertFalse(ds.addCard(dc,1));

        //now adds a card that's level 3 on a card that's level 2
        dc = dcd.removeCard(0,1);
        assertTrue(ds.addCard(dc,1));

        //tries to add a card that's level 3 on a card that's level 3
        dc = dcd.removeCard(0,1);
        assertFalse(ds.addCard(dc,1));
    }

    @Test
    public void ValidateNewCardTest() {
        DevelopmentCardsDeck dcd = new DevelopmentCardsDeck();
        ArrayList<DevelopmentCard> ls = new ArrayList<>();
        List<DevelopmentCard> ls2 = new ArrayList<>();

        ls.add(dcd.removeCard(0,0));
        ls.add(dcd.removeCard(1,0));
        ls.add(dcd.removeCard(2,0));

        ls2 = ls.stream().filter(x -> {for(int i = 0; i < 3; i++) if(ds.validateNewCard(x,i)) {return true;}
        return false;}).collect(Collectors.toList());

        for(DevelopmentCard dc : ls2) {
            for(int i = 0; i < 3; i++) {
                assertTrue(ds.validateNewCard(dc,i));
            }
        }
    }


}
