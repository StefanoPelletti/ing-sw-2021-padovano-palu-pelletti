package it.polimi.ingsw.ActionsTest;
import it.polimi.ingsw.Networking.Message.MSG_ACTION_ACTIVATE_PRODUCTION;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Controller.ActionManager;
import it.polimi.ingsw.Server.Controller.GameManager;
import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.Model.Enumerators.Color;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Model.SpecialAbilities.Production;
import it.polimi.ingsw.Catcher;
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

        g.addPlayer("Primo", 1);
        g.addPlayer("Secondo", 2);
        g.addPlayer("Terzo", 3);
        g.addPlayer("Quarto",4);
        g.addAllObservers(c);

        p = g.getPlayer(1);
        c.emptyQueue();




    }

    //verifies that a Standard Production is refused if the player has not a card in the given devSlot
    @Test
    public void standardProductionError(){
        MSG_ACTION_ACTIVATE_PRODUCTION message = new MSG_ACTION_ACTIVATE_PRODUCTION(
                new boolean[]{false, true, false},
                false,
                new boolean[]{false, false},
                null,
                null,
                null,
                null);

        assertFalse(am.activateProduction(p, message));
        System.out.println(g.getErrorObject().getErrorMessage());
        assertEquals(MessageType.MSG_ERROR, c.messages.get(0).getMessageType());
        assertEquals(1, c.messages.size());
    }


    //verifies that a Leader Production is refused if the player has not a production LeaderCard in position 1
    @Test
    public void leaderProductionError1(){
        MSG_ACTION_ACTIVATE_PRODUCTION message = new MSG_ACTION_ACTIVATE_PRODUCTION(
                new boolean[]{false, false, false},
                false,
                new boolean[]{true, false},
                null,
                null,
                Resource.SERVANT,
                Resource.COIN);

        assertFalse(am.activateProduction(p, message));
        System.out.println(g.getErrorObject().getErrorMessage());
        assertEquals(MessageType.MSG_ERROR, c.messages.get(0).getMessageType());
        assertEquals(1, c.messages.size());
    }

    //verifies that a Leader Production is refused if the player has not a production LeaderCard in position 2
    @Test
    public void leaderProductionError2(){
        MSG_ACTION_ACTIVATE_PRODUCTION message = new MSG_ACTION_ACTIVATE_PRODUCTION(
                new boolean[]{false, false, false},
                false,
                new boolean[]{false, true},
                null,
                null,
                null,
                Resource.COIN);

        assertFalse(am.activateProduction(p, message));
        System.out.println(g.getErrorObject().getErrorMessage());
        assertEquals(MessageType.MSG_ERROR, c.messages.get(0).getMessageType());
        assertEquals(1, c.messages.size());
    }

    //verifies that an error is generated if the player activates a devCard without enough resources
    @Test
    public void standardProductionRefused(){
        MSG_ACTION_ACTIVATE_PRODUCTION message = new MSG_ACTION_ACTIVATE_PRODUCTION(
                new boolean[]{false, true, false},
                false,
                new boolean[]{false, false},
                null,
                null,
                null,
                null);

        DevelopmentCard card1 = new DevelopmentCard(1, Color.GREEN, 6,
                null,
                new Power(new HashMap<Resource, Integer>() {{ put(Resource.SHIELD, 1); put(Resource.SERVANT, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.STONE, 3); }}));
        assertTrue(p.getDevelopmentSlot().addCard(card1, 1));
        c.emptyQueue();

        assertFalse(am.activateProduction(p, message));
        System.out.println(g.getErrorObject().getErrorMessage());
        assertEquals(MessageType.MSG_ERROR, c.messages.get(0).getMessageType());
        assertEquals(1, c.messages.size());
    }

    //verifies that an error is generated if the player activates a leaderCard without enough resources
    @Test
    public void leaderProductionRefused(){
        MSG_ACTION_ACTIVATE_PRODUCTION message = new MSG_ACTION_ACTIVATE_PRODUCTION(
                new boolean[]{false, false, false},
                false,
                new boolean[]{true, true},
                null,
                null,
                Resource.SERVANT,
                Resource.SHIELD);

        LeaderCard l1 = new LeaderCard(4,
                null, new Production(Resource.SHIELD));

        LeaderCard l2 = new LeaderCard(4,
                null, new Production(Resource.SERVANT));
        ArrayList<LeaderCard> cards= new ArrayList<>();
        cards.add(l1);
        cards.add(l2);
        p.associateLeaderCards(cards);
        p.setLeaderCards(0, true);
        p.setLeaderCards(1, true);
        c.emptyQueue();

        assertFalse(am.activateProduction(p, message));
        System.out.println(g.getErrorObject().getErrorMessage());
        assertEquals(MessageType.MSG_ERROR, c.messages.get(0).getMessageType());
        assertEquals(1, c.messages.size());
    }


    //verifies that an error is generated if the player activates his basic power without enough resources
    @Test
    public void basicProductionRefused(){
        ArrayList<Resource> basicInput = new ArrayList<>();
        basicInput.add(Resource.COIN);
        basicInput.add(Resource.STONE);
        MSG_ACTION_ACTIVATE_PRODUCTION message = new MSG_ACTION_ACTIVATE_PRODUCTION(
                new boolean[]{false, false, false},
                true,
                new boolean[]{false, false},
                basicInput,
                Resource.SHIELD,
                null,
                null);

        assertFalse(am.activateProduction(p, message));
        System.out.println(g.getErrorObject().getErrorMessage());
        assertEquals(MessageType.MSG_ERROR, c.messages.get(0).getMessageType());
        assertEquals(1, c.messages.size());
    }


}