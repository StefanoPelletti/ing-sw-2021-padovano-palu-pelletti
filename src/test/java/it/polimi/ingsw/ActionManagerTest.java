package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import org.junit.jupiter.api.*;

import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.Controller.*;

import java.util.*;

public class ActionManagerTest {
    GameManager gm = new GameManager();
    Game g = new Game();
    FaithTrackManager ftm = new FaithTrackManager(g);
    ActionManager am = new ActionManager(gm,ftm,g);

    @Test
    //this test ensures that all the cards are removed from the purchasable cards
    //if the player has no resources at all
    public void buyDevelopmentCardTest1() {
        DevelopmentCardsDeck dc = new DevelopmentCardsDeck();
        DevelopmentSlot ds = new DevelopmentSlot();
        LeaderCardsDeck lcd = new LeaderCardsDeck();
        WarehouseDepot wh = new WarehouseDepot();
        Player p = new Player("user1", 1);
        assertTrue(am.buyDevelopmentCard(p));
    }

    @Deprecated
    //this test is useless for now, it is only used to see if there is any major bug.
    //it will be eliminated soon from this world.
    public void buyDevelopmentCardTest2() {
        DevelopmentCardsDeck dc = new DevelopmentCardsDeck();
        DevelopmentSlot ds = new DevelopmentSlot();
        LeaderCardsDeck lcd = new LeaderCardsDeck();
        WarehouseDepot wh = new WarehouseDepot();
        Strongbox sb = new Strongbox();
        Player p = new Player("user1", 1);

        sb.addResource(Resource.SERVANT, 4);
        sb.addResource(Resource.SHIELD, 4);
        sb.addResource(Resource.STONE, 4);
        sb.addResource(Resource.COIN, 4);

        assertTrue(am.buyDevelopmentCard(p));
    }
}
