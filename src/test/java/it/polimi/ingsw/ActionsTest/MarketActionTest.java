package it.polimi.ingsw.ActionsTest;
import it.polimi.ingsw.Networking.Message.MSG_ACTION_GET_MARKET_RESOURCES;
import it.polimi.ingsw.Networking.Message.MSG_ACTION_MARKET_CHOICE;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Controller.ActionManager;
import it.polimi.ingsw.Server.Controller.GameManager;
import it.polimi.ingsw.Server.Model.Enumerators.Color;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Model.Game;
import it.polimi.ingsw.Server.Model.LeaderCard;
import it.polimi.ingsw.Server.Model.Marbles.*;
import it.polimi.ingsw.Server.Model.Market;
import it.polimi.ingsw.Server.Model.Player;
import it.polimi.ingsw.Server.Model.Requirements.CardRequirements;
import it.polimi.ingsw.Server.Model.Requirements.ResourceRequirements;
import it.polimi.ingsw.Server.Model.SpecialAbilities.ExtraDepot;
import it.polimi.ingsw.Server.Model.SpecialAbilities.MarketResources;
import it.polimi.ingsw.Server.Utils.Catcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MarketActionTest {

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
        gm.getFaithTrackManager().addObserver(c);
        c.emptyQueue();
        p = g.getPlayer(1);
    }



    //players have NO leaderCards with Market special ability, so WhiteMarbles get destroyed
    @Test
    public void getMarketResourceBWRW()
    {
        MSG_ACTION_GET_MARKET_RESOURCES msg;
        msg = new MSG_ACTION_GET_MARKET_RESOURCES(false, 0);

        MarketMarble[][] re = { {new BlueMarble(),new WhiteMarble(),new RedMarble(),new WhiteMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()}};

        Market market = g.getMarket();
        market.setGrid(re, new WhiteMarble());

        //in this case the action should: modify the Market, activate the MarketHelper, modify the Player  and return true

        assertTrue( am.getMarketResources(p, msg));
        assertTrue(g.getMarketHelper().isEnabled());

        ArrayList<Resource> resources = g.getMarketHelper().getResources();
        assertEquals(1, resources.stream().filter(x -> x == Resource.SHIELD).count());
        assertEquals(1, resources.size());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Market).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertTrue(c.messages.stream().noneMatch(x -> x.getMessageType() == MessageType.MSG_ERROR));
        assertEquals(3, c.messages.size());
    }

    //players have NO leaderCards with Market special ability, so WhiteMarbles get destroyed
    @Test
    public void getMarketResourceBWBG()
    {
        MSG_ACTION_GET_MARKET_RESOURCES msg;
        msg = new MSG_ACTION_GET_MARKET_RESOURCES(false, 0);

        MarketMarble[][] re = { {new BlueMarble(),new WhiteMarble(),new BlueMarble(),new GreyMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()}};


        Market market = g.getMarket();
        market.setGrid(re, new WhiteMarble());

        //in this case the action should: modify the Market, activate the MarketHelper, NOT modify the Player and return true

        assertTrue( am.getMarketResources(p, msg));
        assertTrue(g.getMarketHelper().isEnabled());

        ArrayList<Resource> resources = g.getMarketHelper().getResources();
        assertEquals(2, resources.stream().filter(x -> x == Resource.SHIELD).count());
        assertEquals(1, resources.stream().filter(x -> x == Resource.STONE).count());
        assertEquals(3, resources.size());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Market).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertTrue(c.messages.stream().noneMatch(x -> x.getMessageType() == MessageType.MSG_UPD_Player));
        assertTrue(c.messages.stream().noneMatch(x -> x.getMessageType() == MessageType.MSG_ERROR));
        assertEquals(2, c.messages.size());
    }

    //players have NO leaderCards with Market special ability, so WhiteMarbles get destroyed
    @Test
    public void getMarketResourceWWWR()
    {
        MSG_ACTION_GET_MARKET_RESOURCES msg;
        msg = new MSG_ACTION_GET_MARKET_RESOURCES(false, 0);

        MarketMarble[][] re = { {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new RedMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()}};


        Market market = g.getMarket();
        market.setGrid(re, new WhiteMarble());

        //in this case the action should: modify the Market, NOT activate the MarketHelper, modify the Player and return true

        assertTrue( am.getMarketResources(p, msg));
        assertFalse(g.getMarketHelper().isEnabled());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Market).count());
        assertTrue(c.messages.stream().noneMatch(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper));
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertTrue(c.messages.stream().noneMatch(x -> x.getMessageType() == MessageType.MSG_ERROR));
        assertEquals(2, c.messages.size());
    }

    //players have NO leaderCards with Market special ability, so WhiteMarbles get destroyed
    @Test
    public void getMarketResourceWWWW()
    {
        MSG_ACTION_GET_MARKET_RESOURCES msg;
        msg = new MSG_ACTION_GET_MARKET_RESOURCES(false, 0);

        MarketMarble[][] re = { {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()}};


        Market market = g.getMarket();
        market.setGrid(re, new RedMarble());

        //in this case the action should: modify the Market, NOT activate the MarketHelper, NOT modify the Player and return true
        assertTrue( am.getMarketResources(p, msg));
        assertFalse(g.getMarketHelper().isEnabled());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Market).count());
        assertTrue(c.messages.stream().noneMatch(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper));
        assertTrue(c.messages.stream().noneMatch(x -> x.getMessageType() == MessageType.MSG_UPD_Player));
        assertTrue(c.messages.stream().noneMatch(x -> x.getMessageType() == MessageType.MSG_ERROR));
        assertEquals(1, c.messages.size());
    }

    //players have NO leaderCards with Market special ability, so WhiteMarbles get destroyed
    @Test
    public void getMarketResourceBGYP()
    {
        MSG_ACTION_GET_MARKET_RESOURCES msg;
        msg = new MSG_ACTION_GET_MARKET_RESOURCES(false, 0);

        MarketMarble[][] re = { {new BlueMarble(),new GreyMarble(),new YellowMarble(),new PurpleMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()}};


        Market market = g.getMarket();
        market.setGrid(re, new RedMarble());

        //in this case the action should: modify the Market, NOT activate the MarketHelper, NOT modify the Player and return true
        assertTrue( am.getMarketResources(p, msg));
        assertTrue(g.getMarketHelper().isEnabled());

        ArrayList<Resource> resources = g.getMarketHelper().getResources();
        assertEquals(1, resources.stream().filter(x -> x == Resource.SHIELD).count());
        assertEquals(1, resources.stream().filter(x -> x == Resource.STONE).count());
        assertEquals(1, resources.stream().filter(x -> x == Resource.COIN).count());
        assertEquals(1, resources.stream().filter(x -> x == Resource.SERVANT).count());
        assertEquals(4, resources.size());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Market).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertTrue(c.messages.stream().noneMatch(x -> x.getMessageType() == MessageType.MSG_UPD_Player));
        assertTrue(c.messages.stream().noneMatch(x -> x.getMessageType() == MessageType.MSG_ERROR));
        assertEquals(2, c.messages.size());
    }

    @Test
    public void getMarketResourceWWGG()
    {
        MSG_ACTION_GET_MARKET_RESOURCES msg;
        msg = new MSG_ACTION_GET_MARKET_RESOURCES(false, 0);

        MarketMarble[][] re = { {new WhiteMarble(),new WhiteMarble(),new GreyMarble(),new GreyMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()}};


        Market market = g.getMarket();
        market.setGrid(re, new RedMarble());

        ArrayList<LeaderCard> lc = new ArrayList<>();

        lc.add(new LeaderCard(5,
                new CardRequirements(new HashMap<Color, Integer[]>() {{put(Color.YELLOW, new Integer[] {2,-1}); put(Color.BLUE, new Integer[] {1,-1}); }}),
                new MarketResources(Resource.SERVANT)));

        lc.add(new LeaderCard(5,
                new CardRequirements(new HashMap<Color, Integer[]>() {{put(Color.GREEN, new Integer[] {2,-1}); put(Color.PURPLE, new Integer[] {1,-1}); }}),
                new MarketResources(Resource.SHIELD)));

        p.associateLeaderCards(lc);
        p.getLeaderCards()[0].setEnable(true);

        c.emptyQueue();

        //in this case the action should: modify the Market (always), activate the MarketHelper with exchanged resources, NOT modify the Player and return true
        assertTrue( am.getMarketResources(p, msg));
        assertTrue(g.getMarketHelper().isEnabled());

        ArrayList<Resource> resources = g.getMarketHelper().getResources();
        assertEquals(2, resources.stream().filter(x -> x == Resource.STONE).count());
        assertEquals(2, resources.stream().filter(x -> x == Resource.SERVANT).count());
        assertEquals(4, resources.size());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Market).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertTrue(c.messages.stream().noneMatch(x -> x.getMessageType() == MessageType.MSG_UPD_Player));
        assertTrue(c.messages.stream().noneMatch(x -> x.getMessageType() == MessageType.MSG_ERROR));
        assertEquals(2, c.messages.size());
    }

    @Test
    public void getMarketResourceGRWW()
    {
        MSG_ACTION_GET_MARKET_RESOURCES msg;
        msg = new MSG_ACTION_GET_MARKET_RESOURCES(false, 0);

        MarketMarble[][] re = { {new GreyMarble(),new RedMarble(),new WhiteMarble(),new WhiteMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()}};


        Market market = g.getMarket();
        market.setGrid(re, new RedMarble());

        ArrayList<LeaderCard> lc = new ArrayList<>();

        lc.add(new LeaderCard(5,
                new CardRequirements(new HashMap<Color, Integer[]>() {{put(Color.YELLOW, new Integer[] {2,-1}); put(Color.BLUE, new Integer[] {1,-1}); }}),
                new MarketResources(Resource.SERVANT)));

        lc.add(new LeaderCard(5,
                new CardRequirements(new HashMap<Color, Integer[]>() {{put(Color.GREEN, new Integer[] {2,-1}); put(Color.PURPLE, new Integer[] {1,-1}); }}),
                new MarketResources(Resource.SHIELD)));

        p.associateLeaderCards(lc);
        p.getLeaderCards()[1].setEnable(true);

        c.emptyQueue();

        //in this case the action should: modify the Market (always), activate the MarketHelper with the exchanged resource, modify the Player and return true
        assertTrue( am.getMarketResources(p, msg));
        assertTrue(g.getMarketHelper().isEnabled());

        ArrayList<Resource> resources = g.getMarketHelper().getResources();
        assertEquals(1, resources.stream().filter(x -> x == Resource.STONE).count());
        assertEquals(2, resources.stream().filter(x -> x == Resource.SHIELD).count());
        assertEquals(3, resources.size());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Market).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertTrue(c.messages.stream().noneMatch(x -> x.getMessageType() == MessageType.MSG_ERROR));
        assertEquals(3, c.messages.size());
    }

    @Test
    public void getMarketResourceRWWG()
    {
        MSG_ACTION_GET_MARKET_RESOURCES msg;
        msg = new MSG_ACTION_GET_MARKET_RESOURCES(false, 0);

        MarketMarble[][] re = { {new RedMarble(),new WhiteMarble(),new WhiteMarble(),new GreyMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()}};


        Market market = g.getMarket();
        market.setGrid(re, new RedMarble());

        ArrayList<LeaderCard> lc = new ArrayList<>();

        lc.add(new LeaderCard(5,
                new CardRequirements(new HashMap<Color, Integer[]>() {{put(Color.YELLOW, new Integer[] {2,-1}); put(Color.BLUE, new Integer[] {1,-1}); }}),
                new MarketResources(Resource.SERVANT)));

        lc.add(new LeaderCard(5,
                new CardRequirements(new HashMap<Color, Integer[]>() {{put(Color.GREEN, new Integer[] {2,-1}); put(Color.PURPLE, new Integer[] {1,-1}); }}),
                new MarketResources(Resource.SHIELD)));

        p.associateLeaderCards(lc);
        p.getLeaderCards()[0].setEnable(true);
        p.getLeaderCards()[1].setEnable(true);

        c.emptyQueue();

        //in this case the action should: modify the Market (always), activate the MarketHelper with the exchanged resource, modify the Player and return true
        assertTrue( am.getMarketResources(p, msg));
        assertTrue(g.getMarketHelper().isEnabled());

        ArrayList<Resource> resources = g.getMarketHelper().getResources();
        assertEquals(1, resources.stream().filter(x -> x == Resource.STONE).count());
        assertEquals(2, resources.stream().filter(x -> x == Resource.EXTRA).count());
        assertEquals(3, resources.size());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Market).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertTrue(c.messages.stream().noneMatch(x -> x.getMessageType() == MessageType.MSG_ERROR));
        assertEquals(3, c.messages.size());
    }

    //the error is generated only if the marketHelper is enabled, and the getMarketResource gets somehow invoked
    @Test
    public void getMarketResourcesError()
    {

        //can only occur if: message contains impossible data, or the corresponding market helper is enabled
        MSG_ACTION_GET_MARKET_RESOURCES msg;
        msg = new MSG_ACTION_GET_MARKET_RESOURCES(false, 0);

        MarketMarble[][] re = { {new BlueMarble(), new WhiteMarble(),new WhiteMarble(),new WhiteMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()}};


        Market market = g.getMarket();
        market.setGrid(re, new RedMarble());

        assertTrue( am.getMarketResources(p, msg));
        assertEquals(2, c.messages.size());
        c.emptyQueue();

        assertFalse(am.getMarketResources(p, msg));
        assertTrue(g.getMarketHelper().isEnabled());
        assertTrue(g.getErrorObject().isEnabled());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());
    }

