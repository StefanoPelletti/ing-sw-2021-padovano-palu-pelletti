package it.polimi.ingsw;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Model.SpecialAbilities.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpecialAbilityTest {

    @Test
    public void extraDepotF1()
    {
        SpecialAbility x = new ExtraDepot(Resource.COIN);
        assertTrue (x.isExtraDepot());
    }
    @Test
    public void extraDepotF2()
    {
        SpecialAbility x = new ExtraDepot(Resource.COIN);
        assertFalse (x.isDiscountResource());
    }
    @Test
    public void extraDepotF3()
    {
        SpecialAbility x = new ExtraDepot(Resource.COIN);
        assertFalse (x.isProduction());
    }
    @Test
    public void extraDepotF4()
    {
        SpecialAbility x = new ExtraDepot(Resource.COIN);
        assertFalse (x.isMarketResource());
    }
////////////////
    @Test
    public void discountResourceF1()
    {
        SpecialAbility x = new DiscountResource(Resource.COIN);
        assertFalse (x.isExtraDepot());
    }
    @Test
    public void discountResourceF2()
    {
        SpecialAbility x = new DiscountResource(Resource.COIN);
        assertTrue (x.isDiscountResource());
    }
    @Test
    public void discountResourceF3()
    {
        SpecialAbility x = new DiscountResource(Resource.COIN);
        assertFalse (x.isProduction());
    }
    @Test
    public void discountResourceF4()
    {
        SpecialAbility x = new DiscountResource(Resource.COIN);
        assertFalse (x.isMarketResource());
    }
/////////////////////
    @Test
    public void marketResourceF1()
    {
        SpecialAbility x = new MarketResources(Resource.COIN);
        assertFalse (x.isExtraDepot());
    }
    @Test
    public void marketResourceF2()
    {
        SpecialAbility x = new MarketResources(Resource.COIN);
        assertFalse (x.isDiscountResource());
    }
    @Test
    public void marketResourceF3()
    {
        SpecialAbility x = new MarketResources(Resource.COIN);
        assertTrue (x.isMarketResource());
    }
    @Test
    public void marketResourceF4()
    {
        SpecialAbility x = new MarketResources(Resource.COIN);
        assertFalse (x.isProduction());
    }
////////////////////
    @Test
    public void productionF1()
    {
        SpecialAbility x = new Production(Resource.COIN);
        assertFalse (x.isExtraDepot());
    }
    @Test
    public void productionF2()
    {
        SpecialAbility x = new Production(Resource.COIN);
        assertFalse (x.isDiscountResource());
    }
    @Test
    public void productionF3()
    {
        SpecialAbility x = new Production(Resource.COIN);
        assertFalse (x.isMarketResource());
    }
    @Test
    public void productionF4()
    {
        SpecialAbility x = new Production(Resource.COIN);
        assertTrue (x.isProduction());
    }





    @Test
    public void sortingTest()
    {
        ArrayList<SpecialAbility> a = new ArrayList<>();
        a.add(new ExtraDepot(Resource.COIN));
        a.add(new DiscountResource(Resource.SERVANT));
        a.add(new MarketResources(Resource.STONE));
        a.add(new Production(Resource.SHIELD));
        a.add(new ExtraDepot(Resource.SERVANT));
        a.add(new DiscountResource(Resource.STONE));
        a.add(new MarketResources(Resource.SHIELD));
        a.add(new Production(Resource.COIN));

        List<ExtraDepot> e = a.stream().filter(SpecialAbility::isExtraDepot).map(x -> ((ExtraDepot)x)).collect(Collectors.toList());
        List<DiscountResource> d = a.stream().filter(SpecialAbility::isDiscountResource).map(x -> ((DiscountResource)x)).collect(Collectors.toList());
        List<MarketResources> m = a.stream().filter(SpecialAbility::isMarketResource).map(x -> ((MarketResources)x)).collect(Collectors.toList());
        List<Production> p = a.stream().filter(SpecialAbility::isProduction).map(x -> ((Production)x)).collect(Collectors.toList());

        for ( SpecialAbility x : e)
            assertTrue(x.isExtraDepot());

        for ( SpecialAbility x : d)
            assertTrue(x.isDiscountResource());

        for ( SpecialAbility x : m)
            assertTrue(x.isMarketResource());

        for ( SpecialAbility x : p)
            assertTrue(x.isProduction());
    }
}
