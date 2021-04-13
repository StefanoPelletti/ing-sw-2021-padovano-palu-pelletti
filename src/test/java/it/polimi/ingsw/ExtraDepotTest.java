package it.polimi.ingsw;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Model.SpecialAbilities.ExtraDepot;
import it.polimi.ingsw.Server.Model.SpecialAbilities.SpecialAbility;
import org.junit.jupiter.api.*;

public class ExtraDepotTest {

    @Test
    public void correctIsType() {
        SpecialAbility sa = new ExtraDepot(Resource.COIN);
        assert (sa != null);
        assert (sa.isExtraDepot());
        assert (!sa.isProduction());
        assert (!sa.isMarketResource());
        assert (!sa.isDiscountResource());
    }
    @Test
    public void correctResourceCoin() {
        SpecialAbility sa = new ExtraDepot(Resource.COIN);

        ExtraDepot ed = (ExtraDepot) sa;

        assert ( ed.getResourceType().equals(Resource.COIN));
        assert (ed.getNumber() == 0);
    }

    @Test
    public void correctAdding() {
        ExtraDepot ed = new ExtraDepot(Resource.COIN);
        assert (!ed.addResource(0));
        assert (! ed.addResource(5) );
        assert (! ed.addResource(-2));
        assert ( ed.getNumber() == 0 );

        assert ( ed.addResource(1) );
        assert ( ed.getNumber() == 1 );

        assert (!ed.addResource(5));
        assert (ed.addResource(1));
        assert (ed.getNumber() == 2);

        assert (!ed.addResource(1));
        assert ( ed.getNumber()==2);

    }

    @Test
    public void correctRemoving()
    {
        ExtraDepot ed = new ExtraDepot(Resource.COIN);
        assert (ed.addResource(2));
        assert (ed.getNumber()==2);

        assert (!ed.removeResource(3));
        assert (!ed.removeResource(-3));
        assert (!ed.removeResource(-1));
        assert (!ed.removeResource(0));

        assert(ed.removeResource(1));
        assert(ed.getNumber()==1);
        assert(ed.removeResource(1));
        assert(ed.getNumber()==0);
        assert(!ed.removeResource(1));
        assert(ed.getNumber()==0);
    }

    @Test
    public void correctSetting()
    {
        ExtraDepot ed = new ExtraDepot(Resource.COIN);
        assert (ed.setResource(2));
        assert (ed.getNumber()==2);
        assert (ed.setResource(1));
        assert (ed.getNumber()==1);
        assert (ed.setResource(0));
        assert (ed.getNumber()==0);
        assert(!ed.setResource(3));
        assert(!ed.setResource(-1));

    }
}