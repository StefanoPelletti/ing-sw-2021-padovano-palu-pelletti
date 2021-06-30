package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.modelSimplified.GameSimplified;
import it.polimi.ingsw.client.modelSimplified.PlayerSimplified;
import it.polimi.ingsw.client.modelSimplified.StrongboxSimplified;
import it.polimi.ingsw.client.modelSimplified.WarehouseDepotSimplified;
import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.actionMessages.*;
import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_Full;
import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_LeaderBoard;
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
    /**
     * Card Layout for the Left Panel.
     *
     * @see #centralLeftPanel
     */
    CardLayout cardLayoutLeft;
    /**
     * Card Layout for the Right Panel.
     *
     * @see #centralRightPanel
     */
    CardLayout cardLayoutRight;
    /**
     * The Central Left Panel is used to show the Player's Depots and Strongboxes. <p>
     * It is used with the cardLeftLayout, and its cards are named after the Players' names.
     * It is updatable with an update(name) method.
     */
    CentralLeftPanel centralLeftPanel;
    /**
     * The Central Right Panel is used to house the main tabs of the game. <ul>
     * <li>                       This Player's Development Slot                    (updatable)
     * <li>                       Other Players' Development Slot and Leader Cards  (updatable)
     * <li>                       The Market Panel                                  (updatable)
     * <li>                       The Development Cards Deck                        (updatable)
     * <li> The middle object   : LeaderCards Picker                                (updatable)
     * <li> The middle object   : Resource Picker                                   (updatable)
     * <li> The middle object   : Market Helper                                     (updatable)
     * <li> The middle object   : Card Vendor                                       (updatable)
     * <li> The action          : get Market Resources                              (updatable)
     * <li> The action          : activate Leader Card                              (updatable)
     * <li> The action          : change Depot config                               (updatable)
     * <li> The action          : production selection                              (updatable)
     * <li> The action          : activate production
     * <li> Finally             : The Leaderboard                                   (updatable)
     */
    CentralRightPanel centralRightPanel;

    /**
     * Reference to the mainFrame of the Board
     */
    JFrame mainFrame;

    /**
     * Reference to the mainPanel. The mainPanel is the new ContentPane of the mainFrame.
     */
    MainPanel mainPanel;

    /**
     * The last valid Left Card of the central Left Panel. <p>
     * By default it should always be Ark.nickname
     */
    String lastLeftCard;

    /**
     * The last valid Right Card of the central Right Panel. <p>
     * It is changed based on runtime actions.
     */
    String lastRightCard;

    static final String BACKPATH = "resources/cardsBack/BACK (1).png";


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

    static final String LEADERBOARD = "Leader Board";
    Leaderboard_Panel leaderboard_panel;


    //fonts
    static final String TIMES = "Times New Roman";
    static final String PAPYRUS = "Papyrus";


    /**
     * The Left Panel houses the notification pane, the turnOf label, the turn label, the leaderCards of this Player and 2 always-on buttons. <p>
     * It is updatable by the method it offers. Go check them.
     */
    LeftPanel leftPanel;

    /**
     * The Bottom Panel houses the actions buttons and the Players Recap panel. <p>
     * The Players Recap panel is only available in multiplayer mode. Otherwise, a possible set of actions by Lorenzo is shown.
     * It is updatable and modifiable by default update() and disable/enableButtons() methods.
     */
    BottomPanel bottomPanel;

    /**
     * These buttons are located in the bottom Panel. <p>
     * Their reference is saved because they can also be disabled.
     */
    JButton activate_LeaderCards_Button, change_Depot_Config_Button, get_MarketResource_Button;
    JButton discard_LeaderCard_Button, buy_DevCard_Button, activate_Production_Button;
    JButton endTurn_Button;

    /**
     * The Top Panel houses the Faith Track. <p>
     * It offers an update() method which will automatically update the FaithTrack and the FaithZones. Works in both Solo and Multiplayer mode.
     */
    TopPanel topPanel;


    //ACTION LISTENERS
    ActionListener quit_actionListener = e -> {
        Ark.sweep();
        mainFrame.dispose();
        SwingUtilities.invokeLater(new MainMenu());
    };
    ActionListener show_DevDeck_actionListener = e -> {
        changeRightCard(DEVDECK);
        changeLeftCard(Ark.nickname);
    };
    ActionListener show_Market_actionListener = e -> {
        changeRightCard(MARKET);
        changeLeftCard(Ark.nickname);
    };
    ActionListener show_activateLeaderCard_actionListener = e -> {
        changeRightCard(ACTIVATELEADERCARD);
        changeLeftCard(Ark.nickname);
    };
    ActionListener show_discardLeaderCard_actionListener = e -> {
        changeRightCard(DISCARDLEADERCARD);
        changeLeftCard(Ark.nickname);
    };
    ActionListener show_changeDepotConfig_actionListener = e -> {
        changeRightCard(CHANGEDEPOTCONFIG);
        changeLeftCard(Ark.nickname);
    };
    ActionListener show_getMarketResource_actionListener = e -> {
        changeRightCard(GETMARKETRESOURCES);
        changeLeftCard(Ark.nickname);
        Ark.triedAction = true;
    };
    ActionListener show_ProductionSelection_actionListener = e -> {
        changeRightCard(PRODSELECTION);
        changeLeftCard(Ark.nickname);
        Ark.triedAction = true;
    };

    /**
     * ActionListener for a generic Back button. <p>
     * Basically sends the two Central panels back to the Last valid Card.
     */
    ActionListener genericBack_actionListener = e -> {
        changeRightCard(lastRightCard);
        changeLeftCard(lastLeftCard);
    };
    /* *
     * ACTION LISTENERS FOR ACTIONS
     * */
    /**
     * ActionListener for the Choose LeaderCard action. <p>
     * Basically gathers information from the panel or the source, composes a message and sends it.
     *
     * @see it.polimi.ingsw.client.gui.Board.LeaderCardsPicker_Panel the panel that uses that actionListener
     */
    ActionListener init_leaderCardsPicker_actionListener = e -> {

        int firstCard = leaderCardsPicker_panel.getFirst();
        int secondCard = leaderCardsPicker_panel.getSecond();

        MSG_INIT_CHOOSE_LEADERCARDS message = new MSG_INIT_CHOOSE_LEADERCARDS(firstCard, secondCard);

        send(message);

        changeRightCard(Ark.nickname);
        lastRightCard = Ark.nickname;
    };

    /**
     * ActionListener for the Choose Resource action. <p>
     * Basically gathers information from the panel or the source, composes a message and sends it.
     *
     * @see it.polimi.ingsw.client.gui.Board.ResourcePicker_Panel the panel that uses that actionListener
     */
    ActionListener init_resourcePicker_actionListener = e -> {
        ResourcePicker_Panel.CustomResourceButton source = (ResourcePicker_Panel.CustomResourceButton) e.getSource();

        Resource resource = source.getResource();

        MSG_INIT_CHOOSE_RESOURCE message = new MSG_INIT_CHOOSE_RESOURCE(resource);

        send(message);
    };

    /**
     * ActionListener for the Buy Dev Card action. <p>
     * Basically creates a message and sends it.
     */
    ActionListener action_buyDevCard_actionListener = e -> {
        MSG_ACTION_BUY_DEVELOPMENT_CARD message = new MSG_ACTION_BUY_DEVELOPMENT_CARD();

        send(message);
    };

    /**
     * ActionListener for the choose Development Card action. <p>
     * Basically gathers information from the panel or the source, composes a message and sends it.
     *
     * @see it.polimi.ingsw.client.gui.Board.Vendor_Panel the panel that uses that actionListener
     */
    ActionListener action_chooseDevCard_actionListener = e -> {
        Vendor_Panel.SlotButton source = (Vendor_Panel.SlotButton) e.getSource();

        int slotNumber = source.getPosition();
        int cardNumber = vendor_panel.getCurrentCard();

        MSG_ACTION_CHOOSE_DEVELOPMENT_CARD message;

        if (slotNumber == -1) {
            Ark.triedAction = false;
            message = new MSG_ACTION_CHOOSE_DEVELOPMENT_CARD(-1, -1);
            enableActionBottomButtons();
        } else {
            Ark.triedAction = true;
            message = new MSG_ACTION_CHOOSE_DEVELOPMENT_CARD(cardNumber, slotNumber);
        }

        send(message);
    };

    /**
     * ActionListener for the activate Production action. <p>
     * Basically gathers information from the panel(s), composes a message and sends it.
     *
     * @see it.polimi.ingsw.client.gui.Board.Production_Panel the panel that uses that actionListener
     */
    ActionListener action_activateProduction_actionListener = e -> {
        boolean[] standardProduction = new boolean[3];
        boolean basicProduction;
        boolean[] leaderCardOutput = new boolean[2];
        List<Resource> basicInput = new ArrayList<>();

        Ark.triedAction = true;

        for (int i = 0; i < 3; i++)
            standardProduction[i] = productionSelection_panel.devCardButton[i].isSelected();

        basicProduction = productionSelection_panel.getBasicProduction().isSelected();

        leaderCardOutput[0] = productionSelection_panel.getLeaderCard1().isSelected();
        leaderCardOutput[1] = productionSelection_panel.getLeaderCard2().isSelected();

        basicInput.add(production_panel.resources[0]);
        basicInput.add(production_panel.resources[1]);

        Resource basicOutput = production_panel.resources[2];

        Resource lc1Output = production_panel.resources[3];
        Resource lc2Output = production_panel.resources[4];

        MSG_ACTION_ACTIVATE_PRODUCTION message = new MSG_ACTION_ACTIVATE_PRODUCTION(standardProduction, basicProduction, leaderCardOutput, basicInput, basicOutput, lc1Output, lc2Output);
        send(message);

        changeRightCard(Ark.nickname);
    };

    /**
     * ActionListener for the Change Depot Config action. <p>
     * Basically gathers information from the panel or the source, composes a message and sends it.
     *
     * @see it.polimi.ingsw.client.gui.Board.ChangeDepotConfig_Panel the panel that uses that actionListener
     */
    ActionListener action_changeDepotConfig_actionListener = e -> {
        Resource shelf1 = changeDepotConfig_panel.getShelf1();
        Resource[] shelf2 = changeDepotConfig_panel.getShelf2();
        Resource[] shelf3 = changeDepotConfig_panel.getShelf3();
        int extraDepot1 = changeDepotConfig_panel.getFirstDepotQuantity();
        int extraDepot2 = changeDepotConfig_panel.getSecondDepotQuantity();

        Ark.triedAction = false;

        MSG_ACTION_CHANGE_DEPOT_CONFIG message = new MSG_ACTION_CHANGE_DEPOT_CONFIG(shelf1, shelf2, shelf3, extraDepot1, extraDepot2);
        send(message);

        changeRightCard(Ark.nickname);
    };

    /**
     * ActionListener for the Get Market Resources action. <p>
     * Basically gathers information from the panel or the source, composes a message and sends it.
     *
     * @see it.polimi.ingsw.client.gui.Board.GetMarketResource_Panel the panel that uses that actionListener
     */
    ActionListener action_getMarketResource_actionListener = e -> {
        GetMarketResource_Panel.MarketButton source = (GetMarketResource_Panel.MarketButton) e.getSource();
        int number = source.getNum();
        boolean column = source.isColumn();

        Ark.triedAction = true;

        MSG_ACTION_GET_MARKET_RESOURCES message = new MSG_ACTION_GET_MARKET_RESOURCES(column, number);
        send(message);
    };

    /**
     * ActionListener for the Market Helper choice action. <p>
     * Basically gathers information from the panel or the source, composes a message and sends it.
     *
     * @see it.polimi.ingsw.client.gui.Board.MarketHelper_Panel the panel that uses that actionListener
     */
    ActionListener action_newMarketChoice_actionListener = e -> {
        MarketHelper_Panel.ChoiceButton source = (MarketHelper_Panel.ChoiceButton) e.getSource();

        int choiceNumber = source.getChoiceNumber();

        MSG_ACTION_MARKET_CHOICE message = new MSG_ACTION_MARKET_CHOICE(choiceNumber);

        send(message);
    };

    /**
     * ActionListener for the activateLeaderCard action. <p>
     * Basically gathers information from the panel or the source, composes a message and sends it.
     *
     * @see it.polimi.ingsw.client.gui.Board.ActivateLeaderCard_Panel the panel that uses that actionListener
     */
    ActionListener action_activateLeaderCard_actionListener = e -> {
        ActivateLeaderCard_Panel.ChooseLeaderCardButton source = (ActivateLeaderCard_Panel.ChooseLeaderCardButton) e.getSource();
        int cardNumber = source.getNumber();

        Ark.triedAction = false;

        MSG_ACTION_ACTIVATE_LEADERCARD message = new MSG_ACTION_ACTIVATE_LEADERCARD(cardNumber);
        send(message);

        changeRightCard(Ark.nickname);
    };

    /**
     * ActionListener for the discardLeaderCard action. <p>
     * Basically gathers information from the panel or the source, composes a message and sends it.
     *
     * @see it.polimi.ingsw.client.gui.Board.DiscardLeaderCard_Panel the panel that uses that actionListener
     */
    ActionListener action_discardLeaderCard_actionListener = e -> {
        DiscardLeaderCard_Panel.ChooseLeaderCardButton source = (DiscardLeaderCard_Panel.ChooseLeaderCardButton) e.getSource();
        int cardNumber = source.getNumber();

        Ark.triedAction = false;

        MSG_ACTION_DISCARD_LEADERCARD message = new MSG_ACTION_DISCARD_LEADERCARD(cardNumber);
        send(message);

        changeRightCard(Ark.nickname);
    };

    /**
     * ActionListener for the End Turn action. <p>
     * Basically composes a message and sends it.
     */
    ActionListener action_endTurn_actionListener = e -> {

        Ark.triedAction = false;
        Ark.action = false;
        this.disableBottomButtons();

        MSG_ACTION_ENDTURN message = new MSG_ACTION_ENDTURN();
        send(message);
    };

    public Board() {
        mainFrame = new JFrame(" Masters of Renaissance - Board ");
        mainPanel = new MainPanel();
        mainFrame.setContentPane(mainPanel);

        mainFrame.pack();

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    /**
     * Shortcut method for changing the central Left Panel
     *
     * @param newCard the keyword that points to the next card
     */
    public void changeLeftCard(String newCard) {
        this.cardLayoutLeft.show(centralLeftPanel, newCard);
    }

    /**
     * Shortcut method for changing the central Right Panel
     *
     * @param newCard the keyword that points to the next card
     */
    public void changeRightCard(String newCard) {
        this.cardLayoutRight.show(centralRightPanel, newCard);
    }

    public void run() {
        //empty to implement run method
    }

    /**
     * The MainPanel class is the new contentPane of the MainFrame. <p>
     * It constructs the entire page, composed by 5 panels, and performs a first update.
     */
    class MainPanel extends JPanel {

        private Image image;

        public MainPanel() {
            try {
                image = ImageIO.read(new File("resources/images/board_bg.png"));
            } catch (IOException ignored) {
            }

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

            // INITIAL UPDATE
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

            changeLeftCard(Ark.nickname);
            changeRightCard(Ark.nickname);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }

    }

    /**
     * Class of the Left Panel.
     *
     * @see #leftPanel
     */
    class LeftPanel extends JPanel {

        private final JLabel turnLabel, turnOf;
        private final JTextArea notificationsArea;
        private final MyLeaderCardsPanel myLeaderCardsPanel;

        public LeftPanel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBackground(new Color(215, 200, 145));
            this.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, new Color(62, 43, 9)));

            Ark.addPadding(this, 839, 458, 3, 6);

            JButton quitButton = new JButton("Quit");
            quitButton.addActionListener(quit_actionListener);
            quitButton.setPreferredSize(new Dimension(120, 60));
            quitButton.setFont(new Font(PAPYRUS, Font.BOLD, 20));
            quitButton.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 2;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.insets = new Insets(20, 20, 0, 0);
            this.add(quitButton, c);

            turnLabel = new JLabel();
            turnLabel.setOpaque(false);
            turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
            turnLabel.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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
            turnOf.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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

            JPanel showButtonsPanel = new JPanel();
            showButtonsPanel.setOpaque(false);
            showButtonsPanel.setLayout(new GridBagLayout());

            JButton showDevDeckButton = new JButton("show DevDeck");
            showDevDeckButton.addActionListener(show_DevDeck_actionListener);
            showDevDeckButton.setPreferredSize(new Dimension(200, 70));
            showDevDeckButton.setFont(new Font(PAPYRUS, Font.BOLD, 20));
            showDevDeckButton.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.insets = new Insets(5, 5, 20, 12);
            c.anchor = GridBagConstraints.LINE_START;
            showButtonsPanel.add(showDevDeckButton, c);

            JButton showMarketButton = new JButton("show Market");
            showMarketButton.addActionListener(show_Market_actionListener);
            showMarketButton.setPreferredSize(new Dimension(200, 70));
            showMarketButton.setFont(new Font(PAPYRUS, Font.BOLD, 20));
            showMarketButton.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 0;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.insets = new Insets(5, 12, 20, 5);
            c.anchor = GridBagConstraints.LINE_END;
            showButtonsPanel.add(showMarketButton, c);

            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 5;
            c.weightx = 0.5;
            c.weighty = 0.1;
            c.gridwidth = 2;
            c.gridheight = 1;
            c.anchor = GridBagConstraints.PAGE_END;
            this.add(showButtonsPanel, c);
            //showDevDeck and showMarket buttons Panel
        }

        /**
         * Offered by the leftPanel, updates the LeaderCards panel and the turn labels.
         */
        public void updateAll() {
            this.update();
            this.updateTurnOf();
            this.updateCurrentTurn();
        }

        /**
         * Offered by the leftPanel, updates this Player's Leader Cards.
         */
        public void update() {
            this.myLeaderCardsPanel.update();
        }

        /**
         * Offered by the leftPanel, updates the label of the current Player.
         */
        public void updateTurnOf() {
            this.turnOf.setText("Turn of " + Ark.game.getCurrentPlayerName());
        }

        /**
         * Offered by the leftPanel, updates the label of the current Turn.
         */
        public void updateCurrentTurn() {
            this.turnLabel.setText("Turn " + Ark.game.getTurn());
        }

        /**
         * Offered by the leftPanel, displays a given message into the notification pane.
         *
         * @param message the String to be displayed
         */
        public void updateNotification(String message) {
            Formatter formate;
            Calendar gfg_calender = Calendar.getInstance();
            formate = new Formatter();
            formate.format("%tl:%tM", gfg_calender, gfg_calender);

            // Printing the current hour and minute
            notificationsArea.append("\n\n" + formate + " - ");

            int i = 7;

            List<String> textList;
            textList = new ArrayList<>((Arrays.asList(message.split("\\s+"))));
            Optional<Integer> length = textList.stream().map(String::length).reduce(Integer::sum);
            if (length.isPresent()) {
                for (String s : textList) {
                    if ((s.length() + i) > 34) {
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

    /**
     * This Panel houses this Player's LeaderCards. <p>
     * It is updatable, but updates should be performed using its parent, the leftPanel.
     */
    class MyLeaderCardsPanel extends JPanel {

        private final JLabel leaderCardLabel1, leaderCardLabel2;
        private final JLabel extraResource1_LeaderCard1_Label, extraResource2_LeaderCard1_Label;
        private final JLabel extraResource1_LeaderCard2_Label, extraResource2_LeaderCard2_Label;
        private final JLabel labelUnderLeaderCard1, labelUnderLeaderCard2;

        public MyLeaderCardsPanel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);

            JLabel leaderCardLabel1text = new JLabel();
            leaderCardLabel1text.setText("Leader Card");
            leaderCardLabel1text.setOpaque(false);
            leaderCardLabel1text.setHorizontalAlignment(SwingConstants.CENTER);
            leaderCardLabel1text.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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
            leaderCardLabel1text1.setFont(new Font(PAPYRUS, Font.BOLD, 22));
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
            leaderCardLabel2text.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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
            leaderCardLabel2text2.setFont(new Font(PAPYRUS, Font.BOLD, 22));
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
            labelUnderLeaderCard1.setFont(new Font(PAPYRUS, Font.BOLD, 22));
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
            labelUnderLeaderCard2.setFont(new Font(PAPYRUS, Font.BOLD, 22));
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

        /**
         * Updates both Leader Cards.
         */
        public void update() {
            LeaderCard l;

            l = Ark.myPlayerRef.getLeaderCards()[0];
            updateSpecificCard(l, labelUnderLeaderCard1, leaderCardLabel1, extraResource1_LeaderCard1_Label, extraResource2_LeaderCard1_Label);

            l = Ark.myPlayerRef.getLeaderCards()[1];
            updateSpecificCard(l, labelUnderLeaderCard2, leaderCardLabel2, extraResource1_LeaderCard2_Label, extraResource2_LeaderCard2_Label);
        }

        /**
         * Updates a specific leader Card, either the first or the second. <p>
         * Displays if a card is enabled, an image representing it, the actual extradepot (if it is), or the back of the card.
         *
         * @param leaderCard           the Leader Card model reference
         * @param labelUnderLeaderCard the reference to the Label under that LeaderCardLabel
         * @param leaderCardLabel      the reference to the LeaderCard Label
         * @param extraDepotLabel1     the reference to the first extra Depot label
         * @param extraDepotLabel2     the reference to the second extra Depot label
         */
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
                            case 1:
                                extraDepotLabel1.setIcon(presentIcon);
                                extraDepotLabel2.setIcon(noneIcon);
                                break;
                            case 2:
                                extraDepotLabel1.setIcon(presentIcon);
                                extraDepotLabel2.setIcon(presentIcon);
                                break;
                            default:
                                extraDepotLabel1.setIcon(noneIcon);
                                extraDepotLabel2.setIcon(noneIcon);
                                break;
                        }
                    }
                }
                else {
                    labelUnderLeaderCard.setText("not enabled");
                }
            } else {
                t = scaleImage(new ImageIcon(BACKPATH), 300);
                labelUnderLeaderCard.setText("not present!");
            }
            leaderCardLabel.setIcon(t);
        }
    }

    /**
     * Class of Bottom Panel.
     *
     * @see #bottomPanel
     */
    class BottomPanel extends JPanel {

        private final PlayersRecapPanel playersRecapPanel;

        public BottomPanel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBackground(new Color(215, 200, 145));
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9), 1));

            Ark.addPadding(this, 158, 1678, 6, 3);

            {
                activate_LeaderCards_Button = new JButton("activate card");
                activate_LeaderCards_Button.addActionListener(show_activateLeaderCard_actionListener);
                activate_LeaderCards_Button.setPreferredSize(new Dimension(145, 60));
                activate_LeaderCards_Button.setFont(new Font(PAPYRUS, Font.BOLD, 18));
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
                change_Depot_Config_Button.setFont(new Font(PAPYRUS, Font.BOLD, 18));
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
                get_MarketResource_Button.setFont(new Font(PAPYRUS, Font.BOLD, 18));
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
                discard_LeaderCard_Button.setFont(new Font(PAPYRUS, Font.BOLD, 18));
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
                buy_DevCard_Button.setFont(new Font(PAPYRUS, Font.BOLD, 18));
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
                activate_Production_Button.setFont(new Font(PAPYRUS, Font.BOLD, 18));
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
            endTurn_Button.setFont(new Font(PAPYRUS, Font.BOLD, 24));
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

        /**
         * If the mode is Multiplayer, updates the Players Recap Panel.
         */
        public void update() {
            if (!Ark.solo) {
                this.playersRecapPanel.update();
            }
        }
    }

    /**
     * Class of Players Recap Panel, which is inside the BottomPanel.
     */
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
                    nicknameLabel.setFont(new Font(PAPYRUS, Font.BOLD, 24));
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

                        changeLeftCard(playerName);
                        changeRightCard(playerName);
                    });


                    button.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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
                lorenzoPanel.setBorder(BorderFactory.createLineBorder(new Color(79, 66, 34), 1));
                lorenzoPanel.setOpaque(false);

                JLabel lorenzoLabel = new JLabel("Lorenzo");
                lorenzoLabel.setFont(new Font(PAPYRUS, Font.BOLD, 24));
                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                c.weightx = 0.2;
                c.weighty = 0.2;
                lorenzoPanel.add(lorenzoLabel, c);

                JLabel lorenzoFace = new JLabel();
                lorenzoFace.setIcon(scaleImage(new ImageIcon("resources/punchboard/lorenzo.png"), 110));
                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 1;
                c.weightx = 0.2;
                c.weighty = 0.2;
                lorenzoPanel.add(lorenzoFace, c);

                JPanel spacer = new JPanel(new GridBagLayout());
                spacer.setBorder(BorderFactory.createLineBorder(new Color(155, 129, 66), 1));
                c = new GridBagConstraints();
                spacer.add(Box.createHorizontalStrut(1), c);
                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 0;
                c.gridheight = 2;
                c.weightx = 0;
                c.weighty = 0;
                c.fill = GridBagConstraints.VERTICAL;
                lorenzoPanel.add(spacer, c);

                JPanel tokenPanel = new JPanel(new GridBagLayout());
                tokenPanel.setOpaque(false);

                JLabel possibleActions = new JLabel("Lorenzo tokens");
                possibleActions.setFont(new Font(PAPYRUS, Font.BOLD, 24));
                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                c.weightx = 0.5;
                c.weighty = 0.5;
                tokenPanel.add(possibleActions, c);

                JPanel tokenSlideshow = new JPanel(new FlowLayout());
                tokenSlideshow.setOpaque(false);

                for (int i = 0; i < 7; i++) {
                    String path = "resources/punchboard/cerchio";
                    path += "" + (i + 1) + ".png";

                    JLabel label = new JLabel();
                    label.setIcon(scaleImage(new ImageIcon(path), 65));
                    tokenSlideshow.add(label);
                }

                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 1;
                c.weightx = 0.5;
                c.weighty = 0.5;
                tokenPanel.add(tokenSlideshow, c);

                JLabel check = new JLabel("Check notification area in-game");
                check.setFont(new Font(PAPYRUS, Font.BOLD, 24));
                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 2;
                c.weightx = 0.5;
                c.weighty = 0.5;
                tokenPanel.add(check, c);

                c = new GridBagConstraints();
                c.gridx = 2;
                c.gridy = 0;
                c.gridheight = 2;
                c.weightx = 0.5;
                c.weighty = 0.5;
                c.fill = GridBagConstraints.BOTH;
                lorenzoPanel.add(tokenPanel, c);

                c = new GridBagConstraints();
                c.fill = GridBagConstraints.BOTH;
                c.weightx = 0.5;
                c.weighty = 0.5;
                this.add(lorenzoPanel, c);
            }
        }

        /**
         * It updates the PlayerRecap Panel, specifying the number of resources that each player has.
         */
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

        /**
         * Custom Button that stores a String with a Player's name.
         */
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

    /**
     * Class of Top Panel.
     *
     * @see #topPanel
     */
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
            } catch (IOException ignored) {
            }
            this.setLayout(new GridBagLayout());

            Ark.addPadding(this, 239, 1221, 4, 4);

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

        /**
         * Custom Panel that self constructs. Used for the faith Zones.
         */
        class ZonePanel extends JPanel {

            static final int BOXSIZE = 112;
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
                this.add(Box.createHorizontalStrut(BOXSIZE), c);

                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 1;
                c.weightx = 0;
                c.weighty = 0;
                this.add(Box.createVerticalStrut(BOXSIZE), c);

                this.label = new JLabel();
                this.label.setHorizontalAlignment(SwingConstants.CENTER);
                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 1;
                c.weightx = 0.5;
                c.weighty = 0.5;
                c.fill = GridBagConstraints.BOTH;
                this.add(this.label, c);
            }

            public void setIcon(Icon icon) {
                this.label.setIcon(icon);
            }
        }

        /**
         * Custom Panel that self constructs, and goes in the grid of labels. <p>
         * It also stores a public boolean that tells if this cell is empty or not.
         */
        class CellPanel extends JPanel {

            static final int BOXSIZE = 54;
            private final JLabel label;
            public boolean isEmpty;

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
                this.add(Box.createHorizontalStrut(BOXSIZE), c);

                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 1;
                c.weightx = 0;
                c.weighty = 0;
                this.add(Box.createVerticalStrut(BOXSIZE), c);

                if (useful) {
                    this.label = new JLabel();
                    this.label.setHorizontalAlignment(SwingConstants.CENTER);
                    c = new GridBagConstraints();
                    c.gridx = 1;
                    c.gridy = 1;
                    c.weightx = 0.5;
                    c.weighty = 0.5;
                    c.fill = GridBagConstraints.BOTH;
                    this.add(this.label, c);
                } else
                    this.label = null;

            }

            public void setIcon(Icon icon) {
                this.label.setIcon(icon);
            }
        }

        /**
         * Offered by the Top Panel, updates the entire faith Track.
         * <p> Works in both Solo and Multiplayer mode.
         */
        public void update() {
            boolean[] flippedZones = Ark.myPlayerRef.getFaithTrackPanels();
            if (flippedZones[0])
                this.zones[0].setIcon(scaleImage(new ImageIcon("resources/punchboard/pope_favor1_front.png"), 85));
            else
                this.zones[0].setIcon(scaleImage(new ImageIcon("resources/punchboard/pope_favor1_back.png"), 85));
            if (flippedZones[1])
                this.zones[1].setIcon(scaleImage(new ImageIcon("resources/punchboard/pope_favor2_front.png"), 85));
            else
                this.zones[1].setIcon(scaleImage(new ImageIcon("resources/punchboard/pope_favor2_back.png"), 85));
            if (flippedZones[2])
                this.zones[2].setIcon(scaleImage(new ImageIcon("resources/punchboard/pope_favor3_front.png"), 85));
            else
                this.zones[2].setIcon(scaleImage(new ImageIcon("resources/punchboard/pope_favor3_back.png"), 85));

            int[] positionMap = new int[4];
            int numOfPlayers = Ark.game.getPlayerSimplifiedList().size();
            for (int i = 0; i < 4; i++) {
                if (i >= numOfPlayers)
                    positionMap[i] = -1;
                else
                    positionMap[i] = Ark.game.getPlayer(i + 1).getPosition();
            }

            //empties the currently populated track
            for (int i = 0; i <= 24; i++) {
                if (!this.positions[i].isEmpty) {
                    this.positions[i].setIcon(null);
                    this.positions[i].isEmpty = true;
                }
            }

            for (int i = 0; i < numOfPlayers; i++) {
                if (!this.positions[positionMap[i]].isEmpty) continue;
                this.positions[positionMap[i]].setIcon(getNewIcon(i, positionMap));
                this.positions[positionMap[i]].isEmpty = false;
            }

            if (Ark.solo) {
                if (this.positions[Ark.game.getBlackCrossPosition()].isEmpty) {
                    this.positions[Ark.game.getBlackCrossPosition()].setIcon(scaleImage(new ImageIcon("resources/FaithTrackIcons/L.png"), 40));
                    this.positions[Ark.game.getBlackCrossPosition()].isEmpty = false;
                }
            }

        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }

        /**
         * Returns the icon that should be used in a cell on the faithTrack.
         * <p> Works by sending a intmap containing the position of the players, and performing a logic matrix combination.
         * That combination corresponds to a single image.</p>
         *
         * @param selectedPosition the current position of the intmap
         * @param intmap           an intmap containing the Player's positions
         * @return an image that shows which players are on that cell
         */
        public ImageIcon getNewIcon(int selectedPosition, int[] intmap) {
            String output = "";

            if (Ark.solo) {
                if (intmap[0] == Ark.game.getBlackCrossPosition()) {
                    output = "resources/FaithTrackIcons/PL.png"; //Lorenzo and player sharing the same cell
                } else
                    output = "resources/FaithTrackIcons/P.png"; //player not sharing the same position as Lorenzo
                return scaleImage(new ImageIcon(output), 40);
            }

            boolean[] p = new boolean[4];
            p[selectedPosition] = true;
            for (int i = 0; i < 4; i++) {
                if (intmap[i] == -1) break;
                if (intmap[i] == intmap[selectedPosition]) p[i] = true;
            }

            if (p[0] && !p[1] && !p[2] && !p[3])      // 1
                output = "resources/FaithTrackIcons/1.png";
            else if (!p[0] && p[1] && !p[2] && !p[3])      //2
                output = "resources/FaithTrackIcons/2.png";
            else if (!p[0] && !p[1] && p[2] && !p[3])      // 3
                output = "resources/FaithTrackIcons/3.png";
            else if (!p[0] && !p[1] && !p[2] && p[3])      // 4
                output = "resources/FaithTrackIcons/4.png";
            else if (p[0] && p[1] && !p[2] && !p[3])      // 12
                output = "resources/FaithTrackIcons/12.png";
            else if (p[0] && !p[1] && p[2] && !p[3])      // 13
                output = "resources/FaithTrackIcons/13.png";
            else if (p[0] && !p[1] && !p[2] && p[3])      // 14
                output = "resources/FaithTrackIcons/14.png";
            else if (!p[0] && p[1] && p[2] && !p[3])      // 23
                output = "resources/FaithTrackIcons/23.png";
            else if (!p[0] && p[1] && !p[2] && p[3])      // 24
                output = "resources/FaithTrackIcons/24.png";
            else if (!p[0] && !p[1] && p[2] && p[3])      // 34
                output = "resources/FaithTrackIcons/34.png";
            else if (p[0] && p[1] && p[2] && !p[3])       // 123
                output = "resources/FaithTrackIcons/123.png";
            else if (p[0] && p[1] && !p[2] && p[3])      // 124
                output = "resources/FaithTrackIcons/124.png";
            else if (p[0] && !p[1] && p[2] && p[3])      // 134
                output = "resources/FaithTrackIcons/134.png";
            else if (!p[0] && p[1] && p[2] && p[3])       // 234
                output = "resources/FaithTrackIcons/234.png";
            else if (p[0] && p[1] && p[2] && p[3])      // 1234
                output = "resources/FaithTrackIcons/1234.png";


            return scaleImage(new ImageIcon(output), 40);
        }

        /**
         * Tells if a cell in a 3x19 grid is on the faithTrack (useful) or not (not useful)
         *
         * @param row    the row being tested
         * @param column the column being tested
         * @return True if the specified cell is on the faithTrack, False otherwise
         */
        public boolean isUseful(int row, int column) {
            switch (row) {
                case 0:
                    switch (column) {
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
                    switch (column) {
                        case 2:
                        case 7:
                        case 12:
                            return true;
                        default:
                            return false;
                    }
                case 2:
                    switch (column) {
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

        /**
         * Tells the position that a cell in a 3x19 grid corresponds to on the faithTrack.
         * <p>
         * It maps every useful cell on a position >= 0 and <=24
         *
         * @param row    the row being tested
         * @param column the column being testes
         * @return an int, position of that cell on the faithTrack. (between 0 and 24) (-1 if it is not on the faithTrack).
         */
        public int pos(int row, int column) {
            switch (row) {
                case 0:
                    switch (column) {
                        case 2:
                            return 4;
                        case 3:
                            return 5;
                        case 4:
                            return 6;
                        case 5:
                            return 7;
                        case 6:
                            return 8;
                        case 7:
                            return 9;
                        case 12:
                            return 18;
                        case 13:
                            return 19;
                        case 14:
                            return 20;
                        case 15:
                            return 21;
                        case 16:
                            return 22;
                        case 17:
                            return 23;
                        case 18:
                            return 24;
                        default:
                            return -1;
                    }
                case 1:
                    switch (column) {
                        case 2:
                            return 3;
                        case 7:
                            return 10;
                        case 12:
                            return 17;
                        default:
                            return -1;
                    }
                case 2:
                    switch (column) {
                        case 0:
                            return 0;
                        case 1:
                            return 1;
                        case 2:
                            return 2;
                        case 7:
                            return 11;
                        case 8:
                            return 12;
                        case 9:
                            return 13;
                        case 10:
                            return 14;
                        case 11:
                            return 15;
                        case 12:
                            return 16;
                        default:
                            return -1;
                    }
                default:
                    return -1;
            }
        }
    }

    /**
     * Class of the Central Left Panel.
     * Managed by the cardLayoutLeft.
     *
     * @see #centralLeftPanel
     * @see #cardLayoutLeft
     */
    class CentralLeftPanel extends JPanel {
        private final java.util.List<DepotAndStrongbox_Panel> depotAndStrongboxCardPanelList;
        private Image image;

        public CentralLeftPanel() {
            try {
                image = ImageIO.read(new File("resources/images/left_board.png"));
            } catch (IOException ignored) {
            }

            cardLayoutLeft = new CardLayout();
            this.setLayout(cardLayoutLeft);

            depotAndStrongboxCardPanelList = new ArrayList<>();

            for (PlayerSimplified player : Ark.game.getPlayerList()) {
                DepotAndStrongbox_Panel depotAndStrongboxCardPanel = new DepotAndStrongbox_Panel(player.getNickname());
                this.add(depotAndStrongboxCardPanel, player.getNickname());
                depotAndStrongboxCardPanelList.add(depotAndStrongboxCardPanel);
            }
        }

        /**
         * Updates the DepotAndStrongbox_Panel that has the specified name.
         *
         * @param name the name of the Panel that you want to update
         */
        public void update(String name) {
            Optional<DepotAndStrongbox_Panel> result = depotAndStrongboxCardPanelList.stream().filter(p -> p.panelName.equals(name)).findFirst();
            result.ifPresent(DepotAndStrongbox_Panel::update);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }

    /**
     * Class of DepotAndStrongbox Panel, which is inside the LeftPanel.
     */
    class DepotAndStrongbox_Panel extends JPanel {
        private final JLabel shelf1;
        private final JLabel[] shelf2;
        private final JLabel[] shelf3;
        private final JLabel[] strongbox; //coin shield stone servant
        private final String panelName;

        public DepotAndStrongbox_Panel(String panelName) {
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

            Ark.addPadding(this, 601, 273, 5, 6);

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

            strongbox[0] = new JLabel(); //coin
            strongbox[0].setFont(new Font(PAPYRUS, Font.BOLD, 20));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 4;
            c.weightx = 0.5;
            c.weighty = 0.1;
            c.gridwidth = 1;
            c.anchor = GridBagConstraints.FIRST_LINE_END;
            c.insets = new Insets(70, 0, 3, 80);
            this.add(strongbox[0], c);

            strongbox[1] = new JLabel();  //shield
            strongbox[1].setFont(new Font(PAPYRUS, Font.BOLD, 20));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 4;
            c.weightx = 0.5;
            c.weighty = 0.1;
            c.gridwidth = 1;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.insets = new Insets(70, 10, 3, 0);
            this.add(strongbox[1], c);

            strongbox[2] = new JLabel();  //stone
            strongbox[2].setFont(new Font(PAPYRUS, Font.BOLD, 20));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 5;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.gridwidth = 1;
            c.anchor = GridBagConstraints.FIRST_LINE_END;
            c.insets = new Insets(35, 0, 2, 80);
            this.add(strongbox[2], c);

            strongbox[3] = new JLabel();  //servant
            strongbox[3].setFont(new Font(PAPYRUS, Font.BOLD, 20));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 5;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.gridwidth = 1;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.insets = new Insets(35, 10, 2, 0);
            this.add(strongbox[3], c);
        }

        /**
         * Updates the Strongbox and Depot of a this Player.
         * <p>
         * It should be called by the centralLeftPanel, and not directly.
         * </p>
         */
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
     * Class of the Central Right Panel.
     * Managed by the cardLayoutRight.
     *
     * @see #centralRightPanel
     * @see #cardLayoutRight
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
            leaderboard_panel = new Leaderboard_Panel();
            this.add(leaderboard_panel, LEADERBOARD);
            for (PlayerSimplified player : Ark.game.getPlayerList()) {
                if (player.getNickname().equals(Ark.nickname)) continue;

                Others_DevSlot_Panel panel = new Others_DevSlot_Panel(player.getNickname());
                this.add(panel, player.getNickname());
                othersDevSlotList.add(panel);
            }
        }

        /**
         * Updates the DevSlot Panel for the specified Player
         *
         * @param name the name of the Player that gets the update
         */
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

    /**
     * Class of Self_DevSlot_Panel, which is inside the CentralRightPanel.
     * <p> updatable by calling the update(String) method in the centralRightPanel. </p>
     */
    class Self_DevSlot_Panel extends JPanel { //this card is called by Ark.nickname

        private final JLabel[][] grid;
        private Image image;

        public Self_DevSlot_Panel() {
            this.setLayout(new GridBagLayout());
            GridBagConstraints c;

            try {
                image = ImageIO.read(new File("resources/images/right_board.png"));
            } catch (IOException ignored) {
            }

            Ark.addPadding(this, 601, 948, 6, 5);

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

        /**
         * Updates this Player's Development Slot
         */
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

    /**
     * Class of Others_DevSlot_Panel, which is inside the CentralRightPanel.
     * <p> updatable by calling the update(String) method in the centralRightPanel. </p>
     */
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

            Ark.addPadding(this, 601, 273, 5, 6);

            this.devGrid = new JLabel[3];
            this.leadGrid = new JLabel[2];
            this.leadLabel = new JLabel[2];

            JButton back_OthersDevSlot = new JButton("Back");
            //back_OthersDevSlot.addActionListener(back_othersDevSlot_actionListener);
            back_OthersDevSlot.addActionListener(genericBack_actionListener);
            back_OthersDevSlot.setPreferredSize(new Dimension(120, 40));
            back_OthersDevSlot.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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
                label.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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
                this.leadLabel[i].setFont(new Font(PAPYRUS, Font.BOLD, 20));
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

        /**
         * Updates this Player's DevelopmentSlot and Leader Cards.
         */
        public void update() {

            PlayerSimplified player = Ark.game.getPlayerRef(this.panelName);
            DevelopmentCard[] devCard = player.getDevelopmentSlot().getTopCards();
            LeaderCard[] l = player.getLeaderCards();

            for (int i = 0; i < 2; i++) {
                if (l[i] == null) {
                    this.leadGrid[i].setIcon(scaleImage(new ImageIcon(BACKPATH), 234));
                    this.leadLabel[i].setText("not present");
                } else {
                    if (l[i].isEnabled()) {
                        this.leadGrid[i].setIcon(scaleImage(new ImageIcon(l[i].getFrontPath()), 234));
                        this.leadLabel[i].setText("enabled");
                        this.leadGrid[i].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 1));
                    } else
                        this.leadLabel[i].setText("not enabled");
                        this.leadGrid[i].setIcon(scaleImage(new ImageIcon(BACKPATH), 234));
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

    /**
     * Class of DevDeck Panel, which is inside the CentralRightpanel.
     *
     * @see #devDeck_panel
     */
    class DevDeck_Panel extends JPanel { //this card is called by DEVDECK

        private final JLabel[][] labels;

        public DevDeck_Panel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9), 1));

            this.labels = new JLabel[3][4];

            Ark.addPadding(this, 599, 946, 6, 5);

            JLabel level3CardLabel = new JLabel("Level 3");
            level3CardLabel.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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
            level2CardLabel.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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
            level1CardLabel.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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

            JButton backDevDeckButton = new JButton("Back");
            backDevDeckButton.addActionListener(genericBack_actionListener);
            backDevDeckButton.setPreferredSize(new Dimension(120, 40));
            backDevDeckButton.setFont(new Font(PAPYRUS, Font.BOLD, 20));
            backDevDeckButton.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.01;
            c.weighty = 0.01;
            c.insets = new Insets(5, 5, 0, 0);
            this.add(backDevDeckButton, c);

            JLabel greenCardColumnLabel = new JLabel("green");
            greenCardColumnLabel.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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
            blueCardColumnLabel.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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
            yellowCardColumnLabel.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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
            purpleCardColumnLabel.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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

        /**
         * Updates the grid of Development Cards.
         */
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

    /**
     * Class of Market Panel, which is inside the CentralRightPanel.
     *
     * @see #market_panel
     */
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
            } catch (IOException ignored) {
            }

            Ark.addPadding(this, 599, 946, 100, 100);

            JButton back_ShowMarket_Button = new JButton("Back");
            //back_ShowMarket_Button.addActionListener(back_show_Market_actionListener);
            back_ShowMarket_Button.addActionListener(genericBack_actionListener);
            back_ShowMarket_Button.setPreferredSize(new Dimension(120, 40));
            back_ShowMarket_Button.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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
            c.insets = new Insets(0, 160, 0, 0);
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

    /**
     * Class of LeaderCardsPicker Panel, which is inside the CentralRightPanel.
     *
     * @see #leaderCardsPicker_panel
     */
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
            Ark.addPadding(this, 599, 946, 5, 5);

            this.labelCards = new JLabel[4];
            this.checkBoxes = new CustomCheckbox[4];

            JLabel titleLabel = new JLabel("Pick your two Cards!");
            titleLabel.setFont(new Font(PAPYRUS, Font.BOLD, 50));
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
            confirm_LeaderCardsPicker_Button.setFont(new Font(PAPYRUS, Font.BOLD, 28));
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

        /**
         * Returns the number of the first chosen Leader Card.
         *
         * @return the number of the first chosen Leader Card
         */
        public int getFirst() {
            return this.first;
        }

        /**
         * Returns the number of the second chosen Leader Card.
         *
         * @return the number of the second chosen Leader Card
         */
        public int getSecond() {
            return this.second;
        }

        /**
         * Updates the row of LeaderCards with new images
         */
        public void update() {
            for (int i = 0; i < 4; i++)
                labelCards[i].setIcon(scaleImage(new ImageIcon(Ark.game.getLeaderCardsPicker().getCard(i).getFrontPath()), 300));
        }

        /**
         * Code of the changeListener used by the LeaderCards checkboxes.
         * <p> It allows to have a maximum of two selected checkboxes in parallel.</p>
         *
         * @param e the event source (a CustomCheckbox)
         */
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

        /**
         * Custom Checkbox that can store an int.
         */
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

    /**
     * Class of ResourcePicker Panel, which is inside the CentralRightPanel.
     *
     * @see #resourcePicker_panel
     */
    class ResourcePicker_Panel extends JPanel {

        private final JLabel subtitleLabel;

        public ResourcePicker_Panel() {
            GridBagConstraints c;
            this.setOpaque(false);
            this.setLayout(new GridBagLayout());

            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9), 1));
            Ark.addPadding(this, 599, 946, 5, 5);

            JLabel titleLabel = new JLabel("Pick your Resources!");
            titleLabel.setFont(new Font(PAPYRUS, Font.BOLD, 50));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 4;
            c.weightx = 0.5;
            c.weighty = 0.1;
            this.add(titleLabel, c);

            subtitleLabel = new JLabel();
            subtitleLabel.setFont(new Font(PAPYRUS, Font.BOLD, 40));
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

        /**
         * Updates the label that tells how many resources are left
         */
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

    /**
     * Class of action: getMarketResource Panel, which is inside the CentralRightPanel.
     *
     * @see #getMarketResource_panel
     */
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

            Ark.addPadding(this, 599, 946, 100, 100);

            JButton backGetMarketResourceButton = new JButton("Back");
            backGetMarketResourceButton.addActionListener(genericBack_actionListener);
            backGetMarketResourceButton.setPreferredSize(new Dimension(120, 40));
            backGetMarketResourceButton.setFont(new Font(PAPYRUS, Font.BOLD, 20));
            backGetMarketResourceButton.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.1;
            c.weighty = 0.1;
            c.insets = new Insets(5, 5, 0, 0);
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            this.add(backGetMarketResourceButton, c);

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
            c.insets = new Insets(0, 160, 0, 0);
            this.add(labelSlide, c);

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    JLabel label = new JLabel();
                    c = new GridBagConstraints();
                    c.gridx = j + 2;
                    c.gridy = i + 3;
                    c.weightx = 0.3;
                    c.weighty = 0.3;
                    this.add(label, c);
                    this.labelGrid[i][j] = label;
                }
            }
            int buttonSize = 45;

            for (int y = 3; y <= 5; y++) {
                MarketButton button = new MarketButton(y - 3, false);
                button.setText("" + (y - 2));
                button.addActionListener(action_getMarketResource_actionListener);
                button.setPreferredSize(new Dimension(buttonSize, buttonSize));
                button.setFont(new Font(TIMES, Font.BOLD, 22));
                button.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 6;
                c.gridy = y;
                c.weightx = 0.3;
                c.weighty = 0.3;
                c.insets = new Insets(5, 0, 0, 0);
                this.add(button, c);
            }

            for (int x = 2; x <= 5; x++) {
                MarketButton button = new MarketButton(x - 2, true);
                button.setText("" + (x - 1));
                button.addActionListener(action_getMarketResource_actionListener);
                button.setPreferredSize(new Dimension(buttonSize, buttonSize));
                button.setFont(new Font(TIMES, Font.BOLD, 22));
                button.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = x;
                c.gridy = 6;
                c.weightx = 0.3;
                c.weighty = 0.3;
                c.insets = new Insets(0, 5, 0, 0);
                this.add(button, c);
            }
        }

        /**
         * Updates the inside Market.
         */
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

        /**
         * A Custom Button that can store a boolean and an int.
         */
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

    /**
     * Class of middle object : Market Helper, which is inside the CentralRightPanel.
     *
     * @see #marketHelper_panel
     */
    class MarketHelper_Panel extends JPanel {

        private final JButton[] choiceButtons;
        private final JLabel[] resourceLabel;

        public MarketHelper_Panel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9), 1));

            Ark.addPadding(this, 599, 946, 4, 12);

            choiceButtons = new ChoiceButton[8];
            resourceLabel = new JLabel[4];

            JLabel titleLabel = new JLabel("Market Helper is here!");
            titleLabel.setFont(new Font(PAPYRUS, Font.BOLD, 50));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.weightx = 0.1;
            c.weighty = 0.1;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(5, 0, 0, 0);
            this.add(titleLabel, c);


            JPanel flowResource = new JPanel(new GridBagLayout());
            Ark.addPadding(flowResource, 46, 920, 7, 2);
            flowResource.setBorder(BorderFactory.createLineBorder(new Color(175, 154, 121), 1));
            flowResource.setBackground(new Color(214, 189, 148));

            JLabel resourceRemaining = new JLabel("remaining resources: ");
            resourceRemaining.setFont(new Font(PAPYRUS, Font.BOLD, 26));
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
                button.setFont(new Font(PAPYRUS, Font.BOLD, 24));
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

        /**
         * Updates the MarketHelper panel with the current options, disabling the options that are not available.
         */
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

        /**
         * A Custom Button that can store an int.
         */
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

    /**
     * Class of action: discardLeaderCard Panel, which inside the CentralRightPanel.
     *
     * @see #discardLeaderCard_panel
     */
    class DiscardLeaderCard_Panel extends JPanel {
        private final ChooseLeaderCardButton[] leaderCardButton;
        private final JLabel[] labelsUnderTheLeaderCard;

        public DiscardLeaderCard_Panel() {
            GridBagConstraints c;
            this.setOpaque(false);
            this.setLayout(new GridBagLayout());
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9), 1));
            Ark.addPadding(this, 599, 946, 100, 100);

            leaderCardButton = new ChooseLeaderCardButton[2];
            labelsUnderTheLeaderCard = new JLabel[2];

            JButton backDiscardLeaderCardButton = new JButton("Back");
            backDiscardLeaderCardButton.addActionListener(genericBack_actionListener);
            backDiscardLeaderCardButton.setPreferredSize(new Dimension(120, 40));
            backDiscardLeaderCardButton.setFont(new Font(PAPYRUS, Font.BOLD, 20));
            backDiscardLeaderCardButton.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.1;
            c.weighty = 0.1;
            c.insets = new Insets(5, 5, 0, 0);
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            this.add(backDiscardLeaderCardButton, c);

            JLabel titleLabel = new JLabel("Select a card to be discarded");
            titleLabel.setFont(new Font(PAPYRUS, Font.BOLD, 50));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 2;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(titleLabel, c);


            JLabel labelOverLeaderCard1 = new JLabel("Leader Card #1");
            labelOverLeaderCard1.setFont(new Font(PAPYRUS, Font.BOLD, 26));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 2;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(labelOverLeaderCard1, c);

            JLabel labelOverLeaderCard2 = new JLabel("Leader Card #2");
            labelOverLeaderCard2.setFont(new Font(PAPYRUS, Font.BOLD, 26));
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
            labelsUnderTheLeaderCard[0].setFont(new Font(PAPYRUS, Font.BOLD, 22));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 4;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.insets = new Insets(0, 0, 10, 0);
            this.add(labelsUnderTheLeaderCard[0], c);


            labelsUnderTheLeaderCard[1] = new JLabel();
            labelsUnderTheLeaderCard[1].setFont(new Font(PAPYRUS, Font.BOLD, 22));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 4;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.insets = new Insets(0, 0, 10, 0);
            this.add(labelsUnderTheLeaderCard[1], c);

        }

        /**
         * Updates the two Leader Cards that are shown, disabling the buttons if a card is already enabled or discarded.
         */
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

        /**
         * A Custom Button that can store an int.
         */
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

    /**
     * Class of action: activateLeaderCard Panel, which inside the CentralRightPanel.
     *
     * @see #activateLeaderCard_panel
     */
    class ActivateLeaderCard_Panel extends JPanel {

        private final ChooseLeaderCardButton[] leaderCardButtons;
        private final JLabel[] labelsUnderTheLeaderCard;

        public ActivateLeaderCard_Panel() {
            GridBagConstraints c;
            this.setOpaque(false);
            this.setLayout(new GridBagLayout());
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9), 1));
            Ark.addPadding(this, 599, 946, 100, 100);

            this.leaderCardButtons = new ChooseLeaderCardButton[2];
            this.labelsUnderTheLeaderCard = new JLabel[2];

            JButton backActivateLeaderCardButton = new JButton("Back");
            backActivateLeaderCardButton.addActionListener(genericBack_actionListener);
            backActivateLeaderCardButton.setPreferredSize(new Dimension(120, 40));
            backActivateLeaderCardButton.setFont(new Font(PAPYRUS, Font.BOLD, 20));
            backActivateLeaderCardButton.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.1;
            c.weighty = 0.1;
            c.insets = new Insets(5, 5, 0, 0);
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            this.add(backActivateLeaderCardButton, c);

            JLabel titleLabel = new JLabel("Select a card to be activated");
            titleLabel.setFont(new Font(PAPYRUS, Font.BOLD, 50));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 2;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(titleLabel, c);

            JLabel labelOverLeaderCard1 = new JLabel("Leader Card #1");
            labelOverLeaderCard1.setFont(new Font(PAPYRUS, Font.BOLD, 26));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 2;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(labelOverLeaderCard1, c);

            JLabel labelOverLeaderCard2 = new JLabel("Leader Card #2");
            labelOverLeaderCard2.setFont(new Font(PAPYRUS, Font.BOLD, 26));
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
            labelsUnderTheLeaderCard[0].setFont(new Font(PAPYRUS, Font.BOLD, 22));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 4;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.insets = new Insets(0, 0, 10, 0);
            this.add(labelsUnderTheLeaderCard[0], c);

            labelsUnderTheLeaderCard[1] = new JLabel();
            labelsUnderTheLeaderCard[1].setFont(new Font(PAPYRUS, Font.BOLD, 22));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 4;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.insets = new Insets(0, 0, 10, 0);
            this.add(labelsUnderTheLeaderCard[1], c);
        }

        /**
         * Updates the two Leader Cards that are shown, disabling the buttons if a card is already enabled or discarded.
         */
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

        /**
         * A Custom Button that can store an int.
         */
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

    /**
     * Class of action: changeDepotConfig Panel, which is inside the CentralRightpanel.
     *
     * @see #changeDepotConfig_panel
     */
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

            Ark.addPadding(this, 599, 946, 3, 7);

            shelf1type = new Resource[1];
            shelf2type = new Resource[2];
            shelf3type = new Resource[3];

            shelf2label = new JLabel[2];
            shelf3label = new JLabel[3];

            depotQuantity = new int[2];

            JButton backChangeDepotConfigCardButton = new JButton("Back");
            backChangeDepotConfigCardButton.addActionListener(genericBack_actionListener);
            backChangeDepotConfigCardButton.setPreferredSize(new Dimension(120, 40));
            backChangeDepotConfigCardButton.setFont(new Font(PAPYRUS, Font.BOLD, 20));
            backChangeDepotConfigCardButton.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.weightx = 0.1;
            c.weighty = 0.1;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.insets = new Insets(5, 5, 0, 0);
            this.add(backChangeDepotConfigCardButton, c);

            JLabel titleLabel = new JLabel("Change depot configuration");
            titleLabel.setFont(new Font(PAPYRUS, Font.BOLD, 50));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 4;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(titleLabel, c);

            JButton resourceChangerButton;

            //first shelf
            JPanel firstShelf = new JPanel(new GridBagLayout());
            firstShelf.setOpaque(false);

            JLabel firstLabel = new JLabel("first shelf:   ");
            firstLabel.setFont(new Font(PAPYRUS, Font.BOLD, 28));
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
            resourceChangerButton.setFont(new Font(PAPYRUS, Font.BOLD, 20));
            resourceChangerButton.setBackground(new Color(231, 210, 181));
            firstShelf.add(resourceChangerButton, c);

            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 2;
            c.weightx = 0.5;
            c.weighty = 0.3;
            c.gridwidth = 2;
            this.add(firstShelf, c);

            //second shelf
            JPanel secondShelf = new JPanel(new GridBagLayout());
            secondShelf.setOpaque(false);

            JLabel secondLabel = new JLabel("second shelf:   ");
            secondLabel.setFont(new Font(PAPYRUS, Font.BOLD, 28));
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
            resourceChangerButton.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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
            resourceChangerButton.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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

            //third shelf
            JPanel thirdShelf = new JPanel(new GridBagLayout());
            thirdShelf.setOpaque(false);

            JLabel thirdLabel = new JLabel("third shelf:   ");
            thirdLabel.setFont(new Font(PAPYRUS, Font.BOLD, 28));
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
            resourceChangerButton.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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
            resourceChangerButton.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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
            resourceChangerButton.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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

            JButton confirmChangeDepotConfigButton = new JButton("confirm!");
            confirmChangeDepotConfigButton.addActionListener(action_changeDepotConfig_actionListener);
            confirmChangeDepotConfigButton.setPreferredSize(new Dimension(200, 60));
            confirmChangeDepotConfigButton.setFont(new Font(PAPYRUS, Font.BOLD, 28));
            confirmChangeDepotConfigButton.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 6;
            c.weightx = 0.5;
            c.weighty = 0.4;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.PAGE_END;
            c.insets = new Insets(0, 0, 10, 0);
            this.add(confirmChangeDepotConfigButton, c);
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

        /**
         * Updates the depot and extraDepot shown to reflect the model status.
         */
        public void update() {
            Resource shelf1r = Ark.myPlayerRef.getWarehouseDepot().getShelf1();
            Resource[] shelf2r = Ark.myPlayerRef.getWarehouseDepot().getShelf2();
            Resource[] shelf3r = Ark.myPlayerRef.getWarehouseDepot().getShelf3();

            this.shelf1type[0] = shelf1r;
            this.shelf1label.setIcon(scaleImage(new ImageIcon(shelf1r.getPathBig()), 100));

            for(int i=0; i<2; i++) {
                this.shelf2type[i] = shelf2r[i];
                this.shelf2label[i].setIcon(scaleImage(new ImageIcon(shelf2r[i].getPathBig()), 100));
            }
            for(int i=0; i<3; i++) {
                this.shelf3type[i] = shelf3r[i];
                this.shelf3label[i].setIcon(scaleImage(new ImageIcon(shelf3r[i].getPathBig()), 100));
            }
            firstExtraDepotPanel.updateValues();
            secondExtraDepotPanel.updateValues();
        }

        /**
         * A Class that contains the lower ExtraDepot labels, buttons, and information.
         * <p> Used inside the Change Depot Config.</p>
         */
        class ExtraDepotPanel extends JPanel {
            private final JLabel label;
            private JLabel number;
            private final JButton less;
            private final JButton more;
            private int[] depotQuantity;
            private int leaderCardNumber;

            /**
             * Custom ActionListener that decreases the number of resources in the ExtraDepot.
             */
            ActionListener less_ActionListener = e -> {
                if (this.depotQuantity[leaderCardNumber] == 0) return;
                this.depotQuantity[leaderCardNumber] = this.depotQuantity[leaderCardNumber] - 1;
                this.number.setText("" + this.depotQuantity[leaderCardNumber]);
            };

            /**
             * Custom ActionListener that increases the number of resources in the ExtraDepot.
             */
            ActionListener more_ActionListener = e -> {
                if (this.depotQuantity[leaderCardNumber] == 2) return;
                this.depotQuantity[leaderCardNumber] = this.depotQuantity[leaderCardNumber] + 1;
                this.number.setText("" + this.depotQuantity[leaderCardNumber]);
            };

            public ExtraDepotPanel(int[] depotQuantity, int leaderCardNumber) {
                this.depotQuantity = depotQuantity;
                this.leaderCardNumber = leaderCardNumber;

                GridBagConstraints c;
                this.setLayout(new GridBagLayout());
                this.setOpaque(false);

                JLabel fixedLabel = new JLabel();
                fixedLabel.setFont(new Font(PAPYRUS, Font.BOLD, 20));
                fixedLabel.setText("   Leader Card #" + (leaderCardNumber + 1) + "   ");
                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                c.weightx = 0.3;
                c.weighty = 0.3;
                this.add(fixedLabel, c);

                label = new JLabel();
                label.setFont(new Font(PAPYRUS, Font.BOLD, 22));
                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 1;
                c.weightx = 0.3;
                c.weighty = 0.3;
                this.add(label, c);

                number = new JLabel();
                number.setFont(new Font(PAPYRUS, Font.BOLD, 28));
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
                less.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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
                more.setFont(new Font(PAPYRUS, Font.BOLD, 20));
                more.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 3;
                c.gridy = 0;
                c.weightx = 0.3;
                c.weighty = 0.3;
                c.gridheight = 2;
                this.add(more, c);
            }

            /**
             * Updates the values used to choose an extra Depot configuration.
             */
            public void updateValues() {
                LeaderCard l = Ark.myPlayerRef.getLeaderCards()[leaderCardNumber];
                if (l == null) {
                    this.label.setText("is not present");
                    this.number.setText("");
                    this.less.setEnabled(false);
                    this.more.setEnabled(false);
                    depotQuantity[leaderCardNumber] = -1;
                } else {
                    if (l.getSpecialAbility().isExtraDepot() && l.isEnabled()) {
                        int num = ((ExtraDepot) l.getSpecialAbility()).getNumber();
                        Resource res = ((ExtraDepot) l.getSpecialAbility()).getResourceType();

                        depotQuantity[leaderCardNumber] = num;

                        this.label.setText(resourceToString(res, true));

                        this.number.setText("" + depotQuantity[leaderCardNumber]);
                        this.less.setEnabled(true);
                        this.more.setEnabled(true);
                    } else if (l.getSpecialAbility().isExtraDepot() && !l.isEnabled()) {
                        this.label.setText("is not enabled");
                        this.number.setText("");
                        this.less.setEnabled(false);
                        this.more.setEnabled(false);
                        depotQuantity[leaderCardNumber] = -1;
                    } else {
                        this.label.setText("is not Extra Depot");
                        this.number.setText("");
                        this.less.setEnabled(false);
                        this.more.setEnabled(false);
                        depotQuantity[leaderCardNumber] = -1;
                    }
                }
            }
        }

        /**
         * A Custom ActionListener that permits to cycle through a defined set of Resources.
         * <p> Manages a single Label, and changes the value in a specified array at a specified position.</p>
         */
        class resourceChanger implements ActionListener {

            private final JLabel managedLabel;
            private final Resource[] arraySource;
            private final int index;

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
                    default:
                        return Resource.NONE;
                }
            }
        }
    }

    /**
     * Class of middle object : Vendor Panel, which is inside the CentralRightPanel.
     *
     * @see #vendor_panel
     */
    class Vendor_Panel extends JPanel {
        private JLabel devCard;
        private SlotButton slot1;
        private SlotButton slot2;
        private SlotButton slot3;
        private int currentCard;

        /**
         * Custom ActionListener that goes forward in the Vendor Card "applet".
         */
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
        /**
         * Custom ActionListener that goes backwards in the Vendor Card "applet".
         */
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
            Ark.addPadding(this, 599, 946, 10, 12);

            currentCard = 0;

            SlotButton cancel = new SlotButton("cancel", -1);
            cancel.addActionListener(action_chooseDevCard_actionListener);
            cancel.setPreferredSize(new Dimension(120, 40));
            cancel.setFont(new Font(PAPYRUS, Font.BOLD, 20));
            cancel.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.weightx = 0.1;
            c.weighty = 0.1;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.insets = new Insets(5, 5, 0, 0);
            this.add(cancel, c);


            JLabel title = new JLabel("Select a card to buy it!");
            title.setFont(new Font(PAPYRUS, Font.BOLD, 40));
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
            prev.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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
            next.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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
            position.setFont(new Font(PAPYRUS, Font.BOLD, 30));
            c = new GridBagConstraints();
            c.gridx = 4;
            c.gridy = 2;
            c.weightx = 4.0;
            c.weighty = 0.001;
            this.add(position, c);

            slot1 = new SlotButton("1", 0);
            slot1.setPreferredSize(new Dimension(120, 120));
            slot1.setFont(new Font(PAPYRUS, Font.BOLD, 30));
            slot1.setBackground(new Color(231, 210, 181));
            slot1.addActionListener(action_chooseDevCard_actionListener);
            c = new GridBagConstraints();
            c.gridx = 4;
            c.gridy = 3;
            c.weightx = 0.3;
            c.weighty = 0.3;
            this.add(slot1, c);

            slot2 = new SlotButton("2", 1);
            slot2.setPreferredSize(new Dimension(120, 120));
            slot2.setFont(new Font(PAPYRUS, Font.BOLD, 30));
            slot2.setBackground(new Color(231, 210, 181));
            slot2.addActionListener(action_chooseDevCard_actionListener);
            c = new GridBagConstraints();
            c.gridx = 4;
            c.gridy = 4;
            c.weightx = 0.3;
            c.weighty = 0.3;
            this.add(slot2, c);

            slot3 = new SlotButton("3", 2);
            slot3.setPreferredSize(new Dimension(120, 120));
            slot3.setFont(new Font(PAPYRUS, Font.BOLD, 30));
            slot3.setBackground(new Color(231, 210, 181));
            slot3.addActionListener(action_chooseDevCard_actionListener);
            c = new GridBagConstraints();
            c.gridx = 4;
            c.gridy = 5;
            c.weightx = 0.3;
            c.weighty = 0.3;
            this.add(slot3, c);
        }

        /**
         * Sets up the first card from the Vendor, enabling the buttons slots that are available to be chosen.
         */
        public void update() {
            if (Ark.game.getDevelopmentCardsVendor().isEnabled()) {
                List<VendorCard> devCards = Ark.game.getDevelopmentCardsVendor().getCards();
                devCard.setIcon(scaleImage(new ImageIcon(devCards.get(0).getCard().getFrontPath()), 450));
                currentCard = 0;
                slot1.setEnabled(devCards.get(0).isSlot1());
                slot2.setEnabled(devCards.get(0).isSlot2());
                slot3.setEnabled(devCards.get(0).isSlot3());
            }
        }

        public int getCurrentCard() {
            return currentCard;
        }

        /**
         * A Custom Button that can store an int.
         */
        class SlotButton extends JButton {
            private final int position;

            public SlotButton(String text, int position) {
                super(text);
                this.position = position;
            }

            public int getPosition() {
                return position;
            }
        }
    }

    /**
     * Class of action : Production (part 1), which is inside the CentralRightPanel.
     *
     * @see #productionSelection_panel
     */
    class ProductionSelection_Panel extends JPanel {
        private final JToggleButton[] leaderCardButton;
        private final JToggleButton[] devCardButton;
        private final JToggleButton basicProduction;

        public ProductionSelection_Panel() {
            this.leaderCardButton = new JToggleButton[2];
            this.devCardButton = new JToggleButton[3];

            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9), 1));
            Ark.addPadding(this, 599, 946, 10, 12);

            JButton backProductionSelectionButton = new JButton("Back");
            backProductionSelectionButton.addActionListener(genericBack_actionListener);
            backProductionSelectionButton.setPreferredSize(new Dimension(120, 40));
            backProductionSelectionButton.setFont(new Font(PAPYRUS, Font.BOLD, 20));
            backProductionSelectionButton.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.weightx = 0.1;
            c.weighty = 0.1;
            c.insets = new Insets(5, 5, 0, 0);
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            this.add(backProductionSelectionButton, c);

            JLabel titleLabel = new JLabel("Choose your productions");
            titleLabel.setFont(new Font(PAPYRUS, Font.BOLD, 50));
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

            for (int i = 0; i < 2; i++) {
                this.leaderCardButton[i] = new JToggleButton();
                this.leaderCardButton[i].setText("Leader Card #" + (i + 1));
                this.leaderCardButton[i].setPreferredSize(new Dimension(240, 60));
                this.leaderCardButton[i].setFont(new Font(PAPYRUS, Font.BOLD, 24));
                this.leaderCardButton[i].setBackground(new Color(231, 210, 181));

                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = i;
                c.weightx = 0.6;
                c.weighty = 0.4;
                c.anchor = GridBagConstraints.LINE_END;
                upperPanel.add(this.leaderCardButton[i], c);
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
            c.insets = new Insets(0, 70, 0, 0);
            upperPanel.add(basicProduction, c);

            JLabel basicProductionLabel = new JLabel("basic production");
            basicProductionLabel.setFont(new Font(PAPYRUS, Font.BOLD, 24));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 0;
            c.weightx = 0.1;
            c.weighty = 0.4;
            c.gridheight = 2;
            c.anchor = GridBagConstraints.LINE_START;
            c.insets = new Insets(0, 0, 0, 60);
            upperPanel.add(basicProductionLabel, c);

            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 3;
            c.weightx = 0.5;
            c.weighty = 0.4;
            c.gridwidth = 2;
            c.fill = GridBagConstraints.BOTH;
            this.add(upperPanel, c);


            JPanel stdProductionPanel = new JPanel(new GridBagLayout());
            stdProductionPanel.setOpaque(false);

            for (int i = 0; i < 3; i++) {
                this.devCardButton[i] = new JToggleButton();
                this.devCardButton[i].setBackground(new Color(231, 210, 181));
                this.devCardButton[i].setPreferredSize(new Dimension(178, 255));
                c = new GridBagConstraints();
                c.gridx = i;
                c.weightx = 0.5;
                c.weighty = 0.4;
                c.insets = new Insets(0, 50, 0, 50);
                stdProductionPanel.add(this.devCardButton[i], c);
            }

            c.gridx = 1;
            c.gridy = 4;
            c.weightx = 0.5;
            c.weighty = 0.4;
            c.gridwidth = 2;
            c.fill = GridBagConstraints.BOTH;
            this.add(stdProductionPanel, c);


            JButton confirmProductionSelectionButton = new JButton("confirm!");
            confirmProductionSelectionButton.addActionListener(e -> {
                production_panel.update();
                cardLayoutRight.show(centralRightPanel, PRODUCTION);
            });
            confirmProductionSelectionButton.setPreferredSize(new Dimension(200, 60));
            confirmProductionSelectionButton.setFont(new Font(PAPYRUS, Font.BOLD, 28));
            confirmProductionSelectionButton.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 5;
            c.weightx = 0.5;
            c.weighty = 0.4;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.PAGE_END;
            c.insets = new Insets(0, 0, 10, 0);
            this.add(confirmProductionSelectionButton, c);

        }

        /**
         * Updates the page to allow selection of possible production items.
         */
        public void update() {
            DevelopmentCard[] topCards = Ark.myPlayerRef.getDevelopmentSlot().getTopCards();
            LeaderCard[] leaderCards = Ark.myPlayerRef.getLeaderCards();


            for (int i = 0; i < 2; i++) {
                if (leaderCards[i] == null)
                    this.leaderCardButton[i].setEnabled(false);
                else this.leaderCardButton[i].setEnabled(leaderCards[i].isEnabled() && leaderCards[i].getSpecialAbility().isProduction());
            }


            for (int i = 0; i < 3; i++) {
                if (topCards[i] == null) {
                    this.devCardButton[i].setIcon(scaleImage(new ImageIcon("resources/cardsBack/BACK (1).png"), 235));
                    this.devCardButton[i].setEnabled(false);
                } else {
                    this.devCardButton[i].setIcon(scaleImage(new ImageIcon(topCards[i].getFrontPath()), 235));
                    this.devCardButton[i].setEnabled(true);
                }
            }
        }

        public JToggleButton getLeaderCard1() {
            return leaderCardButton[0];
        }

        public JToggleButton getLeaderCard2() {
            return leaderCardButton[1];
        }

        public JToggleButton getBasicProduction() {
            return basicProduction;
        }

        public JToggleButton getDevCard1() {
            return devCardButton[0];
        }

        public JToggleButton getDevCard2() {
            return devCardButton[1];
        }

        public JToggleButton getDevCard3() {
            return devCardButton[2];
        }
    }

    /**
     * Class of action : Production (part 2), which is inside the CentralRightPanel.
     *
     * @see #production_panel
     */
    class Production_Panel extends JPanel {
        private final JLabel basicFirstChoice;
        private final JLabel basicSecondChoice;
        private final JLabel basicOutputChoice;
        private final JLabel lc1OutputChoice;
        private final JLabel lc2OutputChoice;

        /**
         * 0: basic input 1, 1: basic input 2, 2: basic output, 3: leaderCard 1, 4: leaderCard 2
         */
        private final Resource[] resources;

        private final JButton next1;
        private final JButton next2;
        private final JButton next3;
        private final JButton next4;
        private final JButton next5;

        public Production_Panel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9), 1));
            Ark.addPadding(this, 599, 946, 10, 12);

            this.resources = new Resource[5];

            JButton backProductionSelectionButton = new JButton("Back");
            backProductionSelectionButton.addActionListener(e -> changeRightCard(PRODSELECTION));
            backProductionSelectionButton.setPreferredSize(new Dimension(120, 40));
            backProductionSelectionButton.setFont(new Font(PAPYRUS, Font.BOLD, 20));
            backProductionSelectionButton.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.weightx = 0.1;
            c.weighty = 0.1;
            c.insets = new Insets(5, 5, 0, 0);
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            this.add(backProductionSelectionButton, c);

            JLabel titleLabel = new JLabel("Choose the resources!");
            titleLabel.setFont(new Font(PAPYRUS, Font.BOLD, 35));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 1;
            c.gridwidth = 5;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(titleLabel, c);

            JLabel basicProduction = new JLabel("Basic Production:");
            basicProduction.setFont(new Font(PAPYRUS, Font.BOLD, 25));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 2;
            c.gridheight = 2;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.insets = new Insets(0, 22, 0, 0);
            this.add(basicProduction, c);

            JLabel emptySpace = new JLabel();
            emptySpace.setOpaque(false);
            emptySpace.setHorizontalAlignment(SwingConstants.CENTER);
            emptySpace.setFont(new Font(PAPYRUS, Font.BOLD, 20));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 4;
            c.gridwidth = 8;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.insets = new Insets(20, 0, 0, 0);
            this.add(emptySpace, c);

            JLabel lc1 = new JLabel("Leader card 1:");
            lc1.setFont(new Font(PAPYRUS, Font.BOLD, 25));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 5;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(lc1, c);

            JLabel lc2 = new JLabel("Leader card 2:");
            lc2.setFont(new Font(PAPYRUS, Font.BOLD, 25));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 6;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(lc2, c);

            JLabel input1 = new JLabel("input 1: ");
            input1.setFont(new Font(PAPYRUS, Font.BOLD, 25));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 2;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.insets = new Insets(0, 10, 0, 0);
            this.add(input1, c);

            basicFirstChoice = new JLabel();
            c = new GridBagConstraints();
            c.gridx = 3;
            c.gridy = 2;
            c.weightx = 0.5;
            c.weighty = 0.2;
            this.add(basicFirstChoice, c);

            next1 = new JButton(">");
            next1.addActionListener(new resourceChanger(basicFirstChoice, this.resources, 0));
            next1.setPreferredSize(new Dimension(45, 45));
            next1.setFont(new Font(PAPYRUS, Font.BOLD, 15));
            next1.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 4;
            c.gridy = 2;
            c.weightx = 0.9;
            c.weighty = 0.3;
            c.anchor = GridBagConstraints.WEST;
            this.add(next1, c);

            JLabel input2 = new JLabel("input 2: ");
            input2.setFont(new Font(PAPYRUS, Font.BOLD, 25));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 3;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.insets = new Insets(0, 10, 0, 0);
            this.add(input2, c);

            basicSecondChoice = new JLabel();
            c = new GridBagConstraints();
            c.gridx = 3;
            c.gridy = 3;
            c.weightx = 0.5;
            c.weighty = 0.2;
            this.add(basicSecondChoice, c);

            next2 = new JButton(">");
            next2.addActionListener(new resourceChanger(basicSecondChoice, this.resources, 1));
            next2.setPreferredSize(new Dimension(45, 45));
            next2.setFont(new Font(PAPYRUS, Font.BOLD, 15));
            next2.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 4;
            c.gridy = 3;
            c.weightx = 0.9;
            c.weighty = 0.3;
            c.anchor = GridBagConstraints.WEST;
            this.add(next2, c);

            JLabel output = new JLabel("output: ");
            output.setFont(new Font(PAPYRUS, Font.BOLD, 25));
            c = new GridBagConstraints();
            c.gridx = 5;
            c.gridy = 2;
            c.gridheight = 2;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(output, c);

            basicOutputChoice = new JLabel();
            c = new GridBagConstraints();
            c.gridx = 6;
            c.gridy = 2;
            c.gridheight = 2;
            c.weightx = 0.5;
            c.weighty = 0.2;
            this.add(basicOutputChoice, c);

            next3 = new JButton(">");
            next3.addActionListener(new resourceChanger(basicOutputChoice, this.resources, 2));
            next3.setPreferredSize(new Dimension(45, 45));
            next3.setFont(new Font(PAPYRUS, Font.BOLD, 15));
            next3.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 7;
            c.gridy = 2;
            c.gridheight = 2;
            c.weightx = 0.9;
            c.weighty = 0.3;
            c.insets = new Insets(0, 0, 0, 100);
            c.anchor = GridBagConstraints.WEST;
            this.add(next3, c);

            lc1OutputChoice = new JLabel();
            c = new GridBagConstraints();
            c.gridx = 4;
            c.gridy = 5;
            c.weightx = 0.5;
            c.weighty = 0.2;
            this.add(lc1OutputChoice, c);

            next4 = new JButton(">");
            next4.addActionListener(new resourceChanger(lc1OutputChoice, this.resources, 3));
            next4.setPreferredSize(new Dimension(45, 45));
            next4.setFont(new Font(PAPYRUS, Font.BOLD, 15));
            next4.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 5;
            c.gridy = 5;
            c.weightx = 0.9;
            c.weighty = 0.3;
            c.anchor = GridBagConstraints.WEST;
            this.add(next4, c);

            lc2OutputChoice = new JLabel();
            c = new GridBagConstraints();
            c.gridx = 4;
            c.gridy = 6;
            c.weightx = 0.5;
            c.weighty = 0.2;
            this.add(lc2OutputChoice, c);

            next5 = new JButton(">");
            next5.addActionListener(new resourceChanger(lc2OutputChoice, this.resources, 4));
            next5.setPreferredSize(new Dimension(45, 45));
            next5.setFont(new Font(PAPYRUS, Font.BOLD, 15));
            next5.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 5;
            c.gridy = 6;
            c.weightx = 0.9;
            c.weighty = 0.3;
            c.anchor = GridBagConstraints.WEST;
            this.add(next5, c);

            ImageIcon cross = scaleImage(new ImageIcon("resources/punchboard/faith.png"), 80);
            JLabel faithPoint1 = new JLabel("+");
            faithPoint1.setIcon(cross);
            faithPoint1.setFont(new Font(PAPYRUS, Font.BOLD, 35));
            c = new GridBagConstraints();
            c.gridx = 3;
            c.gridy = 5;
            c.weightx = 0.5;
            c.weighty = 0.2;
            this.add(faithPoint1, c);

            JLabel faithPoint2 = new JLabel("+");
            faithPoint2.setIcon(cross);
            faithPoint2.setFont(new Font(PAPYRUS, Font.BOLD, 35));
            c = new GridBagConstraints();
            c.gridx = 3;
            c.gridy = 6;
            c.weightx = 0.5;
            c.weighty = 0.2;
            this.add(faithPoint2, c);

            JButton confirm_Production_Button = new JButton("confirm!");
            confirm_Production_Button.addActionListener(action_activateProduction_actionListener);
            confirm_Production_Button.setPreferredSize(new Dimension(200, 60));
            confirm_Production_Button.setFont(new Font(PAPYRUS, Font.BOLD, 28));
            confirm_Production_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 7;
            c.gridwidth = 10;
            c.weightx = 0.5;
            c.weighty = 0.4;
            c.anchor = GridBagConstraints.PAGE_END;
            c.insets = new Insets(0, 0, 10, 0);
            this.add(confirm_Production_Button, c);
        }

        /**
         * Updates this page based on the items that were selected in the production selection panel.
         */
        public void update() {
            next1.setEnabled(productionSelection_panel.getBasicProduction().isSelected());
            next2.setEnabled(productionSelection_panel.getBasicProduction().isSelected());
            next3.setEnabled(productionSelection_panel.getBasicProduction().isSelected());
            next4.setEnabled(productionSelection_panel.getLeaderCard1().isSelected());
            next5.setEnabled(productionSelection_panel.getLeaderCard2().isSelected());

            basicFirstChoice.setIcon(scaleImage(new ImageIcon(Resource.STONE.getPathBig()), 80));
            basicSecondChoice.setIcon(scaleImage(new ImageIcon(Resource.COIN.getPathBig()), 80));
            basicOutputChoice.setIcon(scaleImage(new ImageIcon(Resource.SHIELD.getPathBig()), 80));
            lc1OutputChoice.setIcon(scaleImage(new ImageIcon(Resource.COIN.getPathBig()), 80));
            lc2OutputChoice.setIcon(scaleImage(new ImageIcon(Resource.SERVANT.getPathBig()), 80));

            this.resources[0] = Resource.STONE;
            this.resources[1] = Resource.COIN;
            this.resources[2] = Resource.SHIELD;
            this.resources[3] = Resource.COIN;
            this.resources[4] = Resource.SERVANT;
        }

        /**
         * A Custom ActionListener that permits to cycle between a defined set of Resources.
         */
        class resourceChanger implements ActionListener {
            private final JLabel managedLabel;
            private final Resource[] arrayRef;
            private final int index;

            public resourceChanger(JLabel managedLabel, Resource[] arrayRef, int index) {
                this.managedLabel = managedLabel;
                this.arrayRef = arrayRef;
                this.index = index;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                Resource nextResource = nextResource(arrayRef[index]);
                arrayRef[index] = nextResource;
                managedLabel.setIcon(scaleImage(new ImageIcon(nextResource.getPathBig()), 80));
            }

            private Resource nextResource(Resource currentResource) {
                switch (currentResource) { // coin shield stone servant
                    case COIN:
                        return Resource.SHIELD;
                    case SHIELD:
                        return Resource.STONE;
                    case STONE:
                        return Resource.SERVANT;
                    default:
                        return Resource.COIN;
                }
            }
        }
    }

    /**
     * Class of LeaderBoard Panel, which is inside the CentralRightPanel.
     *
     * @see #leaderboard_panel
     */
    class Leaderboard_Panel extends JPanel {
        private final JLabel resultLabel;
        private final JLabel[] entries;

        /**
         * Maximum lines of the leaderboard, by design is 4. (== Maximum Players)
         */
        static final int LINES = 4;

        public Leaderboard_Panel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9), 1));
            Ark.addPadding(this, 599, 946, 1, 5);

            this.entries = new JLabel[LINES];

            JButton backLeaderboardButton = new JButton("Back");
            backLeaderboardButton.addActionListener(genericBack_actionListener);
            backLeaderboardButton.setPreferredSize(new Dimension(120, 40));
            backLeaderboardButton.setFont(new Font(PAPYRUS, Font.BOLD, 20));
            backLeaderboardButton.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.weightx = 0.1;
            c.weighty = 0.1;
            c.insets = new Insets(5, 5, 0, 0);
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            this.add(backLeaderboardButton, c);


            resultLabel = new JLabel();
            resultLabel.setFont(new Font(PAPYRUS, Font.BOLD, 50));
            resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.weightx = 0.1;
            c.weighty = 0.1;
            c.insets = new Insets(60, 0, 30, 0);
            c.anchor = GridBagConstraints.PAGE_START;
            this.add(resultLabel, c);

            for (int i = 0; i < LINES; i++) {
                this.entries[i] = new JLabel();
                this.entries[i].setFont(new Font(PAPYRUS, Font.BOLD, 34));
                this.entries[i].setHorizontalAlignment(SwingConstants.CENTER);
                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = i + 2;
                c.weightx = 0.1;
                c.weighty = 0.5;
                if (i == LINES - 1)
                    c.insets = new Insets(0, 0, 50, 0);
                else
                    c.insets = new Insets(0, 0, 0, 0);
                c.anchor = GridBagConstraints.PAGE_START;
                this.add(this.entries[i], c);

            }
        }

        /**
         * Updates this page to show the final result of this Game.
         * <p> Works even in Solo mode and Multiplayer mode</p>
         */
        public void update() {
            if (Ark.game.isLeaderBoardEnabled()) {
                Map<String, Integer> board = Ark.game.getLeaderBoard().getBoard();
                boolean tie = false;
                Integer maxValue = 0;
                for (String nickname : board.keySet()) {
                    if (board.get(nickname) > maxValue) maxValue = board.get(nickname);
                }

                if (Ark.solo) {
                    String key = "Lorenzo";
                    entries[0].setText("" + Ark.nickname + " - " + board.get(Ark.nickname) + " points");
                    if (board.get(key) == 1) //lorenzo Lost
                        resultLabel.setText("You made it!");
                    if (board.get(key) == 2) //lorenzo won
                        resultLabel.setText("Lorenzo won!");
                } else {
                    if (board.get(Ark.nickname).equals(maxValue)) {
                        for (String nickname : board.keySet()) {
                            if (nickname.equals(Ark.nickname))
                                continue;
                            if (maxValue.equals(board.get(nickname)))
                                tie = true;
                        }
                        if (tie)
                            resultLabel.setText("Did you just Tied?");
                        else
                            resultLabel.setText("You made it!");
                    } else
                        resultLabel.setText("You lost!");
                }

                String entry;
                List<PlayerSimplified> playerList = Ark.game.getPlayerSimplifiedList();
                for (int i = 0; i < playerList.size(); i++) {
                    entry = "";
                    entry += playerList.get(i).getNickname();
                    entry += " - ";
                    entry += board.get(playerList.get(i).getNickname());
                    entry += " points";
                    this.entries[i].setText(entry);
                }
            }
        }
    }


    //HELPER METHODS (Graphics)

    /**
     * Disables all the buttons on the Bottom Panel.
     */
    public void disableBottomButtons() {
        activate_LeaderCards_Button.setEnabled(false);
        change_Depot_Config_Button.setEnabled(false);
        get_MarketResource_Button.setEnabled(false);
        discard_LeaderCard_Button.setEnabled(false);
        buy_DevCard_Button.setEnabled(false);
        activate_Production_Button.setEnabled(false);
        endTurn_Button.setEnabled(false);
    }

    /**
     * Enables all the buttons on the Bottom Panel.
     */
    public void enableAllBottomButtons() {
        activate_LeaderCards_Button.setEnabled(true);
        change_Depot_Config_Button.setEnabled(true);
        get_MarketResource_Button.setEnabled(true);
        discard_LeaderCard_Button.setEnabled(true);
        buy_DevCard_Button.setEnabled(true);
        activate_Production_Button.setEnabled(true);
        endTurn_Button.setEnabled(true);
    }

    /**
     * Enables certain buttons on the Bottom Panel, after checking if the Player can still do the related actions.
     */
    public void enableActionBottomButtons() {
        if (!Ark.action) {
            enableAllBottomButtons();
            endTurn_Button.setEnabled(false);
        } else {
            activate_LeaderCards_Button.setEnabled(true);
            change_Depot_Config_Button.setEnabled(true);
            get_MarketResource_Button.setEnabled(false);
            discard_LeaderCard_Button.setEnabled(true);
            buy_DevCard_Button.setEnabled(false);
            activate_Production_Button.setEnabled(false);
            endTurn_Button.setEnabled(true);
        }
    }

    /**
     * Scales a given ImageIcon by imposing a square around the icon.
     * <p>
     * Uses the function below.
     *
     * @param icon            the ImageIcon being scaled
     * @param squareDimension the desired squared dimension
     * @return a scaled ImageIcon from the input one
     * @see #scaleImage(ImageIcon, int, int)
     */
    public ImageIcon scaleImage(ImageIcon icon, int squareDimension) {
        return scaleImage(icon, squareDimension, squareDimension);
    }

    /**
     * Scales a give ImageIcon by imposing a desired rectangle dimension.
     * <p>
     * Maintains the aspect ratio, looking at the minimum dimension from height or width.
     * By default scales using a SCALE_SMOOTH algorithm. See code.
     *
     * @param icon   the ImageIcon being scaled
     * @param width  the desired width in pixel
     * @param height the desired height in pixel
     * @return a scaled ImageIcon from the input one
     */
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

    /**
     * Converts a Resource Type to the corresponding word.
     * <p>
     * Instead of writing STONE, it results in Stone.
     * If specified, can even add the s at the end of the word: Stones.
     *
     * @param resource the Resource to be parsed
     * @param plural   True if you want to add an s at the end, False otherwise
     * @return a very normal String
     */
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

    /**
     * Tries to send a message by forwarding the message to the Ark. <p>
     * There it could go in Internet or go directly through the controller.
     * If an exception gets thrown, the Player will be asked to quit
     *
     * @param message the message that is being sent
     */
    public void send(Message message) {
        try {
            Ark.send(message);
        } catch (IOException e) {
            leftPanel.updateNotification("You were disconnected. Please quit");
            Ark.yourTurn = false;
            disableBottomButtons();
        }
    }
}