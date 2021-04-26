package it.polimi.ingsw.ActionsTest;
import it.polimi.ingsw.Networking.Message.MSG_ACTION_BUY_DEVELOPMENT_CARD;
import it.polimi.ingsw.Networking.Message.MSG_ACTION_CHOOSE_DEVELOPMENT_CARD;
import it.polimi.ingsw.Networking.Message.MSG_ACTION_GET_MARKET_RESOURCES;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Controller.ActionManager;
import it.polimi.ingsw.Server.Controller.GameManager;
import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.Model.Enumerators.Color;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Model.Middles.DevelopmentCardsVendor;
import it.polimi.ingsw.Server.Model.Middles.LeaderBoard;
import it.polimi.ingsw.Server.Model.Requirements.CardRequirements;
import it.polimi.ingsw.Server.Model.Requirements.ResourceRequirements;
import it.polimi.ingsw.Server.Model.SpecialAbilities.DiscountResource;
import it.polimi.ingsw.Server.Model.SpecialAbilities.ExtraDepot;
import it.polimi.ingsw.Server.Utils.Catcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BuyDevelopmentCardTest {

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

    @Test
    //this test ensures that DevelopmentCardsVendor is correctly set to disabled at the beginning.
    public void buyDevelopmentCardTest0() {
        DevelopmentCardsVendor dcv = g.getDevelopmentCardsVendor();
        assertFalse(dcv.isEnabled());
    }

    @Test
    //if the player has enough resources it is expected to have a list of 4 cards and an enabled DevCardsVendor.
    public void buyDevelopmentCardTest1() {
        p.getStrongbox().addResource(Resource.SERVANT, 20);
        p.getStrongbox().addResource(Resource.SHIELD, 20);
        p.getStrongbox().addResource(Resource.STONE, 20);
        p.getStrongbox().addResource(Resource.COIN, 20);

        assertTrue(am.buyDevelopmentCard(p));
        DevelopmentCardsVendor dcv = g.getDevelopmentCardsVendor();
        assertTrue(dcv.isEnabled());
        //the cards that the player can buy and put in the slots at the beginning are only the 4 "level 1" cards.
        assertEquals(dcv.getCards().size(), 4);
        assertTrue(c.messages.stream().noneMatch(x -> x.getMessageType() == MessageType.MSG_ERROR));
    }

    @Test
    //if the player has no resources it is expected to return false and to have an ObjectError.
    public void buyDevelopmentCardTest2() {
        assertFalse(am.buyDevelopmentCard(p));
        DevelopmentCardsVendor dcv = g.getDevelopmentCardsVendor();
        assertEquals(1, c.messages.size());
        assertSame(c.messages.get(0).getMessageType(), MessageType.MSG_ERROR);
        assertEquals("You cannot buy any card.", g.getErrorObject().getErrorMessage());
        assertFalse(dcv.isEnabled());
    }

    @Test
    //if the player has enough resources but not space in any slot it is expected to return false and to have an ObjectError.
    public void buyDevelopmentCardTest3() {
        DevelopmentCardsVendor dcv = g.getDevelopmentCardsVendor();
        p.getStrongbox().addResource(Resource.SERVANT, 20);
        p.getStrongbox().addResource(Resource.SHIELD, 20);
        p.getStrongbox().addResource(Resource.STONE, 20);
        p.getStrongbox().addResource(Resource.COIN, 20);
        c.emptyQueue();

        for(int c = 0; c < 4; c++) {
            for (int p = 0; p < 4; p++) {
                g.getDevelopmentCardsDeck().removeCard(2, c);
            }
        }

        assertFalse(am.buyDevelopmentCard(p));
        assertEquals(1, c.messages.size());
        assertSame(MessageType.MSG_ERROR, c.messages.get(0).getMessageType());
        assertFalse(dcv.isEnabled());
        assertEquals("You cannot place the cards in any slot.", g.getErrorObject().getErrorMessage());
    }

    @Test
    //test that ensures that the player can buy a card with the DiscountResource ability.
    public void buyDevelopmentCardTest4() {
        DevelopmentCardsVendor dcv = g.getDevelopmentCardsVendor();

        ArrayList<LeaderCard> lc = new ArrayList<>();

        lc.add(new LeaderCard(2,
                    new CardRequirements(new HashMap<Color, Integer[]>() {{put(Color.GREEN, new Integer[] {1,-1}); put(Color.BLUE, new Integer[] {1,-1}); }}),
                    new DiscountResource(Resource.STONE)));

        lc.add(new LeaderCard(3,
                    new ResourceRequirements(new HashMap<Resource,Integer>() {{put(Resource.STONE, 5); }}),
                    new ExtraDepot(Resource.SERVANT)));

        p.associateLeaderCards(lc);
        p.getLeaderCards()[0].setEnable(true);
        c.emptyQueue();

        DevelopmentCard[][][] grid = new DevelopmentCard[3][4][4];

        for(int r = 0; r < 3; r++) {
            for(int c = 0; c < 4; c++) {
                for(int p = 0; p < 4; p++) {
                    grid[r][c][p] = null;
                }
            }
        }

        grid[2][0][3] = new DevelopmentCard(1, Color.GREEN, 2,
                new HashMap<Resource,Integer>() {{ put(Resource.SHIELD, 1); put(Resource.SERVANT, 1); put(Resource.STONE, 1);}} ,
                new Power(new HashMap<Resource, Integer>() {{ put(Resource.STONE, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.SERVANT, 1); }}));


        g.getDevelopmentCardsDeck().setGrid(grid);
        p.getStrongbox().addResource(Resource.SERVANT, 1);
        p.getStrongbox().addResource(Resource.SHIELD, 1);
        c.emptyQueue();

        assertTrue(am.buyDevelopmentCard(p));
        assertTrue(dcv.isEnabled());
        assertTrue(c.messages.stream().noneMatch(x -> x.getMessageType() == MessageType.MSG_ERROR));
    }

    @Test
    //test that ensures that the player can buy a card with the DiscountResource ability.
    public void buyDevelopmentCardTest5() {
        DevelopmentCardsVendor dcv = g.getDevelopmentCardsVendor();

        ArrayList<LeaderCard> lc = new ArrayList<>();

        lc.add(new LeaderCard(2,
                new CardRequirements(new HashMap<Color, Integer[]>() {{put(Color.GREEN, new Integer[] {1,-1}); put(Color.BLUE, new Integer[] {1,-1}); }}),
                new DiscountResource(Resource.STONE)));

        lc.add(new LeaderCard(2,
                new CardRequirements(new HashMap<Color, Integer[]>() {{put(Color.BLUE, new Integer[] {1,-1}); put(Color.PURPLE, new Integer[] {1,-1}); }}),
                new DiscountResource(Resource.SHIELD)));

        p.associateLeaderCards(lc);
        p.getLeaderCards()[0].setEnable(true);
        p.getLeaderCards()[1].setEnable(true);
        c.emptyQueue();

        DevelopmentCard[][][] grid = new DevelopmentCard[3][4][4];

        for(int r = 0; r < 3; r++) {
            for(int c = 0; c < 4; c++) {
                for(int p = 0; p < 4; p++) {
                    grid[r][c][p] = null;
                }
            }
        }

        grid[2][0][3] = new DevelopmentCard(1, Color.GREEN, 2,
                new HashMap<Resource,Integer>() {{ put(Resource.SHIELD, 1); put(Resource.SERVANT, 1); put(Resource.STONE, 1);}} ,
                new Power(new HashMap<Resource, Integer>() {{ put(Resource.STONE, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.SERVANT, 1); }}));


        g.getDevelopmentCardsDeck().setGrid(grid);
        p.getStrongbox().addResource(Resource.SERVANT, 1);
        c.emptyQueue();

        assertTrue(am.buyDevelopmentCard(p));
        assertTrue(dcv.isEnabled());
        assertTrue(c.messages.stream().noneMatch(x -> x.getMessageType() == MessageType.MSG_ERROR));
    }

    @Test
    //test that ensures that the player can buy a card with the DiscountResource ability.
    public void buyDevelopmentCardTest6() {
        DevelopmentCardsVendor dcv = g.getDevelopmentCardsVendor();

        ArrayList<LeaderCard> lc = new ArrayList<>();

        lc.add(new LeaderCard(2,
                new CardRequirements(new HashMap<Color, Integer[]>() {{put(Color.GREEN, new Integer[] {1,-1}); put(Color.BLUE, new Integer[] {1,-1}); }}),
                new DiscountResource(Resource.STONE)));

        lc.add(new LeaderCard(2,
                new CardRequirements(new HashMap<Color, Integer[]>() {{put(Color.BLUE, new Integer[] {1,-1}); put(Color.PURPLE, new Integer[] {1,-1}); }}),
                new DiscountResource(Resource.SHIELD)));

        p.associateLeaderCards(lc);
        p.getLeaderCards()[0].setEnable(true);
        p.getLeaderCards()[1].setEnable(true);
        c.emptyQueue();

        DevelopmentCard[][][] grid = new DevelopmentCard[3][4][4];

        for(int r = 0; r < 3; r++) {
            for(int c = 0; c < 4; c++) {
                for(int p = 0; p < 4; p++) {
                    grid[r][c][p] = null;
                }
            }
        }

        grid[2][0][3] = new DevelopmentCard(1, Color.GREEN, 2,
                new HashMap<Resource,Integer>() {{ put(Resource.SHIELD, 2); put(Resource.SERVANT, 1); put(Resource.STONE, 1);}} ,
                new Power(new HashMap<Resource, Integer>() {{ put(Resource.STONE, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.SERVANT, 1); }}));


        g.getDevelopmentCardsDeck().setGrid(grid);
        p.getStrongbox().addResource(Resource.SERVANT, 1);
        c.emptyQueue();

        assertFalse(am.buyDevelopmentCard(p));
        assertFalse(dcv.isEnabled());
        assertEquals(1, c.messages.size());
        assertSame(MessageType.MSG_ERROR, c.messages.get(0).getMessageType());
        assertEquals("You cannot buy any card.", g.getErrorObject().getErrorMessage());
    }

    @Test
    //test for the starting "exception".
    public void chooseDevelopmentCardTest0() {
        DevelopmentCardsVendor dcv = g.getDevelopmentCardsVendor();
        assertFalse(am.buyDevelopmentCard(p));
        c.emptyQueue();
        MSG_ACTION_CHOOSE_DEVELOPMENT_CARD msg = new MSG_ACTION_CHOOSE_DEVELOPMENT_CARD(1,2);
        assertFalse(am.chooseDevelopmentCard(p,msg));
        assertEquals(1, c.messages.size());
        assertSame(MessageType.MSG_ERROR, c.messages.get(0).getMessageType());
        assertFalse(dcv.isEnabled());
        assertEquals("Errore! è stato chiamato il metodo chooseDevelopmentCard (2/2) senza oggetto vendor attivo nel model!", g.getErrorObject().getErrorMessage());
    }

    @Test
    //if the player selects a card and a slot the card is added and the resources are removed from the inventory.
    public void chooseDevelopmentCardTest1() {
        DevelopmentCardsVendor dcv = g.getDevelopmentCardsVendor();

        p.getStrongbox().addResource(Resource.SERVANT, 20);
        p.getStrongbox().addResource(Resource.SHIELD, 20);
        p.getStrongbox().addResource(Resource.STONE, 20);
        p.getStrongbox().addResource(Resource.COIN, 20);

        assertTrue(am.buyDevelopmentCard(p));
        c.emptyQueue();

        MSG_ACTION_CHOOSE_DEVELOPMENT_CARD msg = new MSG_ACTION_CHOOSE_DEVELOPMENT_CARD(1,1);
        assertTrue(am.chooseDevelopmentCard(p, msg));
        assertFalse(dcv.isEnabled());
    }

    @Test
    //test that checks the correct functioning of the DiscountResource ability in the ChooseDevelopmentCard method.
    public void chooseDevelopmentCardTest2() {
        DevelopmentCardsVendor dcv = g.getDevelopmentCardsVendor();

        ArrayList<LeaderCard> lc = new ArrayList<>();

        lc.add(new LeaderCard(2,
                new CardRequirements(new HashMap<Color, Integer[]>() {{put(Color.GREEN, new Integer[] {1,-1}); put(Color.BLUE, new Integer[] {1,-1}); }}),
                new DiscountResource(Resource.STONE)));

        lc.add(new LeaderCard(2,
                new CardRequirements(new HashMap<Color, Integer[]>() {{put(Color.BLUE, new Integer[] {1,-1}); put(Color.PURPLE, new Integer[] {1,-1}); }}),
                new DiscountResource(Resource.SHIELD)));

        p.associateLeaderCards(lc);
        p.getLeaderCards()[0].setEnable(true);
        p.getLeaderCards()[1].setEnable(true);
        c.emptyQueue();

        DevelopmentCard[][][] grid = new DevelopmentCard[3][4][4];

        for(int r = 0; r < 3; r++) {
            for(int c = 0; c < 4; c++) {
                for(int p = 0; p < 4; p++) {
                    grid[r][c][p] = null;
                }
            }
        }

        grid[2][0][3] = new DevelopmentCard(1, Color.GREEN, 2,
                new HashMap<Resource,Integer>() {{ put(Resource.SHIELD, 1); put(Resource.SERVANT, 1); put(Resource.STONE, 1);}} ,
                new Power(new HashMap<Resource, Integer>() {{ put(Resource.STONE, 1); }},
                        new HashMap<Resource, Integer>() {{ put(Resource.SERVANT, 1); }}));

        g.getDevelopmentCardsDeck().setGrid(grid);
        p.getStrongbox().addResource(Resource.SERVANT, 1);
        c.emptyQueue();

        assertTrue(am.buyDevelopmentCard(p));
        c.emptyQueue();

        MSG_ACTION_CHOOSE_DEVELOPMENT_CARD msg = new MSG_ACTION_CHOOSE_DEVELOPMENT_CARD(1,1);
        assertTrue(am.chooseDevelopmentCard(p, msg));
        assertFalse(dcv.isEnabled());
    }
}