//------------- testing the second part of the MarketAction : chooseResource


//Resources from the market: SHIELD SHIELD STONE
//Actions performed on controller: 0 : save in Depot (check)
//                                 0 : save in Depot (check error, impossible choice)
//                                 0 : save in Depot (check same error)
    @Test
    public void chooseResource1()
    {
        MSG_ACTION_GET_MARKET_RESOURCES msg;
        msg = new MSG_ACTION_GET_MARKET_RESOURCES(false, 0);

        MarketMarble[][] re = { {new BlueMarble(),new BlueMarble(),new WhiteMarble(),new GreyMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()}};


        Market market = g.getMarket();
        market.setGrid(re, new RedMarble());

        ArrayList<LeaderCard> lc = new ArrayList<>();

        lc.add(new LeaderCard(5,
                new CardRequirements(new HashMap<Color, Integer[]>() {{put(Color.YELLOW, new Integer[] {2,-1}); put(Color.BLUE, new Integer[] {1,-1}); }}),
                new MarketResources(Resource.SERVANT)));

        lc.add(new LeaderCard(5,
                new CardRequirements(new HashMap<Color, Integer[]>() {{put(Color.GREEN, new Integer[] {2,-1}); put(Color.PURPLE, new Integer[] {1,-1}); }}),
                new MarketResources(Resource.SHIELD)));

        p.associateLeaderCards(lc);
        c.emptyQueue();

        //in this case the action should: modify the Market (always), activate the MarketHelper with the exchanged resource, modify the Player and return true
        assertTrue( am.getMarketResources(p, msg));
        assertTrue(g.getMarketHelper().isEnabled());

        ArrayList<Resource> resources = g.getMarketHelper().getResources();
        assertEquals(2, resources.stream().filter(x -> x == Resource.SHIELD).count());
        assertEquals(1, resources.stream().filter(x -> x == Resource.STONE).count());
        assertEquals(3, resources.size());
        c.emptyQueue();

        MSG_ACTION_MARKET_CHOICE message = new MSG_ACTION_MARKET_CHOICE(0);

        assertTrue( am.newChoiceMarket(p, message));
        assertTrue(g.getMarketHelper().isEnabled());
        resources = g.getMarketHelper().getResources();
        assertEquals(1, resources.stream().filter(x -> x == Resource.SHIELD).count());
        assertEquals(1, resources.stream().filter(x -> x == Resource.STONE).count());
        assertEquals(2, resources.size());

        assertEquals(1, p.getWarehouseDepot().getResources().entrySet().stream().filter(x -> x.getKey() == Resource.SHIELD).map(Map.Entry::getValue).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_WarehouseDepot).count());
        assertTrue(c.messages.stream().noneMatch(x -> x.getMessageType() == MessageType.MSG_ERROR));
        assertEquals(2, c.messages.size());
        c.emptyQueue();

        assertFalse( am.newChoiceMarket(p, message));
        assertTrue(g.getMarketHelper().isEnabled());
        assertTrue(g.getErrorObject().isEnabled());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());

        g.getErrorObject().setEnabled(false);
        c.emptyQueue();
        assertFalse( am.newChoiceMarket(p, message));
        assertTrue(g.getMarketHelper().isEnabled());
        assertTrue(g.getErrorObject().isEnabled());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());

    }

    //Resources from the market: SHIELD SHIELD STONE
