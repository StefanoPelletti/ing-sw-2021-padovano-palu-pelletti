package it.polimi.ingsw.actionsTest;

import it.polimi.ingsw.Catcher;
import it.polimi.ingsw.networking.message.MSG_INIT_CHOOSE_LEADERCARDS;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.controller.ActionManager;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enumerators.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChooseLeaderCardsTest {

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

    @Test
    public void errorState() {
        MSG_INIT_CHOOSE_LEADERCARDS message = new MSG_INIT_CHOOSE_LEADERCARDS(1, 2);
        assertFalse(am.chooseLeaderCard(p, message));
        assertNull(p.getLeaderCards()[0]);
        assertNull(p.getLeaderCards()[1]);

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());
    }

    @Test
    public void standardTurn() {
        MSG_INIT_CHOOSE_LEADERCARDS message = new MSG_INIT_CHOOSE_LEADERCARDS(0, 1);
        List<LeaderCard> list = p.getStartingCards();
        g.setLeaderCardsObjectCards(list);
        g.setLeaderCardsObjectEnabled(true);
        g.changeStatus(Status.STANDARD_TURN);
        c.emptyQueue();

        assertTrue(am.chooseLeaderCard(p, message));
        assertEquals(list.get(0), p.getLeaderCards()[0]);
        assertEquals(list.get(1), p.getLeaderCards()[1]);
        assertFalse(g.isLeaderCardsObjectEnabled());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_LeaderCardsObject).count());
        assertEquals(3, c.messages.size());
    }

    @Test
    public void continuingStateINIT_1() {
        MSG_INIT_CHOOSE_LEADERCARDS message = new MSG_INIT_CHOOSE_LEADERCARDS(0, 1);
        List<LeaderCard> list = p.getStartingCards();
        g.setLeaderCardsObjectCards(list);
        g.setLeaderCardsObjectEnabled(true);
        g.changeStatus(Status.INIT_1);
        c.emptyQueue();

        assertTrue(am.chooseLeaderCard(p, message));
        assertEquals(list.get(0), p.getLeaderCards()[0]);
        assertEquals(list.get(1), p.getLeaderCards()[1]);
        assertTrue(g.isLeaderCardsObjectEnabled());
        assertEquals(2, g.getCurrentPlayerInt());

        assertEquals(g.getLeaderCardsObject().getCards(), gm.currentPlayer().getStartingCards());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Game).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_LeaderCardsObject).count());
        assertEquals(4, c.messages.size());
    }

    @Test
    public void endingStateINIT_1() {
        MSG_INIT_CHOOSE_LEADERCARDS message = new MSG_INIT_CHOOSE_LEADERCARDS(2, 3);
        g.setCurrentPlayer(4);
        p = g.getCurrentPlayer();
        List<LeaderCard> list = p.getStartingCards();
        g.setLeaderCardsObjectCards(list);
        g.setLeaderCardsObjectEnabled(true);
        g.changeStatus(Status.INIT_1);
        c.emptyQueue();

        assertTrue(am.chooseLeaderCard(p, message));
        assertEquals(list.get(2), p.getLeaderCards()[0]);
        assertEquals(list.get(3), p.getLeaderCards()[1]);
        assertFalse(g.isLeaderCardsObjectEnabled());
        assertEquals(2, g.getCurrentPlayerInt());
        assertTrue(g.isResourceObjectEnabled());
        assertEquals(1, g.getResourceObject().getNumOfResources());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(3, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Game).count()); //one for the turn+1, 2 for the advancing players
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_LeaderCardsObject).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_ResourceObject).count());
        assertEquals(7, c.messages.size());
    }

    @Test
    public void playerWhoDisconnectedBeforeLeaderCards() {
        MSG_INIT_CHOOSE_LEADERCARDS message = new MSG_INIT_CHOOSE_LEADERCARDS(0, 1);
        Player p2 = g.getPlayer(2);
        p2.setDisconnectedBeforeLeaderCard(true);
        g.changeStatus(Status.STANDARD_TURN);

        assertTrue(am.endTurn(p, false));
        c.emptyQueue();

        assertEquals(2, g.getCurrentPlayerInt());
        assertTrue(g.isLeaderCardsObjectEnabled());
        assertEquals(g.getLeaderCardsObject().getCards(), gm.currentPlayer().getStartingCards());
        assertTrue(gm.currentPlayer().isDisconnectedBeforeLeaderCard());

        assertTrue(am.chooseLeaderCard(p2, message));

        assertEquals(2, g.getCurrentPlayerInt());
        assertFalse(g.isLeaderCardsObjectEnabled());
        assertFalse(gm.currentPlayer().isDisconnectedBeforeLeaderCard());
        assertEquals(p2.getLeaderCards()[0], p2.getStartingCards().get(0));
        assertEquals(p2.getLeaderCards()[1], p2.getStartingCards().get(1));
        assertFalse(g.isResourceObjectEnabled());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_LeaderCardsObject).count());
        assertEquals(3, c.messages.size());
    }

    @Test
    public void playerWhoDisconnectedBeforeLeaderCardsAndBeforeResources() {
        MSG_INIT_CHOOSE_LEADERCARDS message = new MSG_INIT_CHOOSE_LEADERCARDS(0, 1);
        Player p2 = g.getPlayer(2);
        p2.setDisconnectedBeforeLeaderCard(true);
        p2.setDisconnectedBeforeResource(true);
        g.changeStatus(Status.STANDARD_TURN);

        assertTrue(am.endTurn(p, false));
        c.emptyQueue();
        assertTrue(am.chooseLeaderCard(p2, message));

        assertEquals(2, g.getCurrentPlayerInt());
        assertFalse(g.isLeaderCardsObjectEnabled());
        assertFalse(gm.currentPlayer().isDisconnectedBeforeLeaderCard());
        assertTrue(gm.currentPlayer().isDisconnectedBeforeResource());
        assertTrue(g.isResourceObjectEnabled());
        assertEquals(1, g.getResourceObject().getNumOfResources());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_LeaderCardsObject).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_ResourceObject).count());
        assertEquals(4, c.messages.size());
    }

    @Test
    public void solo() {
        gm = new GameManager(1);
        am = gm.getActionManager();
        g = gm.getGame();

        c = new Catcher();
        g.addPlayer("Unico", 1);
        gm.addAllObserver(c);
        p = g.getPlayer(1);

        MSG_INIT_CHOOSE_LEADERCARDS message = new MSG_INIT_CHOOSE_LEADERCARDS(0, 1);
        List<LeaderCard> list = p.getStartingCards();
        g.setLeaderCardsObjectCards(list);
        g.setLeaderCardsObjectEnabled(true);
        assertTrue(g.isLeaderCardsObjectEnabled());
        g.changeStatus(Status.INIT_1);
        c.emptyQueue();

        assertTrue(am.chooseLeaderCard(p, message));

        assertEquals(list.get(0), p.getLeaderCards()[0]);
        assertEquals(list.get(1), p.getLeaderCards()[1]);
        assertFalse(g.isLeaderCardsObjectEnabled());
        assertSame(Status.STANDARD_TURN, g.getStatus());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_LeaderCardsObject).count());
        assertEquals(3, c.messages.size());
    }
}
