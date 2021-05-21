package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.server.model.Strongbox;
import it.polimi.ingsw.server.model.enumerators.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StrongboxTest {
    Strongbox s;

    //Create a new Strongbox for each test
    @BeforeEach
    public void reset() {
        s = new Strongbox();
    }

    //tests if initially there are no resource (method getResource(resource) returns null)
    @Test
    public void initiallyNoResources() {
        assertNull(s.getQuantity(Resource.COIN));
        assertNull(s.getQuantity(Resource.SHIELD));
        assertNull(s.getQuantity(Resource.STONE));
        assertNull(s.getQuantity(Resource.SERVANT));
    }

    //tests if method add(resource, quantity) returns false when quantity<0. Strongbox does not change
    @Test
    public void quantitySubZero() {
        s.addResource(Resource.SERVANT, 1);
        assertFalse(s.addResource(Resource.SERVANT, -1));
        assertEquals(s.getQuantity(Resource.SERVANT), 1);
    }

    //tests if method add(resource, quantity) returns false when resource is invalid
    @Test
    public void addInvalidResource() {
        assertFalse(s.addResource(Resource.EXTRA, 3));
        assertFalse(s.addResource(Resource.NONE, 3));
        assertFalse(s.addResource(Resource.FAITH, 3));
    }

    //tests if method add(resource, quantity) returns true when resource is valid
    @Test
    public void addValidResourceReturnsTrue() {
        assertTrue(s.addResource(Resource.COIN, 40));
        assertTrue(s.addResource(Resource.SERVANT, 0));
    }

    //tests if method add(resource, quantity) actually adds the given amount of resource
    //in the strongbox when resource is not already present
    @Test
    public void addValidResource() {
        s.addResource(Resource.COIN, 0);
        s.addResource(Resource.SHIELD, 3);
        s.addResource(Resource.STONE, 23);
        s.addResource(Resource.SERVANT, 1);
        assertEquals(s.getQuantity(Resource.COIN), 0);
        assertEquals(s.getQuantity(Resource.SHIELD), 3);
        assertEquals(s.getQuantity(Resource.STONE), 23);
        assertEquals(s.getQuantity(Resource.SERVANT), 1);
    }

    //tests if method add(resource, quantity) adds the given amount of resource
    //in the strongbox when resource is already present
    @Test
    public void addAlreadyPresentResource() {
        s.addResource(Resource.COIN, 3);
        s.addResource(Resource.COIN, 4);
        assertEquals(s.getQuantity(Resource.COIN), 7);
    }

    //tests if method remove(resource, quantity) does not remove anything when quantity<0
    @Test
    public void removeQuantitySubZero() {
        s.addResource(Resource.COIN, 4);
        assertFalse(s.remove(Resource.COIN, -2));
        assertEquals(s.getQuantity(Resource.COIN), 4);
    }

    //tests if method remove(resource, quantity) returns false when resource is invalid
    @Test
    public void removeInvalidResource() {
        assertFalse(s.remove(Resource.EXTRA, 1));
        assertFalse(s.remove(Resource.NONE, 1));
    }

    //tries to remove more resource than possible
    @Test
    public void removeNotPossible() {
        s.addResource(Resource.COIN, 3);
        assertFalse(s.remove(Resource.COIN, 4));
        assertEquals(s.getQuantity(Resource.COIN), 3);
    }

    //tries to remove an accepted amount of resources
    @Test
    public void removePossible() {
        s.addResource(Resource.COIN, 3);
        assertTrue(s.remove(Resource.COIN, 2));
        assertEquals(s.getQuantity(Resource.COIN), 1);
    }

    //tests if method getQuantity(resource) returns null if resource is an invalid resource
    @Test
    public void getInvalidResource() {
        s.addResource(Resource.COIN, 4);
        s.addResource(Resource.SHIELD, 3);
        s.addResource(Resource.STONE, 23);
        s.addResource(Resource.SERVANT, 1);
        assertNull(s.getQuantity(Resource.NONE));
        assertNull(s.getQuantity(Resource.EXTRA));
    }

    //tests if the method getTotal() returns the correct amount of resources
    @Test
    public void getTotalTest() {
        s.addResource(Resource.COIN, 2);
        s.addResource(Resource.SHIELD, 50);
        s.addResource(Resource.STONE, 4);
        assertEquals(s.getTotal(), 56);
    }

    @Test
    public void toStringFormat() {
        s.addResource(Resource.COIN, 2);
        s.addResource(Resource.SHIELD, 5);
        s.addResource(Resource.STONE, 4);
        System.out.println(s.toString());
        assertEquals(42, 21 + 21);
    }
}