//Actions performed on controller: 0 : save in Depot (check)
//                                 1 : save in ExtraDepot (check)
//                                 1 : save in ExtraDepot (check)
//                                -> MarketHelper deactivates (check)
//                                 0 : save in Depot (gives Error because MarketHelper is not active) (check)
    @Test
    public void chooseResource2()
    {
        MSG_ACTION_GET_MARKET_RESOURCES msg;
        msg = new MSG_ACTION_GET_MARKET_RESOURCES(false, 0);

        MarketMarble[][] re = { {new BlueMarble(),new BlueMarble(),new WhiteMarble(),new GreyMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()}};


        Market market = g.getMarket();
        market.setGrid(re, new RedMarble());

        ArrayList<LeaderCard> lc = new ArrayList<>();

        lc.add(new LeaderCard(3,
                new ResourceRequirements(new HashMap<Resource,Integer>() {{put(Resource.SERVANT, 5); }}),
                new ExtraDepot(Resource.SHIELD)));

        lc.add(new LeaderCard(3,
                new ResourceRequirements(new HashMap<Resource,Integer>() {{put(Resource.COIN, 5); }}),
                new ExtraDepot(Resource.STONE)));

        p.associateLeaderCards(lc);
        p.getLeaderCards()[0].setEnable(true);
        p.getLeaderCards()[1].setEnable(true);
        ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).addObserver(c);
        ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).addObserver(c);
        c.emptyQueue();

        ArrayList<Resource> resources;

        //in this case the action should: modify the Market (always), activate the MarketHelper with the exchanged resource, modify the Player and return true
        assertTrue( am.getMarketResources(p, msg));

        MSG_ACTION_MARKET_CHOICE message = new MSG_ACTION_MARKET_CHOICE(0);

        assertTrue( am.newChoiceMarket(p, message));
        assertTrue(g.getMarketHelper().isEnabled());

        resources = g.getMarketHelper().getResources();
        assertEquals(2, resources.size());

        c.emptyQueue();

        message = new MSG_ACTION_MARKET_CHOICE(1);

        assertTrue( am.newChoiceMarket(p, message));
        assertTrue(g.getMarketHelper().isEnabled());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Extradepot).count());
        assertEquals(2, c.messages.size());

        resources = g.getMarketHelper().getResources();
        assertEquals(0, resources.stream().filter(x -> x == Resource.SHIELD).count());
        assertEquals(1, resources.stream().filter(x -> x == Resource.STONE).count());
        assertEquals(1, resources.size());
        assertEquals(1, ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).getNumber());
        assertEquals(0, ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).getNumber());
        c.emptyQueue();

        message = new MSG_ACTION_MARKET_CHOICE(1);

        assertTrue( am.newChoiceMarket(p, message));
        assertFalse(g.getMarketHelper().isEnabled());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Extradepot).count());
        assertEquals(2, c.messages.size());

        assertEquals(1, ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).getNumber());
        assertEquals(1, ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).getNumber());
        assertFalse(g.getMarketHelper().isEnabled());
        c.emptyQueue();

        message = new MSG_ACTION_MARKET_CHOICE(1);

        assertFalse( am.newChoiceMarket(p, message));
        assertFalse(g.getMarketHelper().isEnabled());
        assertTrue( g.getErrorObject().isEnabled());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());

        assertEquals(1, ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).getNumber());
        assertEquals(1, ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).getNumber());
    }

    //Resources from the market: SHIELD STONE SHIELD SERVANT
