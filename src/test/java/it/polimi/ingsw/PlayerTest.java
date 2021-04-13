package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import it.polimi.ingsw.Server.Model.*;

public class PlayerTest {
    Player player;

    @BeforeEach
    public void newPlayer(){
        player=new Player("Pino", 1);
    }

    //checks if the method getNickname() is correct
    @Test
    public void getNicknameTest(){
        assertEquals(player.getNickname(), "Pino");
    }


    //verifies that the method getPlayerNumber() is correct
    @Test
    public void getPlayerNumberTest(){
        assertEquals(player.getPlayerNumber(), 1);
    }

    //verifies that when the player is created his attributes are initialized correctly
    @Test
    public void onInitTest(){
        assertNotNull(player.getWarehouseDepot());
        assertNotNull(player.getDevelopmentSlot());
        assertNotNull(player.getWarehouseDepot());
        assertNotNull(player.getStrongbox());
        assertEquals(player.getVP(), 0);
        assertEquals(player.getPosition(), 1);
    }

    //verifies that the method setVP(VP) does not accept negative parameter
    @Test
    public void setNegativeVP(){
        assertFalse(player.setVP(-1));
    }

    //verifies that the method setVP(VP) returns true if VP>0
    @Test
    public void setOkVP(){
        assertTrue(player.setVP(1));
    }

    //verifies that the method setVP(VP) works properly
    @Test
    public void setVPTest(){
        player.setVP(2);
        assertEquals(player.getVP(), 2);
    }

    //verifies that the method addVP(VP) does not accept negative parameter
    @Test
    public void addNegativeVP(){
        assertFalse(player.addVP(-1));
    }

    //verifies that the method setVP(VP) returns true if VP>0
    @Test
    public void addOkVP(){
        assertTrue(player.addVP(1));
    }

    //verifies that the method addVP(VP) works properly
    @Test
    public void addVPTest(){
        player.setVP(2);
        player.addVP(200);
        assertEquals(player.getVP(), 202);
        player.addVP(0);
        assertEquals(player.getVP(), 202);
    }

    //verifies that method setPositionTest works properly
    @Test
    public void setPositionTest(){
        assertTrue(player.setPosition(4));
        assertEquals(player.getPosition(), 4);
        assertFalse(player.setPosition(-1));
    }


    //verifies that method associate LeaderCards works properly
    @Test
    public void associateLeaderCardsTest(){
        LeaderCardsDeck ld= new LeaderCardsDeck();
        LeaderCard l1=ld.pickFourCards().get(0);
        LeaderCard l2=ld.pickFourCards().get(1);
        player.associateLeaderCards(l1, l2);
        assertEquals(l1, player.getLeaderCards()[0]);
        assertEquals(l2, player.getLeaderCards()[1]);
    }

    //verifies that the leaderCard methods do not return null
    @Test
    public void leaderCardsTest(){
        LeaderCardsDeck ld= new LeaderCardsDeck();
        LeaderCard l1=ld.pickFourCards().get(0);
        LeaderCard l2=ld.pickFourCards().get(1);
        player.associateLeaderCards(l1, l2);
        assertNotNull(player.getCardsWithDiscountResourceAbility());
        assertNotNull(player.getCardsWithExtraDepotAbility());
        assertNotNull(player.getCardsWithProductionAbility());
        assertNotNull(player.getCardsWithMarketResourceAbility());
    }
}
