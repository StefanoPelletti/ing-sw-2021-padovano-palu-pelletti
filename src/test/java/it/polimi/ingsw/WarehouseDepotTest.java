package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.*;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Model.WarehouseDepot;

public class WarehouseDepotTest {
    WarehouseDepot warehouseDepot;

    @BeforeEach
    public void newDepot(){
        warehouseDepot =new WarehouseDepot();
    }

    //tries to put null shelves
    @Test
    public void depotAndNull(){
        assertFalse(WarehouseDepot.validateNewConfig(null, warehouseDepot.getShelf2(), warehouseDepot.getShelf3()));
        assertFalse(WarehouseDepot.validateNewConfig(warehouseDepot.getShelf1(), null, warehouseDepot.getShelf3()));
        assertFalse(WarehouseDepot.validateNewConfig(warehouseDepot.getShelf1(), warehouseDepot.getShelf2(), null));
    }

    //tries to put a 3 length shelf2 or a 2 length shelf3
    @Test
    public void depotAndLengthsNotPermitted(){
        assertFalse(WarehouseDepot.validateNewConfig(warehouseDepot.getShelf1(), warehouseDepot.getShelf3(), warehouseDepot.getShelf3()));
        assertFalse(WarehouseDepot.validateNewConfig(warehouseDepot.getShelf1(), warehouseDepot.getShelf2(), warehouseDepot.getShelf2()));
    }

    //tries to put invalid resources in shelf1
    @Test
    public void shelf1AndResourcesNotPermitted(){
        assertFalse(WarehouseDepot.validateNewConfig(Resource.EXTRA, warehouseDepot.getShelf2(), warehouseDepot.getShelf3()));
    }

    //tries to put invalid resources in shelf2
    @Test
    public void shelf2AndResourcesNotPermitted(){
        Resource[] tmpShelf2=new Resource[]{null, Resource.NONE};
        assertFalse(WarehouseDepot.validateNewConfig(warehouseDepot.getShelf1(), tmpShelf2, warehouseDepot.getShelf3()));
        tmpShelf2[0]=Resource.EXTRA;
        assertFalse(WarehouseDepot.validateNewConfig(warehouseDepot.getShelf1(), tmpShelf2, warehouseDepot.getShelf3()));
    }

    //tries to put invalid resources in shelf3
    @Test
    public void shelf3AndResourcesNotPermitted(){
        Resource[] tmpShelf3=new Resource[]{null, Resource.NONE, Resource.NONE};
        assertFalse(WarehouseDepot.validateNewConfig(warehouseDepot.getShelf1(), warehouseDepot.getShelf2(), tmpShelf3));
        tmpShelf3[0]=Resource.EXTRA;
        assertFalse(WarehouseDepot.validateNewConfig(warehouseDepot.getShelf1(), warehouseDepot.getShelf2(), tmpShelf3));
    }

    //tries to put the same resource in shelf 1 and 2
    @Test
    public void shelf1AndShelf2WithCommonResources(){
        Resource[] tmpShelf2= new Resource[]{Resource.SERVANT, Resource.NONE};
        assertFalse(WarehouseDepot.validateNewConfig(Resource.SERVANT, tmpShelf2, warehouseDepot.getShelf3()));
        tmpShelf2[0]=Resource.COIN;
        assertFalse(WarehouseDepot.validateNewConfig(Resource.COIN, tmpShelf2, warehouseDepot.getShelf3()));
        tmpShelf2[0]=Resource.STONE;
        assertFalse(WarehouseDepot.validateNewConfig(Resource.STONE, tmpShelf2, warehouseDepot.getShelf3()));
        tmpShelf2[0]=Resource.SHIELD;
        assertFalse(WarehouseDepot.validateNewConfig(Resource.SHIELD, tmpShelf2, warehouseDepot.getShelf3()));
    }

    //tries to put the same resource in shelf 1 and 3
    @Test
    public void shelf1AndShelf3WithCommonResources(){
        Resource[] tmpShelf3= new Resource[]{Resource.SERVANT, Resource.NONE, Resource.NONE};
        assertFalse(WarehouseDepot.validateNewConfig(Resource.SERVANT, warehouseDepot.getShelf2(), tmpShelf3));
        tmpShelf3[0]=Resource.COIN;
        assertFalse(WarehouseDepot.validateNewConfig(Resource.COIN, warehouseDepot.getShelf2(), tmpShelf3));
        tmpShelf3[0]=Resource.STONE;
        assertFalse(WarehouseDepot.validateNewConfig(Resource.STONE, warehouseDepot.getShelf2(), tmpShelf3));
        tmpShelf3[0]=Resource.SHIELD;
        assertFalse(WarehouseDepot.validateNewConfig(Resource.SHIELD, warehouseDepot.getShelf2(), tmpShelf3));
    }

