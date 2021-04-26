package it.polimi.ingsw.ModelTest;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Model.SpecialAbilities.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpecialAbilityTest {

    @Test
    public void extraDepot()
    {
        SpecialAbility x = new ExtraDepot(Resource.COIN);
        assertTrue (x.isExtraDepot());
        assertFalse (x.isDiscountResource());
        assertFalse (x.isProduction());
        assertFalse (x.isMarketResource());
    }

    @Test
    public void discountResource()
    {
        SpecialAbility x = new DiscountResource(Resource.COIN);
        assertFalse (x.isExtraDepot());
        assertTrue (x.isDiscountResource());
        assertFalse (x.isProduction());
        assertFalse (x.isMarketResource());
    }


    @Test
    public void marketResource()
    {
        SpecialAbility x = new MarketResources(Resource.COIN);
        assertFalse (x.isExtraDepot());
        assertFalse (x.isDiscountResource());
        assertTrue (x.isMarketResource());
        assertFalse (x.isProduction());
    }


    @Test
    public void production()
    {
        SpecialAbility x = new Production(Resource.COIN);
        assertFalse (x.isExtraDepot());
        assertFalse (x.isDiscountResource());
        assertFalse (x.isMarketResource());
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
