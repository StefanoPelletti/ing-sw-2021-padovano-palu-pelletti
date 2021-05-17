package it.polimi.ingsw.ModelTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.Server.Model.Game;
import it.polimi.ingsw.Server.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}