package it.polimi.ingsw.actionsTest;

import it.polimi.ingsw.Catcher;
import it.polimi.ingsw.networking.message.actionMessages.MSG_ACTION_CHANGE_DEPOT_CONFIG;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.controller.ActionManager;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enumerators.Color;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.model.requirements.ReqValue;
import it.polimi.ingsw.server.model.requirements.CardRequirements;
import it.polimi.ingsw.server.model.requirements.ResourceRequirements;
import it.polimi.ingsw.server.model.specialAbilities.ExtraDepot;
import it.polimi.ingsw.server.model.specialAbilities.MarketResources;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ChangeDepotConfigTest {
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

    //shelf 1       : coin
    //shelf 2       : none none
    //shelf 3       : servant servant servant
    //extradepot 1  : 1 coin
    //extradepot 2  : 2 shield
    //IN
    //shelf 1       : none
    //shelf 2       : shield shield
    //shelf 3       : servant servant servant
    //extradepot 1  : 2 coin
    //extradepot 2  : 0 shield
    @Test
    public void validConfig1() {
        ArrayList<LeaderCard> cards = new ArrayList<>();
        cards.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SHIELD, 5)),
                new ExtraDepot(Resource.COIN)));
        cards.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SERVANT, 5)),
                new ExtraDepot(Resource.SHIELD)));
        p.associateLeaderCards(cards);
        p.getLeaderCards()[0].setEnabled(true);
        p.getLeaderCards()[1].setEnabled(true);
        ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).addObserver(c);
        ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).addObserver(c);
        Resource r0 = Resource.COIN;
        Resource[] r1 = new Resource[]{Resource.NONE, Resource.NONE};
        Resource[] r2 = new Resource[]{Resource.SERVANT, Resource.SERVANT, Resource.SERVANT};
        ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).setResource(1);
        ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).setResource(2);
        p.getWarehouseDepot().setConfig(r0, r1, r2);
        c.emptyQueue();

//assert Warehouse and Extradepot
        assertSame(p.getWarehouseDepot().getShelf1(), Resource.COIN);
        assertSame(p.getWarehouseDepot().getShelf2()[0], Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf2()[1], Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf3()[0], Resource.SERVANT);
        assertSame(p.getWarehouseDepot().getShelf3()[1], Resource.SERVANT);
        assertSame(p.getWarehouseDepot().getShelf3()[2], Resource.SERVANT);
        assertEquals(1, ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).getNumber());
        assertEquals(2, ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).getNumber());

        Resource[] r3 = new Resource[]{Resource.SHIELD, Resource.SHIELD};
        Resource[] r4 = new Resource[]{Resource.SERVANT, Resource.SERVANT, Resource.SERVANT};
        MSG_ACTION_CHANGE_DEPOT_CONFIG msg = new MSG_ACTION_CHANGE_DEPOT_CONFIG(Resource.NONE, r3, r4, 2, 0);
        assertTrue(am.changeDepotConfig(p, msg));

//assert new config
        assertSame(p.getWarehouseDepot().getShelf1(), Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf2()[0], Resource.SHIELD);
        assertSame(p.getWarehouseDepot().getShelf2()[1], Resource.SHIELD);
        assertSame(p.getWarehouseDepot().getShelf3()[0], Resource.SERVANT);
        assertSame(p.getWarehouseDepot().getShelf3()[1], Resource.SERVANT);
        assertSame(p.getWarehouseDepot().getShelf3()[2], Resource.SERVANT);
        assertEquals(2, ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).getNumber());
        assertEquals(0, ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).getNumber());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_WarehouseDepot).count());
        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Extradepot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(4, c.messages.size());

    }

    //shelf 1       : shield
    //shelf 2       : coin coin
    //shelf 3       : servant servant none
    //extradepot 1  : 0 coin
    //extradepot 2  : 1 shield
    //IN
    //shelf 1       : none
    //shelf 2       : servant servant
    //shelf 3       : none none none
    //extradepot 1  : 2 coin
    //extradepot 2  : 2 shield
    @Test
    public void validConfig2() {
        ArrayList<LeaderCard> cards = new ArrayList<>();
        cards.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SHIELD, 5)),
                new ExtraDepot(Resource.COIN)));
        cards.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SERVANT, 5)),
                new ExtraDepot(Resource.SHIELD)));
        p.associateLeaderCards(cards);
        p.getLeaderCards()[0].setEnabled(true);
        p.getLeaderCards()[1].setEnabled(true);
        ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).addObserver(c);
        ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).addObserver(c);
        Resource r0 = Resource.SHIELD;
        Resource[] r1 = new Resource[]{Resource.COIN, Resource.COIN};
        Resource[] r2 = new Resource[]{Resource.SERVANT, Resource.NONE, Resource.SERVANT};
        ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).setResource(0);
        ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).setResource(1);
        p.getWarehouseDepot().setConfig(r0, r1, r2);
        c.emptyQueue();
