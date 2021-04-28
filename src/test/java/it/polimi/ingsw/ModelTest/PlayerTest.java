package it.polimi.ingsw.ModelTest;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Model.Requirements.ResourceRequirements;
import it.polimi.ingsw.Server.Model.SpecialAbilities.ExtraDepot;
import org.junit.jupiter.api.*;
import it.polimi.ingsw.Server.Model.*;

import java.util.ArrayList;
import java.util.Map;

public class PlayerTest {
    Player player;

    @BeforeEach
    public void reset(){
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
        assertEquals(player.getPosition(), 0);
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
        ArrayList<LeaderCard> l = new ArrayList<>(ld.pickFourCards());
        player.associateLeaderCards(l);
        assertEquals(l.get(0), player.getLeaderCards()[0]);
        assertEquals(l.get(1), player.getLeaderCards()[1]);
    }

    //verifies that the leaderCard methods do not return null
    @Test
    public void leaderCardsTest(){
        LeaderCardsDeck ld= new LeaderCardsDeck();
        ArrayList<LeaderCard> l = new ArrayList<>();
        l.add(ld.pickFourCards().get(0));
        l.add(ld.pickFourCards().get(1));
        player.associateLeaderCards(l);
        assertNotNull(player.getCardsWithDiscountResourceAbility());
        assertNotNull(player.getCardsWithExtraDepotAbility());
        assertNotNull(player.getCardsWithProductionAbility());
        assertNotNull(player.getCardsWithMarketResourceAbility());
    }

    //tests method getTotal()
    @Test
    public void getTotalTest(){
        assertEquals(0, player.getTotal()); //at the beginning, total must be 0

        player.getWarehouseDepot().add(Resource.SERVANT);
        player.getWarehouseDepot().add(Resource.COIN);
        player.getWarehouseDepot().add(Resource.COIN);

        player.getStrongbox().addResource(Resource.STONE, 4);
        player.getStrongbox().addResource(Resource.COIN, 3);

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.STONE, 5)),
                new ExtraDepot(Resource.SERVANT)));
        leaderCards.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SHIELD, 5)),
                new ExtraDepot(Resource.COIN)));
        player.associateLeaderCards(leaderCards);
        player.setLeaderCards(0, true);
        ((ExtraDepot) player.getLeaderCards()[0].getSpecialAbility()).addResource(1);
        assertEquals(11, player.getTotal());

        player.setLeaderCards(1, true);
        ((ExtraDepot) player.getLeaderCards()[0].getSpecialAbility()).setResource(2);
        ((ExtraDepot) player.getLeaderCards()[1].getSpecialAbility()).addResource(1);

        assertEquals(13, player.getTotal());
    }

    //tests method getResources
    @Test
    public void getResourcesTest(){
        Map<Resource, Integer> resources = player.getResources(); //initially player ha 0 resources
        for(Resource r: resources.keySet()){
            assertEquals(0, resources.get(r));
        }

        player.getWarehouseDepot().add(Resource.SHIELD);
        player.getWarehouseDepot().add(Resource.COIN);
        player.getWarehouseDepot().add(Resource.STONE);

        player.getStrongbox().addResource(Resource.STONE, 2);
        player.getStrongbox().addResource(Resource.COIN, 3);

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.STONE, 5)),
                new ExtraDepot(Resource.SERVANT)));
        leaderCards.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SHIELD, 5)),
                new ExtraDepot(Resource.COIN)));
        player.associateLeaderCards(leaderCards);

        resources = player.getResources();
        assertEquals(1, resources.get(Resource.SHIELD));
        assertEquals(4, resources.get(Resource.COIN));
        assertEquals(3, resources.get(Resource.STONE));
        assertEquals(0, resources.get(Resource.SERVANT));

        player.setLeaderCards(0, true);
        ((ExtraDepot) player.getLeaderCards()[0].getSpecialAbility()).setResource(2);

        resources = player.getResources();
        assertEquals(1, resources.get(Resource.SHIELD));
        assertEquals(4, resources.get(Resource.COIN));
        assertEquals(3, resources.get(Resource.STONE));
        assertEquals(2, resources.get(Resource.SERVANT));
    }


    //tests method getDepotAndExtraDepotResources()
    @Test
    public void getDepotAndExtraDepotResourcesTest(){
        Map<Resource, Integer> resources = player.getDepotAndExtraDepotResources(); //initially player ha 0 resources
        for(Resource r: resources.keySet()){
            assertEquals(0, resources.get(r));
        }

        player.getWarehouseDepot().add(Resource.SHIELD);
        player.getWarehouseDepot().add(Resource.COIN);
        player.getWarehouseDepot().add(Resource.COIN);
        player.getWarehouseDepot().add(Resource.SERVANT);

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.STONE, 5)),
                new ExtraDepot(Resource.SERVANT)));
        leaderCards.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SHIELD, 5)),
                new ExtraDepot(Resource.STONE)));
        player.associateLeaderCards(leaderCards);

        resources = player.getDepotAndExtraDepotResources();
        assertEquals(1, resources.get(Resource.SHIELD));
        assertEquals(2, resources.get(Resource.COIN));
        assertEquals(0, resources.get(Resource.STONE));
        assertEquals(1, resources.get(Resource.SERVANT));

        player.setLeaderCards(0, true);
        ((ExtraDepot) player.getLeaderCards()[0].getSpecialAbility()).setResource(2);

        resources = player.getDepotAndExtraDepotResources();
        assertEquals(1, resources.get(Resource.SHIELD));
        assertEquals(2, resources.get(Resource.COIN));
        assertEquals(0, resources.get(Resource.STONE));
        assertEquals(3, resources.get(Resource.SERVANT));

        player.setLeaderCards(1, true);
        ((ExtraDepot) player.getLeaderCards()[1].getSpecialAbility()).setResource(1);

        resources = player.getDepotAndExtraDepotResources();
        assertEquals(1, resources.get(Resource.SHIELD));
        assertEquals(2, resources.get(Resource.COIN));
        assertEquals(1, resources.get(Resource.STONE));
        assertEquals(3, resources.get(Resource.SERVANT));
    }
}
