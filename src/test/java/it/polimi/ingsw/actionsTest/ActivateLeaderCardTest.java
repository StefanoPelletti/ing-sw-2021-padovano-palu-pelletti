package it.polimi.ingsw.actionsTest;

import it.polimi.ingsw.Catcher;
import it.polimi.ingsw.networking.message.MSG_ACTION_ACTIVATE_LEADERCARD;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.controller.ActionManager;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.model.DevelopmentCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enumerators.Color;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.model.middles.ReqValue;
import it.polimi.ingsw.server.model.requirements.CardRequirements;
import it.polimi.ingsw.server.model.requirements.ResourceRequirements;
import it.polimi.ingsw.server.model.specialAbilities.DiscountResource;
import it.polimi.ingsw.server.model.specialAbilities.ExtraDepot;
import it.polimi.ingsw.server.model.specialAbilities.Production;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ActivateLeaderCardTest {
    GameManager gm;
    Game g;
    ActionManager am;
    Player p;
    Catcher c;
    LeaderCard l1;
    LeaderCard l2;

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


        l1 = new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SHIELD, 5)),
                new ExtraDepot(Resource.COIN));

        l2 = new LeaderCard(2,
                new CardRequirements(Map.of(Color.YELLOW, new ReqValue(1, -1), Color.GREEN, new ReqValue(1, -1))),
                new DiscountResource(Resource.SERVANT));

        gm.addAllObserver(c);


        ArrayList<LeaderCard> cards = new ArrayList<>();
        cards.add(l1);
        cards.add(l2);


        p = g.getPlayer(1);
        p.associateLeaderCards(cards);
        c.emptyQueue();
    }

    //tests that an error is generated if the player wants to activate a cards without the resource requirements
    @Test
    public void ResourceRequirementError() {
        MSG_ACTION_ACTIVATE_LEADERCARD message = new MSG_ACTION_ACTIVATE_LEADERCARD(1);
        assertFalse(am.activateLeaderCard(p, message));
        assertEquals(MessageType.MSG_ERROR, c.messages.get(0).getMessageType());
        assertEquals(1, c.messages.size());
    }

    //tests that an error is generated if the player wants to activate a cards without the card requirements
    @Test
    public void CardRequirementError() {
        MSG_ACTION_ACTIVATE_LEADERCARD message = new MSG_ACTION_ACTIVATE_LEADERCARD(0);
        DevelopmentCard card1 = new DevelopmentCard(1, Color.GREEN, 0, null, null);
        p.getDevelopmentSlot().addCard(card1, 1);
        c.emptyQueue();
        assertFalse(am.activateLeaderCard(p, message));
        assertEquals(MessageType.MSG_ERROR, c.messages.get(0).getMessageType());
        assertEquals(1, c.messages.size());
    }

    //tests that an error is generated if the player wants to activate an already active leaderCard
    @Test
    public void ActivateEnabledLCError() {
        MSG_ACTION_ACTIVATE_LEADERCARD message = new MSG_ACTION_ACTIVATE_LEADERCARD(0);
        p.getStrongbox().addResource(Resource.SHIELD, 5);
        p.setLeaderCards(0, true);
        c.emptyQueue();
        assertFalse(am.activateLeaderCard(p, message));
        assertEquals(MessageType.MSG_ERROR, c.messages.get(0).getMessageType());
        assertEquals(1, c.messages.size());
    }

    //tests that an error is generated when the player tries to activate a discarded leaderCard
    @Test
    public void discardedCardError() {
        MSG_ACTION_ACTIVATE_LEADERCARD message = new MSG_ACTION_ACTIVATE_LEADERCARD(0);
        p.setLeaderCards(0, false);
        c.emptyQueue();
        assertFalse(am.activateLeaderCard(p, message));
        assertEquals(MessageType.MSG_ERROR, c.messages.get(0).getMessageType());
        assertEquals(1, c.messages.size());
    }

    //Verifies that if the player has the required resources, the leaderCard is activated
    @Test
    public void ResourceRequirement() {
        MSG_ACTION_ACTIVATE_LEADERCARD message = new MSG_ACTION_ACTIVATE_LEADERCARD(0);
        p.getStrongbox().addResource(Resource.SHIELD, 4);
        p.getWarehouseDepot().add(Resource.COIN);
        p.getWarehouseDepot().add(Resource.SHIELD);
        c.emptyQueue();

        assertTrue(am.activateLeaderCard(p, message));

        assertTrue(l1.getEnable());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(2, c.messages.size());
    }

    //Verifies that if the player has the required cards, the leaderCard is activated
    //First type of Card Requirement: Cards of specified colors
    @Test
    public void CardRequirement1() {
        MSG_ACTION_ACTIVATE_LEADERCARD message = new MSG_ACTION_ACTIVATE_LEADERCARD(1);
        DevelopmentCard card1 = new DevelopmentCard(1, Color.YELLOW, 0, null, null);
        DevelopmentCard card2 = new DevelopmentCard(2, Color.GREEN, 0, null, null);
        p.getDevelopmentSlot().addCard(card1, 1);
        p.getDevelopmentSlot().addCard(card2, 1);
        c.emptyQueue();

        assertTrue(am.activateLeaderCard(p, message));

        assertTrue(l2.getEnable());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(2, c.messages.size());

    }

    //Verifies that if the player has the required cards, the leaderCard is activated
    //Second type of Card Requirement: Cards of specified levels
    @Test
    public void CardRequirement2() {
        MSG_ACTION_ACTIVATE_LEADERCARD message = new MSG_ACTION_ACTIVATE_LEADERCARD(1);

        LeaderCard l3 = new LeaderCard(4,
                new CardRequirements(Map.of(Color.YELLOW, new ReqValue(1, 2))),
                new Production(Resource.SHIELD));
        ArrayList<LeaderCard> cards = new ArrayList<>();
        cards.add(l1);
        cards.add(l3);
        p.associateLeaderCards(cards);
        DevelopmentCard card1 = new DevelopmentCard(1, Color.GREEN, 0, null, null);
        DevelopmentCard card2 = new DevelopmentCard(2, Color.YELLOW, 0, null, null);
        p.getDevelopmentSlot().addCard(card1, 1);
        p.getDevelopmentSlot().addCard(card2, 1);
        c.emptyQueue();

        assertTrue(am.activateLeaderCard(p, message));

        assertTrue(l3.getEnable());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(2, c.messages.size());
    }
}