//build new config

        Resource r3 = Resource.NONE;
        Resource[] r4 = new Resource[]{Resource.SERVANT, Resource.SERVANT};
        Resource[] r5 = new Resource[]{Resource.NONE, Resource.NONE, Resource.NONE};
        MSG_ACTION_CHANGE_DEPOT_CONFIG msg = new MSG_ACTION_CHANGE_DEPOT_CONFIG(r3, r4, r5, 2, 2);
        assertTrue(am.changeDepotConfig(p, msg));

//assert new config
        assertSame(p.getWarehouseDepot().getShelf1(), Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf2()[0], Resource.SERVANT);
        assertSame(p.getWarehouseDepot().getShelf2()[1], Resource.SERVANT);
        assertSame(p.getWarehouseDepot().getShelf3()[0], Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf3()[1], Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf3()[2], Resource.NONE);
        assertEquals(2, ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).getNumber());
        assertEquals(2, ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).getNumber());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_WarehouseDepot).count());
        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Extradepot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(4, c.messages.size());

    }

    //shelf 1       : none
    //shelf 2       : serv serv
    //shelf 3       : none none coin
    //extradepot 1  : X
    //extradepot 2  : 2 shield
    //IN
    //shelf 1       : coin
    //shelf 2       :  shield shield
    //shelf 3       : serv none serv
    //extradepot 1  : X
    //extradepot 2  : 0 shield
    @Test
    public void validConfig3() {
        ArrayList<LeaderCard> cards = new ArrayList<>();
        cards.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SHIELD, 5)),
                new ExtraDepot(Resource.COIN)));
        cards.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SERVANT, 5)),
                new ExtraDepot(Resource.SHIELD)));
        p.associateLeaderCards(cards);
        //p.getLeaderCards()[0].setEnable(true);
        p.getLeaderCards()[1].setEnabled(true);
        ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).addObserver(c);
        ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).addObserver(c);
        Resource r0 = Resource.NONE;
        Resource[] r1 = new Resource[]{Resource.SERVANT, Resource.SERVANT};
        Resource[] r2 = new Resource[]{Resource.NONE, Resource.NONE, Resource.COIN};
        ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).setResource(2);
        p.getWarehouseDepot().setConfig(r0, r1, r2);
        c.emptyQueue();
//build new config

        Resource r3 = Resource.COIN;
        Resource[] r4 = new Resource[]{Resource.SHIELD, Resource.SHIELD};
        Resource[] r5 = new Resource[]{Resource.SERVANT, Resource.NONE, Resource.SERVANT};
        MSG_ACTION_CHANGE_DEPOT_CONFIG msg = new MSG_ACTION_CHANGE_DEPOT_CONFIG(r3, r4, r5, -1, 0);
        assertTrue(am.changeDepotConfig(p, msg));

//assert new config
        assertSame(p.getWarehouseDepot().getShelf1(), Resource.COIN);
        assertSame(p.getWarehouseDepot().getShelf2()[0], Resource.SHIELD);
        assertSame(p.getWarehouseDepot().getShelf2()[1], Resource.SHIELD);
        assertSame(p.getWarehouseDepot().getShelf3()[0], Resource.SERVANT);
        assertSame(p.getWarehouseDepot().getShelf3()[1], Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf3()[2], Resource.SERVANT);
        assertFalse(p.getLeaderCards()[0].isEnabled());
        assertEquals(0, ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).getNumber());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_WarehouseDepot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Extradepot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(3, c.messages.size());

    }

    //shelf 1       : coin
    //shelf 2       : shield shield
    //shelf 3       : serv serv coin
    //extradepot 1  : 0 coin
    //extradepot 2  : 0 shield
    //IN
    //shelf 1       : coin
    //shelf 2       : serv serv
    //shelf 3       : none none none
    //extradepot 1  : 0 coin
    //extradepot 2  : 2 shield
    @Test
    public void validConfig4() {
        ArrayList<LeaderCard> cards = new ArrayList<>();
        cards.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SHIELD, 5)),
                new ExtraDepot(Resource.COIN)));
        cards.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SERVANT, 5)),
                new ExtraDepot(Resource.SHIELD)));
        p.associateLeaderCards(cards);
        p.getLeaderCards()[0].setEnabled(true);
        p.getLeaderCards()[1].setEnabled(true);
        ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).addObserver(c);
        ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).addObserver(c);
        Resource r0 = Resource.COIN;
        Resource[] r1 = new Resource[]{Resource.SHIELD, Resource.SHIELD};
        Resource[] r2 = new Resource[]{Resource.SERVANT, Resource.SERVANT, Resource.NONE};
        ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).setResource(0);
        ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).setResource(0);
        p.getWarehouseDepot().setConfig(r0, r1, r2);
        c.emptyQueue();
