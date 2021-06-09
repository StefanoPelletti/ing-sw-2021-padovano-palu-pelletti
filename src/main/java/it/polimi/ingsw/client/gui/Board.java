package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.modelSimplified.GameSimplified;
import it.polimi.ingsw.client.modelSimplified.PlayerSimplified;
import it.polimi.ingsw.client.modelSimplified.StrongboxSimplified;
import it.polimi.ingsw.client.modelSimplified.WarehouseDepotSimplified;
import it.polimi.ingsw.networking.message.actionMessages.MSG_ACTION_GET_MARKET_RESOURCES;
import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_Full;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.model.marbles.MarketMarble;
import it.polimi.ingsw.server.model.middles.VendorCard;
import it.polimi.ingsw.server.model.requirements.CardRequirements;
import it.polimi.ingsw.server.model.requirements.ReqValue;
import it.polimi.ingsw.server.model.requirements.ResourceRequirements;
import it.polimi.ingsw.server.model.specialAbilities.DiscountResource;
import it.polimi.ingsw.server.model.specialAbilities.ExtraDepot;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class Board implements Runnable {
    CardLayout cardLayoutLeft;
    CardLayout cardLayoutRight;
    CentralLeftPanel centralLeftPanel;                                                                  //<-- updatable (see the update() methods it offers)
    CentralRightPanel centralRightPanel;                                                                //<-- updatable (see the update() methods it offers)

    JFrame mainFrame;
    MainPanel mainPanel;
    Dimension frameDimension;

    //CENTRAL LEFT PANEL CARDS <- each card is followed by its necessary contents
    String lastLeftCard;

    /*
    //CARDS (CENTRAL RIGHT PANEL)  <- each card is followed by its necessary contents
    */
    String lastRightCard;

    static final String DEVDECK = "Development Cards Deck";
    DevDeck_Panel devDeck_panel;                                                                    //<- updatable after a devdeck update

    static final String MARKET = "Market";
    Market_Panel market_panel;                                                                      //<- updatable

    static final String GETMARKETRESOURCES = "Get Market Resources";
    GetMarketResource_Panel getMarketResource_panel;                                                //<- updatable

    static final String LEADERCARDSPICKER = "Leader Cards Picker";
    LeaderCardsPicker_Panel leaderCardsPicker_panel;                                                //<- updatable

    static final String RESOURCEPICKER = "Resource Picker";
    ResourcePicker_Panel resourcePicker_panel;                                                      //<- updatable

    static final String CHANGEDEPOTCONFIG = "Change Depot Configuration";
    ChangeDepotConfig_Panel changeDepotConfig_panel;                                                //<- updatable

    static final String DISCARDLEADERCARD = "Discard LeaderCard";
    DiscardLeaderCard_Panel discardLeaderCard_panel;                                                //<- updatable

    static final String ACTIVATELEADERCARD = "Activate Leader Card";
    ActivateLeaderCard_Panel activateLeaderCard_panel;                                              //<- updatable

    static final String MARKETHELPER = "Market Helper";
    MarketHelper_Panel marketHelper_panel;                                                          //<- updatable

    static final String VENDOR = "Development Cards Vendor";
    Vendor_Panel vendor_panel;                                                                      //<- updatable

    static final String PRODUCTION = "Production";
    Production_Panel production_panel;                                                              //<- updatable

    static final String PRODSELECTION = "Production Selection";
    ProductionSelection_Panel productionSelection_panel;                                            //<- updatable



    //fonts
    static final String TIMES = "Times New Roman";
    static final String PAP = "Papyrus";




    //STATIC PANELS VARs (LEFT, TOP, BOTTOM)
    //LEFT PANEL
    LeftPanel leftPanel;                                                                            //<-- updatable (see the update() methods it offers)
    JButton show_DevDeck_Button;
    JButton show_Market_Button;
    //BOTTOM PANEL
    JButton activate_LeaderCards_Button, change_Depot_Config_Button, get_MarketResource_Button;
    JButton discard_LeaderCard_Button, buy_DevCard_Button, activate_Production_Button;
    BottomPanel bottomPanel;                                                                        //<-- updatable (see the update() methods it offers)
    JButton endTurn_Button;
    //TOP PANEL
    TopPanel topPanel;                                                                              //<-- updatable (see the update() methods it offers)


    //ACTION LISTENERS
    ActionListener quit_actionListener = e -> {
        leftPanel.updateNotification(" testo di prova per vedere se il testo va a capo automaticamente wtf aiuto");
        leftPanel.updateNotification(" This helps, but if the users clicks anywhere other than after the end of the last line the auto scrolling stops. This is not intuitive. It would be nice if the usual behaviour worked, just by scrolling to the bottom the auto-scroll is reactivated.");
        leftPanel.updateNotification("A work around is possible: you can declare that listener as a class then instantiate it on the event where it is needed. After which you can remove the class after forcing a repaint of the screen. Works like a charm");
        leftPanel.updateNotification("verticalScrollBarMaximumValue = scrollPane.getVerticalScrollBar().getMaximum();");
        leftPanel.updateNotification("I admit that that a method to filter events without extra variables could be found, and would appreciate if somebody post it");
        leftPanel.updateNotification("The Pane then is scrolled down only when vertical scroll bar is expanding, in response to appended lines of text");
        leftPanel.updateNotification("And remember, it's important to shutdown the executor when you've finished. The shutdown() method will cleanly shut down the thread pool when the last task has completed, and will block until this happens. shutdownNow() will terminate the thread pool immediately");
        leftPanel.updateNotification("As a variation of @tangens answer: if you can't wait for the garbage collector to clean up your thread, cancel the timer at the end of your run method");
        leftPanel.updateNotification("Your original question mentions the Swing Timer. If in fact your question is related to SWing, then you should be using the Swing Timer and NOT the util.Timer");

    };
    ActionListener show_DevDeck_actionListener = e -> {
        cardLayoutRight.show(centralRightPanel, DEVDECK);
        cardLayoutLeft.show(centralLeftPanel, Ark.nickname);
    };
    ActionListener show_Market_actionListener = e -> {
        cardLayoutRight.show(centralRightPanel, MARKET);
        cardLayoutLeft.show(centralLeftPanel, Ark.nickname);
    };
    ActionListener show_activateLeaderCard_actionListener = e -> {
        cardLayoutRight.show(centralRightPanel, ACTIVATELEADERCARD);
    };
    ActionListener show_changeDepotConfig_actionListener = e -> {
        cardLayoutRight.show(centralRightPanel, CHANGEDEPOTCONFIG);
    };
    ActionListener show_getMarketResource_actionListener = e -> {
        cardLayoutRight.show(centralRightPanel, GETMARKETRESOURCES);
        cardLayoutLeft.show(centralLeftPanel, Ark.nickname);
    };
    ActionListener show_discardLeaderCard_actionListener = e -> {
        cardLayoutRight.show(centralRightPanel, DISCARDLEADERCARD);
        cardLayoutLeft.show(centralLeftPanel, Ark.nickname);
    };
    ActionListener show_ProductionSelection_actionListener = e -> {
        cardLayoutRight.show(centralRightPanel, PRODSELECTION);
    };
    ActionListener back_show_DevDeck_actionListener = e -> {
        cardLayoutRight.show(centralRightPanel, lastRightCard);
    };
    ActionListener back_show_Market_actionListener = e -> {
        cardLayoutRight.show(centralRightPanel, lastRightCard);
    };
    ActionListener back_getMarketResource_actionListener = e -> {
        cardLayoutRight.show(centralRightPanel, lastRightCard);
    };
    ActionListener back_discardLeaderCard_actionListener = e -> {
        cardLayoutRight.show(centralRightPanel, lastRightCard);
    };
    ActionListener back_activateLeaderCard_actionListener = e -> {
        cardLayoutRight.show(centralRightPanel, lastRightCard);
    };
    ActionListener back_changeDepotConfig_actionListener = e -> {
        cardLayoutRight.show(centralRightPanel, lastRightCard);
    };
    ActionListener back_othersDevSlot_actionListener = e -> {
        cardLayoutRight.show(centralRightPanel, Ark.nickname);
    };
    ActionListener init_leaderCardsPicker_actionListener = e -> {

        int firstCard = leaderCardsPicker_panel.getFirst();
        int secondCard = leaderCardsPicker_panel.getSecond();

    };
    ActionListener init_resourcePicker_actionListener = e -> {
        ResourcePicker_Panel.CustomResourceButton source = (ResourcePicker_Panel.CustomResourceButton) e.getSource();
        Resource resource = source.getResource();

        int resourceLeft = Ark.game.getResourcePicker().getNumOfResources();

    };
    ActionListener action_endTurn_actionListener = e -> {

    };
    ActionListener action_buyDevCard_actionListener = e -> {

    };
    ActionListener action_changeDepotConfig_actionListener = e -> {
        Resource shelf1 = changeDepotConfig_panel.getShelf1();
        Resource[] shelf2 = changeDepotConfig_panel.getShelf2();
        Resource[] shelf3 = changeDepotConfig_panel.getShelf3();

        int extraDepot1 = changeDepotConfig_panel.getFirstDepotQuantity();
        int extraDepot2 = changeDepotConfig_panel.getSecondDepotQuantity();

    };
    ActionListener action_getMarketResource_actionListener = e -> {
        GetMarketResource_Panel.MarketButton source = (GetMarketResource_Panel.MarketButton) e.getSource();

        int number = source.getNum();
        boolean column = source.isColumn();

    };
    ActionListener action_newMarketChoice_actionListener = e -> {
        MarketHelper_Panel.ChoiceButton source = (MarketHelper_Panel.ChoiceButton) e.getSource();

        int choiceNumber = source.getChoiceNumber();

    };
    ActionListener action_activateLeaderCard_actionListener = e -> {
        ActivateLeaderCard_Panel.ChooseLeaderCardButton source = (ActivateLeaderCard_Panel.ChooseLeaderCardButton) e.getSource();

        int cardNumber = source.getNumber();

    };
    ActionListener action_discardLeaderCard_actionListener = e -> {
        DiscardLeaderCard_Panel.ChooseLeaderCardButton source = (DiscardLeaderCard_Panel.ChooseLeaderCardButton) e.getSource();

        int cardNumber = source.getNumber();

    };
    ActionListener action_chooseDevCard_actionListener = e -> {
        Vendor_Panel.SlotButton source = (Vendor_Panel.SlotButton) e.getSource();

        int slotNumber = source.getPosition();
        int cardNumber = vendor_panel.getCurrentCard();
    };


    public Board() {
        mainFrame = new JFrame("Huh?");
        mainPanel = new MainPanel();
        mainFrame.setContentPane(mainPanel);


        mainFrame.pack();

        //frameDimension = new Dimension(1630, 1030);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //mainFrame.setMinimumSize(frameDimension);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        // you can write here shortcuts, like going directly to the Settings and opening multiple frames
        // only if called by the main, otherwise it must be empty
        // new MainMenu();


        Ark.solo = false;
        Ark.nickname = "A";
        Ark.myPlayerNumber = 1;
        Ark.game = new GameSimplified();

        Ark.gameManager = new GameManager(4);
        Ark.actionManager = Ark.gameManager.getActionManager();
        Game game = Ark.gameManager.getGame();
        game.addPlayer("A", 1);
        game.addPlayer("B", 2);
        game.addPlayer("C", 3);
        game.addPlayer("D", 4);
        Player A = game.getPlayer("A");
        Player B = game.getPlayer("B");
        Player C = game.getPlayer("C");
        Player D = game.getPlayer("D");
        game.setLeaderCardsPickerCards(new java.util.ArrayList<LeaderCard>());
        Ark.gameManager.getFaithTrackManager().advance(A);
        Ark.gameManager.getFaithTrackManager().advance(A);
        for (int i = 0; i < 4; i++) {
            Ark.gameManager.getFaithTrackManager().advance(B);
        }
        for (int i = 0; i < 6; i++) {
            Ark.gameManager.getFaithTrackManager().advance(C);
        }
        for (int i = 0; i < 15; i++) {
            Ark.gameManager.getFaithTrackManager().advance(D);
        }

        List<VendorCard> buyable = new ArrayList<>();
        buyable.add(new VendorCard(new DevelopmentCard(1, it.polimi.ingsw.server.model.enumerators.Color.GREEN, 12,
                Map.of(Resource.SHIELD, 4, Resource.COIN, 4),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.COIN, 3, Resource.SHIELD, 1)),
                "resources/cardsFront/DFRONT (46).png", "resources/cardsBack/BACK (10)"), true, false, true));

        buyable.add(new VendorCard(new DevelopmentCard(2, it.polimi.ingsw.server.model.enumerators.Color.YELLOW, 5,
                Map.of(Resource.STONE, 4),
                new Power(Map.of(Resource.SHIELD, 1),
                        Map.of(Resource.FAITH, 2)),
                "resources/cardsFront/DFRONT (21).png", "resources/cardsBack/BACK (9)"), false, true, true));

        buyable.add(new VendorCard(new DevelopmentCard(1, it.polimi.ingsw.server.model.enumerators.Color.GREEN, 3,
                Map.of(Resource.SHIELD, 3),
                new Power(Map.of(Resource.SERVANT, 2),
                        Map.of(Resource.COIN, 1, Resource.SHIELD, 1, Resource.STONE, 1)),
                "resources/cardsFront/DFRONT (10).png", "resources/cardsBack/BACK (2)"), false, false, true));
        game.getDevelopmentCardsVendor().setCards(buyable);
        game.getDevelopmentCardsVendor().setEnabled(true);


        ArrayList<LeaderCard> list = new ArrayList<>();
        list.add(new LeaderCard(2,
                new CardRequirements(Map.of(it.polimi.ingsw.server.model.enumerators.Color.YELLOW, new ReqValue(1, -1), it.polimi.ingsw.server.model.enumerators.Color.GREEN, new ReqValue(1, -1))),
                new DiscountResource(Resource.SERVANT)
                , "resources/cardsFront/LFRONT (1).png", "resources/cardsBack/BACK (1).png"));
        list.add(new LeaderCard(2,
                new CardRequirements(Map.of(it.polimi.ingsw.server.model.enumerators.Color.BLUE, new ReqValue(1, -1), it.polimi.ingsw.server.model.enumerators.Color.PURPLE, new ReqValue(1, -1))),
                new DiscountResource(Resource.SHIELD)
                , "resources/cardsFront/LFRONT (2).png", "resource/cardsBack/BACK (1).png"));
        B.associateLeaderCards(list);
        list.clear();
        list.add(new LeaderCard(2,
                new CardRequirements(Map.of(it.polimi.ingsw.server.model.enumerators.Color.GREEN, new ReqValue(1, -1), it.polimi.ingsw.server.model.enumerators.Color.BLUE, new ReqValue(1, -1))),
                new DiscountResource(Resource.STONE)
                , "resources/cardsFront/LFRONT (3).png", "resource/cardsBack/BACK (1).png"));
        list.add(new LeaderCard(2,
                new CardRequirements(Map.of(it.polimi.ingsw.server.model.enumerators.Color.YELLOW, new ReqValue(1, -1), it.polimi.ingsw.server.model.enumerators.Color.PURPLE, new ReqValue(1, -1))),
                new DiscountResource(Resource.COIN)
                , "resources/cardsFront/LFRONT (4).png", "resource/cardsBack/BACK (1).png"));
        C.associateLeaderCards(list);
        list.clear();
        list.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.COIN, 5)),
                new ExtraDepot(Resource.STONE)
                , "resources/cardsFront/LFRONT (5).png", "resource/cardsBack/BACK (1).png"));
        list.add(new LeaderCard(3,
                new ResourceRequirements(Map.of(Resource.SERVANT, 5)),
                new ExtraDepot(Resource.SHIELD)
                , "resources/cardsFront/LFRONT (6).png", "resource/cardsBack/BACK (1).png"));
        A.associateLeaderCards(list);
        A.setFaithTrackPanels(1);
        A.setFaithTrackPanels(0);

        B.getLeaderCards()[0].setEnabled(true);
        A.getLeaderCards()[0].setEnabled(true);
        Ark.actionManager.getMarketResources(A, new MSG_ACTION_GET_MARKET_RESOURCES(false, 0));

        A.getWarehouseDepot().add(Resource.SERVANT);
        A.getWarehouseDepot().add(Resource.COIN);
        A.getWarehouseDepot().add(Resource.COIN);
        A.getWarehouseDepot().add(Resource.SHIELD);

        B.getWarehouseDepot().add(Resource.STONE);
        B.getWarehouseDepot().add(Resource.SERVANT);
        B.getWarehouseDepot().add(Resource.SERVANT);

        C.getWarehouseDepot().add(Resource.SERVANT);
        C.getWarehouseDepot().swapRow(1, 2);

        D.getWarehouseDepot().add(Resource.SHIELD);
        D.getWarehouseDepot().swapRow(1, 3);
        D.getWarehouseDepot().add(Resource.SHIELD);
        D.getWarehouseDepot().add(Resource.SHIELD);

        A.getStrongbox().addResource(Resource.SHIELD, 1);
        A.getStrongbox().addResource(Resource.COIN, 1);
        A.getStrongbox().addResource(Resource.STONE, 1);
        A.getStrongbox().addResource(Resource.SERVANT, 1);

        B.getStrongbox().addResource(Resource.COIN, 1);
        B.getStrongbox().addResource(Resource.SHIELD, 5);

        C.getStrongbox().addResource(Resource.SERVANT, 3);
        C.getStrongbox().addResource(Resource.COIN, 2);
        C.getStrongbox().addResource(Resource.SHIELD, 5);

        D.getStrongbox().addResource(Resource.STONE, 10);
        D.getStrongbox().addResource(Resource.SHIELD, 11);
        D.getStrongbox().addResource(Resource.COIN, 9);
        D.getStrongbox().addResource(Resource.SERVANT, 12);

        D.getDevelopmentSlot().addCard(new DevelopmentCard(1, it.polimi.ingsw.server.model.enumerators.Color.GREEN, 12,
                Map.of(Resource.SHIELD, 4, Resource.COIN, 4),
                new Power(Map.of(Resource.STONE, 1),
                        Map.of(Resource.COIN, 3, Resource.SHIELD, 1)),
                "resources/cardsFront/DFRONT (46).png", "resources/cardsBack/BACK (10)"
        ), 2);

        D.getDevelopmentSlot().addCard(new DevelopmentCard(2, it.polimi.ingsw.server.model.enumerators.Color.GREEN, 11,
                Map.of(Resource.SHIELD, 7),
                new Power(Map.of(Resource.SERVANT, 1),
                        Map.of(Resource.COIN, 1, Resource.FAITH, 3)),
                "resources/cardsFront/DFRONT (42).png", "resources/cardsBack/BACK (10)"
        ), 2);

        D.getDevelopmentSlot().addCard(new DevelopmentCard(1, it.polimi.ingsw.server.model.enumerators.Color.YELLOW, 1,
                Map.of(Resource.STONE, 2),
                new Power(Map.of(Resource.SERVANT, 1),
                        Map.of(Resource.FAITH, 1)),
                "resources/cardsFront/DFRONT (5).png", "resources/cardsBack/BACK (5)"
        ), 1);

        A.getDevelopmentSlot().addCard(new DevelopmentCard(1, it.polimi.ingsw.server.model.enumerators.Color.GREEN, 3,
                Map.of(Resource.SHIELD, 3),
                new Power(Map.of(Resource.SERVANT, 2),
                        Map.of(Resource.COIN, 1, Resource.SHIELD, 1, Resource.STONE, 1)),
                "resources/cardsFront/DFRONT (10).png", "resources/cardsBack/BACK (2)"
        ), 0);

        A.getDevelopmentSlot().addCard(new DevelopmentCard(1, it.polimi.ingsw.server.model.enumerators.Color.GREEN, 4,
                Map.of(Resource.SHIELD, 2, Resource.COIN, 2),
                new Power(Map.of(Resource.STONE, 1, Resource.SERVANT, 1),
                        Map.of(Resource.COIN, 2, Resource.FAITH, 1)),
                "resources/cardsFront/DFRONT (14).png", "resources/cardsBack/BACK (2)"
        ), 1);

        A.getDevelopmentSlot().addCard(new DevelopmentCard(2, it.polimi.ingsw.server.model.enumerators.Color.YELLOW, 5,
                Map.of(Resource.STONE, 4),
                new Power(Map.of(Resource.SHIELD, 1),
                        Map.of(Resource.FAITH, 2)),
                "resources/cardsFront/DFRONT (21).png", "resources/cardsBack/BACK (9)"
        ), 1);

        B.getDevelopmentSlot().addCard(new DevelopmentCard(1, it.polimi.ingsw.server.model.enumerators.Color.GREEN, 1,
                Map.of(Resource.SHIELD, 2),
                new Power(Map.of(Resource.COIN, 1),
                        Map.of(Resource.FAITH, 1)),
                "resources/cardsFront/DFRONT (2).png", "resources/cardsBack/BACK (2)"
        ), 1);

        B.getDevelopmentSlot().addCard(new DevelopmentCard(2, it.polimi.ingsw.server.model.enumerators.Color.YELLOW, 5,
                Map.of(Resource.STONE, 4),
                new Power(Map.of(Resource.SHIELD, 1),
                        Map.of(Resource.FAITH, 2)),
                "resources/cardsFront/DFRONT (21).png", "resources/cardsBack/BACK (9)"
        ), 1);

        B.getDevelopmentSlot().addCard(new DevelopmentCard(3, it.polimi.ingsw.server.model.enumerators.Color.YELLOW, 9,
                Map.of(Resource.STONE, 6),
                new Power(Map.of(Resource.SHIELD, 2),
                        Map.of(Resource.SERVANT, 3, Resource.FAITH, 2)),
                "resources/cardsFront/DFRONT (37).png", "resources/cardsBack/BACK (13)"
        ), 1);

        C.getDevelopmentSlot().addCard(new DevelopmentCard(1, it.polimi.ingsw.server.model.enumerators.Color.YELLOW, 2,
                Map.of(Resource.SHIELD, 1, Resource.STONE, 1, Resource.COIN, 1),
                new Power(Map.of(Resource.SHIELD, 1),
                        Map.of(Resource.COIN, 1)),
                "resources/cardsFront/DFRONT (9).png", "resources/cardsBack/BACK (5)"
        ), 1);

        game.setLeaderCardsPickerCards(game.getCurrentPlayerStartingCards());

        Ark.game = new GameSimplified();
        MSG_UPD_Full message = Ark.gameManager.getFullModel();
        Ark.game.updateAll(message);
        Ark.myPlayerRef = Ark.game.getPlayerRef(1);
        SwingUtilities.invokeLater(new Board());

    }

    //HELPER METHODS (graphics)
    public static void addPadding(JComponent object, int height, int width, int maximumWidth, int maximumHeight) {
        GridBagConstraints c;

        c = new GridBagConstraints(); //padding horizontal
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = maximumWidth;
        object.add(Box.createHorizontalStrut(width),c);

        c = new GridBagConstraints(); //padding vertical
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = maximumHeight;
        c.gridwidth = 1;
        object.add(Box.createVerticalStrut(height),c);
    }

    public void changeLeftCard(String newCard) {
        this.cardLayoutLeft.show(centralLeftPanel, newCard);
    }

    public void changeRightCard(String newCard) {
        this.cardLayoutRight.show(centralRightPanel, newCard);
    }

    public void run() {
        //empty to implement run method
    }

    public ImageIcon scaleImage(ImageIcon icon, int squareDimension) {
        return scaleImage(icon, squareDimension, squareDimension);
    }

    public ImageIcon scaleImage(ImageIcon icon, int width, int height) {
        int newWidth = icon.getIconWidth();
        int newHeight = icon.getIconHeight();
        if (icon.getIconWidth() > width) {
            newWidth = width;
            newHeight = (newWidth * icon.getIconHeight()) / icon.getIconWidth();
        }
        if (newHeight > height) {
            newHeight = height;
            newWidth = (icon.getIconWidth() * newHeight) / icon.getIconHeight();
        }
        return new ImageIcon(icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH));
    }

    //HELPER METHODS (non Graphics)
    public String resourceToString(Resource resource, boolean plural) {
        String result = "";
        switch (resource) {
            case COIN:
                result += "Coin";
                break;
            case SHIELD:
                result += "Shield";
                break;
            case STONE:
                result += "Stone";
                break;
            case SERVANT:
                result += "Servant";
                break;
        }
        if (plural)
            result += "s";
        return result;
    }

    // this is the custom, new contentPane to set in the mainFrame.
    class MainPanel extends JPanel {

        private Image image;

        public MainPanel() {
            try {
                image = ImageIO.read(new File("resources/images/board_bg.png"));
            } catch (IOException e) {
            }

            {
                GridBagConstraints c;
                this.setLayout(new GridBagLayout());

                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                c.gridwidth = 1;
                c.gridheight = 2;
                leftPanel = new LeftPanel();
                this.add(leftPanel, c);

                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 0;
                c.gridwidth = 2;
                c.gridheight = 1;
                topPanel = new TopPanel();
                this.add(topPanel, c);

                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 2;
                c.gridwidth = 3;
                c.gridheight = 1;
                bottomPanel = new BottomPanel();
                this.add(bottomPanel, c);

                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 1;
                c.gridwidth = 1;
                c.gridheight = 1;
                centralLeftPanel = new CentralLeftPanel();
                this.add(centralLeftPanel, c);

                c = new GridBagConstraints();
                c.gridx = 2;
                c.gridy = 1;
                c.gridwidth = 1;
                c.gridheight = 1;
                centralRightPanel = new CentralRightPanel();
                this.add(centralRightPanel, c);
            }

            //INITIAL UPDATE (?)
            {
                leftPanel.updateAll();
                bottomPanel.update();
                devDeck_panel.update();
                for (PlayerSimplified player : Ark.game.getPlayerList()) {
                    centralLeftPanel.update(player.getNickname());
                    centralRightPanel.update(player.getNickname());
                }
                leaderCardsPicker_panel.update();
                changeDepotConfig_panel.update();
                market_panel.update();
                getMarketResource_panel.update();
                changeDepotConfig_panel.update();
                discardLeaderCard_panel.update();
                activateLeaderCard_panel.update();
                resourcePicker_panel.update();
                marketHelper_panel.update();
                vendor_panel.update();
                productionSelection_panel.update();
                topPanel.update();
            }

            lastRightCard = Ark.nickname;
            lastLeftCard = Ark.nickname;

            //controls for first player, eg leadercardpicker enabled
            changeRightCard(Ark.nickname);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }

    }

    //LEFT PANEL
    class LeftPanel extends JPanel {

        JLabel turnLabel, turnOf; //<- updatable, but not with a custom method
        JTextArea notificationsArea; //<- updatable, but not with a custom method
        MyLeaderCardsPanel myLeaderCardsPanel; //<- updatable for leaderCards of this player and extradepot with a single update()

        public LeftPanel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBackground(new Color(215, 200, 145));
            this.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, new Color(62, 43, 9)));

            //addPadding(this, 839, 458, 3, 6);
            addPadding(this, 839, 458, 3, 6);

            JButton quit_Button = new JButton("Quit");
            quit_Button.addActionListener(quit_actionListener);
            quit_Button.setPreferredSize(new Dimension(120, 60));
            quit_Button.setFont(new Font(PAP, Font.BOLD, 20));
            quit_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 2;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.insets = new Insets(20, 20, 0, 0);
            this.add(quit_Button, c);

            turnLabel = new JLabel();
            turnLabel.setOpaque(false);
            turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
            turnLabel.setFont(new Font(PAP, Font.BOLD, 20));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(20, 0, 0, 0);
            this.add(turnLabel, c);

            turnOf = new JLabel();
            turnOf.setOpaque(false);
            turnOf.setHorizontalAlignment(SwingConstants.CENTER);
            turnOf.setFont(new Font(PAP, Font.BOLD, 20));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 2;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.fill = GridBagConstraints.HORIZONTAL;
            this.add(turnOf, c);

            notificationsArea = new JTextArea();
            notificationsArea.setFont(new Font(TIMES, Font.BOLD, 20));
            notificationsArea.setBackground(new Color(231, 210, 181));
            notificationsArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

            JScrollPane scrollPane = new JScrollPane(notificationsArea);
            scrollPane.setPreferredSize(new Dimension(400, 200));
            scrollPane.getHorizontalScrollBar().setBackground(new Color(222, 209, 156));
            scrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = new Color(178, 49, 35);
                }
            });
            scrollPane.getVerticalScrollBar().setBackground(new Color(222, 209, 156));
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = new Color(178, 49, 35);
                }
            });
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 3;
            c.gridwidth = 2;
            c.gridheight = 1;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(20, 0, 0, 0);
            this.add(scrollPane, c);

            myLeaderCardsPanel = new MyLeaderCardsPanel();
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 4;
            c.weighty = 0.1;
            c.weightx = 0.5;
            c.gridwidth = 2;
            c.gridheight = 1;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(10, 0, 0, 0);
            this.add(myLeaderCardsPanel, c);


            {
                JPanel showButtonsPanel = new JPanel();
                showButtonsPanel.setOpaque(false);
                showButtonsPanel.setLayout(new GridBagLayout());

                show_DevDeck_Button = new JButton("show DevDeck");
                show_DevDeck_Button.addActionListener(show_DevDeck_actionListener);
                show_DevDeck_Button.setPreferredSize(new Dimension(200, 70));
                show_DevDeck_Button.setFont(new Font(PAP, Font.BOLD, 20));
                show_DevDeck_Button.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                c.weightx = 0.5;
                c.weighty = 0.5;
                c.insets = new Insets(5, 5, 20, 12);
                c.anchor = GridBagConstraints.LINE_START;
                showButtonsPanel.add(show_DevDeck_Button, c);

                show_Market_Button = new JButton("show Market");
                show_Market_Button.addActionListener(show_Market_actionListener);
                show_Market_Button.setPreferredSize(new Dimension(200, 70));
                show_Market_Button.setFont(new Font(PAP, Font.BOLD, 20));
                show_Market_Button.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 0;
                c.weightx = 0.5;
                c.weighty = 0.5;
                c.insets = new Insets(5, 12, 20, 5);
                c.anchor = GridBagConstraints.LINE_END;
                showButtonsPanel.add(show_Market_Button, c);

                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 5;
                c.weightx = 0.5;
                c.weighty = 0.1;
                c.gridwidth = 2;
                c.gridheight = 1;
                c.anchor = GridBagConstraints.PAGE_END;
                this.add(showButtonsPanel, c);
            } //showDevDeck and showMarket buttons Panel
        }

        public void updateAll() {
            this.update();
            this.updateTurnOf();
            this.updateCurrentTurn();
        }

        public void update() {
            this.myLeaderCardsPanel.update();
        }

        public void updateTurnOf() {
            this.turnOf.setText("Turn of " + Ark.game.getCurrentPlayerName());
        }

        public void updateCurrentTurn() {
            this.turnLabel.setText("Turn " + Ark.game.getTurn());
        }

        public void updateNotification(String message) {
            Formatter formate;
            Calendar gfg_calender = Calendar.getInstance();
            formate = new Formatter();
            formate.format("%tl:%tM", gfg_calender, gfg_calender);

            // Printing the current hour and minute
            notificationsArea.append("\n" + formate + " - ");

            int i = 7;

            List<String> textList;
            textList = new ArrayList<>((Arrays.asList(message.split("\\s+"))));
            Optional<Integer> length = textList.stream().map(String::length).reduce(Integer::sum);
            if (length.isPresent()) {
                for (String s : textList) {
                    if ((s.length() + i) > 35) {
                        i = s.length();
                        notificationsArea.append("\n" + s + " ");
                    } else {
                        i += s.length();
                        notificationsArea.append(s + " ");
                    }
                }
            }
        }
    }

    class MyLeaderCardsPanel extends JPanel {

        JLabel leaderCardLabel1, leaderCardLabel2;
        JLabel extraResource1_LeaderCard1_Label, extraResource2_LeaderCard1_Label;
        JLabel extraResource1_LeaderCard2_Label, extraResource2_LeaderCard2_Label;
        JLabel labelUnderLeaderCard1, labelUnderLeaderCard2;

        public MyLeaderCardsPanel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);

            JLabel leaderCardLabel1text = new JLabel();
            leaderCardLabel1text.setText("Leader Card");
            leaderCardLabel1text.setOpaque(false);
            leaderCardLabel1text.setHorizontalAlignment(SwingConstants.CENTER);
            leaderCardLabel1text.setFont(new Font(PAP, Font.BOLD, 20));
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.weighty = 0.1;
            c.weightx = 0.5;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(0, 0, 0, 0);
            this.add(leaderCardLabel1text, c);

            JLabel leaderCardLabel1text1 = new JLabel();
            leaderCardLabel1text1.setText("#1");
            leaderCardLabel1text1.setOpaque(false);
            leaderCardLabel1text1.setHorizontalAlignment(SwingConstants.CENTER);
            leaderCardLabel1text1.setFont(new Font(PAP, Font.BOLD, 22));
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 1;
            c.weighty = 0.1;
            c.weightx = 0.5;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(0, 0, 0, 0);
            this.add(leaderCardLabel1text1, c);

            JLabel leaderCardLabel2text = new JLabel();
            leaderCardLabel2text.setText("Leader Card");
            leaderCardLabel2text.setOpaque(false);
            leaderCardLabel2text.setHorizontalAlignment(SwingConstants.CENTER);
            leaderCardLabel2text.setFont(new Font(PAP, Font.BOLD, 20));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 0;
            c.weighty = 0.1;
            c.weightx = 0.5;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(0, 0, 0, 0);
            this.add(leaderCardLabel2text, c);

            JLabel leaderCardLabel2text2 = new JLabel();
            leaderCardLabel2text2.setText("#2");
            leaderCardLabel2text2.setOpaque(false);
            leaderCardLabel2text2.setHorizontalAlignment(SwingConstants.CENTER);
            leaderCardLabel2text2.setFont(new Font(PAP, Font.BOLD, 22));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 1;
            c.weighty = 0.1;
            c.weightx = 0.5;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(0, 0, 0, 0);
            this.add(leaderCardLabel2text2, c);

            leaderCardLabel1 = new JLabel();
            leaderCardLabel1.setLayout(null);

            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 2;
            c.weighty = 0.1;
            c.weightx = 0.5;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(0, 0, 0, 0);
            this.add(leaderCardLabel1, c);

            extraResource1_LeaderCard1_Label = new JLabel();
            extraResource1_LeaderCard1_Label.setBounds(40, 234, 50, 50);
            leaderCardLabel1.add(extraResource1_LeaderCard1_Label);

            extraResource2_LeaderCard1_Label = new JLabel();
            extraResource2_LeaderCard1_Label.setBounds(115, 234, 50, 50);
            leaderCardLabel1.add(extraResource2_LeaderCard1_Label);

            labelUnderLeaderCard1 = new JLabel();
            labelUnderLeaderCard1.setOpaque(false);
            labelUnderLeaderCard1.setHorizontalAlignment(SwingConstants.CENTER);
            labelUnderLeaderCard1.setFont(new Font(PAP, Font.BOLD, 22));
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 3;
            c.anchor = GridBagConstraints.PAGE_START;
            this.add(labelUnderLeaderCard1, c);

            leaderCardLabel2 = new JLabel();
            leaderCardLabel2.setLayout(null);

            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 2;
            c.weighty = 0.1;
            c.weightx = 0.5;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(0, 0, 0, 0);
            this.add(leaderCardLabel2, c);

            extraResource1_LeaderCard2_Label = new JLabel();
            extraResource1_LeaderCard2_Label.setBounds(40, 234, 50, 50);
            leaderCardLabel2.add(extraResource1_LeaderCard2_Label, c);

            extraResource2_LeaderCard2_Label = new JLabel();
            extraResource2_LeaderCard2_Label.setBounds(115, 234, 50, 50);
            leaderCardLabel2.add(extraResource2_LeaderCard2_Label, c);

            labelUnderLeaderCard2 = new JLabel();
            labelUnderLeaderCard2.setOpaque(false);
            labelUnderLeaderCard2.setHorizontalAlignment(SwingConstants.CENTER);
            labelUnderLeaderCard2.setFont(new Font(PAP, Font.BOLD, 22));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 3;
            c.anchor = GridBagConstraints.PAGE_START;
            this.add(labelUnderLeaderCard2, c);

            JLabel spacer = new JLabel("");
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 0;
            c.weighty = 0.1;
            c.weightx = 0.5;
            c.gridheight = 3;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(0, 10, 0, 10);
            this.add(spacer, c);
        }

        public void update() {
            LeaderCard l1 = Ark.myPlayerRef.getLeaderCards()[0];
            updateSpecificCard(l1, labelUnderLeaderCard1, leaderCardLabel1, extraResource1_LeaderCard1_Label, extraResource2_LeaderCard1_Label);

            LeaderCard l2 = Ark.myPlayerRef.getLeaderCards()[1];
            updateSpecificCard(l2, labelUnderLeaderCard2, leaderCardLabel2, extraResource1_LeaderCard2_Label, extraResource2_LeaderCard2_Label);
        }

        private void updateSpecificCard(LeaderCard leaderCard, JLabel labelUnderLeaderCard, JLabel leaderCardLabel, JLabel extraDepotLabel1, JLabel extraDepotLabel2) {
            ImageIcon t;
            if (leaderCard != null) {
                t = scaleImage(new ImageIcon(leaderCard.getFrontPath()), 300);
                if (leaderCard.isEnabled()) {
                    labelUnderLeaderCard.setText("ENABLED!");
                    leaderCardLabel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                    if (leaderCard.getSpecialAbility().isExtraDepot()) {
                        ExtraDepot depot = ((ExtraDepot) leaderCard.getSpecialAbility());
                        int num = depot.getNumber();
                        Resource type = depot.getResourceType();

                        ImageIcon presentIcon = scaleImage(new ImageIcon(type.getPathLittle()), 50);
                        ImageIcon noneIcon = scaleImage(new ImageIcon(Resource.NONE.getPathLittle()), 50);
                        switch (num) {
                            case 0:
                                extraDepotLabel1.setIcon(noneIcon);
                                extraDepotLabel2.setIcon(noneIcon);
                                break;
                            case 1:
                                extraDepotLabel1.setIcon(presentIcon);
                                extraDepotLabel2.setIcon(noneIcon);
                                break;
                            case 2:
                                extraDepotLabel1.setIcon(presentIcon);
                                extraDepotLabel2.setIcon(presentIcon);
                                break;
                        }
                    }
                }
            } else {
                t = scaleImage(new ImageIcon("resources/cardsBack/BACK (1).png"), 300);
                labelUnderLeaderCard.setText("not present!");
            }
            leaderCardLabel.setIcon(t);
        }
    }

    //BOTTOM PANEL
    class BottomPanel extends JPanel {

        PlayersRecapPanel playersRecapPanel;

        public BottomPanel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBackground(new Color(215, 200, 145));
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9), 1));

            addPadding(this, 158, 1678, 6, 3);

            {
                activate_LeaderCards_Button = new JButton("activate card");
                activate_LeaderCards_Button.addActionListener(show_activateLeaderCard_actionListener);
                activate_LeaderCards_Button.setPreferredSize(new Dimension(145, 60));
                activate_LeaderCards_Button.setFont(new Font(PAP, Font.BOLD, 18));
                activate_LeaderCards_Button.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 1;
                c.weighty = 0.5;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.anchor = GridBagConstraints.LINE_START;
                c.insets = new Insets(15, 8, 0, 2);
                this.add(activate_LeaderCards_Button, c);

                change_Depot_Config_Button = new JButton("modify depot");
                change_Depot_Config_Button.addActionListener(show_changeDepotConfig_actionListener);
                change_Depot_Config_Button.setPreferredSize(new Dimension(145, 60));
                change_Depot_Config_Button.setFont(new Font(PAP, Font.BOLD, 18));
                change_Depot_Config_Button.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 2;
                c.gridy = 1;
                c.weighty = 0.5;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.anchor = GridBagConstraints.LINE_START;
                c.insets = new Insets(15, 2, 0, 2);
                this.add(change_Depot_Config_Button, c);

                get_MarketResource_Button = new JButton("go to market");
                get_MarketResource_Button.addActionListener(show_getMarketResource_actionListener);
                get_MarketResource_Button.setPreferredSize(new Dimension(145, 60));
                get_MarketResource_Button.setFont(new Font(PAP, Font.BOLD, 18));
                get_MarketResource_Button.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 3;
                c.gridy = 1;
                c.weightx = 0.5;
                c.weighty = 0.5;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.anchor = GridBagConstraints.LINE_START;
                c.insets = new Insets(15, 2, 0, 6);
                this.add(get_MarketResource_Button, c);
            } //top row of action buttons

            {
                discard_LeaderCard_Button = new JButton("discard card");
                discard_LeaderCard_Button.addActionListener(show_discardLeaderCard_actionListener);
                discard_LeaderCard_Button.setPreferredSize(new Dimension(145, 60));
                discard_LeaderCard_Button.setFont(new Font(PAP, Font.BOLD, 18));
                discard_LeaderCard_Button.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 2;
                c.weightx = 0.5;
                c.weighty = 0.5;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.anchor = GridBagConstraints.LINE_START;
                c.insets = new Insets(0, 8, 15, 2);
                this.add(discard_LeaderCard_Button, c);

                buy_DevCard_Button = new JButton("buy card");
                buy_DevCard_Button.addActionListener(action_buyDevCard_actionListener);
                buy_DevCard_Button.setPreferredSize(new Dimension(145, 60));
                buy_DevCard_Button.setFont(new Font(PAP, Font.BOLD, 18));
                buy_DevCard_Button.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 2;
                c.gridy = 2;
                c.weightx = 0.5;
                c.weighty = 0.5;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.anchor = GridBagConstraints.LINE_START;
                c.insets = new Insets(0, 2, 15, 2);
                this.add(buy_DevCard_Button, c);

                activate_Production_Button = new JButton("produce");
                activate_Production_Button.addActionListener(show_ProductionSelection_actionListener);
                activate_Production_Button.setPreferredSize(new Dimension(145, 60));
                activate_Production_Button.setFont(new Font(PAP, Font.BOLD, 18));
                activate_Production_Button.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 3;
                c.gridy = 2;
                c.weightx = 0.5;
                c.weighty = 0.5;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.anchor = GridBagConstraints.LINE_START;
                c.insets = new Insets(0, 2, 15, 6);
                this.add(activate_Production_Button, c);
            } //bottom row of action buttons

            playersRecapPanel = new PlayersRecapPanel();
            playersRecapPanel.setPreferredSize(new Dimension(1060, 150));
            c = new GridBagConstraints();
            c.gridx = 4;
            c.gridy = 1;
            c.weighty = 0.5;
            c.weightx = 0.5;
            c.gridwidth = 1;
            c.gridheight = 2;
            this.add(playersRecapPanel, c);

            endTurn_Button = new JButton("end Turn");
            endTurn_Button.addActionListener(action_endTurn_actionListener);
            endTurn_Button.setPreferredSize(new Dimension(145, 140));
            endTurn_Button.setFont(new Font(PAP, Font.BOLD, 24));
            endTurn_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 5;
            c.gridy = 1;
            c.weighty = 0.5;
            c.weightx = 0.5;
            c.gridwidth = 1;
            c.gridheight = 2;
            c.insets = new Insets(0, 0, 0, 2);
            this.add(endTurn_Button, c);
        }

        public void update() {
            if(!Ark.solo) {
                this.playersRecapPanel.update();
            }
        }
    }

    class PlayersRecapPanel extends JPanel {
        private Map<String, java.util.List<JLabel>> map;

        public PlayersRecapPanel() {
            GridBagConstraints c;

            this.setBackground(new Color(216, 193, 153));
            this.setLayout(new GridBagLayout());

            if (!Ark.solo) {

                java.util.List<JLabel> labelList = new ArrayList<>();
                map = new HashMap<>();
                for (int i = 0; i < Ark.game.getPlayerList().size(); i++) //for all players except the one me
                {
                    labelList.clear();
                    PlayerSimplified player = Ark.game.getPlayer(i + 1);
                    if (player.equals(Ark.myPlayerRef))
                        continue;

                    JPanel playerPanel = new JPanel(new GridBagLayout());
                    playerPanel.setOpaque(false);
                    playerPanel.setBorder(BorderFactory.createLineBorder(new Color(79, 66, 34), 1));

                    JLabel nicknameLabel = new JLabel("" + player.getPlayerNumber() + " - " + player.getNickname());
                    nicknameLabel.setFont(new Font(PAP, Font.BOLD, 24));
                    c = new GridBagConstraints();
                    c.gridx = 0;
                    c.gridy = 0;
                    c.weightx = 1;
                    c.weighty = 0.5;
                    c.gridwidth = 2;
                    c.gridheight = 1;
                    c.anchor = GridBagConstraints.PAGE_START;
                    c.insets = new Insets(1, 1, 1, 1);
                    playerPanel.add(nicknameLabel, c);

                    JLabel coinLabel = new JLabel();
                    coinLabel.setForeground(Color.DARK_GRAY);
                    coinLabel.setFont(new Font(TIMES, Font.BOLD, 20));
                    labelList.add(coinLabel);
                    c = new GridBagConstraints();
                    c.gridx = 0;
                    c.gridy = 1;
                    c.weightx = 0.2;
                    c.weighty = 0.5;
                    c.gridwidth = 1;
                    c.gridheight = 1;
                    c.anchor = GridBagConstraints.LINE_START;
                    c.insets = new Insets(1, 14, 1, 1);
                    playerPanel.add(coinLabel, c);

                    JLabel shieldLabel = new JLabel();
                    shieldLabel.setForeground(Color.DARK_GRAY);
                    shieldLabel.setFont(new Font(TIMES, Font.BOLD, 20));
                    labelList.add(shieldLabel);
                    c = new GridBagConstraints();
                    c.gridx = 0;
                    c.gridy = 2;
                    c.weightx = 0.2;
                    c.weighty = 0.5;
                    c.gridwidth = 1;
                    c.gridheight = 1;
                    c.anchor = GridBagConstraints.LINE_START;
                    c.insets = new Insets(1, 14, 1, 1);
                    playerPanel.add(shieldLabel, c);

                    JLabel stoneLabel = new JLabel();
                    stoneLabel.setForeground(Color.DARK_GRAY);
                    stoneLabel.setFont(new Font(TIMES, Font.BOLD, 20));
                    labelList.add(stoneLabel);
                    c = new GridBagConstraints();
                    c.gridx = 0;
                    c.gridy = 3;
                    c.weightx = 0.2;
                    c.weighty = 0.5;
                    c.gridwidth = 1;
                    c.gridheight = 1;
                    c.anchor = GridBagConstraints.LINE_START;
                    c.insets = new Insets(1, 14, 1, 1);
                    playerPanel.add(stoneLabel, c);

                    JLabel servantLabel = new JLabel();
                    servantLabel.setForeground(Color.DARK_GRAY);
                    servantLabel.setFont(new Font(TIMES, Font.BOLD, 20));
                    labelList.add(servantLabel);
                    c = new GridBagConstraints();
                    c.gridx = 0;
                    c.gridy = 4;
                    c.weightx = 0.2;
                    c.weighty = 0.5;
                    c.gridwidth = 1;
                    c.gridheight = 1;
                    c.anchor = GridBagConstraints.LINE_START;
                    c.insets = new Insets(1, 14, 3, 1);
                    playerPanel.add(servantLabel, c);


                    ShowPlayerButton button = new ShowPlayerButton("show more", player.getNickname());
                    button.addActionListener(e -> {
                        ShowPlayerButton b = (ShowPlayerButton) e.getSource();
                        String playerName = b.getPlayerName();

                        cardLayoutLeft.show(centralLeftPanel, playerName);
                        cardLayoutRight.show(centralRightPanel, playerName);
                    });


                    button.setFont(new Font(PAP, Font.BOLD, 20));
                    button.setPreferredSize(new Dimension(150, 70));
                    button.setBackground(new Color(231, 210, 181));
                    c = new GridBagConstraints();
                    c.gridx = 1;
                    c.gridy = 1;
                    c.weightx = 0.8;
                    c.weighty = 0.5;
                    c.gridwidth = 1;
                    c.gridheight = 4;
                    c.anchor = GridBagConstraints.LINE_END;
                    c.insets = new Insets(10, 0, 10, 10);
                    playerPanel.add(button, c);

                    map.put(player.getNickname(), new ArrayList<>(labelList));
                    c = new GridBagConstraints();
                    c.fill = GridBagConstraints.BOTH;
                    c.gridx = i;
                    c.weightx = 0.5;
                    c.weighty = 0.5;
                    this.add(playerPanel, c);
                }
            } else {
                JPanel lorenzoPanel = new JPanel(new GridBagLayout());
                lorenzoPanel.setBorder(BorderFactory.createLineBorder(new Color(79, 66, 34),1));
                lorenzoPanel.setOpaque(false);

                JLabel lorenzoLabel = new JLabel("Lorenzo");
                lorenzoLabel.setFont(new Font(PAP, Font.BOLD, 24));
                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                c.weightx = 0.2;
                c.weighty = 0.2;
                lorenzoPanel.add(lorenzoLabel,c);

                JLabel lorenzoFace = new JLabel();
                lorenzoFace.setIcon(scaleImage(new ImageIcon("resources/punchboard/lorenzo.png"),110));
                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 1;
                c.weightx = 0.2;
                c.weighty = 0.2;
                lorenzoPanel.add(lorenzoFace,c);

                JPanel spacer = new JPanel(new GridBagLayout());
                spacer.setBorder(BorderFactory.createLineBorder(new Color(155, 129, 66),1));
                c = new GridBagConstraints();
                spacer.add(Box.createHorizontalStrut(1),c);
                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 0;
                c.gridheight = 2;
                c.weightx = 0;
                c.weighty = 0;
                c.fill = GridBagConstraints.VERTICAL;
                lorenzoPanel.add(spacer,c);

                JPanel tokenPanel = new JPanel(new GridBagLayout());
                tokenPanel.setOpaque(false);

                JLabel possibleActions = new JLabel("Lorenzo tokens");
                possibleActions.setFont(new Font(PAP, Font.BOLD, 24));
                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                c.weightx = 0.5;
                c.weighty = 0.5;
                tokenPanel.add(possibleActions,c);

                JPanel tokenSlideshow = new JPanel(new FlowLayout());
                tokenSlideshow.setOpaque(false);

                for(int i=0; i<7; i++) {
                    String path = "resources/punchboard/cerchio";
                    path += "" + (i+1) + ".png";

                    JLabel label = new JLabel();
                    label.setIcon(scaleImage(new ImageIcon(path),65));
                    tokenSlideshow.add(label);
                }

                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 1;
                c.weightx = 0.5;
                c.weighty = 0.5;
                tokenPanel.add(tokenSlideshow,c);

                JLabel check = new JLabel("Check notification area in-game");
                check.setFont(new Font(PAP, Font.BOLD, 24));
                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 2;
                c.weightx = 0.5;
                c.weighty = 0.5;
                tokenPanel.add(check,c);

                c = new GridBagConstraints();
                c.gridx = 2;
                c.gridy = 0;
                c.gridheight = 2;
                c.weightx = 0.5;
                c.weighty = 0.5;
                c.fill = GridBagConstraints.BOTH;
                lorenzoPanel.add(tokenPanel,c);



                c = new GridBagConstraints();
                c.fill = GridBagConstraints.BOTH;
                c.weightx = 0.5;
                c.weighty = 0.5;
                this.add(lorenzoPanel, c);
            }
        }

        public void update() {
            if (!Ark.solo) {
                JLabel label;
                for (String name : map.keySet()) {
                    List<JLabel> labels = map.get(name);
                    Map<Resource, Integer> resources = Ark.game.getPlayerRef(name).getResources();
                    for (int i = 0; i < 4; i++) {
                        label = labels.get(i); //0 coin //1 shield //2 stone //servant
                        switch (i) {
                            case 0:
                                label.setText("Coins: " + resources.get(Resource.COIN));
                                break;
                            case 1:
                                label.setText("Shields: " + resources.get(Resource.SHIELD));
                                break;
                            case 2:
                                label.setText("Stones: " + resources.get(Resource.STONE));
                                break;
                            case 3:
                                label.setText("Servants: " + resources.get(Resource.SERVANT));
                                break;
                            default:
                                label.setText("Default?");
                        }
                    }
                }
            }
        }

        class ShowPlayerButton extends JButton {
            private final String playerName;

            public ShowPlayerButton(String text, String playerName) {
                super(text);
                this.playerName = playerName;
            }

            public String getPlayerName() {
                return this.playerName;
            }
        }
    }

    //TOP PANEL
    class TopPanel extends JPanel {

        private Image image;

        private final CellPanel[] positions;
        private final ZonePanel[] zones;

        public TopPanel() {

            this.positions = new CellPanel[25];
            this.zones = new ZonePanel[3];

            GridBagConstraints c;
            try {
                image = ImageIO.read(new File("resources/images/upper_board.png"));
            } catch (IOException e) {
            }
            this.setLayout(new GridBagLayout());

            addPadding(this, 239, 1221, 4, 4);

            c = new GridBagConstraints(); //left padding
            c.weightx = 0;
            c.weighty = 0;
            c.gridx = 1;
            c.gridy = 2;
            this.add(Box.createHorizontalStrut(40), c);

            c = new GridBagConstraints(); //right padding
            c.weightx = 0;
            c.weighty = 0;
            c.gridx = 3;
            c.gridy = 2;
            this.add(Box.createHorizontalStrut(36), c);

            c = new GridBagConstraints(); //top padding
            c.weightx = 0;
            c.weighty = 0;
            c.gridx = 2;
            c.gridy = 1;
            this.add(Box.createVerticalStrut(32), c);

            c = new GridBagConstraints(); //bottom padding
            c.weightx = 1;
            c.weighty = 0;
            c.gridx = 2;
            c.gridy = 3;
            this.add(Box.createVerticalStrut(25), c);


            JPanel internalPane = new JPanel();
            internalPane.setLayout(new GridBagLayout());
            internalPane.setOpaque(false);
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 2;
            c.fill = GridBagConstraints.BOTH;
            c.weighty = 0.5;
            c.weightx = 0.5;
            this.add(internalPane, c);


            CellPanel cellPanel;
            ZonePanel zonePanel;

            for (int row = 0; row <= 2; row++) {
                for (int col = 0; col <= 3; col++) {

                    if (isUseful(row, col)) {
                        cellPanel = new CellPanel(true);

                        this.positions[pos(row, col)] = cellPanel;
                    } else
                        cellPanel = new CellPanel(false);

                    c = new GridBagConstraints();
                    c.gridx = col;
                    c.gridy = row;
                    c.weightx = 0.3;
                    c.weighty = 0.3;
                    c.insets = new Insets(2, 2, 2, 2);
                    internalPane.add(cellPanel, c);
                }
            }

            for (int col = 4; col <= 5; col++) {
                cellPanel = new CellPanel(true);
                this.positions[pos(0, col)] = cellPanel;
                c = new GridBagConstraints();
                c.gridx = col;
                c.gridy = 0;
                c.weightx = 0.3;
                c.weighty = 0.3;
                c.insets = new Insets(2, 2, 2, 2);
                internalPane.add(cellPanel, c);
            }

            zonePanel = new ZonePanel();
            this.zones[0] = zonePanel;
            c = new GridBagConstraints();
            c.gridx = 4;
            c.gridy = 1;
            c.weightx = 0.3;
            c.weighty = 0.3;
            c.gridwidth = 2;
            c.gridheight = 2;
            c.insets = new Insets(2, 2, 2, 2);
            internalPane.add(zonePanel, c);

            for (int row = 0; row <= 2; row++) {
                for (int col = 6; col <= 8; col++) {

                    if (isUseful(row, col)) {
                        cellPanel = new CellPanel(true);
                        this.positions[pos(row, col)] = cellPanel;
                    } else
                        cellPanel = new CellPanel(false);

                    c = new GridBagConstraints();
                    c.gridx = col;
                    c.gridy = row;
                    c.weightx = 0.3;
                    c.weighty = 0.3;
                    c.insets = new Insets(2, 2, 2, 2);
                    internalPane.add(cellPanel, c);
                }
            }

            for (int col = 9; col <= 10; col++) {
                cellPanel = new CellPanel(true);
                this.positions[pos(2, col)] = cellPanel;
                c = new GridBagConstraints();
                c.gridx = col;
                c.gridy = 2;
                c.weightx = 0.3;
                c.weighty = 0.3;
                c.insets = new Insets(2, 2, 2, 2);
                internalPane.add(cellPanel, c);
            }

            zonePanel = new ZonePanel();
            this.zones[1] = zonePanel;
            c = new GridBagConstraints();
            c.gridx = 9;
            c.gridy = 0;
            c.weightx = 0.3;
            c.weighty = 0.3;
            c.gridwidth = 2;
            c.gridheight = 2;
            c.insets = new Insets(2, 2, 2, 2);
            internalPane.add(zonePanel, c);

            for (int row = 0; row <= 2; row++) {
                for (int col = 11; col <= 14; col++) {

                    if (isUseful(row, col)) {
                        cellPanel = new CellPanel(true);
                        this.positions[pos(row, col)] = cellPanel;
                    } else
                        cellPanel = new CellPanel(false);

                    c = new GridBagConstraints();
                    c.gridx = col;
                    c.gridy = row;
                    c.weightx = 0.3;
                    c.weighty = 0.3;
                    c.insets = new Insets(2, 2, 2, 2);
                    internalPane.add(cellPanel, c);
                }
            }

            for (int col = 15; col <= 16; col++) {
                cellPanel = new CellPanel(true);
                this.positions[pos(0, col)] = cellPanel;
                c = new GridBagConstraints();
                c.gridx = col;
                c.gridy = 0;
                c.weightx = 0.3;
                c.weighty = 0.3;
                c.insets = new Insets(2, 2, 2, 2);
                internalPane.add(cellPanel, c);
            }

            zonePanel = new ZonePanel();
            this.zones[2] = zonePanel;
            c = new GridBagConstraints();
            c.gridx = 15;
            c.gridy = 1;
            c.weightx = 0.3;
            c.weighty = 0.3;
            c.gridwidth = 2;
            c.gridheight = 2;
            c.insets = new Insets(2, 2, 2, 2);
            internalPane.add(zonePanel, c);

            for (int row = 0; row <= 2; row++) {
                for (int col = 17; col <= 18; col++) {

                    if (isUseful(row, col)) {
                        cellPanel = new CellPanel(true);
                        this.positions[pos(row, col)] = cellPanel;
                    } else
                        cellPanel = new CellPanel(false);

                    c = new GridBagConstraints();
                    c.gridx = col;
                    c.gridy = row;
                    c.weightx = 0.3;
                    c.weighty = 0.3;
                    c.insets = new Insets(2, 2, 2, 2);
                    internalPane.add(cellPanel, c);
                }
            }
        }


        class ZonePanel extends JPanel {

            static final int boxSize = 112;
            private final JLabel label;

            public ZonePanel() {
                GridBagConstraints c;
                this.setLayout(new GridBagLayout());
                this.setOpaque(false);

                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 0;
                c.weightx = 0;
                c.weighty = 0;
                this.add(Box.createHorizontalStrut(boxSize),c);

                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 1;
                c.weightx = 0;
                c.weighty = 0;
                this.add(Box.createVerticalStrut(boxSize),c);

                this.label = new JLabel();
                this.label.setHorizontalAlignment(SwingConstants.CENTER);
                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 1;
                c.weightx = 0.5;
                c.weighty = 0.5;
                c.fill = GridBagConstraints.BOTH;
                this.add(this.label,c);
            }

            public void setIcon(Icon icon)
            {
                this.label.setIcon(icon);
            }
        }

        class CellPanel extends JPanel {

            static final int boxSize = 54;
            private final JLabel label;
            boolean isEmpty;

            public CellPanel(boolean useful) {
                this.isEmpty = true;
                GridBagConstraints c;
                this.setLayout(new GridBagLayout());
                this.setOpaque(false);

                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 0;
                c.weightx = 0;
                c.weighty = 0;
                this.add(Box.createHorizontalStrut(boxSize),c);

                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 1;
                c.weightx = 0;
                c.weighty = 0;
                this.add(Box.createVerticalStrut(boxSize),c);

                if (useful) {
                    this.label = new JLabel();
                    this.label.setHorizontalAlignment(SwingConstants.CENTER);
                    c = new GridBagConstraints();
                    c.gridx = 1;
                    c.gridy = 1;
                    c.weightx = 0.5;
                    c.weighty = 0.5;
                    c.fill = GridBagConstraints.BOTH;
                    this.add(this.label,c);
                }
                else
                    this.label = null;

            }

            public void setIcon(Icon icon)
            {
                this.label.setIcon(icon);
            }
        }

        public void update() {
            boolean[] flippedZones = Ark.myPlayerRef.getFaithTrackPanels();
            if(flippedZones[0])
                this.zones[0].setIcon(scaleImage(new ImageIcon("resources/punchboard/pope_favor1_front.png"),85));
            else
                this.zones[0].setIcon(scaleImage(new ImageIcon("resources/punchboard/pope_favor1_back.png"),85));
            if(flippedZones[1])
                this.zones[1].setIcon(scaleImage(new ImageIcon("resources/punchboard/pope_favor2_front.png"),85));
            else
                this.zones[1].setIcon(scaleImage(new ImageIcon("resources/punchboard/pope_favor2_back.png"),85));
            if(flippedZones[2])
                this.zones[2].setIcon(scaleImage(new ImageIcon("resources/punchboard/pope_favor3_front.png"),85));
            else
                this.zones[2].setIcon(scaleImage(new ImageIcon("resources/punchboard/pope_favor3_back.png"),85));

            int[] positionMap = new int[4];
            int numOfPlayers = Ark.game.getPlayerSimplifiedList().size();
            for(int i=0; i<4;i++)
            {
                if(i>=numOfPlayers)
                    positionMap[i] = -1;
                else
                    positionMap[i] = Ark.game.getPlayer(i+1).getPosition();
            }

            //empties the currently populated track
            for(int i=0; i<=24;i++) {
                if (!this.positions[i].isEmpty) {
                    this.positions[i].setIcon(null);
                    this.positions[i].isEmpty = true;
                }
            }

            for(int i=0; i<numOfPlayers; i++) {
                if(!this.positions[positionMap[i]].isEmpty) continue;
                this.positions[positionMap[i]].setIcon(getNewIcon(i, positionMap));
                this.positions[positionMap[i]].isEmpty = false;
            }

            if(Ark.solo) {
                if(this.positions[Ark.game.getBlackCrossPosition()].isEmpty)
                    this.positions[Ark.game.getBlackCrossPosition()].setIcon(scaleImage(new ImageIcon("resources/FaithTrackIcons/L.png"), 40));
            }

        }



        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }

        public ImageIcon getNewIcon(int selectedPosition, int[] intmap)
        {
            String output = "";

            if(Ark.solo) {
                if(intmap[0] == Ark.game.getBlackCrossPosition()) {
                    output = "resources/FaithTrackIcons/PL.png"; //Lorenzo and player sharing the same cell
                }
                else
                    output = "resources/FaithTrackIcons/P.png"; //player not sharing the same position as Lorenzo
                return scaleImage(new ImageIcon(output), 40);
            }

             boolean[] p = new boolean[4];
             p[selectedPosition] = true;
             for(int i=0; i<4; i++) {
                 if (intmap[i] == -1) break;
                 if (intmap[i] == intmap[selectedPosition]) p[i] = true;
             }

             if(      p[0] && !p[1] && !p[2] && !p[3])      // 1
                 output = "resources/FaithTrackIcons/1.png";
             else if(!p[0] &&  p[1] && !p[2] && !p[3])      //2
                 output = "resources/FaithTrackIcons/2.png";
             else if(!p[0] && !p[1] &&  p[2] && !p[3])      // 3
                 output = "resources/FaithTrackIcons/3.png";
             else if(!p[0] && !p[1] && !p[2] &&  p[3])      // 4
                 output = "resources/FaithTrackIcons/4.png";
             else if( p[0] &&  p[1] && !p[2] && !p[3])      // 12
                 output = "resources/FaithTrackIcons/12.png";
             else if( p[0] && !p[1] &&  p[2] && !p[3])      // 13
                 output = "resources/FaithTrackIcons/13.png";
             else if( p[0] && !p[1] && !p[2] &&  p[3])      // 14
                 output = "resources/FaithTrackIcons/14.png";
             else if(!p[0] &&  p[1] &&  p[2] && !p[3])      // 23
                 output = "resources/FaithTrackIcons/23.png";
             else if(!p[0] &&  p[1] && !p[2] &&  p[3])      // 24
                 output = "resources/FaithTrackIcons/24.png";
             else if(!p[0] && !p[1] &&  p[2] &&  p[3])      // 34
                 output = "resources/FaithTrackIcons/34.png";
             else if( p[0] &&  p[1] &&  p[2] && !p[3])       // 123
                 output = "resources/FaithTrackIcons/123.png";
             else if( p[0] &&  p[1] && !p[2] &&  p[3])      // 124
                 output = "resources/FaithTrackIcons/124.png";
             else if( p[0] && !p[1] &&  p[2] &&  p[3])      // 134
                 output = "resources/FaithTrackIcons/134.png";
             else if(!p[0] &&  p[1] &&  p[2] &&  p[3])       // 234
                 output = "resources/FaithTrackIcons/234.png";
             else if( p[0] &&  p[1] &&  p[2] &&  p[3])      // 1234
                 output = "resources/FaithTrackIcons/1234.png";


            return scaleImage(new ImageIcon(output), 40);
        }

        public boolean isUseful(int row, int col) {
            switch (row) {
                case 0:
                    switch (col) {
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 12:
                        case 13:
                        case 14:
                        case 15:
                        case 16:
                        case 17:
                        case 18:
                            return true;
                        default:
                            return false;
                    }
                case 1:
                    switch (col) {
                        case 2:
                        case 7:
                        case 12:
                            return true;
                        default:
                            return false;
                    }
                case 2:
                    switch (col) {
                        case 0:
                        case 1:
                        case 2:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                        case 11:
                        case 12:
                            return true;
                        default:
                            return false;
                    }
                default:
                    return false;
            }
        }

        public int pos(int row, int col ) {
            switch (row) {
                case 0:
                    switch (col) {
                        case 2:  return 4;
                        case 3:  return 5;
                        case 4:  return 6;
                        case 5:  return 7;
                        case 6:  return 8;
                        case 7:  return 9;
                        case 12: return 18;
                        case 13: return 19;
                        case 14: return 20;
                        case 15: return 21;
                        case 16: return 22;
                        case 17: return 23;
                        case 18: return 24;
                        default: return -1;
                    }
                case 1:
                    switch (col) {
                        case 2:  return 3;
                        case 7:  return 10;
                        case 12: return 17;
                        default: return -1;
                    }
                case 2:
                    switch (col) {
                        case 0:  return 0;
                        case 1:  return 1;
                        case 2:  return 2;
                        case 7:  return 11;
                        case 8:  return 12;
                        case 9:  return 13;
                        case 10: return 14;
                        case 11: return 15;
                        case 12: return 16;
                        default: return -1;
                    }
                default:
                    return -1;
            }
        }
    }

    /**
     * CENTRAL LEFT PANEL <- CARDLAYOUTLEFT
     */
    class CentralLeftPanel extends JPanel {
        java.util.List<DepotAndStrongbox_Card_Panel> depotAndStrongboxCardPanelList;
        private Image image;

        public CentralLeftPanel() {
            try {
                image = ImageIO.read(new File("resources/images/left_board.png"));
            } catch (IOException e) {
            }

            cardLayoutLeft = new CardLayout();
            this.setLayout(cardLayoutLeft);

            depotAndStrongboxCardPanelList = new ArrayList<>();

            for (PlayerSimplified player : Ark.game.getPlayerList()) {
                DepotAndStrongbox_Card_Panel depotAndStrongboxCardPanel = new DepotAndStrongbox_Card_Panel(player.getNickname());
                this.add(depotAndStrongboxCardPanel, player.getNickname());
                depotAndStrongboxCardPanelList.add(depotAndStrongboxCardPanel);
            }
        }

        public void update(String name) {
            Optional<DepotAndStrongbox_Card_Panel> result = depotAndStrongboxCardPanelList.stream().filter(p -> p.panelName.equals(name)).findFirst();
            result.ifPresent(DepotAndStrongbox_Card_Panel::update);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }

    class DepotAndStrongbox_Card_Panel extends JPanel {
        JLabel shelf1;
        JLabel[] shelf2;
        JLabel[] shelf3;
        JLabel[] strongbox; //coin shield stone servant
        private final String panelName;

        //many labels
        public DepotAndStrongbox_Card_Panel(String panelName) {
            super();
            this.panelName = panelName;
            this.setOpaque(false);
            this.setLayout(new GridBagLayout());
            shelf1 = new JLabel();
            shelf2 = new JLabel[2];
            shelf2[0] = new JLabel();
            shelf2[1] = new JLabel();
            shelf3 = new JLabel[3];
            shelf3[0] = new JLabel();
            shelf3[1] = new JLabel();
            shelf3[2] = new JLabel();

            GridBagConstraints c;

            addPadding(this, 601, 273, 5, 6);

            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.weightx = 0.2;
            c.weighty = 0.2;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(105, 60, 0, 0);
            this.add(shelf1, c);

            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 2;
            c.weightx = 0.5;
            c.weighty = 0.2;
            c.gridwidth = 1;
            c.anchor = GridBagConstraints.FIRST_LINE_END;
            c.insets = new Insets(15, 70, 0, 8);
            this.add(shelf2[0], c);

            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 2;
            c.weightx = 0.5;
            c.weighty = 0.2;
            c.gridwidth = 1;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.insets = new Insets(15, 0, 0, 0);
            this.add(shelf2[1], c);

            JPanel thirdshelfPanel = new JPanel(new GridBagLayout());
            thirdshelfPanel.setOpaque(false);
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.gridwidth = 1;
            c.anchor = GridBagConstraints.FIRST_LINE_END;
            c.insets = new Insets(0, 60, 0, 8);
            thirdshelfPanel.add(shelf3[0], c);

            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 0;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.gridwidth = 1;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(0, 0, 0, 8);
            thirdshelfPanel.add(shelf3[1], c);

            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 0;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.gridwidth = 1;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.insets = new Insets(0, 0, 0, 0);
            thirdshelfPanel.add(shelf3[2], c);

            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 3;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(15, 0, 0, 0);
            this.add(thirdshelfPanel, c);

            strongbox = new JLabel[4];

            {
                strongbox[0] = new JLabel(); //coin //shield //stone //servant
                strongbox[0].setFont(new Font(PAP, Font.BOLD, 20));
                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 4;
                c.weightx = 0.5;
                c.weighty = 0.1;
                c.gridwidth = 1;
                c.anchor = GridBagConstraints.FIRST_LINE_END;
                c.insets = new Insets(70, 0, 3, 80);
                this.add(strongbox[0], c);
            } //coin
            {
                strongbox[1] = new JLabel();  //shield //stone //servant
                strongbox[1].setFont(new Font(PAP, Font.BOLD, 20));
                c = new GridBagConstraints();
                c.gridx = 2;
                c.gridy = 4;
                c.weightx = 0.5;
                c.weighty = 0.1;
                c.gridwidth = 1;
                c.anchor = GridBagConstraints.FIRST_LINE_START;
                c.insets = new Insets(70, 10, 3, 0);
                this.add(strongbox[1], c);
            } //shield
            {
                strongbox[2] = new JLabel();  //stone //servant
                strongbox[2].setFont(new Font(PAP, Font.BOLD, 20));
                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 5;
                c.weightx = 0.5;
                c.weighty = 0.5;
                c.gridwidth = 1;
                c.anchor = GridBagConstraints.FIRST_LINE_END;
                c.insets = new Insets(35, 0, 2, 80);
                this.add(strongbox[2], c);
            } //stone
            {
                strongbox[3] = new JLabel();  //servant
                strongbox[3].setFont(new Font(PAP, Font.BOLD, 20));
                c = new GridBagConstraints();
                c.gridx = 2;
                c.gridy = 5;
                c.weightx = 0.5;
                c.weighty = 0.5;
                c.gridwidth = 1;
                c.anchor = GridBagConstraints.FIRST_LINE_START;
                c.insets = new Insets(35, 10, 2, 0);
                this.add(strongbox[3], c);
            } //servant

        }

        public void update() {
            PlayerSimplified playerRef = Ark.game.getPlayerRef(this.panelName);
            WarehouseDepotSimplified depotRef = playerRef.getWarehouseDepot();
            StrongboxSimplified strongboxRef = playerRef.getStrongbox();

            Resource shelf1r = depotRef.getShelf1();
            Resource[] shelf2r = depotRef.getShelf2();
            Resource[] shelf3r = depotRef.getShelf3();

            this.shelf1.setIcon(scaleImage(new ImageIcon(shelf1r.getPathLittle()), 50));
            this.shelf2[0].setIcon(scaleImage(new ImageIcon(shelf2r[0].getPathLittle()), 50));
            this.shelf2[1].setIcon(scaleImage(new ImageIcon(shelf2r[1].getPathLittle()), 50));
            this.shelf3[0].setIcon(scaleImage(new ImageIcon(shelf3r[0].getPathLittle()), 50));
            this.shelf3[1].setIcon(scaleImage(new ImageIcon(shelf3r[1].getPathLittle()), 50));
            this.shelf3[2].setIcon(scaleImage(new ImageIcon(shelf3r[2].getPathLittle()), 50));

            Integer num;

            num = strongboxRef.getQuantity(Resource.COIN);
            if (num == null)
                this.strongbox[0].setText("0");
            else
                this.strongbox[0].setText("" + num);
            num = strongboxRef.getQuantity(Resource.SHIELD);
            if (num == null)
                this.strongbox[1].setText("0");
            else
                this.strongbox[1].setText("" + num);
            num = strongboxRef.getQuantity(Resource.STONE);
            if (num == null)
                this.strongbox[2].setText("0");
            else
                this.strongbox[2].setText("" + num);
            num = strongboxRef.getQuantity(Resource.SERVANT);
            if (num == null)
                this.strongbox[3].setText("0");
            else
                this.strongbox[3].setText("" + num);
        }
    }

    /**
     * CENTRAL RIGHT PANEL <- CARDLAYOUTRIGHT
     */
    class CentralRightPanel extends JPanel {

        private final java.util.List<Others_DevSlot_Panel> othersDevSlotList;
        private final Self_DevSlot_Panel self_devSlot_panel;

        public CentralRightPanel() {
            cardLayoutRight = new CardLayout();
            this.setLayout(cardLayoutRight);
            this.setOpaque(false);
            this.othersDevSlotList = new ArrayList<>();

            self_devSlot_panel = new Self_DevSlot_Panel();
            this.add(self_devSlot_panel, Ark.nickname);
            devDeck_panel = new DevDeck_Panel();
            this.add(devDeck_panel, DEVDECK);
            market_panel = new Market_Panel();
            this.add(market_panel, MARKET);
            leaderCardsPicker_panel = new LeaderCardsPicker_Panel();
            this.add(leaderCardsPicker_panel, LEADERCARDSPICKER);
            getMarketResource_panel = new GetMarketResource_Panel();
            this.add(getMarketResource_panel, GETMARKETRESOURCES);
            changeDepotConfig_panel = new ChangeDepotConfig_Panel();
            this.add(changeDepotConfig_panel, CHANGEDEPOTCONFIG);
            discardLeaderCard_panel = new DiscardLeaderCard_Panel();
            this.add(discardLeaderCard_panel, DISCARDLEADERCARD);
            activateLeaderCard_panel = new ActivateLeaderCard_Panel();
            this.add(activateLeaderCard_panel, ACTIVATELEADERCARD);
            resourcePicker_panel = new ResourcePicker_Panel();
            this.add(resourcePicker_panel, RESOURCEPICKER);
            marketHelper_panel = new MarketHelper_Panel();
            this.add(marketHelper_panel, MARKETHELPER);
            vendor_panel = new Vendor_Panel();
            this.add(vendor_panel, VENDOR);
            productionSelection_panel = new ProductionSelection_Panel();
            this.add(productionSelection_panel, PRODSELECTION);
            production_panel = new Production_Panel();
            this.add(production_panel, PRODUCTION);
            for (PlayerSimplified player : Ark.game.getPlayerList()) {
                if (player.getNickname().equals(Ark.nickname)) continue;
                Others_DevSlot_Panel panel = new Others_DevSlot_Panel(player.getNickname());
                this.add(panel, player.getNickname());
                othersDevSlotList.add(panel);
            }
        }

        public void update(String name) {
            if (name.equals(Ark.nickname))
                this.self_devSlot_panel.update();
            else {
                Optional<Others_DevSlot_Panel> result = othersDevSlotList.stream().filter(p -> p.panelName.equals(name)).findFirst();
                result.ifPresent(Others_DevSlot_Panel::update);
            }
        }

        public void updateCurrentPlayer() {
            this.update(Ark.game.getCurrentPlayerName());
        }

    }

    class Self_DevSlot_Panel extends JPanel { //this card is called by Ark.nickname

        private final JLabel[][] grid;
        private Image image;

        public Self_DevSlot_Panel() //TODO devslot, update
        {
            this.setLayout(new GridBagLayout());
            GridBagConstraints c;

            try {
                image = ImageIO.read(new File("resources/images/right_board.png"));
            } catch (IOException e) {
            }

            addPadding(this, 601, 948, 6, 5);

            this.grid = new JLabel[3][3];

            JLabel spacerLabel = new JLabel();
            spacerLabel.setPreferredSize(new Dimension(180, 200));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.weightx = 0.1;
            c.weighty = 0.1;
            this.add(spacerLabel, c);

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    grid[row][col] = new JLabel();
                    c = new GridBagConstraints();
                    c.gridx = col + 2;
                    c.gridy = 1;
                    c.weightx = 0.5;
                    c.weighty = 0.5;
                    c.anchor = GridBagConstraints.PAGE_END;
                    c.insets = new Insets(0, 0, 250 - (row * 50), 0);
                    this.add(grid[row][col], c);
                }
            }


            for (int i = 0; i < 3; i++) {
                spacerLabel = new JLabel();
                spacerLabel.setPreferredSize(new Dimension(165, 0));
                c.gridx = i + 2;
                c.gridy = 1;
                c.weightx = 0.5;
                c.weighty = 0.5;
                c.anchor = GridBagConstraints.PAGE_END;
                this.add(spacerLabel, c);
            }

            spacerLabel = new JLabel();
            spacerLabel.setPreferredSize(new Dimension(35, 200));
            c = new GridBagConstraints();
            c.gridx = 5;
            c.gridy = 1;
            c.weightx = 0.1;
            c.weighty = 0.1;
            this.add(spacerLabel, c);

        }

        public void update() {
            DevelopmentCard[][] ref = Ark.myPlayerRef.getDevelopmentSlot().getCards();
            DevelopmentCard temp;

            DevelopmentCard[][] cards = new DevelopmentCard[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    cards[i][j] = ref[i][j];
                }
            }

            //rotates matrix 90 degrees clockwise
            for (int x = 0; x < 3 / 2; x++) {
                for (int y = x; y < 3 - x - 1; y++) {
                    temp = cards[x][y];
                    cards[x][y] = cards[y][3 - 1 - x];
                    cards[y][3 - 1 - x]
                            = cards[3 - 1 - x][3 - 1 - y];
                    cards[3 - 1 - x][3 - 1 - y] = cards[3 - 1 - y][x];
                    cards[3 - 1 - y][x] = temp;
                }
            }

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    if (cards[row][col] != null) {
                        grid[row][col].setIcon(scaleImage(new ImageIcon(cards[row][col].getFrontPath()), 250));
                        grid[row][col].setBorder(BorderFactory.createLineBorder(new Color(178, 49, 35), 3));
                    }
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }

    class Others_DevSlot_Panel extends JPanel {
        private Image image;
        private final String panelName;
        private final JLabel[] devGrid;
        private final JLabel[] leadGrid;
        private final JLabel[] leadLabel;

        public Others_DevSlot_Panel(String panelName) {
            this.panelName = panelName;
            this.setLayout(new GridBagLayout());
            GridBagConstraints c;

            try {
                image = ImageIO.read(new File("resources/images/right_board.png"));
            } catch (IOException ignored) {
            }

            addPadding(this, 601, 273, 5, 6);

            this.devGrid = new JLabel[3];
            this.leadGrid = new JLabel[2];
            this.leadLabel = new JLabel[2];

            JButton back_OthersDevSlot = new JButton("Back");
            back_OthersDevSlot.addActionListener(back_othersDevSlot_actionListener);
            back_OthersDevSlot.setPreferredSize(new Dimension(120, 40));
            back_OthersDevSlot.setFont(new Font(PAP, Font.BOLD, 20));
            back_OthersDevSlot.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.01;
            c.weighty = 0.01;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.insets = new Insets(6, 6, 0, 0);
            this.add(back_OthersDevSlot, c);

            JPanel leaderCardPanel = new JPanel();
            leaderCardPanel.setLayout(new GridBagLayout());
            leaderCardPanel.setOpaque(false);

            for (int i = 0; i < 2; i++) {
                JPanel leadPanel = new JPanel(new GridBagLayout());
                leadPanel.setBackground(new Color(231, 210, 181));
                leadPanel.setBorder(BorderFactory.createLineBorder(new Color(231, 210, 181), 1));

                JLabel label = new JLabel();
                if (i == 0) label.setText("Leader Card #1");
                else label.setText("Leader Card #2");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font(PAP, Font.BOLD, 20));
                c = new GridBagConstraints();
                c.gridy = 0;
                c.weighty = 0.3;
                c.fill = GridBagConstraints.BOTH;
                leadPanel.add(label, c);

                this.leadGrid[i] = new JLabel();
                this.leadGrid[i].setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

                c = new GridBagConstraints();
                c.gridy = 1;
                c.weighty = 0.8;
                c.fill = GridBagConstraints.BOTH;
                leadPanel.add(this.leadGrid[i], c);

                this.leadLabel[i] = new JLabel();
                this.leadLabel[i].setFont(new Font(PAP, Font.BOLD, 20));
                this.leadLabel[i].setHorizontalAlignment(SwingConstants.CENTER);

                c.gridy = 2;
                c.weighty = 0.3;
                c.fill = GridBagConstraints.BOTH;
                leadPanel.add(this.leadLabel[i], c);

                c = new GridBagConstraints();
                c.gridx = i;
                c.weightx = 0.5;
                int pad = 40;
                c.insets = new Insets(20, pad * i, 0, pad * (1 - i));
                if (i == 0) c.anchor = GridBagConstraints.FIRST_LINE_END;
                else c.anchor = GridBagConstraints.FIRST_LINE_START;
                leaderCardPanel.add(leadPanel, c);
            }

            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 1;
            c.weightx = 0.5;
            c.fill = GridBagConstraints.BOTH;
            this.add(leaderCardPanel, c);

            JPanel devPanel = new JPanel();
            devPanel.setLayout(new GridBagLayout());
            devPanel.setOpaque(false);

            for (int i = 0; i < 3; i++) {
                JPanel tpane = new JPanel();
                tpane.setOpaque(false);
                this.devGrid[i] = new JLabel();

                this.devGrid[i].setBorder(BorderFactory.createLineBorder(new Color(231, 210, 181), 1));
                tpane.add(this.devGrid[i]);
                c.gridx = i;
                c.weightx = 0.3;
                c.fill = GridBagConstraints.BOTH;
                if (i == 0) c.insets = new Insets(0, 50, 10, 0);
                if (i == 1) c.insets = new Insets(0, 10, 10, 0);
                if (i == 2) c.insets = new Insets(0, 0, 10, 40);
                devPanel.add(tpane, c);
            }

            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 2;
            c.weightx = 0.5;
            c.weighty = 0.8;
            c.fill = GridBagConstraints.BOTH;
            this.add(devPanel, c);

        }

        public void update() {

            PlayerSimplified player = Ark.game.getPlayerRef(this.panelName);
            DevelopmentCard[] devCard = player.getDevelopmentSlot().getTopCards();
            LeaderCard[] l = player.getLeaderCards();

            for (int i = 0; i < 2; i++) {
                if (l[i] == null) {
                    this.leadGrid[i].setIcon(scaleImage(new ImageIcon("resources/cardsBack/BACK (1).png"), 234));
                    this.leadLabel[i].setText("not present");
                } else {
                    this.leadGrid[i].setIcon(scaleImage(new ImageIcon(l[i].getFrontPath()), 234));
                    if (l[i].isEnabled()) {
                        this.leadLabel[i].setText("enabled");
                        this.leadGrid[i].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 1));
                    } else
                        this.leadLabel[i].setText("not enabled");
                }
            }

            for (int i = 0; i < 3; i++) {
                if (devCard[i] == null)
                    this.devGrid[i].setIcon(scaleImage(new ImageIcon("resources/cardsBack/BACK (1).png"), 220));
                else
                    this.devGrid[i].setIcon(scaleImage(new ImageIcon(devCard[i].getFrontPath()), 220));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }

    class DevDeck_Panel extends JPanel { //this card is called by DEVDECK

        private final JLabel[][] labels;

        public DevDeck_Panel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9), 1));

            this.labels = new JLabel[3][4];

            addPadding(this, 599, 946, 6, 5);

            JLabel level3CardLabel = new JLabel("Level 3");
            level3CardLabel.setFont(new Font(PAP, Font.BOLD, 20));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 2;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.01;
            c.weighty = 0.5;
            c.anchor = GridBagConstraints.LINE_END;
            c.insets = new Insets(0, 0, 0, 0);
            this.add(level3CardLabel, c);

            JLabel level2CardLabel = new JLabel("Level 2");
            level2CardLabel.setFont(new Font(PAP, Font.BOLD, 20));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 3;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.01;
            c.weighty = 0.5;
            c.anchor = GridBagConstraints.LINE_END;
            c.insets = new Insets(0, 0, 0, 0);
            this.add(level2CardLabel, c);

            JLabel level1CardLabel = new JLabel("Level 1");
            level1CardLabel.setFont(new Font(PAP, Font.BOLD, 20));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 4;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.01;
            c.weighty = 0.5;
            c.anchor = GridBagConstraints.LINE_END;
            c.insets = new Insets(0, 0, 0, 0);
            this.add(level1CardLabel, c);

            JButton back_DevDeck_Button = new JButton("Back");
            back_DevDeck_Button.addActionListener(back_show_DevDeck_actionListener);
            back_DevDeck_Button.setPreferredSize(new Dimension(120, 40));
            back_DevDeck_Button.setFont(new Font(PAP, Font.BOLD, 20));
            back_DevDeck_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.01;
            c.weighty = 0.01;
            c.insets = new Insets(5, 5, 0, 0);
            this.add(back_DevDeck_Button, c);

            JLabel greenCardColumnLabel = new JLabel("green");
            greenCardColumnLabel.setFont(new Font(PAP, Font.BOLD, 20));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.5;
            c.weighty = 0.01;
            c.insets = new Insets(0, 0, 0, 0);
            this.add(greenCardColumnLabel, c);

            JLabel blueCardColumnLabel = new JLabel("blue");
            blueCardColumnLabel.setFont(new Font(PAP, Font.BOLD, 20));
            c = new GridBagConstraints();
            c.gridx = 3;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.5;
            c.weighty = 0.01;
            c.insets = new Insets(0, 0, 0, 0);
            this.add(blueCardColumnLabel, c);

            JLabel yellowCardColumnLabel = new JLabel("yellow");
            yellowCardColumnLabel.setFont(new Font(PAP, Font.BOLD, 20));
            c = new GridBagConstraints();
            c.gridx = 4;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.5;
            c.weighty = 0.01;
            c.insets = new Insets(0, 0, 0, 0);
            this.add(yellowCardColumnLabel, c);

            JLabel purpleCardColumnLabel = new JLabel("purple");
            purpleCardColumnLabel.setFont(new Font(PAP, Font.BOLD, 20));
            c = new GridBagConstraints();
            c.gridx = 5;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.5;
            c.weighty = 0.01;
            c.insets = new Insets(0, 0, 0, 0);
            this.add(purpleCardColumnLabel, c);

            for (int col = 0; col < 4; col++) {
                for (int row = 0; row < 3; row++) {
                    JLabel label = new JLabel();
                    c = new GridBagConstraints();
                    c.gridx = col + 2;
                    c.gridy = row + 2;
                    c.weightx = 0.5;
                    c.weighty = 0.5;
                    this.add(label, c);
                    this.labels[row][col] = label;
                }
            }
        }

        public void update() {
            DevelopmentCard[][] cards = Ark.game.getDevDeck().getCards();

            for (int col = 0; col < 4; col++) {
                for (int row = 0; row < 3; row++) {
                    ImageIcon cardIcon;
                    if (cards[row][col] == null)
                        cardIcon = scaleImage(new ImageIcon("resources/cardsBack/BACK (1).png"), 175);
                    else
                        cardIcon = scaleImage(new ImageIcon(cards[row][col].getFrontPath()), 175);
                    labels[row][col].setIcon(cardIcon);
                }
            }
        }
    }

    class Market_Panel extends JPanel {
        private final JLabel[][] labelGrid; //<- contains the labels for the marblegrid
        private final JLabel labelSlide; //<- contains the label for the slidemarble
        private Image image;

        public Market_Panel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9), 1));

            this.labelGrid = new JLabel[3][4];
            this.labelSlide = new JLabel();

            try {
                image = ImageIO.read(new File("resources/images/market2.png"));
            } catch (IOException e) {
            }

            addPadding(this, 599, 946, 100, 100);

            JButton back_ShowMarket_Button = new JButton("Back");
            back_ShowMarket_Button.addActionListener(back_show_Market_actionListener);
            back_ShowMarket_Button.setPreferredSize(new Dimension(120, 40));
            back_ShowMarket_Button.setFont(new Font(PAP, Font.BOLD, 20));
            back_ShowMarket_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.1;
            c.weighty = 0.1;
            c.insets = new Insets(5, 5, 0, 0);
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            this.add(back_ShowMarket_Button, c);

            JPanel topPadding = new JPanel();
            topPadding.setOpaque(false);
            topPadding.add(Box.createVerticalStrut(95));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 1;
            c.weightx = 0;
            c.weighty = 0;
            this.add(topPadding, c);

            JPanel leftPadding = new JPanel();
            leftPadding.setOpaque(false);
            leftPadding.add(Box.createHorizontalStrut(177));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 3;
            c.weightx = 0;
            c.weighty = 0;
            this.add(leftPadding, c);

            JPanel rightPadding = new JPanel();
            rightPadding.setOpaque(false);
            rightPadding.add(Box.createHorizontalStrut(463));
            c = new GridBagConstraints();
            c.gridx = 6;
            c.gridy = 3;
            c.weightx = 0;
            c.weighty = 0;
            this.add(rightPadding, c);

            JPanel bottomPadding = new JPanel();
            bottomPadding.setOpaque(false);
            bottomPadding.add(Box.createVerticalStrut(191));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 6;
            c.weightx = 0;
            c.weighty = 0;
            this.add(bottomPadding, c);

            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 2;
            c.gridwidth = 4;
            c.gridheight = 1;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.insets = new Insets(0,160,0,0);
            this.add(labelSlide, c);

            for (int col = 0; col < 4; col++) {
                for (int row = 0; row < 3; row++) {
                    JLabel label = new JLabel();
                    c = new GridBagConstraints();
                    c.gridx = col + 2;
                    c.gridy = row + 3;
                    c.weightx = 0.3;
                    c.weighty = 0.3;
                    this.add(label, c);
                    this.labelGrid[row][col] = label;
                }
            }
        }

        public void update() {
            MarketMarble[][] grid = Ark.game.getMarket().getGrid();
            MarketMarble slide = Ark.game.getMarket().getSlideMarble();

            for (int col = 0; col < 4; col++) {
                for (int row = 0; row < 3; row++) {
                    labelGrid[row][col].setIcon(scaleImage(new ImageIcon(grid[row][col].getPath()), 67));
                }
            }
            labelSlide.setIcon(scaleImage(new ImageIcon(slide.getPath()), 67));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }

    class LeaderCardsPicker_Panel extends JPanel implements ItemListener {
        private final JLabel[] labelCards;
        private final CustomCheckbox[] checkBoxes;
        private final JButton confirm_LeaderCardsPicker_Button;

        private int first = -1;
        private int second = -1;
        private boolean modifying = false;

        public LeaderCardsPicker_Panel() {
            GridBagConstraints c;
            this.setOpaque(false);
            this.setLayout(new GridBagLayout());

            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9), 1));
            addPadding(this, 599, 946, 5, 5);

            this.labelCards = new JLabel[4];
            this.checkBoxes = new CustomCheckbox[4];

            JLabel titleLabel = new JLabel("Pick your two Cards!");
            titleLabel.setFont(new Font(PAP, Font.BOLD, 50));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 4;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(titleLabel, c);

            for (int i = 0; i < 4; i++) {
                labelCards[i] = new JLabel();

                c = new GridBagConstraints();
                c.gridx = i + 1;
                c.gridy = 2;
                c.gridwidth = 1;
                c.weightx = 0.5;
                c.weighty = 0.2;
                this.add(labelCards[i], c);

                checkBoxes[i] = new CustomCheckbox(i);
                checkBoxes[i].setBackground(new Color(178, 49, 35));
                checkBoxes[i].addItemListener(this);
                c = new GridBagConstraints();
                c.gridx = i + 1;
                c.gridy = 3;
                c.anchor = GridBagConstraints.PAGE_START;
                c.weightx = 0.5;
                c.weighty = 0.4;
                this.add(checkBoxes[i], c);
            }

            confirm_LeaderCardsPicker_Button = new JButton("confirm!");
            confirm_LeaderCardsPicker_Button.addActionListener(init_leaderCardsPicker_actionListener);
            confirm_LeaderCardsPicker_Button.setEnabled(false);
            confirm_LeaderCardsPicker_Button.setPreferredSize(new Dimension(200, 60));
            confirm_LeaderCardsPicker_Button.setFont(new Font(PAP, Font.BOLD, 28));
            confirm_LeaderCardsPicker_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 4;
            c.weightx = 0.5;
            c.weighty = 0.4;
            c.gridwidth = 4;
            c.anchor = GridBagConstraints.PAGE_END;
            c.insets = new Insets(0, 0, 10, 0);
            this.add(confirm_LeaderCardsPicker_Button, c);
        }

        public int getFirst() {
            return this.first;
        }

        public int getSecond() {
            return this.second;
        }

        public void update() {
            for (int i = 0; i < 4; i++)
                labelCards[i].setIcon(scaleImage(new ImageIcon(Ark.game.getLeaderCardsPicker().getCard(i).getFrontPath()), 300));
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            CustomCheckbox source = (CustomCheckbox) e.getSource();
            int number = source.getNumber();

            if (!modifying) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (first == -1 && second == -1) {
                        first = number;
                    } else if (first != -1 && second == -1) {
                        second = number;
                        confirm_LeaderCardsPicker_Button.setEnabled(true);
                    } else if (first != -1 && first != number && second != number) //so a new card was chosen
                    {
                        modifying = true;
                        checkBoxes[first].setSelected(false);
                        first = second;
                        second = number;
                        confirm_LeaderCardsPicker_Button.setEnabled(true);
                        modifying = false;
                    }
                } else //item deselected
                {
                    if (first != -1 && second != -1) {
                        if (first == number)
                            first = second;
                        second = -1;
                        confirm_LeaderCardsPicker_Button.setEnabled(false);
                    } else if (first != -1) {
                        first = -1;
                    }
                }
            }
        } //checkBoxes listener, allows for two and only two checks

        class CustomCheckbox extends JCheckBox {
            private final int number;

            public CustomCheckbox(int number) {
                super();
                this.number = number;
            }

            public int getNumber() {
                return this.number;
            }
        }
    }

    class ResourcePicker_Panel extends JPanel {

        private final JLabel subtitleLabel;

        public ResourcePicker_Panel() {
            GridBagConstraints c;
            this.setOpaque(false);
            this.setLayout(new GridBagLayout());

            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9), 1));
            addPadding(this, 599, 946, 5, 5);

            JLabel titleLabel = new JLabel("Pick your Resources!");
            titleLabel.setFont(new Font(PAP, Font.BOLD, 50));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 4;
            c.weightx = 0.5;
            c.weighty = 0.1;
            this.add(titleLabel, c);

            subtitleLabel = new JLabel();
            subtitleLabel.setFont(new Font(PAP, Font.BOLD, 40));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 2;
            c.gridwidth = 4;
            c.weightx = 0.5;
            c.weighty = 0.1;
            this.add(subtitleLabel, c);

            Resource resource;
            CustomResourceButton button;

            for (int i = 0; i < 4; i++) {
                resource = Resource.COIN;
                if (i == 1) resource = Resource.SHIELD;
                if (i == 2) resource = Resource.STONE;
                if (i == 3) resource = Resource.SERVANT;

                button = new CustomResourceButton(resource);
                button.setBackground(new Color(231, 210, 181));
                button.setPreferredSize(new Dimension(130, 130));
                button.setIcon(scaleImage(new ImageIcon(resource.getPathLittle()), 110));
                button.addActionListener(init_resourcePicker_actionListener);
                c = new GridBagConstraints();
                c.gridx = i + 1;
                c.gridy = 3;
                c.weightx = 0.5;
                c.weighty = 0.5;
                c.insets = new Insets(0, 0, 20, 0);
                this.add(button, c);
            }
        }

        public void update() {
            switch (Ark.game.getResourcePicker().getNumOfResources()) {
                case 2:
                    subtitleLabel.setText("resources left: 2");
                    break;
                case 1:
                    subtitleLabel.setText("resources left: 1");
                    break;
                default:
                    subtitleLabel.setText("resources left: none, get out");
                    break;
            }
        }

        class CustomResourceButton extends JButton {
            private final Resource resource;

            public CustomResourceButton(Resource resource) {
                super();
                this.resource = resource;
            }

            public Resource getResource() {
                return this.resource;
            }
        }
    }

    class GetMarketResource_Panel extends JPanel {
        private final JLabel[][] labelGrid; //<- contains the labels for the marblegrid
        private final JLabel labelSlide; //<- contains the label for the slidemarble
        private Image image;

        public GetMarketResource_Panel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9), 1));

            this.labelGrid = new JLabel[3][4];
            this.labelSlide = new JLabel();

            try {
                image = ImageIO.read(new File("resources/images/market2.png"));
            } catch (IOException ignored) {
            }

            addPadding(this, 599, 946, 100, 100);

            JButton back_GetMarketResource_Button = new JButton("Back");
            back_GetMarketResource_Button.addActionListener(back_getMarketResource_actionListener);
            back_GetMarketResource_Button.setPreferredSize(new Dimension(120, 40));
            back_GetMarketResource_Button.setFont(new Font(PAP, Font.BOLD, 20));
            back_GetMarketResource_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.1;
            c.weighty = 0.1;
            c.insets = new Insets(5, 5, 0, 0);
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            this.add(back_GetMarketResource_Button, c);

            JPanel topPadding = new JPanel();
            topPadding.setOpaque(false);
            topPadding.add(Box.createVerticalStrut(95));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 1;
            c.weightx = 0;
            c.weighty = 0;
            this.add(topPadding, c);

            JPanel leftPadding = new JPanel();
            leftPadding.setOpaque(false);
            leftPadding.add(Box.createHorizontalStrut(177));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 3;
            c.weightx = 0;
            c.weighty = 0;
            this.add(leftPadding, c);

            JPanel rightPadding = new JPanel();
            rightPadding.setOpaque(false);
            rightPadding.add(Box.createHorizontalStrut(412));
            c = new GridBagConstraints();
            c.gridx = 7;
            c.gridy = 3;
            c.weightx = 0;
            c.weighty = 0;
            this.add(rightPadding, c);

            JPanel bottomPadding = new JPanel();
            bottomPadding.setOpaque(false);
            bottomPadding.add(Box.createVerticalStrut(140));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 7;
            c.weightx = 0;
            c.weighty = 0;
            this.add(bottomPadding, c);

            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 2;
            c.gridwidth = 4;
            c.gridheight = 1;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.insets = new Insets(0,160,0,0);
            this.add(labelSlide, c);

            for(int i=0; i<3; i++) {
                for( int j=0; j<4; j++) {
                    JLabel label = new JLabel();
                    c = new GridBagConstraints();
                    c.gridx = j+2;
                    c.gridy = i+3;
                    c.weightx = 0.3;
                    c.weighty = 0.3;
                    this.add(label, c);
                    this.labelGrid[i][j] = label;
                }
            }
            int buttonSize = 45;

            for( int y = 3; y<=5;y++) {
                MarketButton button = new MarketButton(y-3, false);
                button.setText("" + (y-2));
                button.addActionListener(action_getMarketResource_actionListener);
                button.setPreferredSize(new Dimension(buttonSize, buttonSize));
                button.setFont(new Font(TIMES, Font.BOLD, 22));
                button.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 6;
                c.gridy = y;
                c.weightx = 0.3;
                c.weighty = 0.3;
                c.insets = new Insets(5,0,0,0);
                this.add(button, c);
            }

            for( int x=2; x<=5; x++) {
                MarketButton button = new MarketButton(x-2, true);
                button.setText("" + (x-1));
                button.addActionListener(action_getMarketResource_actionListener);
                button.setPreferredSize(new Dimension(buttonSize, buttonSize));
                button.setFont(new Font(TIMES, Font.BOLD, 22));
                button.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = x;
                c.gridy = 6;
                c.weightx = 0.3;
                c.weighty = 0.3;
                c.insets = new Insets(0,5,0,0);
                this.add(button, c);
            }
        }

        public void update() {
            MarketMarble[][] grid = Ark.game.getMarket().getGrid();
            MarketMarble slideMarble = Ark.game.getMarket().getSlideMarble();

            for (int col = 0; col < 4; col++) {
                for (int row = 0; row < 3; row++) {
                    labelGrid[row][col].setIcon(scaleImage(new ImageIcon(grid[row][col].getPath()), 67));
                }
            }
            labelSlide.setIcon(scaleImage(new ImageIcon(slideMarble.getPath()), 67));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }

        class MarketButton extends JButton {
            private final int num;
            private final boolean column;

            public MarketButton(int num, boolean column) {
                super();
                this.num = num;
                this.column = column;
            }

            public int getNum() {
                return num;
            }

            public boolean isColumn() {
                return column;
            }
        }
    }

    class DiscardLeaderCard_Panel extends JPanel {
        private final ChooseLeaderCardButton[] leaderCardButton;
        private final JLabel[] labelsUnderTheLeaderCard;

        public DiscardLeaderCard_Panel() {
            GridBagConstraints c;
            this.setOpaque(false);
            this.setLayout(new GridBagLayout());
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9), 1));
            addPadding(this, 599, 946, 100, 100);

            leaderCardButton = new ChooseLeaderCardButton[2];
            labelsUnderTheLeaderCard = new JLabel[2];

            JButton back_DiscardLeaderCard_Button = new JButton("Back");
            back_DiscardLeaderCard_Button.addActionListener(back_discardLeaderCard_actionListener);
            back_DiscardLeaderCard_Button.setPreferredSize(new Dimension(120, 40));
            back_DiscardLeaderCard_Button.setFont(new Font(PAP, Font.BOLD, 20));
            back_DiscardLeaderCard_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.1;
            c.weighty = 0.1;
            c.insets = new Insets(5, 5, 0, 0);
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            this.add(back_DiscardLeaderCard_Button, c);

            JLabel titleLabel = new JLabel("Select a card to be discarded");
            titleLabel.setFont(new Font(PAP, Font.BOLD, 50));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 2;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(titleLabel, c);


            JLabel labelOverLeaderCard1 = new JLabel("Leader Card #1");
            labelOverLeaderCard1.setFont(new Font(PAP, Font.BOLD, 26));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 2;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(labelOverLeaderCard1, c);

            JLabel labelOverLeaderCard2 = new JLabel("Leader Card #2");
            labelOverLeaderCard2.setFont(new Font(PAP, Font.BOLD, 26));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 2;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(labelOverLeaderCard2, c);

            leaderCardButton[0] = new ChooseLeaderCardButton(0);
            leaderCardButton[0].setPreferredSize(new Dimension(272, 400));
            leaderCardButton[0].setBackground(new Color(231, 210, 181));
            leaderCardButton[0].addActionListener(action_discardLeaderCard_actionListener);
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 3;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(leaderCardButton[0], c);

            leaderCardButton[1] = new ChooseLeaderCardButton(1);
            leaderCardButton[1].setPreferredSize(new Dimension(272, 400));
            leaderCardButton[1].setBackground(new Color(231, 210, 181));
            leaderCardButton[1].addActionListener(action_discardLeaderCard_actionListener);
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 3;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(leaderCardButton[1], c);

            labelsUnderTheLeaderCard[0] = new JLabel();
            labelsUnderTheLeaderCard[0].setFont(new Font(PAP, Font.BOLD, 22));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 4;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.insets = new Insets(0, 0, 10, 0);
            this.add(labelsUnderTheLeaderCard[0], c);


            labelsUnderTheLeaderCard[1] = new JLabel();
            labelsUnderTheLeaderCard[1].setFont(new Font(PAP, Font.BOLD, 22));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 4;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.insets = new Insets(0, 0, 10, 0);
            this.add(labelsUnderTheLeaderCard[1], c);

        }

        public void update() {
            LeaderCard[] cards = Ark.myPlayerRef.getLeaderCards();
            for (int i = 0; i < 2; i++) {
                if (cards[i] == null) {
                    this.leaderCardButton[i].setEnabled(false);
                    this.labelsUnderTheLeaderCard[i].setText("not present");
                    this.leaderCardButton[i].setIcon(scaleImage(new ImageIcon("resources/cardsBack/BACK (1).png"), 375));
                } else {
                    this.leaderCardButton[i].setEnabled(!cards[i].isEnabled());
                    this.leaderCardButton[i].setIcon(scaleImage(new ImageIcon(cards[i].getFrontPath()), 375));
                    if (cards[i].isEnabled())
                        this.labelsUnderTheLeaderCard[i].setText("can't discard anymore");
                    else
                        this.labelsUnderTheLeaderCard[i].setText("select this?");
                }
            }
        }

        class ChooseLeaderCardButton extends JButton {
            private final int number;

            public ChooseLeaderCardButton(int number) {
                super();
                this.number = number;
            }

            public int getNumber() {
                return this.number;
            }
        }
    }

    class ActivateLeaderCard_Panel extends JPanel {

        private final ChooseLeaderCardButton[] leaderCardButtons;
        private final JLabel[] labelsUnderTheLeaderCard;

        public ActivateLeaderCard_Panel() {
            GridBagConstraints c;
            this.setOpaque(false);
            this.setLayout(new GridBagLayout());
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9), 1));
            addPadding(this, 599, 946, 100, 100);

            leaderCardButtons = new ChooseLeaderCardButton[2];
            labelsUnderTheLeaderCard = new JLabel[2];

            JButton back_ActivateLeaderCard_Button = new JButton("Back");
            back_ActivateLeaderCard_Button.addActionListener(back_activateLeaderCard_actionListener);
            back_ActivateLeaderCard_Button.setPreferredSize(new Dimension(120, 40));
            back_ActivateLeaderCard_Button.setFont(new Font(PAP, Font.BOLD, 20));
            back_ActivateLeaderCard_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.1;
            c.weighty = 0.1;
            c.insets = new Insets(5, 5, 0, 0);
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            this.add(back_ActivateLeaderCard_Button, c);

            JLabel titleLabel = new JLabel("Select a card to be activated");
            titleLabel.setFont(new Font(PAP, Font.BOLD, 50));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 2;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(titleLabel, c);

            JLabel labelOverLeaderCard1 = new JLabel("Leader Card #1");
            labelOverLeaderCard1.setFont(new Font(PAP, Font.BOLD, 26));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 2;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(labelOverLeaderCard1, c);

            JLabel labelOverLeaderCard2 = new JLabel("Leader Card #2");
            labelOverLeaderCard2.setFont(new Font(PAP, Font.BOLD, 26));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 2;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(labelOverLeaderCard2, c);

            leaderCardButtons[0] = new ChooseLeaderCardButton(0);
            leaderCardButtons[0].setPreferredSize(new Dimension(272, 400));
            leaderCardButtons[0].setBackground(new Color(231, 210, 181));
            leaderCardButtons[0].addActionListener(action_activateLeaderCard_actionListener);
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 3;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(leaderCardButtons[0], c);

            leaderCardButtons[1] = new ChooseLeaderCardButton(1);
            leaderCardButtons[1].setPreferredSize(new Dimension(272, 400));
            leaderCardButtons[1].setBackground(new Color(231, 210, 181));
            leaderCardButtons[1].addActionListener(action_activateLeaderCard_actionListener);
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 3;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(leaderCardButtons[1], c);

            labelsUnderTheLeaderCard[0] = new JLabel();
            labelsUnderTheLeaderCard[0].setFont(new Font(PAP, Font.BOLD, 22));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 4;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.insets = new Insets(0, 0, 10, 0);
            this.add(labelsUnderTheLeaderCard[0], c);

            labelsUnderTheLeaderCard[1] = new JLabel();
            labelsUnderTheLeaderCard[1].setFont(new Font(PAP, Font.BOLD, 22));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 4;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.insets = new Insets(0, 0, 10, 0);
            this.add(labelsUnderTheLeaderCard[1], c);
        }

        public void update() {
            LeaderCard[] cards = Ark.myPlayerRef.getLeaderCards();
            for (int i = 0; i < 2; i++) {
                if (cards[i] == null) {
                    this.leaderCardButtons[i].setEnabled(false);
                    this.labelsUnderTheLeaderCard[i].setText("not present");
                    this.leaderCardButtons[i].setIcon(scaleImage(new ImageIcon("resources/cardsBack/BACK (1).png"), 375));
                } else if (cards[i].isEnabled()) {
                    this.leaderCardButtons[i].setEnabled(false);
                    this.labelsUnderTheLeaderCard[i].setText("already enabled");
                    this.leaderCardButtons[i].setIcon(scaleImage(new ImageIcon(cards[i].getFrontPath()), 375));
                } else {
                    this.leaderCardButtons[i].setEnabled(true);
                    this.labelsUnderTheLeaderCard[i].setText("select this?");
                    this.leaderCardButtons[i].setIcon(scaleImage(new ImageIcon(cards[i].getFrontPath()), 375));
                }
            }
        }

        class ChooseLeaderCardButton extends JButton {
            private final int number;

            public ChooseLeaderCardButton(int number) {
                super();
                this.number = number;
            }

            public int getNumber() {
                return this.number;
            }
        }
    }

    class ChangeDepotConfig_Panel extends JPanel {
        private final Resource[] shelf1type;
        private final Resource[] shelf2type;
        private final Resource[] shelf3type;
        private final JLabel shelf1label;
        private final JLabel[] shelf2label;
        private final JLabel[] shelf3label;

        private final ExtraDepotPanel firstExtraDepotPanel, secondExtraDepotPanel;
        private final int[] depotQuantity;

        public ChangeDepotConfig_Panel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9), 1));

            addPadding(this, 599, 946, 3, 7);

            shelf1type = new Resource[1];
            shelf2type = new Resource[2];
            shelf3type = new Resource[3];

            shelf2label = new JLabel[2];
            shelf3label = new JLabel[3];

            depotQuantity = new int[2];

            JButton back_ChangeDepotConfig_Card_Button = new JButton("Back");
            back_ChangeDepotConfig_Card_Button.addActionListener(back_changeDepotConfig_actionListener);
            back_ChangeDepotConfig_Card_Button.setPreferredSize(new Dimension(120, 40));
            back_ChangeDepotConfig_Card_Button.setFont(new Font(PAP, Font.BOLD, 20));
            back_ChangeDepotConfig_Card_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.weightx = 0.1;
            c.weighty = 0.1;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.insets = new Insets(5, 5, 0, 0);
            this.add(back_ChangeDepotConfig_Card_Button, c);

            JLabel titleLabel = new JLabel("Change depot configuration");
            titleLabel.setFont(new Font(PAP, Font.BOLD, 50));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 4;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(titleLabel, c);


            JButton resourceChangerButton;
            { //first shelf
                JPanel firstShelf = new JPanel(new GridBagLayout());
                firstShelf.setOpaque(false);

                JLabel firstLabel = new JLabel("first shelf:   ");
                firstLabel.setFont(new Font(PAP, Font.BOLD, 28));
                c = new GridBagConstraints();
                c.weightx = 0.3;
                firstShelf.add(firstLabel, c);

                shelf1label = new JLabel();
                c = new GridBagConstraints();
                c.gridx = 1;
                c.weightx = 0.3;
                c.insets = new Insets(0, 20, 0, 2);
                firstShelf.add(shelf1label, c);

                resourceChangerButton = new JButton(">");
                c = new GridBagConstraints();
                c.gridx = 2;
                c.weightx = 0.3;
                c.insets = new Insets(0, 2, 0, 20);
                resourceChangerButton.addActionListener(new resourceChanger(shelf1label, shelf1type, 0));
                resourceChangerButton.setFont(new Font(PAP, Font.BOLD, 20));
                resourceChangerButton.setBackground(new Color(231, 210, 181));
                firstShelf.add(resourceChangerButton, c);

                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 2;
                c.weightx = 0.5;
                c.weighty = 0.3;
                c.gridwidth = 2;
                this.add(firstShelf, c);
            }

            { //second shelf
                JPanel secondShelf = new JPanel(new GridBagLayout());
                secondShelf.setOpaque(false);

                JLabel secondLabel = new JLabel("second shelf:   ");
                secondLabel.setFont(new Font(PAP, Font.BOLD, 28));
                c = new GridBagConstraints();
                c.weightx = 0.3;
                secondShelf.add(secondLabel, c);

                shelf2label[0] = new JLabel();
                c = new GridBagConstraints();
                c.gridx = 1;
                c.weightx = 0.3;
                c.insets = new Insets(0, 20, 0, 2);
                secondShelf.add(shelf2label[0], c);

                resourceChangerButton = new JButton(">");
                resourceChangerButton.addActionListener(new resourceChanger(shelf2label[0], shelf2type, 0));
                resourceChangerButton.setFont(new Font(PAP, Font.BOLD, 20));
                resourceChangerButton.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 2;
                c.weightx = 0.3;
                c.insets = new Insets(0, 2, 0, 20);
                secondShelf.add(resourceChangerButton, c);

                shelf2label[1] = new JLabel();
                c = new GridBagConstraints();
                c.gridx = 3;
                c.weightx = 0.3;
                c.insets = new Insets(0, 20, 0, 2);
                secondShelf.add(shelf2label[1], c);

                resourceChangerButton = new JButton(">");
                resourceChangerButton.addActionListener(new resourceChanger(shelf2label[1], shelf2type, 1));
                resourceChangerButton.setFont(new Font(PAP, Font.BOLD, 20));
                resourceChangerButton.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 4;
                c.weightx = 0.3;
                c.insets = new Insets(0, 2, 0, 20);
                secondShelf.add(resourceChangerButton, c);

                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 3;
                c.weightx = 0.5;
                c.weighty = 0.3;
                c.gridwidth = 2;
                this.add(secondShelf, c);
            }

            { //third shelf
                JPanel thirdShelf = new JPanel(new GridBagLayout());
                thirdShelf.setOpaque(false);

                JLabel thirdLabel = new JLabel("third shelf:   ");
                thirdLabel.setFont(new Font(PAP, Font.BOLD, 28));
                c = new GridBagConstraints();
                c.weightx = 0.3;
                thirdShelf.add(thirdLabel, c);

                shelf3label[0] = new JLabel();
                c = new GridBagConstraints();
                c.gridx = 1;
                c.weightx = 0.3;
                c.insets = new Insets(0, 20, 0, 2);
                thirdShelf.add(shelf3label[0], c);

                resourceChangerButton = new JButton(">");
                resourceChangerButton.addActionListener(new resourceChanger(shelf3label[0], shelf3type, 0));
                resourceChangerButton.setFont(new Font(PAP, Font.BOLD, 20));
                resourceChangerButton.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 2;
                c.weightx = 0.3;
                c.insets = new Insets(0, 2, 0, 20);
                thirdShelf.add(resourceChangerButton, c);

                shelf3label[1] = new JLabel();
                c = new GridBagConstraints();
                c.gridx = 3;
                c.weightx = 0.3;
                c.insets = new Insets(0, 20, 0, 2);
                thirdShelf.add(shelf3label[1], c);

                resourceChangerButton = new JButton(">");
                resourceChangerButton.addActionListener(new resourceChanger(shelf3label[1], shelf3type, 1));
                resourceChangerButton.setFont(new Font(PAP, Font.BOLD, 20));
                resourceChangerButton.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 4;
                c.weightx = 0.3;
                c.insets = new Insets(0, 2, 0, 20);
                thirdShelf.add(resourceChangerButton, c);

                shelf3label[2] = new JLabel();
                c = new GridBagConstraints();
                c.gridx = 5;
                c.weightx = 0.3;
                c.insets = new Insets(0, 20, 0, 2);
                thirdShelf.add(shelf3label[2], c);

                resourceChangerButton = new JButton(">");
                resourceChangerButton.addActionListener(new resourceChanger(shelf3label[2], shelf3type, 2));
                resourceChangerButton.setFont(new Font(PAP, Font.BOLD, 20));
                resourceChangerButton.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 6;
                c.weightx = 0.3;
                c.insets = new Insets(0, 2, 0, 20);
                thirdShelf.add(resourceChangerButton, c);

                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 4;
                c.weightx = 0.5;
                c.weighty = 0.3;
                c.gridwidth = 2;
                this.add(thirdShelf, c);
            }

            firstExtraDepotPanel = new ExtraDepotPanel(depotQuantity, 0);
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 5;
            c.weightx = 0.5;
            c.weighty = 0.7;
            c.gridwidth = 1;
            this.add(firstExtraDepotPanel, c);


            secondExtraDepotPanel = new ExtraDepotPanel(depotQuantity, 1);
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 5;
            c.weightx = 0.5;
            c.weighty = 0.7;
            c.gridwidth = 1;
            this.add(secondExtraDepotPanel, c);

            JButton confirm_ChangeDepotConfig_Button = new JButton("confirm!");
            confirm_ChangeDepotConfig_Button.addActionListener(action_changeDepotConfig_actionListener);
            confirm_ChangeDepotConfig_Button.setPreferredSize(new Dimension(200, 60));
            confirm_ChangeDepotConfig_Button.setFont(new Font(PAP, Font.BOLD, 28));
            confirm_ChangeDepotConfig_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 6;
            c.weightx = 0.5;
            c.weighty = 0.4;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.PAGE_END;
            c.insets = new Insets(0, 0, 10, 0);
            this.add(confirm_ChangeDepotConfig_Button, c);
        }

        public Resource getShelf1() {
            return this.shelf1type[0];
        }

        public Resource[] getShelf2() {
            return this.shelf2type;
        }

        public Resource[] getShelf3() {
            return this.shelf3type;
        }

        public int getFirstDepotQuantity() {
            return this.depotQuantity[0];
        }

        public int getSecondDepotQuantity() {
            return this.depotQuantity[1];
        }

        public void update() {
            Resource shelf1r = Ark.myPlayerRef.getWarehouseDepot().getShelf1();
            Resource[] shelf2r = Ark.myPlayerRef.getWarehouseDepot().getShelf2();
            Resource[] shelf3r = Ark.myPlayerRef.getWarehouseDepot().getShelf3();

            this.shelf1type[0] = shelf1r;
            this.shelf1label.setIcon(scaleImage(new ImageIcon(shelf1r.getPathBig()), 100));
            this.shelf2type[0] = shelf2r[0];
            this.shelf2label[0].setIcon(scaleImage(new ImageIcon(shelf2r[0].getPathBig()), 100));
            this.shelf2type[1] = shelf2r[0];
            this.shelf2label[1].setIcon(scaleImage(new ImageIcon(shelf2r[1].getPathBig()), 100));
            this.shelf3type[0] = shelf3r[0];
            this.shelf3label[0].setIcon(scaleImage(new ImageIcon(shelf3r[0].getPathBig()), 100));
            this.shelf3type[1] = shelf3r[1];
            this.shelf3label[1].setIcon(scaleImage(new ImageIcon(shelf3r[1].getPathBig()), 100));
            this.shelf3type[2] = shelf3r[2];
            this.shelf3label[2].setIcon(scaleImage(new ImageIcon(shelf3r[2].getPathBig()), 100));

            firstExtraDepotPanel.updateValues();
            secondExtraDepotPanel.updateValues();

        }

        private Resource nextResource(Resource currentResource) {
            switch (currentResource) { //none coin shield stone servant
                case NONE:
                    return Resource.COIN;
                case COIN:
                    return Resource.SHIELD;
                case SHIELD:
                    return Resource.STONE;
                case STONE:
                    return Resource.SERVANT;
                case SERVANT:
                    return Resource.NONE;
                default:
                    return Resource.NONE;
            }
        }

        class ExtraDepotPanel extends JPanel {
            private final JLabel label;
            private JLabel number;
            private final JButton less;
            private final JButton more;
            private int[] depotQuantity;
            private int lcnum;
            ActionListener less_ActionListener = e -> {
                if (this.depotQuantity[lcnum] == 0) return;
                this.depotQuantity[lcnum] = this.depotQuantity[lcnum] - 1;
                this.number.setText("" + this.depotQuantity[lcnum]);
            };
            ActionListener more_ActionListener = e -> {
                if (this.depotQuantity[lcnum] == 2) return;
                this.depotQuantity[lcnum] = this.depotQuantity[lcnum] + 1;
                this.number.setText("" + this.depotQuantity[lcnum]);
            };

            public ExtraDepotPanel(int[] depotQuantity, int lcnum) {
                this.depotQuantity = depotQuantity;
                this.lcnum = lcnum;

                GridBagConstraints c;
                this.setLayout(new GridBagLayout());
                this.setOpaque(false);

                JLabel fixedLabel = new JLabel();
                fixedLabel.setFont(new Font(PAP, Font.BOLD, 20));
                fixedLabel.setText("   Leader Card #" + (lcnum + 1) + "   ");
                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                c.weightx = 0.3;
                c.weighty = 0.3;
                this.add(fixedLabel, c);

                label = new JLabel();
                label.setFont(new Font(PAP, Font.BOLD, 22));
                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 1;
                c.weightx = 0.3;
                c.weighty = 0.3;
                this.add(label, c);

                number = new JLabel();
                number.setFont(new Font(PAP, Font.BOLD, 28));
                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 0;
                c.weightx = 0.3;
                c.weighty = 0.3;
                c.gridheight = 2;
                c.insets = new Insets(0, 10, 0, 10);
                this.add(number, c);

                less = new JButton("<");
                less.addActionListener(less_ActionListener);
                less.setFont(new Font(PAP, Font.BOLD, 20));
                less.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 2;
                c.gridy = 0;
                c.weightx = 0.3;
                c.weighty = 0.3;
                c.gridheight = 2;
                this.add(less, c);

                more = new JButton(">");
                more.addActionListener(more_ActionListener);
                more.setFont(new Font(PAP, Font.BOLD, 20));
                more.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 3;
                c.gridy = 0;
                c.weightx = 0.3;
                c.weighty = 0.3;
                c.gridheight = 2;
                this.add(more, c);
            }

            public void updateValues() {
                LeaderCard l = Ark.myPlayerRef.getLeaderCards()[lcnum];
                if (l == null) {
                    this.label.setText("is not present");
                    this.number.setText("");
                    this.less.setEnabled(false);
                    this.more.setEnabled(false);
                    depotQuantity[lcnum] = -1;
                } else {
                    if (l.getSpecialAbility().isExtraDepot() && l.isEnabled()) {
                        int num = ((ExtraDepot) l.getSpecialAbility()).getNumber();
                        Resource res = ((ExtraDepot) l.getSpecialAbility()).getResourceType();

                        depotQuantity[lcnum] = num;

                        this.label.setText(resourceToString(res, true));

                        this.number.setText("" + depotQuantity[lcnum]);
                        this.less.setEnabled(true);
                        this.more.setEnabled(true);
                    } else if (l.getSpecialAbility().isExtraDepot() && !l.isEnabled()) {
                        this.label.setText("is not enabled");
                        this.number.setText("");
                        this.less.setEnabled(false);
                        this.more.setEnabled(false);
                        depotQuantity[lcnum] = -1;
                    } else {
                        this.label.setText("is not Extra Depot");
                        this.number.setText("");
                        this.less.setEnabled(false);
                        this.more.setEnabled(false);
                        depotQuantity[lcnum] = -1;
                    }
                }
            }
        }

        class resourceChanger implements ActionListener {

            JLabel managedLabel;
            Resource[] arraySource;
            int index;

            public resourceChanger(JLabel managedLabel, Resource[] arraySource, int index) {
                this.managedLabel = managedLabel;
                this.arraySource = arraySource;
                this.index = index;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                Resource nextResource = nextResource(arraySource[index]);
                arraySource[index] = nextResource;
                managedLabel.setIcon(scaleImage(new ImageIcon(nextResource.getPathBig()), 100));
            }
        }
    }

    class MarketHelper_Panel extends JPanel {

        private final JButton[] choiceButtons;
        private final JLabel[] resourceLabel;

        public MarketHelper_Panel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9), 1));

            addPadding(this, 599, 946, 4, 12);

            choiceButtons = new ChoiceButton[8];
            resourceLabel = new JLabel[4];

            JLabel titleLabel = new JLabel("Market Helper is here!");
            titleLabel.setFont(new Font(PAP, Font.BOLD, 50));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.weightx = 0.1;
            c.weighty = 0.1;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(5, 0, 0, 0);
            this.add(titleLabel, c);


            JPanel flowResource = new JPanel(new GridBagLayout());
            addPadding(flowResource, 46, 920, 7, 2);
            flowResource.setBorder(BorderFactory.createLineBorder(new Color(175, 154, 121), 1));
            flowResource.setBackground(new Color(214, 189, 148));

            JLabel resourceRemaining = new JLabel("remaining resources: ");
            resourceRemaining.setFont(new Font(PAP, Font.BOLD, 26));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.insets = new Insets(0, 10, 0, 20);
            flowResource.add(resourceRemaining, c);

            for (int i = 0; i < 4; i++) {
                JLabel resource = new JLabel();
                c = new GridBagConstraints();
                c.gridx = i + 2;
                c.gridy = 1;
                c.insets = new Insets(0, 8, 0, 8);
                this.resourceLabel[i] = resource;
                flowResource.add(resource, c);
            }

            c.gridx = 1;
            c.gridy = 2;
            c.gridwidth = 1;
            c.weightx = 0.1;
            c.weighty = 0.1;
            c.fill = GridBagConstraints.BOTH;
            c.anchor = GridBagConstraints.PAGE_START;
            this.add(flowResource, c);

            for (int i = 0; i < 8; i++) {
                ChoiceButton button = new ChoiceButton(i);
                button.setPreferredSize(new Dimension(700, 46));
                button.addActionListener(action_newMarketChoice_actionListener);
                button.setBackground(new Color(231, 210, 181));
                button.setFont(new Font(PAP, Font.BOLD, 24));
                button.setEnabled(false);

                if (i == 2) button.setText("discard that resource");
                if (i == 3) button.setText("swap the 1st and 2nd rows of your depot");
                if (i == 4) button.setText("swap the 1st and 3rd rows of your depot");
                if (i == 5) button.setText("swap the 2nd and 3rd rows of your depot");
                if (i == 6) button.setText("hop to the next available resource");
                if (i == 7) button.setText("hop back to the previous resource");

                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = i + 3;
                c.gridwidth = 1;
                c.weightx = 0.3;
                c.weighty = 0.05;
                this.choiceButtons[i] = button;
                this.add(button, c);
            }
        }

        public void update() {
            boolean enabled = Ark.game.isMarketHelperEnabled();
            boolean[] choices = Ark.game.getMarketHelper().getChoices();
            Resource[] extraResources = Ark.game.getMarketHelper().getExtraResources();
            List<Resource> resources = Ark.game.getMarketHelper().getResources();
            int currentResource = Ark.game.getMarketHelper().getCurrentResourceInt();

            for (int i = 0; i < 4; i++) {
                this.resourceLabel[i].setBorder(null);

                if (i < resources.size())
                    this.resourceLabel[i].setIcon(scaleImage(new ImageIcon(resources.get(i).getPathLittle()), 50));
                else
                    this.resourceLabel[i].setIcon(null);
            }

            if (!resources.isEmpty())
                this.resourceLabel[currentResource].setBorder(BorderFactory.createLineBorder(new Color(178, 49, 35), 2));


            for (int i = 0; i < 8; i++) {
                if (!enabled) {
                    this.choiceButtons[i].setEnabled(false);
                } else {
                    this.choiceButtons[i].setEnabled(choices[i]);

                    if (i == 0) {
                        if (resources.get(currentResource) != Resource.EXTRA) //so it is a normal choice
                            this.choiceButtons[0].setText("put in depot");
                        else
                            this.choiceButtons[0].setText("convert in " + resourceToString(extraResources[0], false));
                    } else if (i == 1) {
                        if (resources.get(currentResource) != Resource.EXTRA) //so it is a normal choice
                            this.choiceButtons[1].setText("put in the extra depot");
                        else
                            this.choiceButtons[1].setText("convert in " + resourceToString(extraResources[1], false));
                    }
                }
            }
        }

        class ChoiceButton extends JButton {
            private final int choiceNumber;

            public ChoiceButton(int choiceNumber) {
                super();
                this.choiceNumber = choiceNumber;
            }

            public int getChoiceNumber() {
                return this.choiceNumber;
            }
        }
    }

    class Vendor_Panel extends JPanel {
        private JLabel devCard;
        private SlotButton slot1;
        private SlotButton slot2;
        private SlotButton slot3;
        private int currentCard;
        ActionListener next_ActionListener = e -> {
            if (Ark.game.getDevelopmentCardsVendor().isEnabled()) {
                currentCard++;
                List<VendorCard> devCards = Ark.game.getDevelopmentCardsVendor().getCards();
                if (currentCard == devCards.size()) {
                    currentCard = 0;
                }

                devCard.setIcon(scaleImage(new ImageIcon(devCards.get(currentCard).getCard().getFrontPath()), 450));
                slot1.setEnabled(devCards.get(currentCard).isSlot1());
                slot2.setEnabled(devCards.get(currentCard).isSlot2());
                slot3.setEnabled(devCards.get(currentCard).isSlot3());
            }
        };
        ActionListener prev_ActionListener = e -> {
            if (Ark.game.getDevelopmentCardsVendor().isEnabled()) {
                currentCard--;
                List<VendorCard> devCards = Ark.game.getDevelopmentCardsVendor().getCards();
                if (currentCard < 0) {
                    currentCard = devCards.size() - 1;
                }
                devCard.setIcon(scaleImage(new ImageIcon(devCards.get(currentCard).getCard().getFrontPath()), 450));
                slot1.setEnabled(devCards.get(currentCard).isSlot1());
                slot2.setEnabled(devCards.get(currentCard).isSlot2());
                slot3.setEnabled(devCards.get(currentCard).isSlot3());
            }
        };

        public Vendor_Panel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9), 1));
            addPadding(this, 599, 946, 10, 12);

            currentCard = 0;

            JLabel title = new JLabel("Select a card to buy it!");
            title.setFont(new Font(PAP, Font.BOLD, 40));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 5;
            c.weightx = 0.3;
            c.weighty = 0.05;
            c.insets = new Insets(5, 0, 5, 0);
            this.add(title, c);

            JButton prev = new JButton("<");
            prev.addActionListener(prev_ActionListener);
            prev.setPreferredSize(new Dimension(80, 250));
            prev.setFont(new Font(PAP, Font.BOLD, 20));
            prev.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 2;
            c.weightx = 0.9;
            c.weighty = 0.3;
            c.gridheight = 4;
            c.anchor = GridBagConstraints.EAST;
            this.add(prev, c);

            devCard = new JLabel();
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 2;
            c.gridheight = 4;
            c.weightx = 0.5;
            c.weighty = 0.2;
            this.add(devCard, c);

            JButton next = new JButton(">");
            next.addActionListener(next_ActionListener);
            next.setPreferredSize(new Dimension(80, 250));
            next.setFont(new Font(PAP, Font.BOLD, 20));
            next.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 3;
            c.gridy = 2;
            c.weightx = 0.9;
            c.weighty = 0.3;
            c.gridheight = 4;
            c.anchor = GridBagConstraints.WEST;
            this.add(next, c);

            JLabel position = new JLabel("Slot?");
            position.setFont(new Font(PAP, Font.BOLD, 30));
            c = new GridBagConstraints();
            c.gridx = 4;
            c.gridy = 2;
            c.weightx = 4.0;
            c.weighty = 0.001;
            this.add(position, c);

            slot1 = new SlotButton("1", 1);
            slot1.setPreferredSize(new Dimension(120, 120));
            slot1.setFont(new Font(PAP, Font.BOLD, 30));
            slot1.setBackground(new Color(231, 210, 181));
            slot1.addActionListener(action_chooseDevCard_actionListener);
            c = new GridBagConstraints();
            c.gridx = 4;
            c.gridy = 3;
            c.weightx = 0.3;
            c.weighty = 0.3;
            this.add(slot1, c);

            slot2 = new SlotButton("2", 2);
            slot2.setPreferredSize(new Dimension(120, 120));
            slot2.setFont(new Font(PAP, Font.BOLD, 30));
            slot2.setBackground(new Color(231, 210, 181));
            slot2.addActionListener(action_chooseDevCard_actionListener);
            c = new GridBagConstraints();
            c.gridx = 4;
            c.gridy = 4;
            c.weightx = 0.3;
            c.weighty = 0.3;
            this.add(slot2, c);

            slot3 = new SlotButton("3", 3);
            slot3.setPreferredSize(new Dimension(120, 120));
            slot3.setFont(new Font(PAP, Font.BOLD, 30));
            slot3.setBackground(new Color(231, 210, 181));
            slot3.addActionListener(action_chooseDevCard_actionListener);
            c = new GridBagConstraints();
            c.gridx = 4;
            c.gridy = 5;
            c.weightx = 0.3;
            c.weighty = 0.3;
            this.add(slot3, c);
        }

        public void update() {
            if (Ark.game.getDevelopmentCardsVendor().isEnabled()) {
                List<VendorCard> devCards = Ark.game.getDevelopmentCardsVendor().getCards();
                devCard.setIcon(scaleImage(new ImageIcon(devCards.get(0).getCard().getFrontPath()), 450));

                if (!devCards.get(0).isSlot1()) {
                    slot1.setEnabled(false);
                }
                if (!devCards.get(0).isSlot2()) {
                    slot2.setEnabled(false);
                }
                if (!devCards.get(0).isSlot3()) {
                    slot3.setEnabled(false);
                }
            }
        }

        public int getCurrentCard() {
            return currentCard;
        }

        class SlotButton extends JButton {
            int position;

            public SlotButton(String text, int position) {
                super(text);
                this.position = position;
            }
            public int getPosition() {
                return position;
            }
        }
    }

    class ProductionSelection_Panel extends JPanel {
        private final JToggleButton[] leaderCardButton;
        private final JToggleButton[] devCardButton;
        private final JToggleButton basicProduction;

        ActionListener confirm_ProductionSelection_Panel = e -> {
            cardLayoutRight.show(centralRightPanel, PRODUCTION);
            //production.update();
        };

        public ProductionSelection_Panel() {
            leaderCardButton = new JToggleButton[2];
            devCardButton = new JToggleButton[3];

            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9), 1));
            addPadding(this, 599, 946, 10, 12);

            JButton back_ProductionSelection_Button = new JButton("Back");
            back_ProductionSelection_Button.addActionListener(back_activateLeaderCard_actionListener); //da cambiare il back
            back_ProductionSelection_Button.setPreferredSize(new Dimension(120, 40));
            back_ProductionSelection_Button.setFont(new Font(PAP, Font.BOLD, 20));
            back_ProductionSelection_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.weightx = 0.1;
            c.weighty = 0.1;
            c.insets = new Insets(5, 5, 0, 0);
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            this.add(back_ProductionSelection_Button, c);

            JLabel titleLabel = new JLabel("Choose your productions");
            titleLabel.setFont(new Font(PAP, Font.BOLD, 50));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 4;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(titleLabel, c);

            JPanel upperPanel = new JPanel();
            upperPanel.setLayout(new GridBagLayout());
            upperPanel.setOpaque(false);

            for(int i=0; i<2; i++) {
                this.leaderCardButton[i] = new JToggleButton();
                this.leaderCardButton[i].setText("Leader Card #"+(i+1));
                this.leaderCardButton[i].setPreferredSize(new Dimension(240, 60));
                this.leaderCardButton[i].setFont(new Font(PAP, Font.BOLD, 24));
                this.leaderCardButton[i].setBackground(new Color(231,210,181));

                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = i;
                c.weightx = 0.6;
                c.weighty = 0.4;
                c.anchor = GridBagConstraints.LINE_END;
                upperPanel.add(this.leaderCardButton[i] , c);
            }

            basicProduction = new JToggleButton();
            basicProduction.setIcon(scaleImage(new ImageIcon("resources/punchboard/basic_production.png"), 150));
            basicProduction.setBackground(new Color(231, 210, 181));
            basicProduction.setPreferredSize(new Dimension(150, 150));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 0;
            c.weightx = 0.1;
            c.weighty = 0.4;
            c.gridheight = 2;
            c.insets = new Insets(0,70,0,0);
            upperPanel.add(basicProduction, c);

            JLabel basicProductionLabel = new JLabel("basic production");
            basicProductionLabel.setFont(new Font(PAP, Font.BOLD, 24));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 0;
            c.weightx = 0.1;
            c.weighty = 0.4;
            c.gridheight = 2;
            c.anchor = GridBagConstraints.LINE_START;
            c.insets = new Insets(0,0,0,60);
            upperPanel.add(basicProductionLabel,c);

            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 3;
            c.weightx = 0.5;
            c.weighty = 0.4;
            c.gridwidth = 2;
            c.fill = GridBagConstraints.BOTH;
            this.add(upperPanel,c);


            JPanel stdProductionPanel = new JPanel(new GridBagLayout());
            stdProductionPanel.setOpaque(false);

            for(int i=0; i<3; i++) {
                this.devCardButton[i] = new JToggleButton();
                this.devCardButton[i].setBackground(new Color(231, 210, 181));
                this.devCardButton[i].setPreferredSize(new Dimension(178,255));
                c = new GridBagConstraints();
                c.gridx = i;
                c.weightx = 0.5;
                c.weighty = 0.4;
                c.insets = new Insets(0,50,0,50);
                stdProductionPanel.add(this.devCardButton[i], c);
            }

            c.gridx = 1;
            c.gridy = 4;
            c.weightx = 0.5;
            c.weighty = 0.4;
            c.gridwidth = 2;
            c.fill = GridBagConstraints.BOTH;
            this.add(stdProductionPanel, c);


            JButton confirm_ProductionSelection_Button = new JButton("confirm!");
            confirm_ProductionSelection_Button.addActionListener(confirm_ProductionSelection_Panel);
            confirm_ProductionSelection_Button.setPreferredSize(new Dimension(200, 60));
            confirm_ProductionSelection_Button.setFont(new Font(PAP, Font.BOLD, 28));
            confirm_ProductionSelection_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 5;
            c.weightx = 0.5;
            c.weighty = 0.4;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.PAGE_END;
            c.insets = new Insets(0,0,10,0);
            this.add(confirm_ProductionSelection_Button, c);

        }



        public void update() {
            DevelopmentCard[] topCards = Ark.myPlayerRef.getDevelopmentSlot().getTopCards();
            LeaderCard[] leaderCards = Ark.myPlayerRef.getLeaderCards();


            for(int i=0; i<2; i++) {
                if(leaderCards[i] == null)
                    this.leaderCardButton[i].setEnabled(false);
                else if(!leaderCards[i].isEnabled() || !leaderCards[i].getSpecialAbility().isProduction())
                    this.leaderCardButton[i].setEnabled(false);
            }


            for(int i=0; i<3; i++) {
                if(topCards[i] == null) {
                    this.devCardButton[i].setIcon(scaleImage(new ImageIcon("resources/cardsBack/BACK (1).png"), 235));
                    this.devCardButton[i].setEnabled(false);
                }
                else
                {
                    this.devCardButton[i].setIcon(scaleImage(new ImageIcon(topCards[i].getFrontPath()), 235));
                    this.devCardButton[i].setEnabled(true);
                }
            }
        }
        /*
        public JToggleButton getLeaderCard1() {
            return leaderCard1;
        }

        public JToggleButton getLeaderCard2() {
            return leaderCard2;
        }

        public JToggleButton getBasicProduction() {
            return basicProduction;
        }

        public JToggleButton getDevCard1() {
            return devCard1;
        }

        public JToggleButton getDevCard2() {
            return devCard2;
        }

        public JToggleButton getDevCard3() {
            return devCard3;
        }*/
    }

    class Production_Panel extends JPanel {

        public Production_Panel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9), 1));
            addPadding(this, 599, 946, 10, 12);

        }

        public void update() {

        }
    }

}