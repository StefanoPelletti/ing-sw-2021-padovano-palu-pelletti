package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.modelSimplified.GameSimplified;
import it.polimi.ingsw.client.modelSimplified.PlayerSimplified;
import it.polimi.ingsw.client.modelSimplified.StrongboxSimplified;
import it.polimi.ingsw.client.modelSimplified.WarehouseDepotSimplified;
import it.polimi.ingsw.networking.message.actionMessages.MSG_ACTION_DISCARD_LEADERCARD;
import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_Full;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.model.marbles.MarketMarble;
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
import java.util.*;
import java.util.List;

public class Board implements Runnable {
    CardLayout cardLayoutLeft;
    CardLayout cardLayoutRight;
    CentralLeftPanel centralLeftPanel; // <- parent for cardlayout left
    CentralRightPanel centralRightPanel; // <- parent for cardlayout right

    JFrame mainFrame;
    MainPanel mainPanel;
    Dimension frameDimension;

    //STATIC PANELS VARs (LEFT, TOP, BOTTOM)
    //LEFT PANEL
    JButton quit_Button;
    JLabel turnLabel, turnOf; //<- updatable, but not with a custom method
    JTextArea notificationsArea; //<- updatable, but not with a custom method
    MyLeaderCardsPanel myLeaderCardsPanel; //<- updatable for leaderCards of this player and extradepot with a single update()
    JButton show_DevDeck_Button;
    JButton show_Market_Button;
    //BOTTOM PANEL
    JButton activate_LeaderCards_Button, change_Depot_Config_Button, get_MarketResource_Button;
    JButton discard_LeaderCard_Button, buy_DevCard_Button, activate_Production_Button;
    PlayersRecapPanel playersRecapPanel; //<- updatable after any other players gets a resource update with a single update()
    JButton endTurn_Button;


    //CENTRAL LEFT PANEL CARDS <- each card is followed by its necessary contents
    String lastLeftCard;

    //CARDS (CENTRAL RIGHT PANEL)  <- each card is followed by its necessary contents
    String lastRightCard;
    //static final String SELF = Ark.nickname, OTHERS = player.getNickname()  <- this is a placeholder to remind that the card below is called like this
    Self_Board_Card_Panel self_board_card_panel;
    static final String DEVDECK = "Development Cards Deck";
    DevDeck_Board_Card_Panel devDeck_board_card_panel; //<- updatable after a devdeck update
    JButton back_DevDeck_Card_Button;
    static final String MARKET = "Market";
    Market_Board_Card_Panel market_board_card_panel; //<- updatable after a market update //TODO
    JButton back_Market_Card_Button;
    static final String GOTOMARKET = "GoToMarket";
    GoToMarket_Board_Card_Panel goToMarket_board_card_panel;
    JButton back_GoToMarket_Card_Button;
    static final String LPICKER = "Leader Cards Picker";
    LeaderCardsPicker_Board_Card_Panel leaderCardsPicker_board_card_panel; //<- updatable after a leadercards picker update
    JButton confirm_LeaderCardsPicker_Card_Button;
    static final String CHANGEDEPOT = "Change Depot Configuration";
    ChangeDepotConfig_Board_Card_Panel changeDepotConfig_board_card_panel;
    JButton confirm_ChangeDepotConfig_Card_Button;
    JButton back_ChangeDepotConfig_Card_Button;


    //fonts
    static final String TIMES = "Times New Roman";
    static final String PAP = "Papyrus";
    //some URLS
    static final String CoinURL = Resource.COIN.getPathBig();
    static final String StoneURL = Resource.STONE.getPathBig();
    static final String ShieldURL = Resource.SHIELD.getPathBig();
    static final String ServantURL = Resource.SERVANT.getPathBig();
    static final String NoneURL = Resource.NONE.getPathBig();