//Actions performed on controller: 0 : put Shield in depot
//                                 3 : swap row 1-2 of depot (check)
//                                 0 : put Stone in depot first row (check)
//                                 0 : put Shield in depot (second row) (check)
    @Test
    public void chooseResource3()
    {
        MSG_ACTION_GET_MARKET_RESOURCES msg;
        msg = new MSG_ACTION_GET_MARKET_RESOURCES(false, 0);

        MarketMarble[][] re = { {new BlueMarble(),new BlueMarble(),new BlueMarble(),new PurpleMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()},
                {new WhiteMarble(),new WhiteMarble(),new WhiteMarble(),new WhiteMarble()}};


        Market market = g.getMarket();
        market.setGrid(re, new RedMarble());

        ArrayList<LeaderCard> lc = new ArrayList<>();

        lc.add(new LeaderCard(3,
                new ResourceRequirements(new HashMap<Resource,Integer>() {{put(Resource.SERVANT, 5); }}),
                new ExtraDepot(Resource.SHIELD)));

        lc.add(new LeaderCard(3,
                new ResourceRequirements(new HashMap<Resource,Integer>() {{put(Resource.COIN, 5); }}),
                new ExtraDepot(Resource.STONE)));

        p.associateLeaderCards(lc);
        p.getLeaderCards()[0].setEnable(true);
        ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).addObserver(c);
        c.emptyQueue();

        ArrayList<Resource> resources;

        //in this case the action should: modify the Market (always), activate the MarketHelper with the exchanged resource, modify the Player and return true
        assertTrue( am.getMarketResources(p, msg));

        MSG_ACTION_MARKET_CHOICE message = new MSG_ACTION_MARKET_CHOICE(1);

        assertTrue( am.newChoiceMarket(p, message));

        resources = g.getMarketHelper().getResources();
        assertEquals(3, resources.size());

        c.emptyQueue();

        message = new MSG_ACTION_MARKET_CHOICE(1);

        assertTrue( am.newChoiceMarket(p, message));
        assertTrue(g.getMarketHelper().isEnabled());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Extradepot).count());
        assertEquals(2, c.messages.size());

        resources = g.getMarketHelper().getResources();
        assertEquals(1, resources.stream().filter(x -> x == Resource.SHIELD).count());
        assertEquals(1, resources.stream().filter(x -> x == Resource.SERVANT).count());
        assertEquals(2, resources.size());
        assertEquals(2, ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).getNumber());
        assertEquals(0, ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).getNumber());
        c.emptyQueue();

        message = new MSG_ACTION_MARKET_CHOICE(1); //give error

        assertFalse( am.newChoiceMarket(p, message));
        assertTrue( g.getMarketHelper().isEnabled());
        assertTrue( g.getErrorObject().isEnabled());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());

        resources = g.getMarketHelper().getResources();
        assertEquals(1, resources.stream().filter(x -> x == Resource.SHIELD).count());
        assertEquals(1, resources.stream().filter(x -> x == Resource.SERVANT).count());
        assertEquals(2, resources.size());

        assertEquals(2, ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).getNumber());
        assertEquals(0, ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).getNumber());
    }

    //Resources from the market: SHIELD SHIELD SHIELD SERVANT
