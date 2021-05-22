package it.polimi.ingsw.actionsTest;

import it.polimi.ingsw.Catcher;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.controller.ActionManager;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enumerators.Status;
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
//          IF NOT : well. It doesn't go in endgame().
// SOLO mode:
//     IF status is GAME OVER (silently modified by the actionManager) => notify leaderBoard (1 MSG_UPD_Leaderboard)
//     OTHERWISE Lorenzo must play. He can modify the DevDeck, its position in Game, (#? Messages) and surely the ActionTokenStack (which does not generate any message)
//         After the action, the status could be GAME OVER. in THIS case, load the LeaderboardSimplified ( 1 MSG_UPD_LeaderBoard)
//             IF status is not GAME OVER, that's it. Continue the game as always.
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
        p = g.getPlayer(1);
        c.emptyQueue();

        gm2 = new GameManager(1);
        am2 = gm2.getActionManager();
        g2 = gm2.getGame();
        c2 = new Catcher();
        g2.addPlayer("Unico", 1);
        gm2.addAllObserver(c2);
        p2 = g2.getPlayer(1);
        c2.emptyQueue();
    }

    @Test
    //this test ensures that the next player is set correctly.
    public void endTurnTest0() {
        g.changeStatus(Status.STANDARD_TURN);
        assertTrue(am.endTurn(p, true));
        assertEquals(g.getCurrentPlayerInt(), 2);
        assertEquals(2, gm.currentPlayer().getPlayerNumber());
        //just the currentPlayer should change
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Game).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(2, c.messages.size());
    }

    @Test
    //this test ensures that the next player is set correctly if it was the last player's turn.
    public void endTurnTest1() {
        //setting the initial condition and saving variables
        g.changeStatus(Status.STANDARD_TURN);
        g.setCurrentPlayer(g.getCurrentPlayerInt() + 3);
        int turn = g.getTurn();
        c.emptyQueue();

        //both the turn AND the currentPlayer should change
        assertTrue(am.endTurn(p, true));

        //assert Model changes
        assertEquals(g.getTurn(), turn + 1);
        assertEquals(g.getCurrentPlayerInt(), 1);

        //assert correct messages are generated
        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Game).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(3, c.messages.size());
    }

    //test for Last Turn in MultiPlayer (the game should end)
    @Test
    public void endTurnTest2() {
        //setting the initial condition and saving variables
        g.changeStatus(Status.LAST_TURN);
        g.setCurrentPlayer(g.getCurrentPlayerInt() + 3);
        c.emptyQueue();

        //returns false because the LeaderboardSimplified message is the last one.
        assertFalse(am.endTurn(p, true));

        //assert Model changes
        assertTrue(g.getLeaderBoard().isEnabled());

        //assert correct messages are generated
        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Game).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_LeaderBoard).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_Stop).count());
        assertEquals(5, c.messages.size());
    }

    // in the SOLO mode, there is no LAST TURN.
    // when
    // when GAME OVER is set, the leaderboard will be sent, and the thread will terminate.
    @Test
    public void endTurnGameOverPlayerWins() {
        //setting the initial condition and saving variables
        g2.changeStatus(Status.GAME_OVER);
        gm2.setSoloWinner(true); // <- if this is set to true, the game will end

        //returns false because the LeaderboardSimplified message is the last one.
        assertFalse(am2.endTurn(p2, true));

        //assert Model changes
        assertTrue(g2.getLeaderBoard().isEnabled());

        //assert correct messages are generated
        assertEquals(1, c2.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_LeaderBoard).count());
        assertEquals(1, c2.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(1, c2.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_Stop).count());
        assertEquals(3, c2.messages.size());
    }

    // this test should basically not exist.
    // when endTurn() is invoked and Lorenzo triggers his winning condition,
    // GAME OVER status is set and SoloWinner is set to false, thus leading to the endgame().
    // setting GAME OVER status and SoloWinner should be tested as LorenzoMove() testing.
    @Deprecated
    public void endTurnGameOverLorenzoWins() {
        //setting the initial condition and saving variables
        g2.changeStatus(Status.GAME_OVER);
        gm2.setSoloWinner(false);

        //returns false because the LeaderboardSimplified message is the last one.
        assertFalse(am2.endTurn(p2, true));

        //assert correct messages are generated
        assertEquals(1, c2.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_LeaderBoard).count());
        assertEquals(1, c2.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertSame(c2.messages.get(c2.messages.size() - 1).getMessageType(), MessageType.MSG_UPD_LeaderBoard); //<- the LAST message must be MSG_UPD_LeaderBoard
        //assertEquals(1, c2.messages.size()); <- this may change: LorenzoMove() modifies the model.
    }

    @Test
    public void endTurnSoloGameStandard() {
        //setting the initial condition and saving variables
        g2.changeStatus(Status.STANDARD_TURN);
        int turn = g2.getTurn();

        //returns True because it needs a MSG_UPD_end message afterwards
        assertTrue(am2.endTurn(p2, true));

        //assert Model changes
        assertEquals(g2.getTurn(), turn + 1);
    }
}