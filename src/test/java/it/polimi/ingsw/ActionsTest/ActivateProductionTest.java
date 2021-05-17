package it.polimi.ingsw.ActionsTest;
import it.polimi.ingsw.Networking.Message.MSG_ACTION_ACTIVATE_PRODUCTION;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Controller.ActionManager;
import it.polimi.ingsw.Server.Controller.GameManager;
import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.Model.Enumerators.Color;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Model.SpecialAbilities.ExtraDepot;
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

    }

    //verifies that a Standard Production is refused if the player has not a card in the given devSlot
    @Test
    public void standardProductionError() {
        MSG_ACTION_ACTIVATE_PRODUCTION message = new MSG_ACTION_ACTIVATE_PRODUCTION(
                new boolean[]{false, true, false},
                false,
                new boolean[]{false, false},
                null,
                null,
                null,
                null);

        assertFalse(am.activateProduction(p, message));

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());
    }


    //verifies that a Leader Production is refused if the player has not a production LeaderCard in position 1
    @Test
    public void leaderProductionError1() {
        MSG_ACTION_ACTIVATE_PRODUCTION message = new MSG_ACTION_ACTIVATE_PRODUCTION(
                new boolean[]{false, false, false},
                false,
                new boolean[]{true, false},
                null,
                null,
                Resource.SERVANT,
                Resource.COIN);

        assertFalse(am.activateProduction(p, message));

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());
    }

    //verifies that a Leader Production is refused if the player has not a production LeaderCard in position 2
    @Test
    public void leaderProductionError2() {
        MSG_ACTION_ACTIVATE_PRODUCTION message = new MSG_ACTION_ACTIVATE_PRODUCTION(
                new boolean[]{false, false, false},
                false,
                new boolean[]{false, true},
                null,
                null,
                null,
                Resource.COIN);

        assertFalse(am.activateProduction(p, message));

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());
    }

    //verifies that a player can not produce a faith or an extra resource with basic power
    @Test
    public void basicWithNotValidRes() {
        ArrayList<Resource> basicInput = new ArrayList<>();
        basicInput.add(Resource.STONE);
        basicInput.add(Resource.COIN);
        MSG_ACTION_ACTIVATE_PRODUCTION message = new MSG_ACTION_ACTIVATE_PRODUCTION(
                new boolean[]{false, false, false},
                true,
                new boolean[]{false, false},
                basicInput,
                Resource.FAITH,
                null,
                null);

        assertFalse(am.activateProduction(p, message));
        System.out.println(g.getErrorObject().getErrorMessage());
        assertEquals(MessageType.MSG_ERROR, c.messages.get(0).getMessageType());
        assertEquals(1, c.messages.size());
        c.emptyQueue();

        MSG_ACTION_ACTIVATE_PRODUCTION message2 = new MSG_ACTION_ACTIVATE_PRODUCTION(
                new boolean[]{false, false, false},
                true,
                new boolean[]{false, false},
                basicInput,
                Resource.EXTRA,
                null,
                null);

        assertFalse(am.activateProduction(p, message2));

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());
    }

    //verifies that the player can not produce a second faith resource with a leader production
    //verifies that a player can not produce a faith or an extra resource with basic power
    @Test
    public void leaderWithNotValidRes() {
        MSG_ACTION_ACTIVATE_PRODUCTION message = new MSG_ACTION_ACTIVATE_PRODUCTION(
                new boolean[]{false, false, false},
                false,
                new boolean[]{true, true},
                null,
                null,
                Resource.FAITH,
                Resource.COIN);

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(new LeaderCard(0, null, new Production(Resource.SERVANT)));

        leaderCards.add(new LeaderCard(0, null, new Production(Resource.COIN)));

        p.associateLeaderCards(leaderCards);
        p.setLeaderCards(1, true);
        p.setLeaderCards(0, true);
        c.emptyQueue();

        assertFalse(am.activateProduction(p, message));

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());

        c.emptyQueue();

        MSG_ACTION_ACTIVATE_PRODUCTION message2 = new MSG_ACTION_ACTIVATE_PRODUCTION(
                new boolean[]{false, false, false},
                false,
                new boolean[]{true, true},
                null,
                null,
                Resource.COIN,
                Resource.FAITH);
        assertFalse(am.activateProduction(p, message2));

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());

        c.emptyQueue();

        MSG_ACTION_ACTIVATE_PRODUCTION message3 = new MSG_ACTION_ACTIVATE_PRODUCTION(
                new boolean[]{false, false, false},
                false,
                new boolean[]{true, true},
                null,
                null,
                Resource.EXTRA,
                Resource.COIN);
        assertFalse(am.activateProduction(p, message3));

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());

        c.emptyQueue();

        MSG_ACTION_ACTIVATE_PRODUCTION message4 = new MSG_ACTION_ACTIVATE_PRODUCTION(
                new boolean[]{false, false, false},
                false,
                new boolean[]{true, true},
                null,
                null,
                Resource.COIN,
                Resource.EXTRA);

        assertFalse(am.activateProduction(p, message4));

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());
    }


    //verifies that an error is generated if the player activates a devCard without enough resources
    @Test
    public void standardProductionRefused() {
        MSG_ACTION_ACTIVATE_PRODUCTION message = new MSG_ACTION_ACTIVATE_PRODUCTION(
                new boolean[]{false, true, false},
                false,
                new boolean[]{false, false},
                null,
                null,
                null,
                null);

        DevelopmentCard card1 = new DevelopmentCard(1, Color.PURPLE, 9,
                Map.of(Resource.SERVANT, 6),
                new Power(Map.of(Resource.STONE, 2),
                        Map.of(Resource.COIN, 3, Resource.FAITH, 2))
        );
        p.getDevelopmentSlot().addCard(card1, 1);
        c.emptyQueue();

        assertFalse(am.activateProduction(p, message));

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());
    }

    //verifies that an error is generated if the player activates a leaderCard without enough resources
    @Test
    public void leaderProductionRefused() {
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
        ArrayList<LeaderCard> cards = new ArrayList<>();
        cards.add(l1);
        cards.add(l2);
        p.associateLeaderCards(cards);
        p.setLeaderCards(0, true);
        p.setLeaderCards(1, true);
        c.emptyQueue();

        assertFalse(am.activateProduction(p, message));

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());
    }


    //verifies that an error is generated if the player activates his basic power without enough resources
    @Test
    public void basicProductionRefused() {
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

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());
    }


    //verifies that a standard production is accepted if the player has enough resources
    //also verifies if the production gives the correct amount of faith points
    //indirectly tests consumeResources() method in ActionManager
    @Test
    public void standardProductionAccepted() {
        MSG_ACTION_ACTIVATE_PRODUCTION message = new MSG_ACTION_ACTIVATE_PRODUCTION(
                new boolean[]{true, false, false},
                false,
                new boolean[]{false, false},
                null,
                null,
                null,
                null);

        DevelopmentCard card1 = new DevelopmentCard(1, Color.PURPLE, 9,
                Map.of(Resource.SERVANT, 6),
                new Power(Map.of(Resource.STONE, 2, Resource.SHIELD, 1),
                        Map.of(Resource.COIN, 2, Resource.SERVANT, 3, Resource.FAITH, 2))
        );
        p.getDevelopmentSlot().addCard(card1, 0);
        p.getWarehouseDepot().add(Resource.STONE);
        p.getWarehouseDepot().swapRow(1, 2);
        p.getWarehouseDepot().add(Resource.STONE);
        p.getStrongbox().addResource(Resource.SHIELD, 2);
        c.emptyQueue();

        assertTrue(am.activateProduction(p, message));

        assertEquals(p.getWarehouseDepot().getTotal(), 0);
        assertEquals(p.getStrongbox().getQuantity(Resource.SERVANT), 3);
        assertEquals(p.getStrongbox().getQuantity(Resource.SHIELD), 1);
        assertEquals(p.getStrongbox().getQuantity(Resource.COIN), 2);
        assertEquals(p.getPosition(), 2);

        assertEquals(2, c.messages.stream().filter(m -> m.getMessageType().equals(MessageType.MSG_UPD_Player)).count());
        assertEquals(3, c.messages.stream().filter(m -> m.getMessageType().equals(MessageType.MSG_UPD_Strongbox)).count());
        assertEquals(2, c.messages.stream().filter(m -> m.getMessageType().equals(MessageType.MSG_UPD_WarehouseDepot)).count());
        assertEquals(3, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(10, c.messages.size());
    }

    //verifies that a basic production is accepted if the player has enough resources
    //indirectly tests consumeResources() method in ActionManager
    @Test
    public void basicProductionAccepted() {
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

        ExtraDepot depot = new ExtraDepot(Resource.STONE);
        depot.addObserver(c);
        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(new LeaderCard(3,
                null,
                depot));

        leaderCards.add(new LeaderCard(0, null, new Production(Resource.COIN)));

        p.associateLeaderCards(leaderCards);
        p.setLeaderCards(0, true);

        ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).addResource(2);
        p.getStrongbox().addResource(Resource.COIN, 1);
        c.emptyQueue();

        assertTrue(am.activateProduction(p, message));

        assertEquals(1, c.messages.stream().filter(m -> m.getMessageType().equals(MessageType.MSG_UPD_Extradepot)).count());
        assertEquals(2, c.messages.stream().filter(m -> m.getMessageType().equals(MessageType.MSG_UPD_Strongbox)).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(4, c.messages.size());
    }

    //verifies that a leader production is accepted if the player has enough resources
    //indirectly tests consumeResources() method in ActionManager
    @Test
    public void leaderProductionAccepted() {
        MSG_ACTION_ACTIVATE_PRODUCTION message = new MSG_ACTION_ACTIVATE_PRODUCTION(
                new boolean[]{false, false, false},
                false,
                new boolean[]{false, true},
                null,
                null,
                null,
                Resource.SHIELD);

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(new LeaderCard(3,
                null,
                new ExtraDepot(Resource.COIN)));

        leaderCards.add(new LeaderCard(0, null, new Production(Resource.COIN)));

        p.associateLeaderCards(leaderCards);
        p.setLeaderCards(1, true);
        p.getWarehouseDepot().add(Resource.COIN);
        c.emptyQueue();

        assertTrue(am.activateProduction(p, message));

        assertEquals(1, p.getPosition());
        assertEquals(0, p.getWarehouseDepot().getTotal());
        assertEquals(1, p.getStrongbox().getQuantity(Resource.SHIELD));
        assertNull(p.getStrongbox().getQuantity(Resource.SERVANT));
        assertNull(p.getStrongbox().getQuantity(Resource.STONE));
        assertNull(p.getStrongbox().getQuantity(Resource.COIN));

        assertEquals(1, c.messages.stream().filter(m -> m.getMessageType().equals(MessageType.MSG_UPD_WarehouseDepot)).count());
        assertEquals(1, c.messages.stream().filter(m -> m.getMessageType().equals(MessageType.MSG_UPD_Strongbox)).count());
        assertEquals(1, c.messages.stream().filter(m -> m.getMessageType().equals(MessageType.MSG_UPD_Player)).count());
        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());

        assertEquals(5, c.messages.size());
    }

    //verifies that if a slot is selected for production, only the top card of that slot is used for the production
    //indirectly tests consumeResources() method
    @Test
    public void standardProductionOnlyTop() {
        MSG_ACTION_ACTIVATE_PRODUCTION message = new MSG_ACTION_ACTIVATE_PRODUCTION(
                new boolean[]{true, false, false},
                false,
                new boolean[]{false, false},
                null,
                null,
                null,
                null);

        DevelopmentCard dCard1 = new DevelopmentCard(1, Color.BLUE, 4,
                Map.of(Resource.COIN, 2, Resource.SERVANT, 2),
                new Power(Map.of(Resource.SHIELD, 1, Resource.STONE, 1),
                        Map.of(Resource.SERVANT, 2, Resource.FAITH, 1)));

        DevelopmentCard dCard2 = new DevelopmentCard(2, Color.PURPLE, 6,
                Map.of(Resource.SERVANT, 3, Resource.COIN, 2),
                new Power(Map.of(Resource.COIN, 1, Resource.SERVANT, 1),
                        Map.of(Resource.SHIELD, 3)));

        p.getDevelopmentSlot().addCard(dCard1, 0);
        assertTrue(p.getDevelopmentSlot().addCard(dCard2, 0));

        p.getStrongbox().addResource(Resource.COIN, 1);
        p.getStrongbox().addResource(Resource.SHIELD, 1);
        p.getStrongbox().addResource(Resource.STONE, 1);
        p.getWarehouseDepot().add(Resource.SERVANT);

        c.emptyQueue();

        assertTrue(am.activateProduction(p, message));

        assertEquals(0, p.getWarehouseDepot().getTotal());
        assertEquals(1, p.getStrongbox().getQuantity(Resource.STONE));
        assertEquals(0, p.getStrongbox().getQuantity(Resource.COIN));
        assertEquals(4, p.getStrongbox().getQuantity(Resource.SHIELD));
        assertNull(p.getStrongbox().getQuantity(Resource.SERVANT));

        assertEquals(1, c.messages.stream().filter(m -> m.getMessageType().equals(MessageType.MSG_UPD_WarehouseDepot)).count());
        assertEquals(2, c.messages.stream().filter(m -> m.getMessageType().equals(MessageType.MSG_UPD_Strongbox)).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(4, c.messages.size());
    }


    //Verifies if multiple productions are accepted
    @Test
    public void productionAccepted() {
        ArrayList<Resource> basicInput = new ArrayList<>();
        basicInput.add(Resource.SERVANT);
        basicInput.add(Resource.SERVANT);
        MSG_ACTION_ACTIVATE_PRODUCTION message = new MSG_ACTION_ACTIVATE_PRODUCTION(
                new boolean[]{true, false, true},
                true,
                new boolean[]{false, true},
                basicInput,
                Resource.SHIELD,
                null,
                Resource.SERVANT);

        DevelopmentCard dCard1 = new DevelopmentCard(1, Color.BLUE, 10,
                null,
                new Power(Map.of(Resource.COIN, 1, Resource.SHIELD, 1),
                        Map.of(Resource.SERVANT, 2, Resource.STONE, 2, Resource.FAITH, 1)));

        DevelopmentCard dCard2 = new DevelopmentCard(1, Color.YELLOW, 3,
                null,
                new Power(Map.of(Resource.SHIELD, 2),
                        Map.of(Resource.COIN, 1, Resource.SERVANT, 1, Resource.STONE, 1))
        );

        p.getDevelopmentSlot().addCard(dCard1, 0);
        p.getDevelopmentSlot().addCard(dCard2, 2);

        ExtraDepot extraDepot = new ExtraDepot(Resource.COIN);
        extraDepot.addObserver(c);
        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add(new LeaderCard(3,
                null,
                extraDepot));

        leaderCards.add(new LeaderCard(0, null, new Production(Resource.COIN)));

        p.associateLeaderCards(leaderCards);
        p.setLeaderCards(1, true);
        p.setLeaderCards(0, true);

        p.getWarehouseDepot().add(Resource.SERVANT);
        p.getWarehouseDepot().add(Resource.SHIELD);
        p.getWarehouseDepot().add(Resource.SHIELD);
        p.getWarehouseDepot().add(Resource.COIN);
        ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).addResource(1);
        p.getStrongbox().addResource(Resource.SERVANT, 1);
        p.getStrongbox().addResource(Resource.SHIELD, 1);

        c.emptyQueue();

        assertTrue(am.activateProduction(p, message));

        assertEquals(0, p.getWarehouseDepot().getTotal());
        assertEquals(0, ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).getNumber());
        assertEquals(4, p.getStrongbox().getQuantity(Resource.SERVANT));
        assertEquals(1, p.getStrongbox().getQuantity(Resource.SHIELD));
        assertEquals(3, p.getStrongbox().getQuantity(Resource.STONE));
        assertEquals(1, p.getStrongbox().getQuantity(Resource.COIN));
        assertEquals(2, p.getPosition());

        assertEquals(4, c.messages.stream().filter(m -> m.getMessageType().equals(MessageType.MSG_UPD_WarehouseDepot)).count());
        assertEquals(6, c.messages.stream().filter(m -> m.getMessageType().equals(MessageType.MSG_UPD_Strongbox)).count());
        assertEquals(1, c.messages.stream().filter(m -> m.getMessageType().equals(MessageType.MSG_UPD_Extradepot)).count());
        assertEquals(2, c.messages.stream().filter(m -> m.getMessageType().equals(MessageType.MSG_UPD_Player)).count());
        assertEquals(3, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());

        assertEquals(16, c.messages.size());
    }
}