//Actions performed on controller: 1 : save in ExtraDepot
//                                 1 : save in ExtraDepot (check)
//                                 1 : save in ExtraDepot (check) (gives error)
//                                 2 : discard resource (check if other players advance)
    @Test
    public void chooseResource4() {
        MSG_ACTION_GET_MARKET_RESOURCES msg;
        msg = new MSG_ACTION_GET_MARKET_RESOURCES(false, 0);

        MarketMarble[][] re = {{new BlueMarble(), new BlueMarble(), new BlueMarble(), new PurpleMarble()},
                {new WhiteMarble(), new WhiteMarble(), new WhiteMarble(), new WhiteMarble()},
                {new WhiteMarble(), new WhiteMarble(), new WhiteMarble(), new WhiteMarble()}};


        Market market = g.getMarket();
        market.setGrid(re, new RedMarble());

        ArrayList<LeaderCard> lc = new ArrayList<>();

        lc.add(new LeaderCard(3,
                new ResourceRequirements(new HashMap<Resource, Integer>() {{
                    put(Resource.SERVANT, 5);
                }}),
                new ExtraDepot(Resource.SHIELD)));

        lc.add(new LeaderCard(3,
                new ResourceRequirements(new HashMap<Resource, Integer>() {{
                    put(Resource.COIN, 5);
                }}),
                new ExtraDepot(Resource.STONE)));

        p.associateLeaderCards(lc);
        p.getLeaderCards()[0].setEnable(true);
        ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).addObserver(c);
        c.emptyQueue();

        ArrayList<Resource> resources;

        //in this case the action should: modify the Market (always), activate the MarketHelper with the exchanged resource, modify the Player and return true
        assertTrue(am.getMarketResources(p, msg));

        MSG_ACTION_MARKET_CHOICE message = new MSG_ACTION_MARKET_CHOICE(1);

        assertTrue(am.newChoiceMarket(p, message));

        resources = g.getMarketHelper().getResources();
        assertEquals(3, resources.size());

        c.emptyQueue();

        message = new MSG_ACTION_MARKET_CHOICE(1);

        assertTrue(am.newChoiceMarket(p, message));
        assertTrue(g.getMarketHelper().isEnabled());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Extradepot).count());
        assertEquals(2, c.messages.size());

        resources = g.getMarketHelper().getResources();
        assertEquals(1, resources.stream().filter(x -> x == Resource.SHIELD).count());
        assertEquals(1, resources.stream().filter(x -> x == Resource.SERVANT).count());
        assertEquals(2, resources.size());
        assertEquals(2, ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).getNumber());
        assertEquals(0, ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).getNumber());
        c.emptyQueue();

        message = new MSG_ACTION_MARKET_CHOICE(1); //gives error

        assertFalse(am.newChoiceMarket(p, message));
        assertTrue(g.getMarketHelper().isEnabled());
        assertTrue(g.getErrorObject().isEnabled());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());

        resources = g.getMarketHelper().getResources();
        assertEquals(1, resources.stream().filter(x -> x == Resource.SHIELD).count());
        assertEquals(1, resources.stream().filter(x -> x == Resource.SERVANT).count());
        assertEquals(2, resources.size());

        assertEquals(2, ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).getNumber());
        assertEquals(0, ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).getNumber());

        g.getErrorObject().setEnabled(false);
        c.emptyQueue();

        message = new MSG_ACTION_MARKET_CHOICE(2);
        assertTrue(am.newChoiceMarket(p, message));
        assertTrue(g.getMarketHelper().isEnabled());

        assertEquals(3, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(4, c.messages.size());
    }

    //Resources from the market: FAITH EXTRA SHIELD EXTRA