    //tries to put the same resource in shelf 2 and 3
    @Test
    public void shelf2AndShelf3WithCommonResources(){
        Resource[] tmpShelf2=new Resource[]{Resource.SHIELD, Resource.NONE};
        Resource[] tmpShelf3=new Resource[]{Resource.SHIELD, Resource.NONE, Resource.NONE};
        assertFalse(WarehouseDepot.validateNewConfig(warehouseDepot.getShelf1(), tmpShelf2, tmpShelf3));
        tmpShelf2[0]=Resource.STONE;
        tmpShelf3[0]=Resource.STONE;
        assertFalse(WarehouseDepot.validateNewConfig(warehouseDepot.getShelf1(), tmpShelf2, tmpShelf3));
        tmpShelf2[0]=Resource.COIN;
        tmpShelf3[0]=Resource.COIN;
        assertFalse(WarehouseDepot.validateNewConfig(warehouseDepot.getShelf1(), tmpShelf2, tmpShelf3));
        tmpShelf2[0]=Resource.SERVANT;
        tmpShelf3[0]=Resource.SERVANT;
        assertFalse(WarehouseDepot.validateNewConfig(warehouseDepot.getShelf1(), tmpShelf2, tmpShelf3));
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
                    assertFalse(WarehouseDepot.validateNewConfig(warehouseDepot.getShelf1(), tmpShelf2 , warehouseDepot.getShelf3()));
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
                    assertFalse(WarehouseDepot.validateNewConfig(warehouseDepot.getShelf1(), warehouseDepot.getShelf2() , tmpShelf3));
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
        assertTrue(warehouseDepot.setConfig(tmpShelf1, tmpShelf2, tmpShelf3));
        assertEquals(warehouseDepot.getShelf1(), tmpShelf1);
        assertEquals(warehouseDepot.getShelf2()[0], tmpShelf2[0]);
        assertEquals(warehouseDepot.getShelf2()[1], tmpShelf2[1]);
        assertEquals(warehouseDepot.getShelf3()[0], tmpShelf3[0]);
        assertEquals(warehouseDepot.getShelf3()[1], tmpShelf3[1]);
        assertEquals(warehouseDepot.getShelf3()[2], tmpShelf3[2]);
    }

    //tries to set an invalid configuration and checks that the depot did not change
    @Test
    public void setNotValidConfig(){
        Resource tmpShelf1 = Resource.SERVANT;
        Resource[] tmpShelf2= new Resource[]{Resource.SERVANT, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.EXTRA, Resource.SHIELD};
        assertFalse(warehouseDepot.setConfig(tmpShelf1,tmpShelf2,tmpShelf3));
        assertEquals(warehouseDepot.getShelf1(), Resource.NONE);
        assertEquals(warehouseDepot.getShelf2()[0], Resource.NONE);
        assertEquals(warehouseDepot.getShelf2()[1], Resource.NONE);
        assertEquals(warehouseDepot.getShelf3()[0], Resource.NONE);
        assertEquals(warehouseDepot.getShelf3()[1], Resource.NONE);
        assertEquals(warehouseDepot.getShelf3()[2], Resource.NONE);
    }


    //checks if the method getTotal() return the correct amount of resources
    @Test
    public void totalResources(){
        Resource tmpShelf1=Resource.NONE;
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        warehouseDepot.setConfig(tmpShelf1, tmpShelf2, tmpShelf3);
        assertEquals(3, warehouseDepot.getTotal());
        tmpShelf1=Resource.COIN;
        warehouseDepot.setConfig(tmpShelf1,tmpShelf2,tmpShelf3);
        assertEquals(4, warehouseDepot.getTotal());
    }

    //tries to consume a resource that does that does not exist in the depot
    @Test
    public void consumeNotExistingResource(){
        Resource tmpShelf1=Resource.NONE;
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        warehouseDepot.setConfig(tmpShelf1, tmpShelf2, tmpShelf3);
        assertFalse(warehouseDepot.consume(Resource.COIN));

    }

    //tries to consume a resource in shelf1
    @Test
    public void consumeInShelf1() {
        Resource[] tmpShelf2 = new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3 = new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        warehouseDepot.setConfig(Resource.COIN, tmpShelf2, tmpShelf3);
        assertTrue(warehouseDepot.consume(Resource.COIN));
        assertEquals(warehouseDepot.getShelf1(), Resource.NONE);
        assertEquals(warehouseDepot.getShelf2()[0], Resource.NONE);
        assertEquals(warehouseDepot.getShelf2()[1], Resource.STONE);
        assertEquals(warehouseDepot.getShelf3()[0], Resource.SHIELD);
        assertEquals(warehouseDepot.getShelf3()[1], Resource.NONE);
        assertEquals(warehouseDepot.getShelf3()[2], Resource.SHIELD);
    }

