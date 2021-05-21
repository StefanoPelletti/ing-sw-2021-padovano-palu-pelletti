package it.polimi.ingsw.modelTest;

import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enumerators.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    Game game;

    @BeforeEach
    public void reset() {
        game = new Game();
    }

    @Test
    public void addingPlayer() {
        game.addPlayer("Primo", 1);

        Player p = game.getPlayer("Primo");
        assertNotNull(p);
        assertEquals("Primo", p.getNickname());
        assertEquals(1, p.getPlayerNumber());
    }

    @Test
    public void fetchByNumber() {
        game.addPlayer("Primo", 1);

        Player p = game.getPlayer(1);
        assertNotNull(p);
        assertEquals("Primo", p.getNickname());
        assertEquals(1, p.getPlayerNumber());
    }

    @Test
    public void fetchNotPresentPlayer() {
        game.addPlayer("Primo", 1);

        Player p = game.getPlayer("Secondo");
        assertNull(p);
    }

    @Test
    public void fetchSameBehaviour() {
        game.addPlayer("Primo", 1);

        Player p1 = game.getPlayer(1);
        Player p2 = game.getPlayer("Primo");
        assertEquals(p1, p2);
    }

    @Test
    public void changingStatus() {
        game.changeStatus(Status.STANDARD_TURN);
        assertSame(game.getStatus(), Status.STANDARD_TURN);
    }
}