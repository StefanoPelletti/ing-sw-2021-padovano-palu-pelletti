package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.Networking.Message.MSG_ACTION_GET_MARKET_RESOURCES;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Model.Marbles.MarketMarble;
import it.polimi.ingsw.Server.Utils.Catcher;
import org.junit.jupiter.api.*;

import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.Controller.*;

import java.util.*;

public class ActionManagerTest {

    GameManager gm;
    Game g;
    ActionManager am;
    Player p;
    Catcher c;


    @BeforeEach
    public void reset()
    {
        gm = new GameManager(4);
        am = gm.getActionManager();
        g = gm.getGame();
        c = new Catcher();
        g.addPlayer("Primo", 1);
        g.addPlayer("Secondo", 2);
        g.addPlayer("Terzo", 3);
        g.addPlayer("Quarto",4);

        p = g.getPlayer(1);
        g.addAllObservers(c);
    }

    @Test
    public void marketActionBasicTest()
    {
        MSG_ACTION_GET_MARKET_RESOURCES msg = new MSG_ACTION_GET_MARKET_RESOURCES(false, 1);

        assertTrue ( am.getMarketResources(p, msg));
        c.printQueueHeaders();
    }

    @Test
    //this test ensures that all the cards are removed from the purchasable cards
    //if the player has no resources at all
    public void buyDevelopmentCardTest1() {
        DevelopmentCardsDeck dc = g.getDevelopmentCardsDeck();
        DevelopmentSlot ds = p.getDevelopmentSlot();
        LeaderCardsDeck lcd = g.getLeaderCardsDeck();
        WarehouseDepot wh = p.getWarehouseDepot();
        assertFalse(am.buyDevelopmentCard(p));
        assertEquals(1, c.messages.size());
        assertSame(c.messages.get(0).getMessageType(), MessageType.MSG_UPD_Error);
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
