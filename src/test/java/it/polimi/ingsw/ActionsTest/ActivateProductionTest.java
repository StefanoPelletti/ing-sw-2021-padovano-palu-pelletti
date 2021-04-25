package it.polimi.ingsw.ActionsTest;
import it.polimi.ingsw.Server.Controller.ActionManager;
import it.polimi.ingsw.Server.Controller.GameManager;
import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.Model.Enumerators.Color;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Model.Requirements.CardRequirements;
import it.polimi.ingsw.Server.Model.Requirements.ResourceRequirements;
import it.polimi.ingsw.Server.Model.SpecialAbilities.DiscountResource;
import it.polimi.ingsw.Server.Model.SpecialAbilities.ExtraDepot;
import it.polimi.ingsw.Server.Utils.Catcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ActivateProductionTest {
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
        g.addAllObservers(c);

        g.addPlayer("Primo", 1);
        g.addPlayer("Secondo", 2);
        g.addPlayer("Terzo", 3);
        g.addPlayer("Quarto",4);

        p = g.getPlayer(1);
        c.emptyQueue();
        DevelopmentCard card = new DevelopmentCard(2, Color.GREEN, 6,
                new HashMap<Resource,Integer>() {{ put(Resource.SHIELD, 3); put(Resource.SERVANT, 2); }} ,
                new Power(new HashMap<Resource, Integer>() {{ put(Resource.SHIELD, 1); put(Resource.SERVANT, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.STONE, 3); }}));


    }

    @Test
    public void test(){

    }
}