//build new config

        Resource r3 = Resource.COIN;
        Resource[] r4 = new Resource[]{Resource.SERVANT, Resource.SERVANT};
        Resource[] r5 = new Resource[]{Resource.NONE, Resource.NONE, Resource.NONE};
        MSG_ACTION_CHANGE_DEPOT_CONFIG msg = new MSG_ACTION_CHANGE_DEPOT_CONFIG(r3, r4, r5, 0, 2);
        assertTrue(am.changeDepotConfig(p, msg));

//assert new config
        assertSame(p.getWarehouseDepot().getShelf1(), Resource.COIN);
        assertSame(p.getWarehouseDepot().getShelf2()[0], Resource.SERVANT);
        assertSame(p.getWarehouseDepot().getShelf2()[1], Resource.SERVANT);
        assertSame(p.getWarehouseDepot().getShelf3()[0], Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf3()[1], Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf3()[2], Resource.NONE);
        assertTrue(p.getLeaderCards()[0].isEnabled());
        assertTrue(p.getLeaderCards()[1].isEnabled());
        assertEquals(0, ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).getNumber());
        assertEquals(2, ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).getNumber());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_WarehouseDepot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Extradepot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(3, c.messages.size());

    }

    //shelf 1       : none
    //shelf 2       : shield shield
    //shelf 3       : coi coin non
    //extradepot 1  : 0 coin
    //extradepot 2  : 0 shield
    //IN
    //shelf 1       : none
    //shelf 2       : non n
    //shelf 3       : none none none
    //extradepot 1  : 2 coin
    //extradepot 2  : 2 shield
    @Test
    public void validConfig5() {
        ArrayList<LeaderCard> cards = new ArrayList<>();
        cards.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SHIELD, 5)),
                new ExtraDepot(Resource.COIN)));
        cards.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SERVANT, 5)),
                new ExtraDepot(Resource.SHIELD)));
        p.associateLeaderCards(cards);
        p.getLeaderCards()[0].setEnabled(true);
        p.getLeaderCards()[1].setEnabled(true);
        ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).addObserver(c);
        ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).addObserver(c);
        Resource r0 = Resource.NONE;
        Resource[] r1 = new Resource[]{Resource.SHIELD, Resource.SHIELD};
        Resource[] r2 = new Resource[]{Resource.COIN, Resource.COIN, Resource.NONE};
        ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).setResource(0);
        ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).setResource(0);
        p.getWarehouseDepot().setConfig(r0, r1, r2);
        c.emptyQueue();
//build new config

        Resource r3 = Resource.NONE;
        Resource[] r4 = new Resource[]{Resource.NONE, Resource.NONE};
        Resource[] r5 = new Resource[]{Resource.NONE, Resource.NONE, Resource.NONE};
        MSG_ACTION_CHANGE_DEPOT_CONFIG msg = new MSG_ACTION_CHANGE_DEPOT_CONFIG(r3, r4, r5, 2, 2);
        assertTrue(am.changeDepotConfig(p, msg));

