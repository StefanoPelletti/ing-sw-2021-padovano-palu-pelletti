package it.polimi.ingsw.actionsTest;

import it.polimi.ingsw.Catcher;
import it.polimi.ingsw.server.controller.ActionManager;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import org.junit.jupiter.api.BeforeEach;

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


}
