package it.polimi.ingsw.ActionsTest;
import it.polimi.ingsw.Networking.Message.MSG_ACTION_CHOOSE_DEVELOPMENT_CARD;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Controller.ActionManager;
import it.polimi.ingsw.Server.Controller.GameManager;
import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.Model.Enumerators.Color;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Model.Enumerators.Status;
import it.polimi.ingsw.Server.Model.Middles.DevelopmentCardsVendor;
import it.polimi.ingsw.Server.Model.Requirements.CardRequirements;
import it.polimi.ingsw.Server.Model.Requirements.ResourceRequirements;
import it.polimi.ingsw.Server.Model.SpecialAbilities.DiscountResource;
import it.polimi.ingsw.Server.Model.SpecialAbilities.ExtraDepot;
import it.polimi.ingsw.Catcher;
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

    @Test
    //ensures that DevelopmentCardsVendor is correctly set to disabled at the beginning.
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

        assertEquals(4, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Strongbox).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_DevCardsVendor).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(6, c.messages.size());
    }

    @Test
    //if the player has no resources it is expected to return false and to have an ObjectError.
    public void buyDevelopmentCardTest2() {
        assertFalse(am.buyDevelopmentCard(p));

        DevelopmentCardsVendor dcv = g.getDevelopmentCardsVendor();
        assertFalse(dcv.isEnabled());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());
    }

    @Test
    //if the player has enough resources but not space in any slot it is expected to return false and to have an ObjectError.
    public void buyDevelopmentCardTest3() {
        DevelopmentCardsVendor dcv = g.getDevelopmentCardsVendor();
        p.getStrongbox().addResource(Resource.SERVANT, 20);
        p.getStrongbox().addResource(Resource.SHIELD, 20);
        p.getStrongbox().addResource(Resource.STONE, 20);
        p.getStrongbox().addResource(Resource.COIN, 20);


        for (int c = 0; c < 4; c++) {
            for (int p = 0; p < 4; p++) {
                g.getDevelopmentCardsDeck().removeCard(2, c);
            }
        }

        c.emptyQueue();
        assertFalse(am.buyDevelopmentCard(p));

        assertFalse(dcv.isEnabled());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());
    }

    @Test
    //test that ensures that the player can buy a card with the DiscountResource ability.
    public void buyDevelopmentCardTest4() {
        DevelopmentCardsVendor dcv = g.getDevelopmentCardsVendor();

        ArrayList<LeaderCard> lc = new ArrayList<>();

        lc.add(new LeaderCard(2,
                new CardRequirements(Map.of(Color.GREEN, new Integer[]{1, -1}, Color.BLUE, new Integer[]{1, -1})),
                new DiscountResource(Resource.STONE)));

        lc.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.STONE, 5)),
                new ExtraDepot(Resource.SERVANT)));

        p.associateLeaderCards(lc);
        p.getLeaderCards()[0].setEnable(true);

        DevelopmentCard[][][] grid = new DevelopmentCard[3][4][4];

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                for (int p = 0; p < 4; p++) {
                    grid[r][c][p] = null;
                }
            }
        }

        grid[2][0][3] = new DevelopmentCard(1, Color.GREEN, 2,
                Map.of(Resource.SHIELD, 1, Resource.SERVANT, 1, Resource.STONE, 1),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.SERVANT, 1)));


        g.getDevelopmentCardsDeck().setGrid(grid);
        p.getStrongbox().addResource(Resource.SERVANT, 1);
        p.getStrongbox().addResource(Resource.SHIELD, 1);
        c.emptyQueue();

        assertTrue(am.buyDevelopmentCard(p));

        assertTrue(dcv.isEnabled());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_DevCardsVendor).count());
        assertEquals(2, c.messages.size());
    }

    @Test
    //test that ensures that the player can buy a card with the DiscountResource ability.
    public void buyDevelopmentCardTest5() {
        DevelopmentCardsVendor dcv = g.getDevelopmentCardsVendor();

        ArrayList<LeaderCard> lc = new ArrayList<>();

        lc.add(new LeaderCard(2,
                new CardRequirements(Map.of(Color.GREEN, new Integer[]{1, -1}, Color.BLUE, new Integer[]{1, -1})),
                new DiscountResource(Resource.STONE)));

        lc.add(new LeaderCard(2,
                new CardRequirements(Map.of(Color.BLUE, new Integer[]{1, -1}, Color.PURPLE, new Integer[]{1, -1})),
                new DiscountResource(Resource.SHIELD)));

        p.associateLeaderCards(lc);
        p.getLeaderCards()[0].setEnable(true);
        p.getLeaderCards()[1].setEnable(true);

        DevelopmentCard[][][] grid = new DevelopmentCard[3][4][4];

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                for (int p = 0; p < 4; p++) {
                    grid[r][c][p] = null;
                }
            }
        }

        grid[2][0][3] = new DevelopmentCard(1, Color.GREEN, 2,
                Map.of(Resource.SHIELD, 1, Resource.SERVANT, 1, Resource.STONE, 1),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.SERVANT, 1)));


        g.getDevelopmentCardsDeck().setGrid(grid);
        p.getStrongbox().addResource(Resource.SERVANT, 1);
        c.emptyQueue();

        assertTrue(am.buyDevelopmentCard(p));

        assertTrue(dcv.isEnabled());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_DevCardsVendor).count());
        assertEquals(2, c.messages.size());
    }

    @Test
    //test that ensures that the player can buy a card with the DiscountResource ability.
    public void buyDevelopmentCardTest6() {
        DevelopmentCardsVendor dcv = g.getDevelopmentCardsVendor();

        ArrayList<LeaderCard> lc = new ArrayList<>();

        lc.add(new LeaderCard(2,
                new CardRequirements(Map.of(Color.GREEN, new Integer[]{1, -1}, Color.BLUE, new Integer[]{1, -1})),
                new DiscountResource(Resource.STONE)));

        lc.add(new LeaderCard(2,
                new CardRequirements(Map.of(Color.BLUE, new Integer[]{1, -1}, Color.PURPLE, new Integer[]{1, -1})),
                new DiscountResource(Resource.SHIELD)));

        p.associateLeaderCards(lc);
        p.getLeaderCards()[0].setEnable(true);
        p.getLeaderCards()[1].setEnable(true);

        DevelopmentCard[][][] grid = new DevelopmentCard[3][4][4];

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                for (int p = 0; p < 4; p++) {
                    grid[r][c][p] = null;
                }
            }
        }

        grid[2][0][3] = new DevelopmentCard(1, Color.GREEN, 2,
                Map.of(Resource.SHIELD, 2, Resource.SERVANT, 1, Resource.STONE, 1),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.SERVANT, 1)));


        g.getDevelopmentCardsDeck().setGrid(grid);
        p.getStrongbox().addResource(Resource.SERVANT, 1);
        c.emptyQueue();

        assertFalse(am.buyDevelopmentCard(p));

        assertFalse(dcv.isEnabled());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());
    }

    @Test
    //test for the starting "exception".
    public void chooseDevelopmentCardTest0() {
        DevelopmentCardsVendor dcv = g.getDevelopmentCardsVendor();
        assertFalse(am.buyDevelopmentCard(p));
        c.emptyQueue();
        MSG_ACTION_CHOOSE_DEVELOPMENT_CARD msg = new MSG_ACTION_CHOOSE_DEVELOPMENT_CARD(0, 1);

        assertFalse(am.chooseDevelopmentCard(p, msg));

        assertFalse(dcv.isEnabled());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_ERROR).count());
        assertEquals(1, c.messages.size());
    }

    @Test
    //if the player selects a card and a slot then the card is added and the resources are removed from the inventory.
    public void chooseDevelopmentCardTest1() {
        DevelopmentCardsVendor dcv = g.getDevelopmentCardsVendor();

        p.getStrongbox().addResource(Resource.SERVANT, 20);
        p.getStrongbox().addResource(Resource.SHIELD, 20);
        p.getStrongbox().addResource(Resource.STONE, 20);
        p.getStrongbox().addResource(Resource.COIN, 20);

        DevelopmentCard[][][] grid = new DevelopmentCard[3][4][4];

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                for (int p = 0; p < 4; p++) {
                    grid[r][c][p] = null;
                }
            }
        }

        grid[2][0][3] = new DevelopmentCard(1, Color.GREEN, 2,
                Map.of(Resource.SHIELD, 1, Resource.SERVANT, 1, Resource.STONE, 1),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.SERVANT, 1)));

        g.getDevelopmentCardsDeck().setGrid(grid);

        assertTrue(am.buyDevelopmentCard(p));
        c.emptyQueue();

        MSG_ACTION_CHOOSE_DEVELOPMENT_CARD msg = new MSG_ACTION_CHOOSE_DEVELOPMENT_CARD(0, 0);

        assertTrue(am.chooseDevelopmentCard(p, msg));

        assertFalse(dcv.isEnabled());

        assertEquals(3, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Strongbox).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_DevDeck).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_DevCardsVendor).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_DevSlot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(7, c.messages.size());
    }

    @Test
    //test that checks the correct functioning of the DiscountResource ability in the ChooseDevelopmentCard method.
    public void chooseDevelopmentCardTest2() {
        DevelopmentCardsVendor dcv = g.getDevelopmentCardsVendor();

        ArrayList<LeaderCard> lc = new ArrayList<>();

        lc.add(new LeaderCard(2,
                new CardRequirements(Map.of(Color.GREEN, new Integer[]{1, -1}, Color.BLUE, new Integer[]{1, -1})),
                new DiscountResource(Resource.STONE)));

        lc.add(new LeaderCard(2,
                new CardRequirements(Map.of(Color.BLUE, new Integer[]{1, -1}, Color.PURPLE, new Integer[]{1, -1})),
                new DiscountResource(Resource.SHIELD)));

        p.associateLeaderCards(lc);
        p.getLeaderCards()[0].setEnable(true);
        p.getLeaderCards()[1].setEnable(true);

        DevelopmentCard[][][] grid = new DevelopmentCard[3][4][4];

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                for (int p = 0; p < 4; p++) {
                    grid[r][c][p] = null;
                }
            }
        }

        grid[2][0][3] = new DevelopmentCard(1, Color.GREEN, 2,
                Map.of(Resource.SHIELD, 1, Resource.SERVANT, 1, Resource.STONE, 1),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.SERVANT, 1)));

        g.getDevelopmentCardsDeck().setGrid(grid);
        p.getStrongbox().addResource(Resource.SERVANT, 1);

        assertTrue(am.buyDevelopmentCard(p));
        c.emptyQueue();

        MSG_ACTION_CHOOSE_DEVELOPMENT_CARD msg = new MSG_ACTION_CHOOSE_DEVELOPMENT_CARD(0, 0);
        assertTrue(am.chooseDevelopmentCard(p, msg));
        assertFalse(dcv.isEnabled());

        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Strongbox).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_DevDeck).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_DevCardsVendor).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_DevSlot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(5, c.messages.size());
    }

    @Test
    //this test checks if the resources are removed correctly
    public void chooseDevelopmentCardTest3() {
        DevelopmentCardsVendor dcv = g.getDevelopmentCardsVendor();

        //creates a card
        DevelopmentCard[][][] grid = new DevelopmentCard[3][4][4];

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                for (int p = 0; p < 4; p++) {
                    grid[r][c][p] = null;
                }
            }
        }

        grid[2][0][3] = new DevelopmentCard(1, Color.GREEN, 2,
                Map.of(Resource.SHIELD, 4, Resource.SERVANT, 5, Resource.COIN, 2),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.SERVANT, 1)));

        g.getDevelopmentCardsDeck().setGrid(grid);

        //adds the resources that are needed
        ArrayList<LeaderCard> cards = new ArrayList<>();
        cards.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SHIELD, 5)),
                new ExtraDepot(Resource.COIN)));
        cards.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SERVANT, 5)),
                new ExtraDepot(Resource.SHIELD)));
        p.associateLeaderCards(cards);
        p.getLeaderCards()[0].setEnable(true);
        p.getLeaderCards()[1].setEnable(true);
        ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).addObserver(c);
        ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).addObserver(c);
        Resource r0 = Resource.COIN;
        Resource[] r1 = new Resource[]{Resource.SHIELD, Resource.SHIELD};
        Resource[] r2 = new Resource[]{Resource.SERVANT, Resource.SERVANT, Resource.SERVANT};
        ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).setResource(1);
        ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).setResource(2);
        p.getWarehouseDepot().setConfig(r0, r1, r2);

        p.getStrongbox().addResource(Resource.SERVANT, 2);

        assertTrue(am.buyDevelopmentCard(p));
        c.emptyQueue();

        MSG_ACTION_CHOOSE_DEVELOPMENT_CARD msg = new MSG_ACTION_CHOOSE_DEVELOPMENT_CARD(0, 0);

        assertTrue(am.chooseDevelopmentCard(p, msg));

        assertFalse(dcv.isEnabled());

        assertEquals(0, p.getWarehouseDepot().getTotal());
        assertEquals(0, p.getStrongbox().getTotal());
        assertEquals(0, ((ExtraDepot) p.getLeaderCards()[0].getSpecialAbility()).getNumber());
        assertEquals(0, ((ExtraDepot) p.getLeaderCards()[1].getSpecialAbility()).getNumber());

        assertEquals(6, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_WarehouseDepot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Strongbox).count());
        assertEquals(3, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_Extradepot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_DevDeck).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_DevCardsVendor).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_DevSlot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(14, c.messages.size());
    }

    @Test
    //endgame 7 cards
    public void chooseDevelopmentCardTest4() {
        DevelopmentCardsVendor dcv = g.getDevelopmentCardsVendor();

        p.getDevelopmentSlot().addCard(g.getDevelopmentCardsDeck().removeCard(2, 0), 0);
        p.getDevelopmentSlot().addCard(g.getDevelopmentCardsDeck().removeCard(2, 0), 1);
        p.getDevelopmentSlot().addCard(g.getDevelopmentCardsDeck().removeCard(2, 0), 2);
        p.getDevelopmentSlot().addCard(g.getDevelopmentCardsDeck().removeCard(1, 0), 0);
        p.getDevelopmentSlot().addCard(g.getDevelopmentCardsDeck().removeCard(1, 0), 1);
        p.getDevelopmentSlot().addCard(g.getDevelopmentCardsDeck().removeCard(1, 0), 2);

        p.getStrongbox().addResource(Resource.SERVANT, 20);
        p.getStrongbox().addResource(Resource.SHIELD, 20);
        p.getStrongbox().addResource(Resource.STONE, 20);
        p.getStrongbox().addResource(Resource.COIN, 20);

        assertTrue(am.buyDevelopmentCard(p));
        c.emptyQueue();
        MSG_ACTION_CHOOSE_DEVELOPMENT_CARD msg = new MSG_ACTION_CHOOSE_DEVELOPMENT_CARD(0, 0);

        assertTrue(am.chooseDevelopmentCard(p, msg));

        assertFalse(dcv.isEnabled());
        assertEquals(Status.LAST_TURN, g.getStatus());

        assertEquals(2, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_DevSlot).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_DevDeck).count());
        assertEquals(1, c.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_DevCardsVendor).count());
    }

    @Test
    //endgame 7 cards SOLO
    public void chooseDevelopmentCardTest5() {
        DevelopmentCardsVendor dcv = g.getDevelopmentCardsVendor();

        GameManager gameManager = new GameManager(1);
        Game game = gameManager.getGame();
        ActionManager actionManager = gameManager.getActionManager();
        Catcher a = new Catcher();
        game.addPlayer("Primo", 1);
        gameManager.addAllObserver(a);
        Player player = game.getPlayer(1);

        player.getDevelopmentSlot().addCard(game.getDevelopmentCardsDeck().removeCard(2, 0), 0);
        player.getDevelopmentSlot().addCard(game.getDevelopmentCardsDeck().removeCard(2, 0), 1);
        player.getDevelopmentSlot().addCard(game.getDevelopmentCardsDeck().removeCard(2, 0), 2);
        player.getDevelopmentSlot().addCard(game.getDevelopmentCardsDeck().removeCard(1, 0), 0);
        player.getDevelopmentSlot().addCard(game.getDevelopmentCardsDeck().removeCard(1, 0), 1);
        player.getDevelopmentSlot().addCard(game.getDevelopmentCardsDeck().removeCard(1, 0), 2);

        player.getStrongbox().addResource(Resource.SERVANT, 20);
        player.getStrongbox().addResource(Resource.SHIELD, 20);
        player.getStrongbox().addResource(Resource.STONE, 20);
        player.getStrongbox().addResource(Resource.COIN, 20);

        assertTrue(actionManager.buyDevelopmentCard(player));

        a.emptyQueue();

        MSG_ACTION_CHOOSE_DEVELOPMENT_CARD msg = new MSG_ACTION_CHOOSE_DEVELOPMENT_CARD(0, 0);

        assertTrue(actionManager.chooseDevelopmentCard(player, msg));

        assertFalse(dcv.isEnabled());
        assertEquals(Status.GAME_OVER, game.getStatus());
        assertTrue(gameManager.getSoloWinner());

        assertEquals(1, a.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_DevSlot).count());
        assertEquals(1, a.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_DevDeck).count());
        assertEquals(2, a.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_NOTIFICATION).count());
        assertEquals(1, a.messages.stream().filter(x -> x.getMessageType() == MessageType.MSG_UPD_DevCardsVendor).count());
    }
}