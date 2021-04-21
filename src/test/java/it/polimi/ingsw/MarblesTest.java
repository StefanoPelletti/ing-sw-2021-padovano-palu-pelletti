package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Model.Marbles.*;

import java.util.ArrayList;

public class MarblesTest {
    ArrayList<Resource> resources;

    @BeforeEach
    public void resetResourceList(){
        resources= new ArrayList<>();
    }

    //verifies that a blueMarble is converted in a shield
    @Test
    public void blueMarbleTest(){
        BlueMarble blueMarble = new BlueMarble();
        blueMarble.addResource(resources);
        assertEquals(resources.get(0), Resource.SHIELD);
    }

    //verifies that a greyMarble is converted in a stone
    @Test
    public void greyMarbleTest(){
        GreyMarble greyMarble = new GreyMarble();
        greyMarble.addResource(resources);
        assertEquals(resources.get(0), Resource.STONE);
    }

    //verifies that a purpleMarble is converted in a servant
    @Test
    public void purpleMarbleTest(){
        PurpleMarble purpleMarble = new PurpleMarble();
        purpleMarble.addResource(resources);
        assertEquals(resources.get(0), Resource.SERVANT);
    }

    //verifies that a whiteMarble is converted in an extra
    @Test
    public void whiteMarbleTest(){
        WhiteMarble whiteMarble=new WhiteMarble();
        whiteMarble.addResource(resources);
        assertEquals(resources.get(0), Resource.EXTRA);
    }

    //verifies that a blueMarble is converted in a shield
    @Test
    public void yellowMarbleTest(){
        YellowMarble yellowMarble = new YellowMarble();
        yellowMarble.addResource(resources);
        assertEquals(resources.get(0), Resource.COIN);
    }

    //verifies that a redMarble throws a RedMarbleException
    @Test
    public void redMarbleTest(){
        RedMarble redMarble= new RedMarble();
        RedMarbleException exception= new RedMarbleException("a");
        assertThrows(exception.getClass(),()->redMarble.addResource(resources));
    }

    //test with multiple marbles
    @Test
    public void multipleMarble(){
        YellowMarble yellowMarble= new YellowMarble();
        WhiteMarble whiteMarble= new WhiteMarble();
        GreyMarble greyMarble= new GreyMarble();
        BlueMarble blueMarble= new BlueMarble();
        PurpleMarble purpleMarble= new PurpleMarble();
        yellowMarble.addResource(resources);
        whiteMarble.addResource(resources);
        greyMarble.addResource(resources);
        blueMarble.addResource(resources);
        purpleMarble.addResource(resources);
        Resource[] expectedResources={Resource.COIN, Resource.EXTRA, Resource.STONE, Resource.SHIELD, Resource.SERVANT};
        assertArrayEquals(resources.toArray(), expectedResources);
    }
}