//assert new config
        assertSame(p.getWarehouseDepot().getShelf1(), Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf2()[0], Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf2()[1], Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf3()[0], Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf3()[1], Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf3()[2], Resource.NONE);
        assertEquals(2, ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).getNumber());
        assertEquals(2, ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).getNumber());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_WarehouseDepot).count());
        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Extradepot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(4, c.messages.size());

    }

    //shelf 1       : none
    //shelf 2       : shield shield
    //shelf 3       : coin coi c
    //extradepot 1  : 0 coin
    //extradepot 2  : 0 shield
    //IN
    //shelf 1       : none
    //shelf 2       : sh sh
    //shelf 3       : none coin none
    //extradepot 1  : 2 coin
    //extradepot 2  : 0 shield
    @Test
    public void validConfig6() {
        ArrayList<LeaderCard> cards = new ArrayList<>();
        cards.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SHIELD, 5)),
                new ExtraDepot(Resource.COIN)));
        cards.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SERVANT, 5)),
                new ExtraDepot(Resource.SHIELD)));
        p.associateLeaderCards(cards);
        p.getLeaderCards()[0].setEnabled(true);
        p.getLeaderCards()[1].setEnabled(true);
        ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).addObserver(c);
        ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).addObserver(c);
        Resource r0 = Resource.NONE;
        Resource[] r1 = new Resource[]{Resource.SHIELD, Resource.SHIELD};
        Resource[] r2 = new Resource[]{Resource.COIN, Resource.COIN, Resource.COIN};
        ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).setResource(0);
        ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).setResource(0);
        p.getWarehouseDepot().setConfig(r0, r1, r2);
        c.emptyQueue();
//build new config

        Resource r3 = Resource.NONE;
        Resource[] r4 = new Resource[]{Resource.SHIELD, Resource.SHIELD};
        Resource[] r5 = new Resource[]{Resource.NONE, Resource.COIN, Resource.NONE};
        MSG_ACTION_CHANGE_DEPOT_CONFIG msg = new MSG_ACTION_CHANGE_DEPOT_CONFIG(r3, r4, r5, 2, 0);
        assertTrue(am.changeDepotConfig(p, msg));

//assert new config
        assertSame(p.getWarehouseDepot().getShelf1(), Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf2()[0], Resource.SHIELD);
        assertSame(p.getWarehouseDepot().getShelf2()[1], Resource.SHIELD);
        assertSame(p.getWarehouseDepot().getShelf3()[0], Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf3()[1], Resource.COIN);
        assertSame(p.getWarehouseDepot().getShelf3()[2], Resource.NONE);
        assertEquals(2, ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).getNumber());
        assertEquals(0, ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).getNumber());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_WarehouseDepot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Extradepot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(3, c.messages.size());

    }

    //shelf 1       : sh
    //shelf 2       : c n
    //shelf 3       : ser ser n
    //extradepot 1  : X
    //extradepot 2  : X
    //IN
    //shelf 1       : c
    //shelf 2       : serv serv
    //shelf 3       : non shi none
    //extradepot 1  : X
    //extradepot 2  : X
    @Test
    public void validConfigNoExtraDepot() {
        ArrayList<LeaderCard> cards = new ArrayList<>();
        cards.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.YELLOW, new ReqValue(2, -1), Color.BLUE, new ReqValue(1, -1))),
                new MarketResources(Resource.SERVANT)));
        cards.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.GREEN, new ReqValue(2, -1), Color.PURPLE, new ReqValue(1, -1))),
                new MarketResources(Resource.SHIELD)));
        p.associateLeaderCards(cards);
        p.getLeaderCards()[0].setEnabled(true);
        p.getLeaderCards()[1].setEnabled(true);
        Resource r0 = Resource.SHIELD;
        Resource[] r1 = new Resource[]{Resource.COIN, Resource.NONE};
        Resource[] r2 = new Resource[]{Resource.SERVANT, Resource.SERVANT, Resource.NONE};
        p.getWarehouseDepot().setConfig(r0, r1, r2);
        c.emptyQueue();
//build new config

        Resource r3 = Resource.COIN;
        Resource[] r4 = new Resource[]{Resource.SERVANT, Resource.SERVANT};
        Resource[] r5 = new Resource[]{Resource.NONE, Resource.SHIELD, Resource.NONE};
        MSG_ACTION_CHANGE_DEPOT_CONFIG msg = new MSG_ACTION_CHANGE_DEPOT_CONFIG(r3, r4, r5, -1, -1);
        assertTrue(am.changeDepotConfig(p, msg));

