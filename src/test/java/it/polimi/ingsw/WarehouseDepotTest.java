package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.*;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Model.WarehouseDepot;

public class WarehouseDepotTest {
    WarehouseDepot w;

    @BeforeEach
    public void newDepot(){
        w=new WarehouseDepot();
    }

    //tries to put null shelves
    @Test
    public void depotAndNull(){
        assertFalse(WarehouseDepot.validateNewConfig(null, w.getShelf2(), w.getShelf3()));
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), null, w.getShelf3()));
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), w.getShelf2(), null));
    }

    //tries to put a 3 length shelf2 or a 2 length shelf3
    @Test
    public void depotAndLengthsNotPermitted(){
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(),w.getShelf3(), w.getShelf3()));
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(),w.getShelf2(), w.getShelf2()));
    }

    //tries to put invalid resources in shelf1
    @Test
    public void shelf1AndResourcesNotPermitted(){
        assertFalse(WarehouseDepot.validateNewConfig(Resource.FAITH, w.getShelf2(), w.getShelf3()));
        assertFalse(WarehouseDepot.validateNewConfig(Resource.EXTRA, w.getShelf2(), w.getShelf3()));
    }

    //tries to put invalid resources in shelf2
    @Test
    public void shelf2AndResourcesNotPermitted(){
        Resource[] tmpShelf2=new Resource[]{null, Resource.NONE};
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), tmpShelf2, w.getShelf3()));
        tmpShelf2[0]=Resource.FAITH;
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), tmpShelf2, w.getShelf3()));
        tmpShelf2[0]=Resource.EXTRA;
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), tmpShelf2, w.getShelf3()));
    }

    //tries to put invalid resources in shelf3
    @Test
    public void shelf3AndResourcesNotPermitted(){
        Resource[] tmpShelf3=new Resource[]{null, Resource.NONE, Resource.NONE};
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), w.getShelf2(), tmpShelf3));
        tmpShelf3[0]=Resource.FAITH;
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), w.getShelf2(), tmpShelf3));
        tmpShelf3[0]=Resource.EXTRA;
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), w.getShelf2(), tmpShelf3));
    }

    //tries to put the same resource in shelf 1 and 2
    @Test
    public void shelf1AndShelf2WithCommonResources(){
        Resource[] tmpShelf2= new Resource[]{Resource.SERVANT, Resource.NONE};
        assertFalse(WarehouseDepot.validateNewConfig(Resource.SERVANT, tmpShelf2, w.getShelf3()));
        tmpShelf2[0]=Resource.COIN;
        assertFalse(WarehouseDepot.validateNewConfig(Resource.COIN, tmpShelf2, w.getShelf3()));
        tmpShelf2[0]=Resource.STONE;
        assertFalse(WarehouseDepot.validateNewConfig(Resource.STONE, tmpShelf2, w.getShelf3()));
        tmpShelf2[0]=Resource.SHIELD;
        assertFalse(WarehouseDepot.validateNewConfig(Resource.SHIELD, tmpShelf2, w.getShelf3()));
    }

    //tries to put the same resource in shelf 1 and 3
    @Test
    public void shelf1AndShelf3WithCommonResources(){
        Resource[] tmpShelf3= new Resource[]{Resource.SERVANT, Resource.NONE, Resource.NONE};
        assertFalse(WarehouseDepot.validateNewConfig(Resource.SERVANT, w.getShelf2(), tmpShelf3));
        tmpShelf3[0]=Resource.COIN;
        assertFalse(WarehouseDepot.validateNewConfig(Resource.COIN, w.getShelf2(), tmpShelf3));
        tmpShelf3[0]=Resource.STONE;
        assertFalse(WarehouseDepot.validateNewConfig(Resource.STONE, w.getShelf2(), tmpShelf3));
        tmpShelf3[0]=Resource.SHIELD;
        assertFalse(WarehouseDepot.validateNewConfig(Resource.SHIELD, w.getShelf2(), tmpShelf3));
    }

    //tries to put the same resource in shelf 2 and 3
    @Test
    public void shelf2AndShelf3WithCommonResources(){
        Resource[] tmpShelf2=new Resource[]{Resource.SHIELD, Resource.NONE};
        Resource[] tmpShelf3=new Resource[]{Resource.SHIELD, Resource.NONE, Resource.NONE};
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), tmpShelf2, tmpShelf3));
        tmpShelf2[0]=Resource.STONE;
        tmpShelf3[0]=Resource.STONE;
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), tmpShelf2, tmpShelf3));
        tmpShelf2[0]=Resource.COIN;
        tmpShelf3[0]=Resource.COIN;
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), tmpShelf2, tmpShelf3));
        tmpShelf2[0]=Resource.SERVANT;
        tmpShelf3[0]=Resource.SERVANT;
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), tmpShelf2, tmpShelf3));
    }

    //tries to put different resources in shelf2
    @Test
    public void shelf2WithDifferentResourcesNotNone(){
        ArrayList<Resource> resources=new ArrayList<>();
        resources.add(Resource.STONE);
        resources.add(Resource.SERVANT);
        resources.add(Resource.COIN);
        resources.add(Resource.SHIELD);
        Resource[] tmpShelf2= new Resource[]{Resource.NONE,Resource.NONE};
        for(Resource i: resources){
            tmpShelf2[0]=i;
            for(Resource j: resources){
                if(i!=j){
                    tmpShelf2[1]=j;
                    assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), tmpShelf2 , w.getShelf3()));
                }
            }
        }
    }

    //tries to put different resources in shelf3
    @Test
    public void shelf3WithDifferentResourcesNotNone(){
        ArrayList<Resource> resources=new ArrayList<>();
        resources.add(Resource.STONE);
        resources.add(Resource.SERVANT);
        resources.add(Resource.COIN);
        resources.add(Resource.SHIELD);
        Resource[] tmpShelf3= new Resource[]{Resource.NONE,Resource.NONE, Resource.NONE};
        for(Resource i: resources){
            tmpShelf3[0]=i;
            for(Resource j: resources){
                if(i!=j){
                    tmpShelf3[1]=j;
                    assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), w.getShelf2() , tmpShelf3));
                }
            }
        }
    }

    //checks if a valid config is accepted
    @Test
    public void validConfig(){
        Resource tmpShelf1= Resource.COIN;
        Resource[] tmpShelf2= new Resource[]{Resource.SERVANT, Resource.NONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        assertTrue(WarehouseDepot.validateNewConfig(tmpShelf1, tmpShelf2, tmpShelf3));
    }

    //tries to set a valid configuration and checks that the depot was changed
    @Test
    public void setValidConfig(){
        Resource tmpShelf1 = Resource.SERVANT;
        Resource[] tmpShelf2= new Resource[]{Resource.STONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.SHIELD, Resource.SHIELD};
        assertTrue(w.setConfig(tmpShelf1, tmpShelf2, tmpShelf3));
        assertEquals(w.getShelf1(), tmpShelf1);
        assertEquals(w.getShelf2()[0], tmpShelf2[0]);
        assertEquals(w.getShelf2()[1], tmpShelf2[1]);
        assertEquals(w.getShelf3()[0], tmpShelf3[0]);
        assertEquals(w.getShelf3()[1], tmpShelf3[1]);
        assertEquals(w.getShelf3()[2], tmpShelf3[2]);
    }

    //tries to set an invalid configuration and checks that the depot did not change
    @Test
    public void setNotValidConfig(){
        Resource tmpShelf1 = Resource.SERVANT;
        Resource[] tmpShelf2= new Resource[]{Resource.SERVANT, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.FAITH, Resource.SHIELD};
        assertFalse(w.setConfig(tmpShelf1,tmpShelf2,tmpShelf3));
        assertEquals(w.getShelf1(), Resource.NONE);
        assertEquals(w.getShelf2()[0], Resource.NONE);
        assertEquals(w.getShelf2()[1], Resource.NONE);
        assertEquals(w.getShelf3()[0], Resource.NONE);
        assertEquals(w.getShelf3()[1], Resource.NONE);
        assertEquals(w.getShelf3()[2], Resource.NONE);
    }


    //checks if the method getTotal() return the correct amount of resources
    @Test
    public void totalResources(){
        Resource tmpShelf1=Resource.NONE;
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        w.setConfig(tmpShelf1, tmpShelf2, tmpShelf3);
        assertEquals(3, w.getTotal());
        tmpShelf1=Resource.COIN;
        w.setConfig(tmpShelf1,tmpShelf2,tmpShelf3);
        assertEquals(4, w.getTotal());
    }

    //tries to consume a resource that does that does not exist in the depot
    @Test
    public void consumeNotExistingResource(){
        Resource tmpShelf1=Resource.NONE;
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        w.setConfig(tmpShelf1, tmpShelf2, tmpShelf3);
        assertFalse(w.consume(Resource.COIN));

    }

    //tries to consume a resource in shelf1
    @Test
    public void consumeInShelf1() {
        Resource[] tmpShelf2 = new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3 = new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        w.setConfig(Resource.COIN, tmpShelf2, tmpShelf3);
        assertTrue(w.consume(Resource.COIN));
        assertEquals(w.getShelf1(), Resource.NONE);
        assertEquals(w.getShelf2()[0], Resource.NONE);
        assertEquals(w.getShelf2()[1], Resource.STONE);
        assertEquals(w.getShelf3()[0], Resource.SHIELD);
        assertEquals(w.getShelf3()[1], Resource.NONE);
        assertEquals(w.getShelf3()[2], Resource.SHIELD);
    }

    //tries to consume a resource present in shelf2
    @Test
    public void consumeInShelf2() {
        Resource[] tmpShelf2 = new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3 = new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        w.setConfig(Resource.COIN, tmpShelf2, tmpShelf3);
        assertTrue(w.consume(Resource.STONE));
        assertEquals(w.getShelf1(), Resource.COIN);
        assertEquals(w.getShelf2()[0], Resource.NONE);
        assertEquals(w.getShelf2()[1], Resource.NONE);
        assertEquals(w.getShelf3()[0], Resource.SHIELD);
        assertEquals(w.getShelf3()[1], Resource.NONE);
        assertEquals(w.getShelf3()[2], Resource.SHIELD);
    }

    //tries to consume a resource present in shelf3
    @Test
    public void consumeInShelf3() {
        Resource[] tmpShelf2 = new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3 = new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        w.setConfig(Resource.COIN, tmpShelf2, tmpShelf3);
        assertTrue(w.consume(Resource.SHIELD));
        assertEquals(w.getShelf1(), Resource.COIN);
        assertEquals(w.getShelf2()[0], Resource.NONE);
        assertEquals(w.getShelf2()[1], Resource.STONE);
        assertEquals(w.getShelf3()[0], Resource.SHIELD);
        assertEquals(w.getShelf3()[1], Resource.NONE);
        assertEquals(w.getShelf3()[2], Resource.NONE);
    }

    //checks if a resource not addable anywhere is not accepted
    @Test
    public void isNotAddableTest(){
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        w.setConfig(Resource.COIN, tmpShelf2, tmpShelf3);
        assertFalse(w.isAddable(Resource.COIN));
    }

    //checks if a resource addable to shelf1 is accepted
    @Test
    public void isAddableInShelf1(){
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        w.setConfig(Resource.NONE, tmpShelf2, tmpShelf3);
        assertTrue(w.isAddable(Resource.COIN));
    }

    //checks if a resource addable to shelf2 is accepted
    @Test
    public void isAddableInShelf2(){
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        w.setConfig(Resource.NONE, tmpShelf2, tmpShelf3);
        assertTrue(w.isAddable(Resource.STONE));
    }

    //checks if a resource addable to shelf3 is accepted
    @Test
    public void isAddableInShelf3(){
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        w.setConfig(Resource.COIN, tmpShelf2, tmpShelf3);
        assertTrue(w.isAddable(Resource.SHIELD));
    }

    //tries to add a resource that is not addable
    @Test
    public void addTestNotAccepted(){
        Resource tmpShelf1=Resource.COIN;
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        w.setConfig(tmpShelf1,tmpShelf2,tmpShelf3);
        assertFalse(w.add(Resource.COIN));
    }

    //tries to add a resource in shelf1
    @Test
    public void addShelf1(){
        Resource tmpShelf1=Resource.NONE;
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        w.setConfig(tmpShelf1,tmpShelf2,tmpShelf3);
        assertTrue(w.add(Resource.COIN));
    }

    //tries to add a resource in shelf2
    @Test
    public void addShelf2(){
        Resource tmpShelf1=Resource.NONE;
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        w.setConfig(tmpShelf1,tmpShelf2,tmpShelf3);
        assertTrue(w.add(Resource.STONE));
    }

    //tries to add a resource in shelf3
    @Test
    public void addShelf3(){
        Resource tmpShelf1=Resource.COIN;
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        w.setConfig(tmpShelf1,tmpShelf2,tmpShelf3);
        assertTrue(w.add(Resource.SHIELD));
    }

    //checks if the method getResources() gives the expected amount for each resource
    @Test
    public void getResourcesTest(){
        Resource tmpShelf1=Resource.COIN;
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        w.setConfig(tmpShelf1, tmpShelf2, tmpShelf3);
        Map<Resource, Integer> m=w.getResources();
        assertEquals(m.get(Resource.SHIELD), 2);
        assertEquals(m.get(Resource.COIN), 1);
        assertEquals(m.get(Resource.STONE), 1);
        assertEquals(m.get(Resource.SERVANT), 0);
    }

    //checks if the method getResources() returns 0 resources when depot is empty
    @Test
    public void get0Resources(){
        Map<Resource, Integer> m=w.getResources();
        assertEquals(m.get(Resource.SHIELD), 0);
        assertEquals(m.get(Resource.COIN), 0);
        assertEquals(m.get(Resource.STONE), 0);
        assertEquals(m.get(Resource.SERVANT), 0);
    }

}
