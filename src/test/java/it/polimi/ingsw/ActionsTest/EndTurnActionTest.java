package it.polimi.ingsw.ActionsTest;
import it.polimi.ingsw.Server.Controller.ActionManager;
import it.polimi.ingsw.Server.Controller.GameManager;
import it.polimi.ingsw.Server.Model.Enumerators.Status;
import it.polimi.ingsw.Server.Model.Game;
import it.polimi.ingsw.Server.Model.Player;
import it.polimi.ingsw.Catcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EndTurnActionTest {

    GameManager gm;
    GameManager gm2;
    Game g;
    Game g2;
    ActionManager am;
    ActionManager am2;
    Player p;
    Player p2;
    Catcher c;
    Catcher c2;

    @BeforeEach
    public void reset()
    {
        gm = new GameManager(4);
        gm2 = new GameManager(1);
        am = gm.getActionManager();
        am2 = gm2.getActionManager();
        g = gm.getGame();
        g2 = gm2.getGame();

        c = new Catcher();
        g.addAllObservers(c);
        g.addPlayer("Primo", 1);
        g.addPlayer("Secondo", 2);
        g.addPlayer("Terzo", 3);
        g.addPlayer("Quarto",4);

        c2 = new Catcher();
        g2.addAllObservers(c2);
        g.addPlayer("Unico", 1);

        p2 = g2.getPlayer(1);

        p = g.getPlayer(1);
    }

    @Test
    //this test ensures that the next player is set correctly.
    public void endTurnTest0() {
        g.changeStatus(Status.STANDARD_TURN);
        assertTrue(am.endTurn(p));
        assertEquals(g.getCurrentPlayerInt(), 2);
    }

    @Test
    //this test ensures that the next player is set correctly if it was the last player's turn.
    public void endTurnTest1() {
        g.changeStatus(Status.STANDARD_TURN);
        g.setCurrentPlayer(g.getCurrentPlayerInt() + 3);
        assertTrue(am.endTurn(p));
        assertEquals(g.getCurrentPlayerInt(), 1);
    }

    @Test
    public void endTurnTest2() {
        g.changeStatus(Status.LAST_TURN);
        g.setCurrentPlayer(g.getCurrentPlayerInt() + 3);
        assertEquals(g.getCurrentPlayerInt(), 4);
        assertFalse(am.endTurn(p));
    }

    @Test
    public void endTurnTest3() {
        g2.changeStatus(Status.GAME_OVER);
        assertFalse(am2.endTurn(p2));
    }

}