//                Player activates a zone: he receives points, as well as the second one (check)
//                                 6 : skip Extra
//                                 0 : put in Depot
//                                 0 : transform in SERVANT
//                                 7 : skip backwards
//                                 1 : transform in SHIELD
    @Test
    public void chooseResource5() {

        MarketMarble[][] re = {{new RedMarble(), new WhiteMarble(), new BlueMarble(), new WhiteMarble()},
                {new WhiteMarble(), new WhiteMarble(), new WhiteMarble(), new WhiteMarble()},
                {new WhiteMarble(), new WhiteMarble(), new WhiteMarble(), new WhiteMarble()}};


        Market market = g.getMarket();
        market.setGrid(re, new WhiteMarble());

        ArrayList<LeaderCard> lc = new ArrayList<>();

        lc.add(new LeaderCard(5,
                new CardRequirements(new HashMap<Color, Integer[]>() {{put(Color.YELLOW, new Integer[] {2,-1}); put(Color.BLUE, new Integer[] {1,-1}); }}),
                new MarketResources(Resource.SERVANT)));

        lc.add(new LeaderCard(5,
                new CardRequirements(new HashMap<Color, Integer[]>() {{put(Color.GREEN, new Integer[] {2,-1}); put(Color.PURPLE, new Integer[] {1,-1}); }}),
                new MarketResources(Resource.SHIELD)));

        p.associateLeaderCards(lc);
        p.getLeaderCards()[0].setEnable(true);
        p.getLeaderCards()[1].setEnable(true);
        p.setPosition(7);
        g.getPlayer(2).setPosition(5);
        c.emptyQueue();

        ArrayList<Resource> resources;

        assertTrue(am.getMarketResources(p, new MSG_ACTION_GET_MARKET_RESOURCES(false, 0)));
        assertEquals(3, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Market).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_FaithTrack).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(7, c.messages.size());
        c.emptyQueue();

        MSG_ACTION_MARKET_CHOICE message = new MSG_ACTION_MARKET_CHOICE(6);
        resources = g.getMarketHelper().getResources();
        assertEquals(3, resources.size());
        assertSame(Resource.EXTRA, g.getMarketHelper().getCurrentResource());
        assertTrue(am.newChoiceMarket(p, message));
        assertSame(Resource.SHIELD, g.getMarketHelper().getCurrentResource());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(1, c.messages.size());
        resources = g.getMarketHelper().getResources();
        assertEquals(3, resources.size());
        c.emptyQueue();

        message = new MSG_ACTION_MARKET_CHOICE(0); //put in depot
        assertTrue(am.newChoiceMarket(p, message));
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_WarehouseDepot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(2, c.messages.size());
        resources = g.getMarketHelper().getResources();
        assertEquals(0, resources.stream().filter(x -> x == Resource.SHIELD).count());
        assertEquals(2, resources.stream().filter(x -> x == Resource.EXTRA).count());
        assertEquals(2, resources.size());
        c.emptyQueue();

        message = new MSG_ACTION_MARKET_CHOICE(0); // transform in Servant
        assertSame(Resource.EXTRA, g.getMarketHelper().getCurrentResource());
        assertTrue(am.newChoiceMarket(p, message));
        assertSame(Resource.SERVANT, g.getMarketHelper().getCurrentResource());
        resources = g.getMarketHelper().getResources();
        assertEquals(1, resources.stream().filter(x -> x == Resource.SERVANT).count());
        assertEquals(1, resources.stream().filter(x -> x == Resource.EXTRA).count());
        assertEquals(2, resources.size());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(1, c.messages.size());
        c.emptyQueue();;

        message = new MSG_ACTION_MARKET_CHOICE(7); //skip backwards
        assertTrue(am.newChoiceMarket(p, message));
        assertSame(Resource.EXTRA, g.getMarketHelper().getCurrentResource());
        assertEquals(1, resources.stream().filter(x -> x == Resource.SERVANT).count());
        assertEquals(1, resources.stream().filter(x -> x == Resource.EXTRA).count());
        assertEquals(2, resources.size());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(1, c.messages.size());
        c.emptyQueue();

        message = new MSG_ACTION_MARKET_CHOICE(1); // transform in Shield
        assertSame(Resource.EXTRA, g.getMarketHelper().getCurrentResource());
        assertTrue(am.newChoiceMarket(p, message));
        assertSame(Resource.SHIELD, g.getMarketHelper().getCurrentResource());
        resources = g.getMarketHelper().getResources();
        assertEquals(1, resources.stream().filter(x -> x == Resource.SERVANT).count());
        assertEquals(1, resources.stream().filter(x -> x == Resource.SHIELD).count());
        assertEquals(2, resources.size());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(1, c.messages.size());

    }

    //Resources from the market: STONE STONE SHIELD