    public static void main(String[] args) {
        // you can write here shortcuts, like going directly to the Settings and opening multiple frames
        // only if called by the main, otherwise it must be empty
        // new MainMenu();
        // cl.show(cardPanel, CREATE);



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
        for(int i=0; i<4; i++){
            Ark.gameManager.getFaithTrackManager().advance(B);
        }
        for(int i =0; i<6; i++){
            Ark.gameManager.getFaithTrackManager().advance(C);
        }
        for(int i =0; i<15; i++){
            Ark.gameManager.getFaithTrackManager().advance(D);
        }



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

        B.getLeaderCards()[0].setEnabled(true);
        A.getLeaderCards()[0].setEnabled(true);
        Ark.actionManager.discardLeaderCard(D, new MSG_ACTION_DISCARD_LEADERCARD(1));

        A.getWarehouseDepot().add(Resource.SERVANT);
        A.getWarehouseDepot().add(Resource.COIN);
        A.getWarehouseDepot().add(Resource.COIN);
        A.getWarehouseDepot().add(Resource.SHIELD);

        B.getWarehouseDepot().add(Resource.STONE);
        B.getWarehouseDepot().add(Resource.SERVANT);
        B.getWarehouseDepot().add(Resource.SERVANT);

        C.getWarehouseDepot().add(Resource.SERVANT);
        C.getWarehouseDepot().swapRow(1,2);

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

        D.getDevelopmentSlot().addCard( new DevelopmentCard(1, it.polimi.ingsw.server.model.enumerators.Color.YELLOW, 1,
                Map.of(Resource.STONE, 2),
                new Power(Map.of(Resource.SERVANT, 1),
                        Map.of(Resource.FAITH, 1)),
                "resources/cardsFront/DFRONT (5).png", "resources/cardsBack/BACK (5)"
        ),1);

        A.getDevelopmentSlot().addCard(new DevelopmentCard(1, it.polimi.ingsw.server.model.enumerators.Color.GREEN, 10,
                Map.of(Resource.SHIELD, 5, Resource.SERVANT, 2),
                new Power(Map.of(Resource.COIN, 1, Resource.SERVANT, 1),
                        Map.of(Resource.SHIELD, 2, Resource.STONE, 2, Resource.FAITH, 1)),
                "resources/cardsFront/DFRONT (38).png", "resources/cardsBack/BACK (10)"
        ), 0);

        A.getDevelopmentSlot().addCard( new DevelopmentCard(1, it.polimi.ingsw.server.model.enumerators.Color.GREEN, 9,
                Map.of(Resource.SHIELD, 6),
                new Power(Map.of(Resource.COIN, 2),
                        Map.of(Resource.STONE, 3, Resource.FAITH, 2)),
                "resources/cardsFront/DFRONT (34).png", "resources/cardsBack/BACK (10)"
        ) , 1);

        A.getDevelopmentSlot().addCard(new DevelopmentCard(3, it.polimi.ingsw.server.model.enumerators.Color.BLUE, 12,
                Map.of(Resource.COIN, 4, Resource.STONE, 4),
                new Power(Map.of(Resource.SERVANT, 1),
                        Map.of(Resource.COIN, 1, Resource.SHIELD, 3)),
                "resources/cardsFront/DFRONT (48).png", "resources/cardsBack/BACK (12)"
        ), 2);

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

        B.getDevelopmentSlot().addCard( new DevelopmentCard(3, it.polimi.ingsw.server.model.enumerators.Color.YELLOW, 9,
                Map.of(Resource.STONE, 6),
                new Power(Map.of(Resource.SHIELD, 2),
                        Map.of(Resource.SERVANT, 3, Resource.FAITH, 2)),
                "resources/cardsFront/DFRONT (37).png", "resources/cardsBack/BACK (13)"
        ),1);

        C.getDevelopmentSlot().addCard( new DevelopmentCard(1, it.polimi.ingsw.server.model.enumerators.Color.YELLOW, 2,
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

    public void run() {
        //empty to implement run method
    }

    public Board() {
        mainFrame = new JFrame("Title");
        mainPanel = new MainPanel();
        mainFrame.setContentPane(mainPanel);


        mainFrame.pack();

        //frameDimension = new Dimension(1630, 1030);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //mainFrame.setMinimumSize(frameDimension);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    class MainPanel extends JPanel {

        private Image image;

        public MainPanel()
        {
            try {
                image = ImageIO.read(new File("resources/images/board_bg.png"));
            } catch (IOException e) {}

            {
                GridBagConstraints c;
                this.setLayout(new GridBagLayout());

                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                c.gridwidth = 1;
                c.gridheight = 2;
                this.add(new LeftPanel(), c);

                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 0;
                c.gridwidth = 2;
                c.gridheight = 1;
                this.add(new TopPanel(), c);

                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 2;
                c.gridwidth = 3;
                c.gridheight = 1;
                this.add(new BottomPanel(), c);

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
            {
                quit_Button.addActionListener(quit_Button_actionListener);
                show_DevDeck_Button.addActionListener(show_DevDeck_Button_actionListener);
                show_Market_Button.addActionListener(show_Market_Button_actionListener);
                get_MarketResource_Button.addActionListener(get_MarketResource_Button_actionListener);
                activate_LeaderCards_Button.addActionListener(activate_LeaderCards_Button_actionListener);
                change_Depot_Config_Button.addActionListener(change_Depot_Config_Button_actionListener);
                get_MarketResource_Button.addActionListener(get_MarketResource_Button_actionListener);
                discard_LeaderCard_Button.addActionListener(discard_LeaderCard_Button_actionListener);
                buy_DevCard_Button.addActionListener(buy_DevCard_Button_actionListener);
                activate_Production_Button.addActionListener(activate_Production_Button_actionListener);
                endTurn_Button.addActionListener(endTurn_Button_actionListener);
                back_DevDeck_Card_Button.addActionListener(back_DevDeck_Card_Button_actionListener);
                back_Market_Card_Button.addActionListener(back_DevDeck_Card_Button_actionListener);
                back_GoToMarket_Card_Button.addActionListener(back_GoToMarket_Card_Button_actionListener);
            }
            //INITIAL UPDATE (?)
            {
                myLeaderCardsPanel.update();
                playersRecapPanel.update();
                devDeck_board_card_panel.update();
                for(PlayerSimplified player : Ark.game.getPlayerList())
                {
                    centralLeftPanel.update(player.getNickname());
                }
                leaderCardsPicker_board_card_panel.update();
                changeDepotConfig_board_card_panel.update();
                market_board_card_panel.update();
                goToMarket_board_card_panel.update();
                changeDepotConfig_board_card_panel.update();
            }

            lastRightCard = Ark.nickname;
            lastLeftCard = Ark.nickname;

            //controls for first player, eg leadercardpicker enabled
            cardLayoutRight.show(centralRightPanel, CHANGEDEPOT);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }

    }

    //LEFT PANEL
    class LeftPanel extends JPanel {
        public LeftPanel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBackground(new Color(215, 200, 145));
            this.setBorder(BorderFactory.createMatteBorder(1,1,0,1, new Color(62, 43, 9)));

            addPadding(this, 839,457,3,6);

            quit_Button = new JButton("Quit");
            quit_Button.setPreferredSize(new Dimension(120, 60));
            quit_Button.setFont(new Font(PAP, Font.BOLD, 20));
            quit_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 2;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.insets = new Insets(20,20, 0,0);
            this.add(quit_Button, c);

            turnLabel = new JLabel();
            turnLabel.setOpaque(false);
            turnLabel.setText("Turn 5");
            turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
            turnLabel.setFont(new Font(PAP, Font.BOLD, 20));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(20,0, 0,0);
            this.add(turnLabel, c);

            turnOf = new JLabel();
            turnOf.setOpaque(false);
            turnOf.setText("Turn of "+Ark.game.getCurrentPlayerName());
            turnOf.setHorizontalAlignment(SwingConstants.CENTER);
            turnOf.setFont(new Font(PAP, Font.BOLD, 20));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 2;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.fill = GridBagConstraints.HORIZONTAL;

            this.add(turnOf, c);

            notificationsArea = new JTextArea();
            notificationsArea.setText("text");
            notificationsArea.setFont(new Font(TIMES, Font.BOLD, 20));
            notificationsArea.setBackground(new Color(222, 209, 156));
            notificationsArea.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

            JScrollPane scrollPane = new JScrollPane(notificationsArea);
            scrollPane.setPreferredSize(new Dimension(400,200));
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
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(20,0, 0,0);
            this.add(scrollPane, c);

            myLeaderCardsPanel = new MyLeaderCardsPanel();
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 4;
            c.weighty = 0.1;
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
                show_DevDeck_Button.setPreferredSize(new Dimension(200, 70));
                show_DevDeck_Button.setFont(new Font(PAP, Font.BOLD, 20));
                show_DevDeck_Button.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                c.insets = new Insets(5,5,20,12);
                c.anchor = GridBagConstraints.LINE_START;
                showButtonsPanel.add(show_DevDeck_Button, c);

                show_Market_Button = new JButton("show Market");
                show_Market_Button.setPreferredSize(new Dimension(200, 70));
                show_Market_Button.setFont(new Font(PAP, Font.BOLD, 20));
                show_Market_Button.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 0;
                c.insets = new Insets(5,12,20,5);
                c.anchor = GridBagConstraints.LINE_END;
                showButtonsPanel.add(show_Market_Button, c);

                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 5;
                c.weighty = 0.1;
                c.gridwidth = 2;
                c.gridheight = 1;
                c.anchor = GridBagConstraints.PAGE_END;
                this.add(showButtonsPanel, c);
            } //showDevDeck and showMarket buttons Panel
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
            if(leaderCard != null) {
                t = scaleImage(new ImageIcon(leaderCard.getFrontPath()), 300);
                if(leaderCard.isEnabled()) {
                    labelUnderLeaderCard.setText("ENABLED!");
                    leaderCardLabel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                    if(leaderCard.getSpecialAbility().isExtraDepot())
                    {
                        ExtraDepot depot = ((ExtraDepot) leaderCard.getSpecialAbility());
                        int num = depot.getNumber();
                        Resource type = depot.getResourceType();

                        ImageIcon presentIcon = scaleImage(new ImageIcon(type.getPathLittle()),50);
                        ImageIcon noneIcon = scaleImage(new ImageIcon(Resource.NONE.getPathLittle()),50);
                        switch (num)
                        {
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
            }
            else {
                t = scaleImage(new ImageIcon("resources/cardsBack/BACK (1).png"), 300);
                labelUnderLeaderCard.setText("not present!");
            }
            leaderCardLabel.setIcon(t);
        }
    }


    //BOTTOM PANEL
    class BottomPanel extends JPanel {
        public BottomPanel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBackground(new Color(215, 200, 145));
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9),1));

            addPadding(this, 158,1678,6,3);

            {
                activate_LeaderCards_Button = new JButton("activate card");
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
                get_MarketResource_Button.setPreferredSize(new Dimension(145, 60));
                get_MarketResource_Button.setFont(new Font(PAP, Font.BOLD, 18));
                get_MarketResource_Button.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 3;
                c.gridy = 1;
                c.weighty = 0.5;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.anchor = GridBagConstraints.LINE_START;
                c.insets = new Insets(15, 2, 0, 6);
                this.add(get_MarketResource_Button, c);
            } //top row of action buttons

            {
                discard_LeaderCard_Button = new JButton("discard card");
                discard_LeaderCard_Button.setPreferredSize(new Dimension(145, 60));
                discard_LeaderCard_Button.setFont(new Font(PAP, Font.BOLD, 18));
                discard_LeaderCard_Button.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 2;
                c.weighty = 0.5;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.anchor = GridBagConstraints.LINE_START;
                c.insets = new Insets(0, 8, 15, 2);
                this.add(discard_LeaderCard_Button, c);


                buy_DevCard_Button = new JButton("buy card");
                buy_DevCard_Button.setPreferredSize(new Dimension(145, 60));
                buy_DevCard_Button.setFont(new Font(PAP, Font.BOLD, 18));
                buy_DevCard_Button.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 2;
                c.gridy = 2;
                c.weighty = 0.5;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.anchor = GridBagConstraints.LINE_START;
                c.insets = new Insets(0, 2, 15, 2);
                this.add(buy_DevCard_Button, c);

                activate_Production_Button = new JButton("produce");
                activate_Production_Button.setPreferredSize(new Dimension(145, 60));
                activate_Production_Button.setFont(new Font(PAP, Font.BOLD, 18));
                activate_Production_Button.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 3;
                c.gridy = 2;
                c.weighty = 0.5;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.anchor = GridBagConstraints.LINE_START;
                c.insets = new Insets(0, 2, 15, 6);
                this.add(activate_Production_Button, c);
            } //bottom row of action buttons

            playersRecapPanel = new PlayersRecapPanel();
            playersRecapPanel.setPreferredSize(new Dimension(1060,150));
            c = new GridBagConstraints();
            c.gridx = 4;
            c.gridy = 1;
            c.weighty = 0.5;
            c.gridwidth = 1;
            c.gridheight = 2;
            this.add(playersRecapPanel,c);

            endTurn_Button = new JButton("end Turn");
            endTurn_Button.setPreferredSize(new Dimension(145, 140));
            endTurn_Button.setFont(new Font(PAP, Font.BOLD, 24));
            endTurn_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 5;
            c.gridy = 1;
            c.weighty = 0.5;
            c.gridwidth = 1;
            c.gridheight = 2;
            c.insets = new Insets(0, 0, 0, 0);
            this.add(endTurn_Button, c);
        }
    }

    class PlayersRecapPanel extends JPanel //TODO solo mode
    {
        private Map<String, java.util.List<JLabel>> map;

        public PlayersRecapPanel()
        {
            GridBagConstraints c;

            this.setBackground(new Color(242, 224, 178));
            this.setLayout(new GridBagLayout());

            if(!Ark.solo) {

                java.util.List<JLabel> labelList = new ArrayList<>();
                map = new HashMap<>();
                for(int i = 0; i<Ark.game.getPlayerList().size(); i++) //for all players except the one me
                {
                    labelList.clear();
                    PlayerSimplified player = Ark.game.getPlayer(i+1);
                    if(player.equals(Ark.myPlayerRef))
                        continue;

                    JPanel playerPanel = new JPanel(new GridBagLayout());
                    playerPanel.setOpaque(false);
                    playerPanel.setBorder(BorderFactory.createLineBorder(new Color(79, 66, 34),2));

                    JLabel nicknameLabel = new JLabel(""+player.getPlayerNumber()+" - "+player.getNickname());
                    nicknameLabel.setFont(new Font(PAP, Font.BOLD, 24));
                    c = new GridBagConstraints();
                    c.gridx = 0;
                    c.gridy = 0;
                    c.weightx = 1;
                    c.weighty = 0.5;
                    c.gridwidth = 2;
                    c.gridheight = 1;
                    c.anchor = GridBagConstraints.PAGE_START;
                    c.insets = new Insets(1,1,1,1);
                    playerPanel.add(nicknameLabel,c);

                    JLabel coinLabel = new JLabel();
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
                    c.insets = new Insets(1,14,1,1);
                    playerPanel.add(coinLabel,c);

                    JLabel shieldLabel = new JLabel();
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
                    c.insets = new Insets(1,14,1,1);
                    playerPanel.add(shieldLabel,c);

                    JLabel stoneLabel = new JLabel();
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
                    c.insets = new Insets(1,14,1,1);
                    playerPanel.add(stoneLabel,c);

                    JLabel servantLabel = new JLabel();
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
                    c.insets = new Insets(1,14,3,1);
                    playerPanel.add(servantLabel,c);


                    ShowPlayerButton button = new ShowPlayerButton("show", player.getNickname());
                    button.addActionListener(e -> {
                        ShowPlayerButton b = (ShowPlayerButton) e.getSource();
                        String playerName = b.getPlayerName();

                        cardLayoutLeft.show(centralLeftPanel, playerName);
                        cardLayoutRight.show(centralRightPanel, playerName);
                    });
                    button.setFont(new Font(PAP, Font.BOLD, 20));
                    button.setMinimumSize(new Dimension(100,100));
                    button.setBackground(new Color(231, 210, 181));
                    c = new GridBagConstraints();
                    c.gridx = 1;
                    c.gridy = 1;
                    c.weightx = 0.8;
                    c.weighty = 0.5;
                    c.gridwidth = 1;
                    c.gridheight = 4;
                    c.anchor = GridBagConstraints.LINE_END;
                    c.insets = new Insets(10,0,10,10);
                    playerPanel.add(button,c);

                    map.put(player.getNickname(), new ArrayList<>(labelList));
                    c = new GridBagConstraints();
                    c.fill = GridBagConstraints.BOTH;
                    c.gridx = i;
                    c.weightx = 0.5;
                    c.weighty = 0.5;
                    this.add(playerPanel, c);
                }
            }
            else
            {
                //TODO solo mode for bottom panel
            }
        }

        public void update() {
            if(!Ark.solo) {
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
            else
            {
                //TODO solo mode update for bottom panel
            }
        }
    }

    class ShowPlayerButton extends JButton {
        private final String playerName;
        public ShowPlayerButton(String text, String playerName)
        {
            super(text);
            this.playerName = playerName;
        }

        public String getPlayerName() { return this.playerName; }
    }


    //TOP PANEL
    class TopPanel extends JPanel {
        private Image image;
        public TopPanel()
        {
            try {
                image = ImageIO.read(new File("resources/images/upper_board.png"));
            } catch (IOException e) {}
            this.setLayout(new GridBagLayout());

            addPadding(this, 239,1221,2,2);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }


    //CENTRAL LEFT PANEL <- CARDLAYOUTLEFT
    class CentralLeftPanel extends JPanel {
        private Image image;

        java.util.List<DepotAndStrongbox_Card_Panel> depotAndStrongboxCardPanelList;

        public CentralLeftPanel()
        {
            try {
                image = ImageIO.read(new File("resources/images/left_board.png"));
            } catch (IOException e) {}

            cardLayoutLeft = new CardLayout();
            this.setLayout(cardLayoutLeft);

            depotAndStrongboxCardPanelList = new ArrayList<>();

            for(PlayerSimplified player : Ark.game.getPlayerList())
            {
                DepotAndStrongbox_Card_Panel depotAndStrongboxCardPanel = new DepotAndStrongbox_Card_Panel(player.getNickname());
                this.add(depotAndStrongboxCardPanel, player.getNickname());
                depotAndStrongboxCardPanelList.add(depotAndStrongboxCardPanel);
            }
        }

        public void update(String name)
        {
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
        private String panelName;

        JLabel shelf1;
        JLabel[] shelf2;
        JLabel[] shelf3;

        JLabel[] strongbox; //coin shield stone servant

        //many labels
        public DepotAndStrongbox_Card_Panel(String panelName) {
            super();
            this.panelName = panelName;
            this.setOpaque(false);
            this.setLayout(new GridBagLayout());
            shelf1 = new JLabel();
            shelf2 = new JLabel[2];
            shelf2[0] = new JLabel(); shelf2[1] = new JLabel();
            shelf3 = new JLabel[3];
            shelf3[0] = new JLabel(); shelf3[1] = new JLabel(); shelf3[2] = new JLabel();

            GridBagConstraints c;

            addPadding(this,601,273,5,6 );

            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.weightx = 0.2;
            c.weighty = 0.2;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(105,60,0,0);
            this.add(shelf1, c);

            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 2;
            c.weightx = 0.5;
            c.weighty = 0.2;
            c.gridwidth = 1;
            c.anchor = GridBagConstraints.FIRST_LINE_END;
            c.insets = new Insets(15,70,0,8);
            this.add(shelf2[0], c);

            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 2;
            c.weightx = 0.5;
            c.weighty = 0.2;
            c.gridwidth = 1;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.insets = new Insets(15,0,0,0);
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
            c.insets = new Insets(0,60,0,8);
            thirdshelfPanel.add(shelf3[0], c);

            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 0;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.gridwidth = 1;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(0,0,0,8);
            thirdshelfPanel.add(shelf3[1], c);

            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 0;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.gridwidth = 1;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.insets = new Insets(0,0,0,0);
            thirdshelfPanel.add(shelf3[2], c);

            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 3;
            c.weightx = 0.5;
            c.weighty = 0.5;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(15,0,0,0);
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
                c.insets = new Insets(35,0,2,80);
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
                c.insets = new Insets(35,10,2,0);
                this.add(strongbox[3], c);
            } //servant

        }

        public void update()
        {
            PlayerSimplified playerRef = Ark.game.getPlayerRef(this.panelName);
            WarehouseDepotSimplified depotRef = playerRef.getWarehouseDepot();
            StrongboxSimplified strongboxRef = playerRef.getStrongbox();

            Resource shelf1r = depotRef.getShelf1();
            Resource[] shelf2r = depotRef.getShelf2();
            Resource[] shelf3r = depotRef.getShelf3();

            this.shelf1.setIcon(scaleImage(new ImageIcon(shelf1r.getPathLittle()),50));
            this.shelf2[0].setIcon(scaleImage(new ImageIcon(shelf2r[0].getPathLittle()),50));
            this.shelf2[1].setIcon(scaleImage(new ImageIcon(shelf2r[1].getPathLittle()),50));
            this.shelf3[0].setIcon(scaleImage(new ImageIcon(shelf3r[0].getPathLittle()),50));
            this.shelf3[1].setIcon(scaleImage(new ImageIcon(shelf3r[1].getPathLittle()),50));
            this.shelf3[2].setIcon(scaleImage(new ImageIcon(shelf3r[2].getPathLittle()),50));

            Integer num;

            num = strongboxRef.getQuantity(Resource.COIN);
            if(num == null)
                this.strongbox[0].setText("0");
            else
                this.strongbox[0].setText(""+num);
            num = strongboxRef.getQuantity(Resource.SHIELD);
            if(num == null)
                this.strongbox[1].setText("0");
            else
                this.strongbox[1].setText(""+num);
            num = strongboxRef.getQuantity(Resource.STONE);
            if(num == null)
                this.strongbox[2].setText("0");
            else
                this.strongbox[2].setText(""+num);
            num = strongboxRef.getQuantity(Resource.SERVANT);
            if(num == null)
                this.strongbox[3].setText("0");
            else
                this.strongbox[3].setText(""+num);
        }
    }

    class ChangeDepotConfig_Board_Card_Panel extends JPanel {
        Resource[] shelf1type;
        Resource[] shelf2type;
        Resource[] shelf3type;
        JLabel shelf1label;
        JLabel[] shelf2label;
        JLabel[] shelf3label;

        ExtraDepotPanel firstExtraDepotPanel, secondExtraDepotPanel;
        int[] depotQuantity;

        public ChangeDepotConfig_Board_Card_Panel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9),1));

            addPadding(this, 599,946,3,7);

            shelf1type = new Resource[1];
            shelf2type = new Resource[2];
            shelf3type = new Resource[3];

            shelf2label = new JLabel[2];
            shelf3label = new JLabel[3];

            depotQuantity = new int[2];

            back_ChangeDepotConfig_Card_Button = new JButton("Back");
            back_ChangeDepotConfig_Card_Button.setPreferredSize(new Dimension(120, 40));
            back_ChangeDepotConfig_Card_Button.setFont(new Font(PAP, Font.BOLD, 20));
            back_ChangeDepotConfig_Card_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.weightx = 0.1;
            c.weighty = 0.1;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.insets = new Insets(5,5,0,0);
            this.add(back_ChangeDepotConfig_Card_Button,c);

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
                c.insets = new Insets(0,20,0,2);
                firstShelf.add(shelf1label, c);

                resourceChangerButton = new JButton(">");
                c = new GridBagConstraints();
                c.gridx = 2;
                c.weightx = 0.3;
                c.insets = new Insets(0,2,0,20);
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
                secondShelf.add(secondLabel,c);

                shelf2label[0] = new JLabel();
                c = new GridBagConstraints();
                c.gridx = 1;
                c.weightx = 0.3;
                c.insets = new Insets(0,20,0,2);
                secondShelf.add(shelf2label[0],c);

                resourceChangerButton = new JButton(">");
                resourceChangerButton.addActionListener(new resourceChanger(shelf2label[0], shelf2type, 0));
                resourceChangerButton.setFont(new Font(PAP, Font.BOLD, 20));
                resourceChangerButton.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 2;
                c.weightx = 0.3;
                c.insets = new Insets(0,2,0,20);
                secondShelf.add(resourceChangerButton,c);

                shelf2label[1] = new JLabel();
                c = new GridBagConstraints();
                c.gridx = 3;
                c.weightx = 0.3;
                c.insets = new Insets(0,20,0,2);
                secondShelf.add(shelf2label[1],c);

                resourceChangerButton = new JButton(">");
                resourceChangerButton.addActionListener(new resourceChanger(shelf2label[1], shelf2type, 1));
                resourceChangerButton.setFont(new Font(PAP, Font.BOLD, 20));
                resourceChangerButton.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 4;
                c.weightx = 0.3;
                c.insets = new Insets(0,2,0,20);
                secondShelf.add(resourceChangerButton,c);

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
                thirdShelf.add(thirdLabel,c);

                shelf3label[0] = new JLabel();
                c = new GridBagConstraints();
                c.gridx = 1;
                c.weightx = 0.3;
                c.insets = new Insets(0,20,0,2);
                thirdShelf.add(shelf3label[0],c);

                resourceChangerButton = new JButton(">");
                resourceChangerButton.addActionListener(new resourceChanger(shelf3label[0], shelf3type, 0));
                resourceChangerButton.setFont(new Font(PAP, Font.BOLD, 20));
                resourceChangerButton.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 2;
                c.weightx = 0.3;
                c.insets = new Insets(0,2,0,20);
                thirdShelf.add(resourceChangerButton,c);

                shelf3label[1] = new JLabel();
                c = new GridBagConstraints();
                c.gridx = 3;
                c.weightx = 0.3;
                c.insets = new Insets(0,20,0,2);
                thirdShelf.add(shelf3label[1],c);

                resourceChangerButton = new JButton(">");
                resourceChangerButton.addActionListener(new resourceChanger(shelf3label[1], shelf3type, 1));
                resourceChangerButton.setFont(new Font(PAP, Font.BOLD, 20));
                resourceChangerButton.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 4;
                c.weightx = 0.3;
                c.insets = new Insets(0,2,0,20);
                thirdShelf.add(resourceChangerButton,c);

                shelf3label[2] = new JLabel();
                c = new GridBagConstraints();
                c.gridx = 5;
                c.weightx = 0.3;
                c.insets = new Insets(0,20,0,2);
                thirdShelf.add(shelf3label[2],c);

                resourceChangerButton = new JButton(">");
                resourceChangerButton.addActionListener(new resourceChanger(shelf3label[2], shelf3type, 2));
                resourceChangerButton.setFont(new Font(PAP, Font.BOLD, 20));
                resourceChangerButton.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 6;
                c.weightx = 0.3;
                c.insets = new Insets(0,2,0,20);
                thirdShelf.add(resourceChangerButton,c);

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
            c.weighty = 0.3;
            c.gridwidth = 1;
            this.add(firstExtraDepotPanel,c);


            secondExtraDepotPanel = new ExtraDepotPanel(depotQuantity, 1);
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 5;
            c.weightx = 0.5;
            c.weighty = 0.3;
            c.gridwidth = 1;
            this.add(secondExtraDepotPanel,c);

            confirm_ChangeDepotConfig_Card_Button = new JButton("confirm!");
            confirm_ChangeDepotConfig_Card_Button.setPreferredSize(new Dimension(200, 60));
            confirm_ChangeDepotConfig_Card_Button.setFont(new Font(PAP, Font.BOLD, 28));
            confirm_ChangeDepotConfig_Card_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 6;
            c.weightx = 0.5;
            c.weighty = 0.4;
            c.gridwidth = 2;
            c.insets = new Insets(0,0,10,0);
            this.add(confirm_ChangeDepotConfig_Card_Button,c);



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
            this.shelf1label.setIcon(scaleImage(new ImageIcon(shelf1r.getPathBig()),100));
            this.shelf2type[0] = shelf2r[0];
            this.shelf2label[0].setIcon(scaleImage(new ImageIcon(shelf2r[0].getPathBig()),100));
            this.shelf2type[1] = shelf2r[0];
            this.shelf2label[1].setIcon(scaleImage(new ImageIcon(shelf2r[1].getPathBig()),100));
            this.shelf3type[0] = shelf3r[0];
            this.shelf3label[0].setIcon(scaleImage(new ImageIcon(shelf3r[0].getPathBig()),100));
            this.shelf3type[1] = shelf3r[1];
            this.shelf3label[1].setIcon(scaleImage(new ImageIcon(shelf3r[1].getPathBig()),100));
            this.shelf3type[2] = shelf3r[2];
            this.shelf3label[2].setIcon(scaleImage(new ImageIcon(shelf3r[2].getPathBig()),100));

            firstExtraDepotPanel.updateValues();
            secondExtraDepotPanel.updateValues();

        }