//assert new config
        assertSame(p.getWarehouseDepot().getShelf1(), Resource.COIN);
        assertSame(p.getWarehouseDepot().getShelf2()[0], Resource.SERVANT);
        assertSame(p.getWarehouseDepot().getShelf2()[1], Resource.SERVANT);
        assertSame(p.getWarehouseDepot().getShelf3()[0], Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf3()[1], Resource.SHIELD);
        assertSame(p.getWarehouseDepot().getShelf3()[2], Resource.NONE);
        assertTrue(p.getLeaderCards()[0].isEnabled());
        assertTrue(p.getLeaderCards()[1].isEnabled());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_WarehouseDepot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(2, c.messages.size());

    }

    //shelf 1       : sh
    //shelf 2       : c n
    //shelf 3       : ser ser n
    //extradepot 1  : X
    //extradepot 2  : X
    //IN
    //self

    @Test
    public void validConfigNoChange() {
        ArrayList<LeaderCard> cards = new ArrayList<>();
        cards.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.YELLOW, new ReqValue(2, -1), Color.BLUE, new ReqValue(1, -1))),
                new MarketResources(Resource.SERVANT)));
        cards.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.GREEN, new ReqValue(2, -1), Color.PURPLE, new ReqValue(1, -1))),
                new MarketResources(Resource.SHIELD)));
        p.associateLeaderCards(cards);
        Resource r0 = Resource.SHIELD;
        Resource[] r1 = new Resource[]{Resource.COIN, Resource.NONE};
        Resource[] r2 = new Resource[]{Resource.SERVANT, Resource.SERVANT, Resource.NONE};
        p.getWarehouseDepot().setConfig(r0, r1, r2);
        c.emptyQueue();
//build new config

        Resource r3 = Resource.SHIELD;
        Resource[] r4 = new Resource[]{Resource.COIN, Resource.NONE};
        Resource[] r5 = new Resource[]{Resource.SERVANT, Resource.SERVANT, Resource.NONE};
        MSG_ACTION_CHANGE_DEPOT_CONFIG msg = new MSG_ACTION_CHANGE_DEPOT_CONFIG(r3, r4, r5, -1, -1);
        assertTrue(am.changeDepotConfig(p, msg));

//assert new config
        assertSame(p.getWarehouseDepot().getShelf1(), Resource.SHIELD);
        assertSame(p.getWarehouseDepot().getShelf2()[0], Resource.COIN);
        assertSame(p.getWarehouseDepot().getShelf2()[1], Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf3()[0], Resource.SERVANT);
        assertSame(p.getWarehouseDepot().getShelf3()[1], Resource.SERVANT);
        assertSame(p.getWarehouseDepot().getShelf3()[2], Resource.NONE);
        assertFalse(p.getLeaderCards()[0].isEnabled());
        assertFalse(p.getLeaderCards()[1].isEnabled());

//no messages generated, cause no updates were made!
        assertEquals(0, c.messages.size());

    }

    @Test
    public void InvalidDepotConfig() {
        Resource r0 = Resource.SHIELD;
        Resource[] r1 = new Resource[]{Resource.COIN, Resource.NONE};
        Resource[] r2 = new Resource[]{Resource.SERVANT, Resource.SERVANT, Resource.NONE};
        p.getWarehouseDepot().setConfig(r0, r1, r2);
        c.emptyQueue();
//build new config

        Resource r3 = Resource.NONE;
        Resource[] r4 = new Resource[]{Resource.SERVANT, Resource.SERVANT};
        Resource[] r5 = new Resource[]{Resource.NONE, Resource.COIN, Resource.SERVANT};
        MSG_ACTION_CHANGE_DEPOT_CONFIG msg = new MSG_ACTION_CHANGE_DEPOT_CONFIG(r3, r4, r5, -1, -1);
        assertFalse(am.changeDepotConfig(p, msg));

//assert new config
        assertSame(p.getWarehouseDepot().getShelf1(), Resource.SHIELD);
        assertSame(p.getWarehouseDepot().getShelf2()[0], Resource.COIN);
        assertSame(p.getWarehouseDepot().getShelf2()[1], Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf3()[0], Resource.SERVANT);
        assertSame(p.getWarehouseDepot().getShelf3()[1], Resource.SERVANT);
        assertSame(p.getWarehouseDepot().getShelf3()[2], Resource.NONE);

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());
    }

    @Test
    public void InvalidExtraDepotConfig1() {
        ArrayList<LeaderCard> cards = new ArrayList<>();
        cards.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SHIELD, 5)),
                new ExtraDepot(Resource.COIN)));
        cards.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SERVANT, 5)),
                new ExtraDepot(Resource.SHIELD)));
        p.associateLeaderCards(cards);
        p.getLeaderCards()[0].setEnabled(true);
        p.getLeaderCards()[1].setEnabled(true);
        ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).addObserver(c);
        ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).addObserver(c);
        ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).setResource(0);
        ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).setResource(0);
        Resource r0 = Resource.SHIELD;
        Resource[] r1 = new Resource[]{Resource.COIN, Resource.NONE};
        Resource[] r2 = new Resource[]{Resource.SERVANT, Resource.SERVANT, Resource.SERVANT};
        p.getWarehouseDepot().setConfig(r0, r1, r2);
        c.emptyQueue();
