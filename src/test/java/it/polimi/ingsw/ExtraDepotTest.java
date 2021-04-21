package it.polimi.ingsw;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Model.SpecialAbilities.ExtraDepot;
import it.polimi.ingsw.Server.Model.SpecialAbilities.SpecialAbility;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class ExtraDepotTest {

    @Test
    public void correctIsType() {
        SpecialAbility sa = new ExtraDepot(Resource.COIN);
        assertNotNull (sa);
        assertTrue (sa.isExtraDepot());
        assertFalse (sa.isProduction());
        assertFalse (sa.isMarketResource());
        assertFalse (sa.isDiscountResource());
    }
    @Test
    public void correctResourceCoin() {
        SpecialAbility sa = new ExtraDepot(Resource.COIN);

        ExtraDepot ed = (ExtraDepot) sa;

        assertEquals ( ed.getResourceType(),Resource.COIN);
        assertSame (ed.getNumber() , 0);
    }

    @Test
    public void correctAdding() {
        ExtraDepot ed = new ExtraDepot(Resource.COIN);
        assertFalse ( ed.addResource(0) );
        assertFalse ( ed.addResource(5) );
        assertFalse ( ed.addResource(-2) );
        assertSame ( ed.getNumber() , 0 );

        assertTrue ( ed.addResource(1) );
        assertSame ( ed.getNumber(), 1 );

        assertFalse (ed.addResource(5) );
        assertTrue ( ed.addResource(1) );
        assertSame ( ed.getNumber(), 2 );

        assertFalse ( ed.addResource(1) );
        assertSame ( ed.getNumber(),2 );

    }

    @Test
    public void correctRemoving()
    {
        ExtraDepot ed = new ExtraDepot(Resource.COIN);

        assertTrue (ed.addResource(2));
        assertSame (ed.getNumber(),2);

        assertFalse (ed.removeResource(3));
        assertFalse (ed.removeResource(-3));
        assertFalse (ed.removeResource(-1));
        assertFalse (ed.removeResource(0));

        assertTrue(ed.removeResource(1));
        assertSame(ed.getNumber(),1);
        assertTrue(ed.removeResource(1));
        assertSame(ed.getNumber(),0);
        assertFalse(ed.removeResource(1));
        assertSame(ed.getNumber(),0);
    }

    @Test
    public void correctSetting()
    {
        ExtraDepot ed = new ExtraDepot(Resource.COIN);

        assertTrue (ed.setResource(2));
        assertSame (ed.getNumber(),2);
        assertTrue (ed.setResource(1));
        assertSame (ed.getNumber(),1);
        assertTrue (ed.setResource(0));
        assertSame (ed.getNumber(),0);
        assertFalse(ed.setResource(3));
        assertFalse(ed.setResource(-1));

    }
}