        class ExtraDepotPanel extends JPanel {
            private JLabel label;
            private JLabel number;
            private JButton less;
            private JButton more;
            private int[] depotQuantity;
            private int lcnum;

            public ExtraDepotPanel(int[] depotQuantity, int lcnum) {
                this.depotQuantity = depotQuantity;
                this.lcnum = lcnum;

                GridBagConstraints c;
                this.setLayout(new GridBagLayout());
                this.setOpaque(false);

                JLabel fixedLabel = new JLabel();
                fixedLabel.setFont(new Font(PAP, Font.BOLD, 20));
                fixedLabel.setText("   Leader Card #"+(lcnum+1)+"   ");
                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                c.weightx = 0.3;
                c.weighty = 0.3;
                this.add(fixedLabel,c);

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
                c.gridheight=2;
                c.insets = new Insets(0,10,0,10);
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
                c.gridheight=2;
                this.add(less,c);

                more = new JButton(">");
                more.addActionListener(more_ActionListener);
                more.setFont(new Font(PAP, Font.BOLD, 20));
                more.setBackground(new Color(231, 210, 181));
                c = new GridBagConstraints();
                c.gridx = 3;
                c.gridy = 0;
                c.weightx = 0.3;
                c.weighty = 0.3;
                c.gridheight=2;
                this.add(more,c);
            }

