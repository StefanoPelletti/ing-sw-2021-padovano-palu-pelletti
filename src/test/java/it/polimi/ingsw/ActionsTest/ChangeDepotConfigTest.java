package it.polimi.ingsw.ActionsTest;

import it.polimi.ingsw.Catcher;
import it.polimi.ingsw.Server.Controller.ActionManager;
import it.polimi.ingsw.Server.Controller.GameManager;
import it.polimi.ingsw.Server.Model.Game;
import it.polimi.ingsw.Server.Model.Player;
import org.junit.jupiter.api.BeforeEach;

import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class ChangeDepotConfigTest {
    GameManager gm;
    Game g;
    ActionManager am;
    Player p;
    Catcher c;

    @BeforeEach
    public void reset()
    {
        gm = new GameManager(4);
        am = gm.getActionManager();
        g = gm.getGame();

        c = new Catcher();
        g.addPlayer("Primo", 1);
        g.addPlayer("Secondo", 2);
        g.addPlayer("Terzo", 3);
        g.addPlayer("Quarto",4);
        g.addAllObservers(c);

        gm.getFaithTrackManager().addObserver(c);
        c.emptyQueue();
        p = g.getPlayer(1);
    }
}
