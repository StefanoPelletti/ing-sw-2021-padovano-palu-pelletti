package it.polimi.ingsw.actionsTest;

import it.polimi.ingsw.Catcher;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.controller.ActionManager;
import it.polimi.ingsw.server.controller.FaithTrackManager;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enumerators.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FaithManagerTest {
    GameManager gm;
    Game g;
    ActionManager am;
    FaithTrackManager ftm;
    Player p;
    Player p2;
    Catcher c;

    @BeforeEach
    public void reset() {
        gm = new GameManager(4);
        am = gm.getActionManager();
        ftm = gm.getFaithTrackManager();
        g = gm.getGame();

        c = new Catcher();

        g.addPlayer("Primo", 1);
        g.addPlayer("Secondo", 2);
        g.addPlayer("Terzo", 3);
        g.addPlayer("Quarto", 4);
        gm.addAllObserver(c);
        p = g.getPlayer(1);
        p2 = g.getPlayer(2);
        c.emptyQueue();
    }

    @Test
    //case -1 test
    public void FaithTrackManagerTest0() {
        p.setPosition(24);
        c.emptyQueue();

        assertFalse(ftm.advance(p));

        assertEquals(0, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
    }

    @Test
    //case 0 test
    public void FaithTrackManagerTest1() {
        assertTrue(ftm.advance(p));

        assertEquals(1, p.getPosition());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
    }

    @Test
    //case 1 test
    public void FaithTrackManagerTest2() {
        p.setPosition(7);
        c.emptyQueue();

        assertTrue(ftm.advance(p));

        assertEquals(8, p.getPosition());
        assertTrue(g.getFaithTrack().getZones()[0]);
        assertFalse(g.getFaithTrack().getZones()[1]);
        assertFalse(g.getFaithTrack().getZones()[2]);

        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_FaithTrack).count());
        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(5, c.messages.size());
    }

    @Test
    //case 2 test
    public void FaithTrackManagerTest3() {
        p.setPosition(7);
        c.emptyQueue();
        assertTrue(ftm.advance(p));
        p.setPosition(15);
        c.emptyQueue();

        assertTrue(ftm.advance(p));

        assertEquals(16, p.getPosition());
        assertTrue(g.getFaithTrack().getZones()[0]);
        assertTrue(g.getFaithTrack().getZones()[1]);
        assertFalse(g.getFaithTrack().getZones()[2]);

        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_FaithTrack).count());
        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(5, c.messages.size());
    }

    @Test
    //case 3 test not SOLO
    public void FaithTrackManagerTest4() {
        g.getFaithTrack().setZones(0, true);
        g.getFaithTrack().setZones(1, true);
        p.setPosition(23);
        c.emptyQueue();

        assertTrue(ftm.advance(p));

        assertEquals(24, p.getPosition());
        assertTrue(g.getFaithTrack().getZones()[0]);
        assertTrue(g.getFaithTrack().getZones()[1]);
        assertTrue(g.getFaithTrack().getZones()[2]);
        assertEquals(Status.LAST_TURN, g.getStatus());

        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_FaithTrack).count());
        assertEquals(3, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(6, c.messages.size());
    }

    @Test
    //case 3 test SOLO
    public void FaithTrackManagerTest5() {
        GameManager gameManager = new GameManager(1);
        Game game = gameManager.getGame();
        Catcher a = new Catcher();
        game.addPlayer("Primo", 1);
        gameManager.addAllObserver(a);
        Player player = game.getPlayer(1);
        FaithTrackManager ftm1 = gameManager.getFaithTrackManager();

        game.getFaithTrack().setZones(0, true);
        game.getFaithTrack().setZones(1, true);
        player.setPosition(23);
        a.emptyQueue();

        assertTrue(ftm1.advance(player));

        assertEquals(24, player.getPosition());
        assertEquals(Status.GAME_OVER, game.getStatus());
        assertTrue(gameManager.getSoloWinner());

        assertEquals(2, a.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(1, a.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_FaithTrack).count());
        assertEquals(2, a.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(5, a.messages.size());
    }

    @Test
    //case 2 players in the same zone
    public void FaithTrackManagerTest6() {
        p.setPosition(7);
        p2.setPosition(5);
        c.emptyQueue();

        assertTrue(ftm.advance(p));

        assertEquals(8, p.getPosition());
        assertTrue(g.getFaithTrack().getZones()[0]);
        assertFalse(g.getFaithTrack().getZones()[1]);
        assertFalse(g.getFaithTrack().getZones()[2]);

        assertEquals(3, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_FaithTrack).count());
        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(6, c.messages.size());
    }

    @Test
    //case 2 test SOLO
    public void FaithTrackManagerTest7() {
        GameManager gameManager = new GameManager(1);
        Game game = gameManager.getGame();
        Catcher a = new Catcher();
        game.addPlayer("Primo", 1);
        gameManager.addAllObserver(a);
        Player player = game.getPlayer(1);
        FaithTrackManager ftm1 = gameManager.getFaithTrackManager();

        game.getFaithTrack().setZones(0, true);
        game.setBlackCrossPosition(15);
        a.emptyQueue();

        assertTrue(ftm1.advanceLorenzo());
        assertEquals(16, game.getBlackCrossPosition());
        assertTrue(game.getFaithTrack().getZones()[1]);

        assertEquals(1, a.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_FaithTrack).count());
        assertEquals(1, a.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Game).count());
        assertEquals(2, a.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(4, a.messages.size());
    }
}