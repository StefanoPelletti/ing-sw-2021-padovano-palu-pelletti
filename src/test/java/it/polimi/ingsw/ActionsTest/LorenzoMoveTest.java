package it.polimi.ingsw.ActionsTest;

import it.polimi.ingsw.Catcher;
import it.polimi.ingsw.Networking.Message.MSG_NOTIFICATION;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Controller.ActionManager;
import it.polimi.ingsw.Server.Controller.GameManager;
import it.polimi.ingsw.Server.Model.DevelopmentCard;
import it.polimi.ingsw.Server.Model.Game;
import it.polimi.ingsw.Server.Model.Player;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

public class LorenzoMoveTest {

    GameManager gm;
    Game g;
    ActionManager am;
    Player p;
    Catcher c;

    @BeforeEach
    public void reset()
    {
        gm = new GameManager(1);
        am = gm.getActionManager();
        g = gm.getGame();

        c = new Catcher();
        g.addPlayer("Giacomo Poretti", 1);
        gm.addAllObserver(c);
        c.emptyQueue();
        p = g.getPlayer(1);
    }

    //given the randomness of the actionTokenTest sorting,
    // this test cycles on the possible things that could happen.
    @Test
    public void allTest()
    {

        boolean r = false;
        boolean ff = false;
        boolean f2 = false;

        assertEquals(1, g.getBlackCrossPosition());
        for( int i=0; i<3; i++) {
            for(int j=0; j<4; j++) {
                for(DevelopmentCard d : g.getDevelopmentCardsDeck().getStack(i,j))
                    assertNotNull(d);
            }
        }

        while(!r || !ff || !f2)
        {
            reset();

            assertTrue(am.endTurn(p, true)); //at the beginning of the game this should always return true.
            String msg = "";
            Optional<String> m = c.messages.stream()
                    .filter(x -> x.getMessageType()== MessageType.MSG_NOTIFICATION)
                    .map(x -> (MSG_NOTIFICATION) x)
                    .map(MSG_NOTIFICATION::getMessage)
                    .filter(message -> message.startsWith("Lorenzo"))
                    .findFirst();
            if(m.isPresent()) msg = m.get();
            if(msg.startsWith("Lorenzo gained two")) //Forward2Case
            {
                ff=true;
                assertEquals(3, g.getBlackCrossPosition());
                //one turn update, two blackCrossPosition update. One notification
                assertEquals(3, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Game).count());
                assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
                assertEquals(5, c.messages.size());
            }
            if(msg.startsWith("Lorenzo gained one")) //ForwardAndShuffle
            {
                f2=true;
                assertEquals(2, g.getBlackCrossPosition());
                //one turn update, one blackCrossPosition update.
                assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Game).count());
                assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
                assertEquals(4, c.messages.size());
                assertTrue(true); //I solemnly trust Collections.Shuffle().
            }
            if(msg.startsWith("Lorenzo destroyed two"))  //remover
            {
                r=true;
                assertEquals(1, g.getBlackCrossPosition());
                //we get ONE updates from DevDeck. The remove method is already tested, so that should be enough.
                //one turn update, one ^, one notification
                assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Game).count());
                assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_DevDeck).count());
                assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
                assertEquals(4, c.messages.size());
            }

        }
    }

    @Test
    public void LorenzoActivatesTheFirstZone()
    {
        boolean stop = false;
        do {
            g.setBlackCrossPosition(7);
            c.emptyQueue();
            assertTrue(am.endTurn(p, true)); //at the beginning of the game this should always return true.
            String msg = "";
            Optional<String> m = c.messages.stream()
                    .filter(x -> x.getMessageType()== MessageType.MSG_NOTIFICATION)
                    .map(x -> (MSG_NOTIFICATION) x)
                    .map(MSG_NOTIFICATION::getMessage)
                    .filter(message -> message.startsWith("Lorenzo"))
                    .findFirst();
            if(m.isPresent()) msg = m.get();
            if(msg.startsWith("Lorenzo gained two"))
                stop = true;
            else
                reset();
        } while(!stop);

        assertEquals(9, g.getBlackCrossPosition());
        assertEquals(3, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Game).count());
        assertEquals(3, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_FaithTrack).count());
        assertEquals(7, c.messages.size());
    }

    @Test
    public void LorenzoEndsTheGameWithTheFaithTrack()
    {
        boolean stop = false;
        do {
            g.setBlackCrossPosition(23);
            c.emptyQueue();
            am.endTurn(p, true);
            String msg = "";
            Optional<String> m = c.messages.stream()
                    .filter(x -> x.getMessageType()== MessageType.MSG_NOTIFICATION)
                    .map(x -> (MSG_NOTIFICATION) x)
                    .map(MSG_NOTIFICATION::getMessage)
                    .filter(message -> message.startsWith("Lorenzo"))
                    .findFirst();
            if(m.isPresent()) msg = m.get();
            if(msg.startsWith("Lorenzo gained two"))
                stop = true;
            else
                reset();
        } while(!stop);

        assertEquals(24, g.getBlackCrossPosition());
        //one from the BlackCross, one notification, one faithTrack, and the leaderboard
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Game).count());
        assertEquals(3, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_FaithTrack).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_LeaderBoard).count());
        assertEquals(6, c.messages.size());
    }
    @Test
    public void LorenzoEndsTheGameByRemovingCards()
    {
        boolean stop = false;
        do {
            for(int c=0; c<4; c++) {
                for(int n=0; n<5; n++) {
                    g.getDevelopmentCardsDeck().removeCard(c); //leaves 2 cards in all columns
                }
            }

            c.emptyQueue();
            am.endTurn(p, true);
            String msg = "";
            Optional<String> m = c.messages.stream()
                    .filter(x -> x.getMessageType()== MessageType.MSG_NOTIFICATION)
                    .map(x -> (MSG_NOTIFICATION) x)
                    .map(MSG_NOTIFICATION::getMessage)
                    .filter(message -> message.startsWith("Lorenzo"))
                    .findFirst();
            if(m.isPresent()) msg = m.get();
            if(msg.startsWith("Lorenzo d"))
                stop = true;
            else
                reset();
        } while(!stop);

        assertTrue(g.getDevelopmentCardsDeck().isOneColumnDestroyed());
        //one from the devDeck, one notification, and the leaderboard
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_DevDeck).count());
        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_LeaderBoard).count());
        assertEquals(4, c.messages.size());
    }
}