//                                 4 : Swap 1-3
//                                 0 : put in Depot
//                                 0 : put in Depot
//                                 0 : put in Depot
//                                 check if terminated
    @Test
    public void chooseResource6() {

        MarketMarble[][] re = {{new GreyMarble(), new WhiteMarble(), new GreyMarble(), new BlueMarble()},
                {new WhiteMarble(), new WhiteMarble(), new WhiteMarble(), new WhiteMarble()},
                {new WhiteMarble(), new WhiteMarble(), new WhiteMarble(), new WhiteMarble()}};


        Market market = g.getMarket();
        market.setGrid(re, new WhiteMarble());

        ArrayList<LeaderCard> lc = new ArrayList<>();

        lc.add(new LeaderCard(5,
                new CardRequirements(new HashMap<Color, Integer[]>() {{
                    put(Color.YELLOW, new Integer[]{2, -1});
                    put(Color.BLUE, new Integer[]{1, -1});
                }}),
                new MarketResources(Resource.SERVANT)));

        lc.add(new LeaderCard(5,
                new CardRequirements(new HashMap<Color, Integer[]>() {{
                    put(Color.GREEN, new Integer[]{2, -1});
                    put(Color.PURPLE, new Integer[]{1, -1});
                }}),
                new MarketResources(Resource.SHIELD)));

        p.associateLeaderCards(lc);
        Resource[] r1 = new Resource[]{Resource.NONE, Resource.NONE};
        Resource[] r2 = new Resource[]{Resource.NONE, Resource.NONE, Resource.NONE};
        p.getWarehouseDepot().setConfig(Resource.STONE, r1, r2);
        c.emptyQueue();

        assertTrue(am.getMarketResources(p, new MSG_ACTION_GET_MARKET_RESOURCES(false, 0)));
        c.emptyQueue();

        MSG_ACTION_MARKET_CHOICE message = new MSG_ACTION_MARKET_CHOICE(4); //swap 1-3
        assertTrue(am.newChoiceMarket(p, message));
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_WarehouseDepot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(2, c.messages.size());
        assertSame(p.getWarehouseDepot().getShelf3()[0], Resource.STONE);
        assertSame(p.getWarehouseDepot().getShelf3()[1], Resource.NONE);
        assertSame(p.getWarehouseDepot().getShelf3()[2], Resource.NONE);
        c.emptyQueue();

        message = new MSG_ACTION_MARKET_CHOICE(0); //put in Depot
        assertTrue(am.newChoiceMarket(p, message));
        ArrayList<Resource> resources = resources = g.getMarketHelper().getResources();
        assertEquals(2, resources.size());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_WarehouseDepot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(2, c.messages.size());
        assertSame(p.getWarehouseDepot().getShelf3()[0], Resource.STONE);
        assertSame(p.getWarehouseDepot().getShelf3()[1], Resource.STONE);
        assertSame(p.getWarehouseDepot().getShelf3()[2], Resource.NONE);
        c.emptyQueue();

        message = new MSG_ACTION_MARKET_CHOICE(0); //put in Depot
        assertTrue(am.newChoiceMarket(p, message));
        resources = resources = g.getMarketHelper().getResources();
        assertEquals(1, resources.size());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_WarehouseDepot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(2, c.messages.size());
        assertSame(p.getWarehouseDepot().getShelf3()[0], Resource.STONE);
        assertSame(p.getWarehouseDepot().getShelf3()[1], Resource.STONE);
        assertSame(p.getWarehouseDepot().getShelf3()[2], Resource.STONE);
        c.emptyQueue();

        message = new MSG_ACTION_MARKET_CHOICE(0); //put in Depot
        assertTrue(am.newChoiceMarket(p, message));
        assertFalse(g.getMarketHelper().isEnabled());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_WarehouseDepot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(2, c.messages.size());
        assertSame(p.getWarehouseDepot().getShelf1(), Resource.SHIELD);
    }

    //Resources from the market: STONE SHIELD
