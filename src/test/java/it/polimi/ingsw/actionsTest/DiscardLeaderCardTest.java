package it.polimi.ingsw.actionsTest;

import it.polimi.ingsw.Catcher;
import it.polimi.ingsw.networking.message.MSG_ACTION_DISCARD_LEADERCARD;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.controller.ActionManager;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enumerators.Color;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.model.requirements.ReqValue;
import it.polimi.ingsw.server.model.requirements.CardRequirements;
import it.polimi.ingsw.server.model.specialAbilities.MarketResources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DiscardLeaderCardTest {

    GameManager gm;
    Game g;
    ActionManager am;
    Player p;
    Catcher c;

    @BeforeEach
    public void reset() {
        gm = new GameManager(4);
        am = gm.getActionManager();
        g = gm.getGame();

        c = new Catcher();
        g.addPlayer("Primo", 1);
        g.addPlayer("Secondo", 2);
        g.addPlayer("Terzo", 3);
        g.addPlayer("Quarto", 4);
        gm.addAllObserver(c);
        c.emptyQueue();
        p = g.getPlayer(1);
    }

    //error generated by trying to removing a card that has already been deactivated
    @Test
    public void discardTestError1() {
        ArrayList<LeaderCard> lc = new ArrayList<>();

        lc.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.YELLOW, new ReqValue(2, -1), Color.BLUE, new ReqValue(1, -1))),
                new MarketResources(Resource.SERVANT)));

        lc.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.GREEN, new ReqValue(2, -1), Color.PURPLE, new ReqValue(1, -1))),
                new MarketResources(Resource.SHIELD)));

        p.associateLeaderCards(lc);
        p.setLeaderCards(0, false);
        p.setLeaderCards(1, false);
        c.emptyQueue();

        assertNull(p.getLeaderCards()[0]);
        assertNull(p.getLeaderCards()[1]);

        assertFalse(am.discardLeaderCard(p, new MSG_ACTION_DISCARD_LEADERCARD(0)));

        assertNull(p.getLeaderCards()[0]);
        assertNull(p.getLeaderCards()[1]);

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());
    }

    //error generated by trying to removing a card that has already been ACTIVATED
    @Test
    public void discardTestError2() {
        ArrayList<LeaderCard> lc = new ArrayList<>();

        lc.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.YELLOW, new ReqValue(2, -1), Color.BLUE, new ReqValue(1, -1))),
                new MarketResources(Resource.SERVANT)));

        lc.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.GREEN, new ReqValue(2, -1), Color.PURPLE, new ReqValue(1, -1))),
                new MarketResources(Resource.SHIELD)));

        p.associateLeaderCards(lc);
        p.setLeaderCards(0, true);
        p.setLeaderCards(1, true);
        c.emptyQueue();

        assertTrue(p.getLeaderCards()[0].isEnabled());
        assertTrue(p.getLeaderCards()[1].isEnabled());

        assertFalse(am.discardLeaderCard(p, new MSG_ACTION_DISCARD_LEADERCARD(0)));
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());

        assertTrue(p.getLeaderCards()[0].isEnabled());
        assertTrue(p.getLeaderCards()[1].isEnabled());
    }

    //card 0 is going to be discarded
    @Test
    public void discardTest1() {
        ArrayList<LeaderCard> lc = new ArrayList<>();

        lc.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.YELLOW, new ReqValue(2, -1), Color.BLUE, new ReqValue(1, -1))),
                new MarketResources(Resource.SERVANT)));

        lc.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.GREEN, new ReqValue(2, -1), Color.PURPLE, new ReqValue(1, -1))),
                new MarketResources(Resource.SHIELD)));

        p.associateLeaderCards(lc);
        p.setLeaderCards(1, true);
        c.emptyQueue();

        assertFalse(p.getLeaderCards()[0].isEnabled());
        assertTrue(p.getLeaderCards()[1].isEnabled());
        assertEquals(0, p.getPosition());

        assertTrue(am.discardLeaderCard(p, new MSG_ACTION_DISCARD_LEADERCARD(0)));

        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(4, c.messages.size());

        assertNull(p.getLeaderCards()[0]);
        assertTrue(p.getLeaderCards()[1].isEnabled());
        assertEquals(1, p.getPosition());
    }

    //card 0 is going to be discarded, card 0 is going to give an error
    @Test
    public void discardTest2() {
        ArrayList<LeaderCard> lc = new ArrayList<>();

        lc.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.YELLOW, new ReqValue(2, -1), Color.BLUE, new ReqValue(1, -1))),
                new MarketResources(Resource.SERVANT)));

        lc.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.GREEN, new ReqValue(2, -1), Color.PURPLE, new ReqValue(1, -1))),
                new MarketResources(Resource.SHIELD)));

        p.associateLeaderCards(lc);
        p.setLeaderCards(1, true);
        c.emptyQueue();

        assertTrue(am.discardLeaderCard(p, new MSG_ACTION_DISCARD_LEADERCARD(0)));
        c.emptyQueue();

        assertFalse(am.discardLeaderCard(p, new MSG_ACTION_DISCARD_LEADERCARD(0)));
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());

        assertNull(p.getLeaderCards()[0]);
        assertTrue(p.getLeaderCards()[1].isEnabled());
    }

    //discarding both leader cards
    @Test
    public void discardTestBoth() {
        ArrayList<LeaderCard> lc = new ArrayList<>();

        lc.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.YELLOW, new ReqValue(2, -1), Color.BLUE, new ReqValue(1, -1))),
                new MarketResources(Resource.SERVANT)));

        lc.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.GREEN, new ReqValue(2, -1), Color.PURPLE, new ReqValue(1, -1))),
                new MarketResources(Resource.SHIELD)));

        p.associateLeaderCards(lc);
        c.emptyQueue();

        assertFalse(p.getLeaderCards()[0].isEnabled());
        assertFalse(p.getLeaderCards()[1].isEnabled());
        assertEquals(0, p.getPosition());

        assertTrue(am.discardLeaderCard(p, new MSG_ACTION_DISCARD_LEADERCARD(0)));
        assertNull(p.getLeaderCards()[0]);
        assertFalse(p.getLeaderCards()[1].isEnabled());
        assertEquals(1, p.getPosition());
        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(4, c.messages.size());
        c.emptyQueue();

        assertTrue(am.discardLeaderCard(p, new MSG_ACTION_DISCARD_LEADERCARD(1)));
        assertNull(p.getLeaderCards()[0]);
        assertNull(p.getLeaderCards()[1]);
        assertEquals(2, p.getPosition());
        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(4, c.messages.size());
        c.emptyQueue();
    }
}