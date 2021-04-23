package it.polimi.ingsw;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.Networking.Message.MSG_ACTION_GET_MARKET_RESOURCES;
import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Model.Marbles.MarketMarble;
import it.polimi.ingsw.Server.Model.Marbles.RedMarble;
import it.polimi.ingsw.Server.Model.Marbles.WhiteMarble;
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

//sets a new Game with 4 players. Also sets a Catcher for notification purposes
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

        //at the start of the game, it should always be true
        assertTrue ( am.getMarketResources(p, msg));
    }

    @Test
    public void redWhiteWhite_Or_WhiteWhiteWhite_SpecialCase()
    {
        MSG_ACTION_GET_MARKET_RESOURCES msg = new MSG_ACTION_GET_MARKET_RESOURCES(true, 0);
        while(true)
        {
            if(g.getMarket().getGrid()[0][0] instanceof WhiteMarble || g.getMarket().getGrid()[0][0] instanceof RedMarble)
                if(g.getMarket().getGrid()[1][0] instanceof WhiteMarble || g.getMarket().getGrid()[1][0] instanceof RedMarble)
                    if(g.getMarket().getGrid()[2][0] instanceof WhiteMarble || g.getMarket().getGrid()[2][0] instanceof RedMarble)
                        break;
            this.reset();
        }

        assertTrue(am.getMarketResources(p, msg));
        c.printQueueHeaders();
        if(c.messages.size()==1) {
            assertSame(c.messages.get(0).getMessageType(), MessageType.MSG_UPD_Market); //this case is the WHITE WHITE WHITE one. Player does not get updated. Only market does.
            assertEquals(1, p.getPosition());
        }
        else if(c.messages.size()==2) { // this is a RED WHITE WHITE case. The player AND the market must be updated, nothing else.
            assertTrue ( c.messages.get(0).getMessageType()==MessageType.MSG_UPD_Player || c.messages.get(0).getMessageType()==MessageType.MSG_UPD_Market);
            assertTrue ( c.messages.get(1).getMessageType()==MessageType.MSG_UPD_Player || c.messages.get(1).getMessageType()==MessageType.MSG_UPD_Market);
        }
        assertFalse ( c.messages.size()>2);
    }

    @Test
    public void getMarketResourcesError()
    {
        MSG_ACTION_GET_MARKET_RESOURCES msg;

            //can only occur if: message contains impossible data, or the corresponding market helper is enabled
            msg = new MSG_ACTION_GET_MARKET_RESOURCES(false, 1);
            assertTrue(am.getMarketResources(p, msg));

            assertTrue(g.getMarketHelper().isEnabled());
            assertTrue(c.messages.stream().anyMatch(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper));
            assertTrue(c.messages.stream().noneMatch(x -> x.getMessageType() == MessageType.MSG_UPD_Error));
            c.emptyQueue();

            assertFalse(am.getMarketResources(p, msg));
            assertEquals(1, c.messages.size());
            assertSame(c.messages.get(0).getMessageType(), MessageType.MSG_UPD_Error);
            assertTrue(g.getMarketHelper().isEnabled());
    }

    @Test
    //this test ensures that all the cards are removed from the purchasable cards
    //if the player has no resources at all
    public void buyDevelopmentCardTest1() {
        DevelopmentCardsDeck dc = g.getDevelopmentCardsDeck();
        DevelopmentSlot ds = p.getDevelopmentSlot();
        LeaderCardsDeck lcd = g.getLeaderCardsDeck();
        WarehouseDepot wh = p.getWarehouseDepot();

        assertFalse(am.buyDevelopmentCard(p)); //if FALSE => enables
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
