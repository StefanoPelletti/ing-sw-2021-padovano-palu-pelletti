package it.polimi.ingsw.ActionsTest;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.Catcher;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Controller.ActionManager;
import it.polimi.ingsw.Server.Controller.FaithTrackManager;
import it.polimi.ingsw.Server.Controller.GameManager;
import it.polimi.ingsw.Server.Model.Enumerators.Status;
import it.polimi.ingsw.Server.Model.FaithTrack;
import it.polimi.ingsw.Server.Model.Game;
import it.polimi.ingsw.Server.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FaithManagerTest {
    GameManager gm;
    Game g;
    ActionManager am;
    FaithTrackManager ftm;
    Player p;
    Player p2;
    Catcher c;

    @BeforeEach
    public void reset()
    {
        gm = new GameManager(4);
        am = gm.getActionManager();
        ftm = gm.getFaithTrackManager();
        g = gm.getGame();

        c = new Catcher();

        g.addPlayer("Primo", 1);
        g.addPlayer("Secondo", 2);
        g.addPlayer("Terzo", 3);
        g.addPlayer("Quarto",4);
        g.addAllObservers(c);
        ftm.addObserver(c);
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
        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_FaithTrack).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(4, c.messages.size());
        assertTrue(g.getFaithTrack().getZones()[0]);
        assertFalse(g.getFaithTrack().getZones()[1]);
        assertFalse(g.getFaithTrack().getZones()[2]);
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
        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_FaithTrack).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(4, c.messages.size());
        assertTrue(g.getFaithTrack().getZones()[0]);
        assertTrue(g.getFaithTrack().getZones()[1]);
        assertFalse(g.getFaithTrack().getZones()[2]);
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
        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_FaithTrack).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(4, c.messages.size());
        assertTrue(g.getFaithTrack().getZones()[0]);
        assertTrue(g.getFaithTrack().getZones()[1]);
        assertTrue(g.getFaithTrack().getZones()[2]);
        assertEquals(Status.LAST_TURN, g.getStatus());
    }

    @Test
    //case 3 test SOLO
    public void FaithTrackManagerTest5() {
        GameManager gameManager =  new GameManager(1);
        Game game = gameManager.getGame();
        ActionManager actionManager = gameManager.getActionManager();
        Catcher a = new Catcher();
        game.addPlayer("Primo", 1);
        game.addAllObservers(a);
        gameManager.getFaithTrackManager().addObserver(a);
        Player player = game.getPlayer(1);
        FaithTrackManager ftm1 = gameManager.getFaithTrackManager();

        game.getFaithTrack().setZones(0, true);
        game.getFaithTrack().setZones(1, true);
        player.setPosition(23);
        a.emptyQueue();
        assertTrue(ftm1.advance(player));
        assertEquals(24, player.getPosition());
        assertEquals(2, a.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(1, a.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_FaithTrack).count());
        assertEquals(1, a.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(4, a.messages.size());
        assertEquals(Status.GAME_OVER, game.getStatus());
        assertTrue(gameManager.getSoloWinner());
    }

    @Test
    //case 2 players in the same zone
    public void FaithTrackManagerTest6() {
        p.setPosition(7);
        p2.setPosition(5);
        c.emptyQueue();
        assertTrue(ftm.advance(p));
        assertEquals(8, p.getPosition());
        assertEquals(3, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_FaithTrack).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(5, c.messages.size());
        assertTrue(g.getFaithTrack().getZones()[0]);
        assertFalse(g.getFaithTrack().getZones()[1]);
        assertFalse(g.getFaithTrack().getZones()[2]);
    }
}