//build new config

        Resource r3 = Resource.NONE;
        Resource[] r4 = new Resource[]{Resource.COIN, Resource.NONE};
        Resource[] r5 = new Resource[]{Resource.SERVANT, Resource.SERVANT, Resource.SERVANT};
        MSG_ACTION_CHANGE_DEPOT_CONFIG msg = new MSG_ACTION_CHANGE_DEPOT_CONFIG(r3, r4, r5, 0, 2);
        assertFalse(am.changeDepotConfig(p, msg));

//assert new config
        assertSame(p.getWarehouseDepot().getShelf1(), Resource.SHIELD);
        assertSame(p.getWarehouseDepot().getShelf2()[0], Resource.COIN);
        assertSame(p.getWarehouseDepot().getShelf2()[1], Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf3()[0], Resource.SERVANT);
        assertSame(p.getWarehouseDepot().getShelf3()[1], Resource.SERVANT);
        assertSame(p.getWarehouseDepot().getShelf3()[2], Resource.SERVANT);

        assertEquals(0, ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).getNumber());
        assertEquals(0, ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).getNumber());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());
    }

    @Test
    public void InvalidLeaderCard() {
        ArrayList<LeaderCard> cards = new ArrayList<>();
        cards.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.YELLOW, new ReqValue(2, -1), Color.BLUE, new ReqValue(1, -1))),
                new MarketResources(Resource.SERVANT)));
        cards.add(new LeaderCard(5,
                new CardRequirements(Map.of(Color.GREEN, new ReqValue(2, -1), Color.PURPLE, new ReqValue(1, -1))),
                new MarketResources(Resource.SHIELD)));
        p.associateLeaderCards(cards);
        p.getLeaderCards()[0].setEnabled(true);
        p.getLeaderCards()[0].setEnabled(false);
        Resource r0 = Resource.SHIELD;
        Resource[] r1 = new Resource[]{Resource.COIN, Resource.NONE};
        Resource[] r2 = new Resource[]{Resource.SERVANT, Resource.SERVANT, Resource.NONE};
        p.getWarehouseDepot().setConfig(r0, r1, r2);
        c.emptyQueue();
//build new config

        Resource r3 = Resource.SHIELD;
        Resource[] r4 = new Resource[]{Resource.COIN, Resource.NONE};
        Resource[] r5 = new Resource[]{Resource.SERVANT, Resource.SERVANT, Resource.NONE};
        MSG_ACTION_CHANGE_DEPOT_CONFIG msg = new MSG_ACTION_CHANGE_DEPOT_CONFIG(r3, r4, r5, 2, 2);
        assertFalse(am.changeDepotConfig(p, msg));

//assert config has not changed
        assertSame(p.getWarehouseDepot().getShelf1(), Resource.SHIELD);
        assertSame(p.getWarehouseDepot().getShelf2()[0], Resource.COIN);
        assertSame(p.getWarehouseDepot().getShelf2()[1], Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf3()[0], Resource.SERVANT);
        assertSame(p.getWarehouseDepot().getShelf3()[1], Resource.SERVANT);
        assertSame(p.getWarehouseDepot().getShelf3()[2], Resource.NONE);
        assertFalse(p.getLeaderCards()[0].isEnabled());
        assertFalse(p.getLeaderCards()[1].isEnabled());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());
        c.emptyQueue();

        msg = new MSG_ACTION_CHANGE_DEPOT_CONFIG(r3, r4, r5, -1, 2);
        assertFalse(am.changeDepotConfig(p, msg));

//assert config has not changed
        assertSame(p.getWarehouseDepot().getShelf1(), Resource.SHIELD);
        assertSame(p.getWarehouseDepot().getShelf2()[0], Resource.COIN);
        assertSame(p.getWarehouseDepot().getShelf2()[1], Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf3()[0], Resource.SERVANT);
        assertSame(p.getWarehouseDepot().getShelf3()[1], Resource.SERVANT);
        assertSame(p.getWarehouseDepot().getShelf3()[2], Resource.NONE);
        assertFalse(p.getLeaderCards()[0].isEnabled());
        assertFalse(p.getLeaderCards()[1].isEnabled());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());
        c.emptyQueue();
    }
}