            public void updateValues() {
                LeaderCard l = Ark.myPlayerRef.getLeaderCards()[lcnum];
                if(l==null)
                {
                    this.label.setText("is not present");
                    this.number.setText("");
                    this.less.setEnabled(false);
                    this.more.setEnabled(false);
                    depotQuantity[lcnum] = -1;
                }
                else
                {
                    if(l.getSpecialAbility().isExtraDepot() && l.isEnabled())
                    {
                        int num = ((ExtraDepot) l.getSpecialAbility()).getNumber();
                        Resource res = ((ExtraDepot) l.getSpecialAbility()).getResourceType();

                        depotQuantity[lcnum] = num;

                        //FIXME
                        switch (res){
                            case COIN: this.label.setText("Stones");
                                break;
                            case SHIELD: this.label.setText("Shields");
                                break;
                            case STONE: this.label.setText("Stones");
                                break;
                            case SERVANT: this.label.setText("Servants");
                                break;
                        }


                        this.number.setText(""+depotQuantity[lcnum]);
                        this.less.setEnabled(true);
                        this.more.setEnabled(true);
                    }
                    else if(l.getSpecialAbility().isExtraDepot() && !l.isEnabled())
                    {
                        this.label.setText("is not enabled");
                        this.number.setText("");
                        this.less.setEnabled(false);
                        this.more.setEnabled(false);
                        depotQuantity[lcnum] = -1;
                    }
                    else
                    {
                        this.label.setText("is not Extra Depot");
                        this.number.setText("");
                        this.less.setEnabled(false);
                        this.more.setEnabled(false);
                        depotQuantity[lcnum] = -1;
                    }
                }
            }

