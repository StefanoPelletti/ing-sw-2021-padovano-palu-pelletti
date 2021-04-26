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

// how does the endTurn work ( for T )
// NON-SOLO mode:
//     IF NOT LAST PLAYER => the currentPlayer advance, the call returns TRUE (1 MSG_UPD_Game)
//     IF LAST PLAYER => the currentPlayer advances, the turn advances (2 MSG_UPD_Game)
//          IF (gameOver condition, essentially) changes the status in GameOver and the method returns FALSE (1 MSG_UPD_LeaderBoard)
// SOLO mode:
//     IF status is GAMEOVER (silently modified by the actionmanager) => notify leaderBoard (1 MSG_UPD_Leaderboard)
//     OTHERWISE Lorenzo must play. He can modify the DevDeck, its position in Game, (#? Messages) and surely the ActionTokenStack (which does not generate any message)
//         After the action, the status could be GAMEOVER. in THIS case, load the LeaderBoard ( 1 MSG_UPD_LeaderBoard)
//             IF status is not gameover, that's it. Continue the game as always.
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
