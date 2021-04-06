package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import kotlin.jvm.internal.MagicApiIntrinsics;
import org.junit.jupiter.api.*;

import java.util.*;
import it.polimi.ingsw.Model.Enumerators.Resource;
import it.polimi.ingsw.Model.WarehouseDepot;

public class WarehouseDepotTest {
    WarehouseDepot w=new WarehouseDepot();


    @Test
    public void depotAndNull(){
        assertFalse(WarehouseDepot.validateNewConfig(null, w.getShelf2(), w.getShelf3()));
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), null, w.getShelf3()));
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), w.getShelf2(), null));
    }

    @Test
    public void depotAndLengthsNotPermitted(){
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(),w.getShelf3(), w.getShelf3()));
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(),w.getShelf2(), w.getShelf2()));
    }

    @Test
    public void shelf1AndResourcesNotPermitted(){
        assertFalse(WarehouseDepot.validateNewConfig(Resource.FAITH, w.getShelf2(), w.getShelf3()));
        assertFalse(WarehouseDepot.validateNewConfig(Resource.EXTRA, w.getShelf2(), w.getShelf3()));
    }

    @Test
    public void shelf2AndResourcesNotPermitted(){
        Resource[] tmpShelf2=new Resource[]{null, Resource.NONE};
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), tmpShelf2, w.getShelf3()));
        tmpShelf2[0]=Resource.FAITH;
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), tmpShelf2, w.getShelf3()));
        tmpShelf2[0]=Resource.EXTRA;
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), tmpShelf2, w.getShelf3()));
        tmpShelf2[0]=null;
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), tmpShelf2, w.getShelf3()));
    }

    @Test
    public void shelf3AndResourcesNotPermitted(){
        Resource[] tmpShelf3=new Resource[]{null, Resource.NONE, Resource.NONE};
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), w.getShelf2(), tmpShelf3));
        tmpShelf3[0]=Resource.FAITH;
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), w.getShelf2(), tmpShelf3));
        tmpShelf3[0]=Resource.EXTRA;
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), w.getShelf2(), tmpShelf3));
        tmpShelf3[0]=null;
        assertFalse(WarehouseDepot.validateNewConfig(w.getShelf1(), tmpShelf3, w.getShelf3()));
    }

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

    @Test
    public void shelf2WithDifferentResourcesNotNone(){
        ArrayList<Resource> resources=new ArrayList<Resource>();
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

    @Test
    public void shelf3WithDifferentResourcesNotNone(){
        ArrayList<Resource> resources=new ArrayList<Resource>();
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

    @Test
    public void validConfig(){
        Resource tmpShelf1= Resource.COIN;
        Resource[] tmpShelf2= new Resource[]{Resource.SERVANT, Resource.NONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        assertTrue(WarehouseDepot.validateNewConfig(tmpShelf1, tmpShelf2, tmpShelf3));
    }

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

    @Test
    public void setNotValidConfig(){
        Resource tmpShelf1 = Resource.SERVANT;
        Resource[] tmpShelf2= new Resource[]{Resource.SERVANT, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.FAITH, Resource.SHIELD};
        assertFalse(w.setConfig(tmpShelf1,tmpShelf2,tmpShelf3));
        assertEquals(w.getShelf1(), Resource.NONE);
        assertEquals(w.getShelf1(), Resource.NONE);
        assertEquals(w.getShelf1(), Resource.NONE);
        assertEquals(w.getShelf1(), Resource.NONE);
        assertEquals(w.getShelf1(), Resource.NONE);
    }


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

    @Test
    public void consumeResource(){
        Resource tmpShelf1=Resource.NONE;
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        w.setConfig(tmpShelf1, tmpShelf2, tmpShelf3);
        assertFalse(w.consume(Resource.COIN));
        w.setConfig(Resource.COIN, tmpShelf2, tmpShelf3);
        assertTrue(w.consume(Resource.COIN));
        assertTrue(w.consume(Resource.STONE));
        assertTrue(w.consume(Resource.SHIELD));
        assertEquals(w.getShelf1(), Resource.NONE);
        assertEquals(w.getShelf2()[0], Resource.NONE);
        assertEquals(w.getShelf2()[1], Resource.NONE);
        assertEquals(w.getShelf3()[0], Resource.SHIELD);
        assertEquals(w.getShelf3()[1], Resource.NONE);
        assertEquals(w.getShelf3()[2], Resource.NONE);
    }

    @Test
    public void isAddableTest(){
        Resource tmpShelf1=Resource.NONE;
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        w.setConfig(tmpShelf1,tmpShelf2,tmpShelf3);
        assertTrue(w.isAddable(Resource.SHIELD));
        assertTrue(w.isAddable(Resource.COIN));
        w.setConfig(Resource.COIN, tmpShelf2, tmpShelf3);
        assertFalse(w.isAddable(Resource.COIN));
        assertTrue(w.isAddable(Resource.STONE));
        assertTrue(w.isAddable(Resource.SHIELD));
        assertEquals(w.getShelf1(), Resource.COIN);
        assertEquals(w.getShelf2()[0], Resource.NONE);
        assertEquals(w.getShelf2()[1], Resource.STONE);
        assertEquals(w.getShelf3()[0], Resource.SHIELD);
        assertEquals(w.getShelf3()[1], Resource.NONE);
        assertEquals(w.getShelf3()[2], Resource.SHIELD);
    }

    @Test
    public void addTest(){
        Resource tmpShelf1=Resource.COIN;
        Resource[] tmpShelf2= new Resource[]{Resource.NONE, Resource.STONE};
        Resource[] tmpShelf3= new Resource[]{Resource.SHIELD, Resource.NONE, Resource.SHIELD};
        w.setConfig(tmpShelf1,tmpShelf2,tmpShelf3);
        assertFalse(w.add(Resource.COIN));
        w.setConfig(Resource.NONE, tmpShelf2, tmpShelf3);
        assertTrue(w.add(Resource.COIN));
        assertTrue(w.add(Resource.STONE));
        assertTrue(w.add(Resource.SHIELD));
        assertEquals(w.getShelf1(), Resource.COIN);
        assertEquals(w.getShelf2()[0], Resource.STONE);
        assertEquals(w.getShelf2()[1], Resource.STONE);
        assertEquals(w.getShelf3()[0], Resource.SHIELD);
        assertEquals(w.getShelf3()[1], Resource.SHIELD);
        assertEquals(w.getShelf3()[2], Resource.SHIELD);
    }

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


}