    //tries to consume a resource present in shelf2
    @Test
    public void consumeInShelf2() {
        Resource[] tmpShelf2 = new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3 = new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        warehouseDepot.setConfig(Resource.COIN, tmpShelf2, tmpShelf3);
        assertTrue(warehouseDepot.consume(Resource.STONE));
        assertEquals(warehouseDepot.getShelf1(), Resource.COIN);
        assertEquals(warehouseDepot.getShelf2()[0], Resource.NONE);
        assertEquals(warehouseDepot.getShelf2()[1], Resource.NONE);
        assertEquals(warehouseDepot.getShelf3()[0], Resource.SHIELD);
        assertEquals(warehouseDepot.getShelf3()[1], Resource.NONE);
        assertEquals(warehouseDepot.getShelf3()[2], Resource.SHIELD);
    }

    //tries to consume a resource present in shelf3
    @Test
    public void consumeInShelf3() {
        Resource[] tmpShelf2 = new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3 = new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        warehouseDepot.setConfig(Resource.COIN, tmpShelf2, tmpShelf3);
        assertTrue(warehouseDepot.consume(Resource.SHIELD));
        assertEquals(warehouseDepot.getShelf1(), Resource.COIN);
        assertEquals(warehouseDepot.getShelf2()[0], Resource.NONE);
        assertEquals(warehouseDepot.getShelf2()[1], Resource.STONE);
        assertEquals(warehouseDepot.getShelf3()[0], Resource.SHIELD);
        assertEquals(warehouseDepot.getShelf3()[1], Resource.NONE);
        assertEquals(warehouseDepot.getShelf3()[2], Resource.NONE);
    }

    //checks if a resource not addable anywhere is not accepted
    @Test
    public void isNotAddableTest(){
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        warehouseDepot.setConfig(Resource.COIN, tmpShelf2, tmpShelf3);
        assertFalse(warehouseDepot.isAddable(Resource.COIN));
    }

    //checks if a resource addable to shelf1 is accepted
    @Test
    public void isAddableInShelf1(){
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        warehouseDepot.setConfig(Resource.NONE, tmpShelf2, tmpShelf3);
        assertTrue(warehouseDepot.isAddable(Resource.COIN));
    }

    //checks if a resource addable to shelf2 is accepted
    @Test
    public void isAddableInShelf2(){
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        warehouseDepot.setConfig(Resource.NONE, tmpShelf2, tmpShelf3);
        assertTrue(warehouseDepot.isAddable(Resource.STONE));
    }

    //checks if a resource addable to shelf3 is accepted
    @Test
    public void isAddableInShelf3(){
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        warehouseDepot.setConfig(Resource.COIN, tmpShelf2, tmpShelf3);
        assertTrue(warehouseDepot.isAddable(Resource.SHIELD));
    }

    //tries to add a resource that is not addable
    @Test
    public void addTestNotAccepted(){
        Resource tmpShelf1=Resource.COIN;
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        warehouseDepot.setConfig(tmpShelf1,tmpShelf2,tmpShelf3);
        assertFalse(warehouseDepot.add(Resource.COIN));
    }

    //tries to add a resource in shelf1
    @Test
    public void addShelf1(){
        Resource tmpShelf1=Resource.NONE;
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        warehouseDepot.setConfig(tmpShelf1,tmpShelf2,tmpShelf3);
        assertTrue(warehouseDepot.add(Resource.COIN));
    }

    //tries to add a resource in shelf2
    @Test
    public void addShelf2(){
        Resource tmpShelf1=Resource.NONE;
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        warehouseDepot.setConfig(tmpShelf1,tmpShelf2,tmpShelf3);
        assertTrue(warehouseDepot.add(Resource.STONE));
    }

    //tries to add a resource in shelf3
    @Test
    public void addShelf3(){
        Resource tmpShelf1=Resource.COIN;
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        warehouseDepot.setConfig(tmpShelf1,tmpShelf2,tmpShelf3);
        assertTrue(warehouseDepot.add(Resource.SHIELD));
    }

    //checks if the method getResources() gives the expected amount for each resource
    @Test
    public void getResourcesTest(){
        Resource tmpShelf1=Resource.COIN;
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        warehouseDepot.setConfig(tmpShelf1, tmpShelf2, tmpShelf3);
        Map<Resource, Integer> m= warehouseDepot.getResources();
        assertEquals(m.get(Resource.SHIELD), 2);
        assertEquals(m.get(Resource.COIN), 1);
        assertEquals(m.get(Resource.STONE), 1);
        assertEquals(m.get(Resource.SERVANT), 0);
    }

    //checks if the method getResources() returns 0 resources when depot is empty
    @Test
    public void get0Resources(){
        Map<Resource, Integer> m= warehouseDepot.getResources();
        assertEquals(m.get(Resource.SHIELD), 0);
        assertEquals(m.get(Resource.COIN), 0);
        assertEquals(m.get(Resource.STONE), 0);
        assertEquals(m.get(Resource.SERVANT), 0);
    }

}
