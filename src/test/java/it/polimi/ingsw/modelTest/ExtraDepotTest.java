package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.model.specialAbilities.ExtraDepot;
import it.polimi.ingsw.server.model.specialAbilities.SpecialAbility;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExtraDepotTest {

    @Test
    public void correctIsType() {
        SpecialAbility sa = new ExtraDepot(Resource.COIN);
        assertNotNull(sa);
        assertTrue(sa.isExtraDepot());
        assertFalse(sa.isProduction());
        assertFalse(sa.isMarketResource());
        assertFalse(sa.isDiscountResource());
    }

    @Test
    public void correctResourceCoin() {
        ExtraDepot sa = new ExtraDepot(Resource.COIN);

        assertEquals(sa.getResourceType(), Resource.COIN);
        assertSame(sa.getNumber(), 0);
    }

    @Test
    public void correctAdding() {
        ExtraDepot ed = new ExtraDepot(Resource.COIN);
        assertFalse(ed.addResource(0));
        assertFalse(ed.addResource(5));
        assertFalse(ed.addResource(-2));
        assertSame(ed.getNumber(), 0);

        assertTrue(ed.addResource(1));
        assertSame(ed.getNumber(), 1);

        assertFalse(ed.addResource(5));
        assertTrue(ed.addResource(1));
        assertSame(ed.getNumber(), 2);

        assertFalse(ed.addResource(1));
        assertSame(ed.getNumber(), 2);

    }

    @Test
    public void correctRemoving() {
        ExtraDepot ed = new ExtraDepot(Resource.COIN);

        assertTrue(ed.addResource(2));
        assertSame(ed.getNumber(), 2);

        assertFalse(ed.removeResource(3));
        assertFalse(ed.removeResource(-3));
        assertFalse(ed.removeResource(-1));
        assertFalse(ed.removeResource(0));

        assertTrue(ed.removeResource(1));
        assertSame(ed.getNumber(), 1);
        assertTrue(ed.removeResource(1));
        assertSame(ed.getNumber(), 0);
        assertFalse(ed.removeResource(1));
        assertSame(ed.getNumber(), 0);
    }

    @Test
    public void correctSetting() {
        ExtraDepot ed = new ExtraDepot(Resource.COIN);

        assertTrue(ed.setResource(2));
        assertSame(ed.getNumber(), 2);
        assertTrue(ed.setResource(1));
        assertSame(ed.getNumber(), 1);
        assertTrue(ed.setResource(0));
        assertSame(ed.getNumber(), 0);
        assertFalse(ed.setResource(3));
        assertFalse(ed.setResource(-1));
    }
}