            ActionListener less_ActionListener = e -> {
                if(this.depotQuantity[lcnum] == 0) return;
                this.depotQuantity[lcnum] = this.depotQuantity[lcnum]-1;
                this.number.setText(""+this.depotQuantity[lcnum]);
            };

            ActionListener more_ActionListener = e -> {
                if(this.depotQuantity[lcnum] == 2) return;
                this.depotQuantity[lcnum] = this.depotQuantity[lcnum]+1;
                this.number.setText(""+this.depotQuantity[lcnum]);
            };
        }

        class resourceChanger implements ActionListener {

            JLabel managedLabel;
            Resource[] arraySource;
            int index;

            public resourceChanger(JLabel managedLabel, Resource[] arraySource, int index)
            {
                this.managedLabel = managedLabel;
                this.arraySource = arraySource;
                this.index = index;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                Resource nextResource = nextResource(arraySource[index]);
                arraySource[index] = nextResource;
                managedLabel.setIcon(scaleImage(new ImageIcon(nextResource.getPathBig()),100));
            }
        }

        private Resource nextResource(Resource currentResource) {
            switch (currentResource) { //none coin shield stone servant
                case NONE: return Resource.COIN;
                case COIN: return Resource.SHIELD;
                case SHIELD: return Resource.STONE;
                case STONE: return Resource.SERVANT;
                case SERVANT: return Resource.NONE;
                default: return Resource.NONE;
            }
        }
    }




    //CENTRAL RIGHT PANEL <- CARDLAYOUTRIGHT
    class CentralRightPanel extends JPanel {
        public CentralRightPanel() {
            cardLayoutRight = new CardLayout();
            this.setLayout(cardLayoutRight);
            this.setOpaque(false);

            self_board_card_panel = new Self_Board_Card_Panel();
            this.add(self_board_card_panel, Ark.nickname);
            devDeck_board_card_panel = new DevDeck_Board_Card_Panel();
            this.add(devDeck_board_card_panel, DEVDECK);
            market_board_card_panel = new Market_Board_Card_Panel();
            this.add(market_board_card_panel, MARKET);
            leaderCardsPicker_board_card_panel = new LeaderCardsPicker_Board_Card_Panel();
            this.add(leaderCardsPicker_board_card_panel, LPICKER);
            goToMarket_board_card_panel = new GoToMarket_Board_Card_Panel();
            this.add(goToMarket_board_card_panel, GOTOMARKET);
            changeDepotConfig_board_card_panel = new ChangeDepotConfig_Board_Card_Panel();
            this.add(changeDepotConfig_board_card_panel, CHANGEDEPOT);
        }
    }

    class Self_Board_Card_Panel extends JPanel { //this card is called by Ark.nickname

        private Image image;

        public Self_Board_Card_Panel() //TODO devslot, update
        {
            this.setLayout(new GridBagLayout());

            try {
                image = ImageIO.read(new File("resources/images/right_board.png"));
            } catch (IOException e) {}

            addPadding(this, 601,948,2,2);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }

    class DevDeck_Board_Card_Panel extends JPanel { //this card is called by DEVDECK

        JLabel[][] labels;

        public DevDeck_Board_Card_Panel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9),1));

            this.labels = new JLabel[3][4];

            addPadding(this, 599,946,6,5);

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
            c.insets = new Insets(0,0,0,0);
            this.add(level3CardLabel,c);

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
            c.insets = new Insets(0,0,0,0);
            this.add(level2CardLabel,c);

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
            c.insets = new Insets(0,0,0,0);
            this.add(level1CardLabel,c);

            back_DevDeck_Card_Button = new JButton("Back");
            back_DevDeck_Card_Button.setPreferredSize(new Dimension(120, 40));
            back_DevDeck_Card_Button.setFont(new Font(PAP, Font.BOLD, 20));
            back_DevDeck_Card_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.01;
            c.weighty = 0.01;
            c.insets = new Insets(5,5,0,0);
            this.add(back_DevDeck_Card_Button,c);

            JLabel greenCardColumnLabel = new JLabel("green");
            greenCardColumnLabel.setFont(new Font(PAP, Font.BOLD, 20));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.5;
            c.weighty = 0.01;
            c.insets = new Insets(0,0,0,0);
            this.add(greenCardColumnLabel,c);

            JLabel blueCardColumnLabel = new JLabel("blue");
            blueCardColumnLabel.setFont(new Font(PAP, Font.BOLD, 20));
            c = new GridBagConstraints();
            c.gridx = 3;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.5;
            c.weighty = 0.01;
            c.insets = new Insets(0,0,0,0);
            this.add(blueCardColumnLabel,c);

            JLabel yellowCardColumnLabel = new JLabel("yellow");
            yellowCardColumnLabel.setFont(new Font(PAP, Font.BOLD, 20));
            c = new GridBagConstraints();
            c.gridx = 4;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.5;
            c.weighty = 0.01;
            c.insets = new Insets(0,0,0,0);
            this.add(yellowCardColumnLabel,c);

            JLabel purpleCardColumnLabel = new JLabel("purple");
            purpleCardColumnLabel.setFont(new Font(PAP, Font.BOLD, 20));
            c = new GridBagConstraints();
            c.gridx = 5;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 0.5;
            c.weighty = 0.01;
            c.insets = new Insets(0,0,0,0);
            this.add(purpleCardColumnLabel,c);

            for(int col = 0; col < 4; col++)
            {
                for(int row = 0; row < 3; row++)
                {
                     JLabel label = new JLabel();
                     c = new GridBagConstraints();
                     c.gridx = col+2;
                     c.gridy = row+2;
                     c.weightx = 0.5;
                     c.weighty = 0.5;
                     this.add(label,c);
                     this.labels[row][col] = label;
                }
            }
        }

        public void update() {
            DevelopmentCard[][] cards = Ark.game.getDevDeck().getCards();

            for(int col = 0; col < 4; col++)
            {
                for(int row = 0; row < 3; row++)
                {
                    ImageIcon cardIcon;
                    if(cards[row][col]==null)
                        cardIcon = scaleImage(new ImageIcon("resources/cardsBack/BACK (1).png"), 175);
                    else
                        cardIcon = scaleImage(new ImageIcon(cards[row][col].getFrontPath()), 175);
                    labels[row][col].setIcon(cardIcon);
                }
            }
        }
    }

    class Market_Board_Card_Panel extends JPanel {
        JLabel[][] labelGrid; //<- contains the labels for the marblegrid
        JLabel labelSlide; //<- contains the label for the slidemarble
        private Image image;

        public Market_Board_Card_Panel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9),1));

            this.labelGrid = new JLabel[3][4];
            this.labelSlide = new JLabel();

            try {
                image = ImageIO.read(new File("resources/images/market2.png"));
            } catch (IOException e) {}

            addPadding(this, 599,946,100,100);

            back_Market_Card_Button = new JButton("Back");
            back_Market_Card_Button.setPreferredSize(new Dimension(120, 40));
            back_Market_Card_Button.setFont(new Font(PAP, Font.BOLD, 20));
            back_Market_Card_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 4.20;
            c.weighty = 11.00;
            c.insets = new Insets(5,5,0,0);
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            this.add(back_Market_Card_Button,c);

            ImageIcon slideMarble = scaleImage(new ImageIcon("resources/punchboard/purple_marble.png"), 64);
            labelSlide.setIcon(slideMarble);
            c = new GridBagConstraints();
            c.gridx = 5;
            c.gridy = 2;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 1.01;
            c.weighty = 1.01;
            this.add(labelSlide, c);

            JLabel label1 = new JLabel();
            c = new GridBagConstraints();
            c.gridx = 50;
            c.gridy = 50;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 30.51;
            c.weighty = 30.51;
            this.add(label1,c);

            for(int col = 0; col < 4; col++)
            {
                for(int row = 0; row < 3; row++)
                {
                    JLabel label = new JLabel();
                    c = new GridBagConstraints();
                    c.gridx = col+2;
                    c.gridy = row+3;
                    c.weightx = 0.4;
                    c.weighty = 1.5;
                    this.add(label,c);
                    this.labelGrid[row][col] = label;
                }
            }
        }

        public void update() {
            MarketMarble[][] grid = Ark.game.getMarket().getGrid();
            MarketMarble slide = Ark.game.getMarket().getSlideMarble();

            for(int col = 0; col < 4; col++)
            {
                for(int row = 0; row < 3; row++)
                {
                    labelGrid[row][col].setIcon(scaleImage(new ImageIcon(grid[row][col].getPath()), 64));
                }
            }
            labelSlide.setIcon(scaleImage(new ImageIcon(slide.getPath()), 64));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }

    class LeaderCardsPicker_Board_Card_Panel extends JPanel implements ItemListener {
        JLabel[] labelCards;
        JCheckBox[] checkBoxes;

        int first = -1;
        int second = -1;
        boolean modifying = false;

        public LeaderCardsPicker_Board_Card_Panel() {
            GridBagConstraints c;
            this.setOpaque(false);
            this.setLayout(new GridBagLayout());

            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9),1));
            addPadding(this, 599,946,5,5);

            this.labelCards= new JLabel[4];
            this.checkBoxes= new JCheckBox[4];

            JLabel titleLabel = new JLabel("Pick your two Cards!");
            titleLabel.setFont(new Font(PAP, Font.BOLD, 50));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 4;
            c.weightx = 0.5;
            c.weighty = 0.5;
            this.add(titleLabel,c);

            for(int i=0; i<4;i++)
            {
                labelCards[i] = new JLabel();

                c = new GridBagConstraints();
                c.gridx = i+1;
                c.gridy = 2;
                c.gridwidth = 1;
                c.weightx = 0.5;
                c.weighty = 0.2;
                this.add(labelCards[i],c);

                checkBoxes[i] = new JCheckBox();
                checkBoxes[i].setBackground(new Color(178, 49, 35));
                checkBoxes[i].addItemListener(this);
                c = new GridBagConstraints();
                c.gridx = i+1;
                c.gridy = 3;
                c.anchor = GridBagConstraints.PAGE_START;
                c.weightx = 0.5;
                c.weighty = 0.4;
                this.add(checkBoxes[i], c);
            }

            confirm_LeaderCardsPicker_Card_Button = new JButton("confirm!");
            confirm_LeaderCardsPicker_Card_Button.setEnabled(false);
            confirm_LeaderCardsPicker_Card_Button.setPreferredSize(new Dimension(200, 60));
            confirm_LeaderCardsPicker_Card_Button.setFont(new Font(PAP, Font.BOLD, 28));
            confirm_LeaderCardsPicker_Card_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 4;
            c.weightx = 0.5;
            c.weighty = 0.4;
            c.gridwidth = 4;
            c.insets = new Insets(0,0,10,0);
            this.add(confirm_LeaderCardsPicker_Card_Button,c);


        }

        public void update()
        {
            for(int i=0; i<4; i++)
            {
                ImageIcon t = scaleImage(new ImageIcon(Ark.game.getLeaderCardsPicker().getCard(i).getFrontPath()),300);
                labelCards[i].setIcon(t);
            }
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
            JCheckBox source = (JCheckBox) e.getSource();
            int number = -1;
            if (this.checkBoxes[0] == source) number = 0;
            if (this.checkBoxes[1] == source) number = 1;
            if (this.checkBoxes[2] == source) number = 2;
            if (this.checkBoxes[3] == source) number = 3;

            if (!modifying) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (first == -1 && second == -1) {
                        first = number;
                    } else if (first != -1 && second == -1) {
                        second = number;
                        confirm_LeaderCardsPicker_Card_Button.setEnabled(true);
                    } else if (first != -1 && second != -1 && first != number && second != number) //so a new card was chosen
                    {
                        modifying = true;
                        this.checkBoxes[first].setSelected(false);
                        first = second;
                        second = number;
                        confirm_LeaderCardsPicker_Card_Button.setEnabled(true);
                        modifying = false;

                    }
                } else //item deselected
                {
                    if (first != -1 && second != -1) {
                        if (first == number)
                            first = second;
                        second = -1;
                        confirm_LeaderCardsPicker_Card_Button.setEnabled(false);
                    } else if (first != -1) {
                        first = -1;
                    }
                }
            }
        } //checkBoxes listener, allows for two and only two checks
    }

    class GoToMarket_Board_Card_Panel extends JPanel {
        JLabel[][] labelGrid; //<- contains the labels for the marblegrid
        JLabel labelSlide; //<- contains the label for the slidemarble
        private Image image;

        public GoToMarket_Board_Card_Panel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9),1));

            this.labelGrid = new JLabel[3][4];
            this.labelSlide = new JLabel();

            try {
                image = ImageIO.read(new File("resources/images/market2.png"));
            } catch (IOException e) {}

            addPadding(this, 599,946,100,100);

            back_GoToMarket_Card_Button = new JButton("Back");
            back_GoToMarket_Card_Button.setPreferredSize(new Dimension(120, 40));
            back_GoToMarket_Card_Button.setFont(new Font(PAP, Font.BOLD, 20));
            back_GoToMarket_Card_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 5.00;
            c.weighty = 15.00;
            c.insets = new Insets(5,5,0,0);
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            this.add(back_GoToMarket_Card_Button,c);

            ImageIcon slideMarble = scaleImage(new ImageIcon("resources/punchboard/purple_marble.png"), 64);
            labelSlide.setIcon(slideMarble);
            c = new GridBagConstraints();
            c.gridx = 5;
            c.gridy = 2;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 1.01;
            c.weighty = 1.01;
            this.add(labelSlide, c);

            JLabel label1 = new JLabel();
            c = new GridBagConstraints();
            c.gridx = 50;
            c.gridy = 50;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 30.51;
            c.weighty = 30.51;
            this.add(label1,c);

            for(int row = 0; row < 4; row++)
            {
                for(int col = 0; col < 5; col++)
                {
                    if(col == 4 && row != 3) {
                        RowOrColumn_Button rc = new RowOrColumn_Button(row, false, "" + (row+1));
                        rc.addActionListener(getRowOrColumn_ActionListener);
                        rc.setPreferredSize(new Dimension(47, 47));
                        rc.setFont(new Font(PAP, Font.BOLD, 20));
                        rc.setBackground(new Color(231, 210, 181));
                        c = new GridBagConstraints();
                        c.gridx = col + 2;
                        c.gridy = row + 3;
                        c.weightx = 0.4;
                        c.weighty = 1.5;
                        this.add(rc, c);
                    } else if(row == 3 && col != 4) {
                        RowOrColumn_Button rc = new RowOrColumn_Button(col, true, "" + (col+1));
                        rc.addActionListener(getRowOrColumn_ActionListener);
                        rc.setPreferredSize(new Dimension(47, 47));
                        rc.setFont(new Font(PAP, Font.BOLD, 20));
                        rc.setBackground(new Color(231, 210, 181));
                        c = new GridBagConstraints();
                        c.gridx = col + 2;
                        c.gridy = row + 3;
                        c.weightx = 0.4;
                        c.weighty = 1.5;
                        this.add(rc, c);
                   } else if(row < 3 && col < 4){
                        JLabel label = new JLabel();
                        c = new GridBagConstraints();
                        c.gridx = col + 2;
                        c.gridy = row + 3;
                        c.weightx = 0.4;
                        c.weighty = 1.5;
                        this.add(label, c);
                        this.labelGrid[row][col] = label;
                   }
                }
            }
        }

        public void update() {
            MarketMarble[][] grid = Ark.game.getMarket().getGrid();
            MarketMarble slide = Ark.game.getMarket().getSlideMarble();

            for(int col = 0; col < 4; col++) {
                for (int row = 0; row < 3; row++) {
                    ImageIcon marbleIcon;
                    marbleIcon = scaleImage(new ImageIcon(grid[row][col].getPath()), 64);
                    labelGrid[row][col].setIcon(marbleIcon);
                }
            }

        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }

    class RowOrColumn_Button extends JButton {
        private final int num;
        private final boolean column;

        public RowOrColumn_Button(int num, boolean column, String title) {
            super(title);
            this.num = num;
            this.column = column;
        }

        public int getNum() {
            return num;
        }

        public boolean getColumn() {
            return column;
        }
    }

    //HELPER METHODS (graphics)
    public static void addPadding(JComponent object, int height, int width, int maxColumns, int maxRows)
    {
        GridBagConstraints c;

        JLabel paddingVertical = new JLabel();
        paddingVertical.setText("");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = 1;
        c.gridheight = maxRows; // <- max rows
        c.insets = new Insets(height,0, 0,0);
        object.add(paddingVertical, c);


        JLabel paddingHorizontal = new JLabel();
        paddingHorizontal.setText("");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.gridwidth = maxColumns; // <- max columns
        c.gridheight = 1;
        c.insets = new Insets(0,width, 0,0);
        object.add(paddingHorizontal, c);
    }

    public ImageIcon scaleImage(ImageIcon icon, int squareDimension)
    {
        return scaleImage(icon, squareDimension, squareDimension);
    }

    public ImageIcon scaleImage(ImageIcon icon, int width, int height)
    {
        int newWidth = icon.getIconWidth();
        int newHeight = icon.getIconHeight();
        if(icon.getIconWidth() > width) {
            newWidth = width;
            newHeight = (newWidth * icon.getIconHeight()) / icon.getIconWidth();
        }
        if(newHeight > height) {
            newHeight = height;
            newWidth = (icon.getIconWidth() * newHeight) / icon.getIconHeight();
        }
        return new ImageIcon(icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH));
    }

    //ACTION LISTENERS
    ActionListener quit_Button_actionListener = e -> {
        notificationsArea.append("\nword");
    };

    ActionListener show_DevDeck_Button_actionListener = e -> {
        cardLayoutRight.show(centralRightPanel, DEVDECK);
        cardLayoutLeft.show(centralLeftPanel,Ark.nickname);
    };

    ActionListener show_Market_Button_actionListener = e -> {
        cardLayoutRight.show(centralRightPanel, MARKET);
        cardLayoutLeft.show(centralLeftPanel,Ark.nickname);
    };

    ActionListener activate_LeaderCards_Button_actionListener = e -> {

    };

    ActionListener change_Depot_Config_Button_actionListener = e -> {
        cardLayoutRight.show(centralRightPanel, CHANGEDEPOT);
    };

    ActionListener get_MarketResource_Button_actionListener = e -> {
        cardLayoutRight.show(centralRightPanel, GOTOMARKET);
        cardLayoutLeft.show(centralLeftPanel, Ark.nickname);
    };

    ActionListener discard_LeaderCard_Button_actionListener = e -> {

    };

    ActionListener buy_DevCard_Button_actionListener = e -> {

    };

    ActionListener activate_Production_Button_actionListener = e -> {
        cardLayoutRight.show(centralRightPanel, Ark.nickname);
    };

    ActionListener endTurn_Button_actionListener = e -> {

    };

    ActionListener back_DevDeck_Card_Button_actionListener = e -> {
        cardLayoutRight.show(centralRightPanel, lastRightCard);
    };

    ActionListener back_Market_Card_Button_actionListener = e -> {
        cardLayoutRight.show(centralRightPanel, lastRightCard);
    };

    ActionListener back_GoToMarket_Card_Button_actionListener = e -> {
        cardLayoutRight.show(centralRightPanel, lastRightCard);
    };

    ActionListener getRowOrColumn_ActionListener = e -> {
        int num;
        boolean column;
        RowOrColumn_Button rc = (RowOrColumn_Button) e.getSource();

        column = rc.getColumn();
        num = rc.getNum();
    };
}
