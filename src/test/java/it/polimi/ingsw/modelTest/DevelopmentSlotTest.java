package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.server.model.DevelopmentCard;
import it.polimi.ingsw.server.model.DevelopmentCardsDeck;
import it.polimi.ingsw.server.model.DevelopmentSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class DevelopmentSlotTest {

    DevelopmentSlot ds;
    DevelopmentCardsDeck dcd;

    @BeforeEach
    public void reset() {
        ds = new DevelopmentSlot();
        dcd = new DevelopmentCardsDeck();
    }

    @Test
    //this test tries to add cards of different and same levels in the same slot
    public void addCardTest1() {

        //adds cards that are not level 1
        DevelopmentCard dc = dcd.removeCard(1, 2); //level 2
        assertFalse(ds.addCard(dc, 1));
        dc = dcd.removeCard(0, 3); //level 3
        assertFalse(ds.addCard(dc, 1));
    }

    @Test
    public void addCardTest2() {

        //adds a card that's actually level 1
        DevelopmentCard dc = dcd.removeCard(2, 0);
        assertTrue(ds.addCard(dc, 1));

        //tries to add a card that's level 1 on a card that's level 1
        dc = dcd.removeCard(2, 1);
        assertFalse(ds.addCard(dc, 1));

        //adds a card that's level 2 on a card that's level 1
        dc = dcd.removeCard(1, 1);
        assertTrue(ds.addCard(dc, 1));

        //tries to add a card that's level 2 on a card that's level 2
        dc = dcd.removeCard(1, 3);
        assertFalse(ds.addCard(dc, 1));

        //adds a card that's level 3 on a card that's level 2
        dc = dcd.removeCard(0, 1);
        assertTrue(ds.addCard(dc, 1));

        //tries to add a card that's level 3 on a card that's level 3
        dc = dcd.removeCard(0, 1);
        assertFalse(ds.addCard(dc, 1));

        List<DevelopmentCard> list = ds.getCards();
        assertEquals(3, list.size());
    }

    @Test
    //this test selects some DevelopmentCard from the Deck and for each one of them it tells if they are positionable and where.
    public void ValidateNewCardTest() {

        ArrayList<DevelopmentCard> ls = new ArrayList<>();
        List<DevelopmentCard> ls2;

        ls.add(dcd.removeCard(0, 0));
        ls.add(dcd.removeCard(1, 0));
        ls.add(dcd.removeCard(2, 0));

        ls2 = ls.stream().filter(x -> {
            for (int i = 0; i < 3; i++)
                if (ds.validateNewCard(x, i))
                    return true;
            return false;
        }).collect(Collectors.toList());

        for (DevelopmentCard dc : ls2) {
            for (int i = 0; i < 3; i++) {
                assertTrue(ds.validateNewCard(dc, i));
            }
        }
    }

    @Test
    //this test verifies that the getAllCards method works when there are no cards in the 3 slots.
    public void getAllCardsTest1() {

        DevelopmentCard[][] dc = ds.getAllCards();
        for (int n = 0; n < 3; n++) {
            for (int h = 0; h < 3; h++) {
                assertNull(dc[n][h]);
            }
        }
    }

    @Test
    //this test verifies that the method works when all cards are inserted.
    public void getAllCardsTest2() {

        DevelopmentCard[][] ldc = new DevelopmentCard[3][3];

        //initializing ldc
        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 3; j++) {
                ldc[i][j] = null;
            }
        }

        int i = 0;
        int j = 0;
        int k;
        for (int c = 0; c < 3; c++) {
            k = 0;
            for (int r = 2; r >= 0; r--) {
                DevelopmentCard dc = dcd.removeCard(r, c);
                ldc[j][k] = dc;
                assertTrue(ds.addCard(dc, i));
                k++;
            }
            j++;
            i++;
        }

        DevelopmentCard[][] temp = ds.getAllCards();
        for (int q = 0; q < 3; q++) {
            for (int t = 0; t < 3; t++) {
                assertEquals(temp[q][t], ldc[q][t]);
            }
        }
    }

    @Test
    //this test verifies that the getTopCards method works correctly when there are no cards at all
    public void getTopCardsTest() {
        DevelopmentSlot ds = new DevelopmentSlot();
        List<DevelopmentCard> dc = ds.getTopCards();

        for (DevelopmentCard c : dc) {
            assertNull(c);
        }
    }

    @Test
    //this test verifies that the getTopCards method works correctly
    public void getTopCardsTest2() {

        DevelopmentCard[] ldc = new DevelopmentCard[3];

        ldc[0] = dcd.removeCard(2, 0);
        ds.addCard(ldc[0], 0);
        ds.addCard(dcd.removeCard(2, 1), 1);
        ldc[1] = dcd.removeCard(1, 0);
        ds.addCard(ldc[1], 1);
        ldc[2] = dcd.removeCard(2, 2);
        ds.addCard(ldc[2], 2);

        List<DevelopmentCard> dc = ds.getTopCards();

        int i = 0;
        for (DevelopmentCard c : dc) {
            assertEquals(c, ldc[i]);
            i++;
        }
    }

    @Test
    public void toStringFormat() {
        DevelopmentCard[] ldc = new DevelopmentCard[3];
        ldc[0] = dcd.removeCard(2, 0);
        ds.addCard(ldc[0], 0);
        ds.addCard(dcd.removeCard(2, 1), 1);
        ldc[1] = dcd.removeCard(1, 0);
        ds.addCard(ldc[1], 1);
        ldc[2] = dcd.removeCard(2, 2);
        ds.addCard(ldc[2], 2);

        System.out.println(ds.toString());
        assertTrue(true);
    }
}