//                                 6 : skip
//                                 6 : skip
//                                 7 : backwards
//                                 2 : discard
//                                 2 : discard
    @Test
    public void chooseResource7() {

        MarketMarble[][] re = {{new GreyMarble(), new WhiteMarble(), new WhiteMarble(), new BlueMarble()},
                {new WhiteMarble(), new WhiteMarble(), new WhiteMarble(), new WhiteMarble()},
                {new WhiteMarble(), new WhiteMarble(), new WhiteMarble(), new WhiteMarble()}};


        Market market = g.getMarket();
        market.setGrid(re, new WhiteMarble());

        c.emptyQueue();

        assertTrue(am.getMarketResources(p, new MSG_ACTION_GET_MARKET_RESOURCES(false, 0)));
        c.emptyQueue();

        MSG_ACTION_MARKET_CHOICE message = new MSG_ACTION_MARKET_CHOICE(6); //skip
        assertSame(g.getMarketHelper().getCurrentResource(), Resource.STONE);
        assertTrue(am.newChoiceMarket(p, message));
        ArrayList<Resource> resources = resources = g.getMarketHelper().getResources();
        assertEquals(2, resources.size());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(1, c.messages.size());
        assertSame(g.getMarketHelper().getCurrentResource(), Resource.SHIELD);
        c.emptyQueue();

        message = new MSG_ACTION_MARKET_CHOICE(6); //skip again
        assertTrue(am.newChoiceMarket(p, message));
        resources = resources = g.getMarketHelper().getResources();
        assertEquals(2, resources.size());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(1, c.messages.size());
        assertSame(g.getMarketHelper().getCurrentResource(), Resource.STONE);
        c.emptyQueue();

        message = new MSG_ACTION_MARKET_CHOICE(7); //skip backwards
        assertTrue(am.newChoiceMarket(p, message));
        resources = resources = g.getMarketHelper().getResources();
        assertEquals(2, resources.size());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(1, c.messages.size());
        assertSame(g.getMarketHelper().getCurrentResource(), Resource.SHIELD);
        c.emptyQueue();

        message = new MSG_ACTION_MARKET_CHOICE(2); //discard
        assertTrue(am.newChoiceMarket(p, message));
        assertTrue(g.getMarketHelper().isEnabled());
        resources = resources = g.getMarketHelper().getResources();
        assertEquals(1, resources.size());
        assertEquals(3, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(4, c.messages.size());
        assertEquals(3, g.getPlayerList().stream().filter(x -> x.getPosition() == 2).count());
        assertSame(g.getMarketHelper().getCurrentResource(), Resource.STONE);
        c.emptyQueue();

        message = new MSG_ACTION_MARKET_CHOICE(2); //discard
        assertTrue(am.newChoiceMarket(p, message));
        assertFalse(g.getMarketHelper().isEnabled());
        assertEquals(3, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Player).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_MarketHelper).count());
        assertEquals(4, c.messages.size());
        assertEquals(3, g.getPlayerList().stream().filter(x -> x.getPosition() == 3).count());
        c.emptyQueue();
    }
}
