package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.modelSimplified.GameSimplified;
import it.polimi.ingsw.client.modelSimplified.PlayerSimplified;
import it.polimi.ingsw.networking.message.actionMessages.MSG_ACTION_DISCARD_LEADERCARD;
import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_Full;
import it.polimi.ingsw.server.controller.ActionManager;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.model.requirements.CardRequirements;
import it.polimi.ingsw.server.model.requirements.ReqValue;
import it.polimi.ingsw.server.model.requirements.ResourceRequirements;
import it.polimi.ingsw.server.model.specialAbilities.DiscountResource;
import it.polimi.ingsw.server.model.specialAbilities.ExtraDepot;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Map;

public class Board implements Runnable {
    CardLayout cardLayoutRight;
    CardLayout cardLayoutLeft;
    CentralRightPanel centralRightPanel; // <- parent for cardlayout right
    CentralLeftPanel centralLeftPanel; // <- parent for cardlayout left

    JFrame mainFrame;
    MainPanel mainPanel;
    Dimension frameDimension;

    //STATIC PANELS VARs
    //LEFT PANEL
    JButton quit_Button;
    JLabel turnLabel, turnOf;
    JTextArea notificationsArea;
    JLabel leaderCardLabel1, leaderCardLabel2;
    JLabel extraResource1_LeaderCard1_Label, extraResource2_LeaderCard1_Label;
    JLabel extraResource1_LeaderCard2_Label, extraResource2_LeaderCard2_Label;
    JLabel labelUnderLeaderCard1, labelUnderLeaderCard2;
    JButton show_DevDeck_Button;
    JButton show_Market_Button;
    //BOTTOM PANEL
    JButton activate_LeaderCards_Button, change_Depot_Config_Button, get_MarketResource_Button;
    JButton discard_LeaderCard_Button, buy_DevCard_Button, activate_Production_Button;
    PlayersRecapPanel playersRecapPanel;
    JButton endTurn_Button;



    //CARDS
    static final String BOARD = "Player Board";

    //fonts
    static final String TIMES = "Times New Roman";
    static final String PAP = "Papyrus";

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
        D.associateLeaderCards(list);

        B.getLeaderCards()[0].setEnabled(true);
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

        Ark.game = new GameSimplified();
        MSG_UPD_Full message = Ark.gameManager.getFullModel();
        Ark.game.updateAll(message);
        Ark.myPlayerRef = Ark.game.getPlayerRef(1);
        SwingUtilities.invokeLater(new Board());
    }

    public void run() {

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
                activate_LeaderCards_Button.addActionListener(activate_LeaderCards_Button_actionListener);
                change_Depot_Config_Button.addActionListener(change_Depot_Config_Button_actionListener);
                get_MarketResource_Button.addActionListener(get_MarketResource_Button_actionListener);
                discard_LeaderCard_Button.addActionListener(discard_LeaderCard_Button_actionListener);
                buy_DevCard_Button.addActionListener(buy_DevCard_Button_actionListener);
                activate_Production_Button.addActionListener(activate_Production_Button_actionListener);
                endTurn_Button.addActionListener(endTurn_Button_actionListener);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }

    }

    class LeftPanel extends JPanel {
        public LeftPanel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBackground(new Color(215, 200, 145));
            this.setBorder(BorderFactory.createMatteBorder(1,1,0,1, new Color(62, 43, 9)));

            JLabel paddingVertical = new JLabel();
            paddingVertical.setText("");
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 1;
            c.gridheight = 6; // <- max rows
            c.insets = new Insets(840,0, 0,0);
            c.insets = new Insets(839,0, 0,0);
            this.add(paddingVertical, c);


            JLabel paddingHorizontal = new JLabel();
            paddingHorizontal.setText("");
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 3; // <- max columns
            c.gridheight = 1;
            c.insets = new Insets(0,459, 0,0);
            c.insets = new Insets(0,457, 0,0);
            this.add(paddingHorizontal, c);

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
            turnOf.setText("Turn of Pablo");
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

            {
                JPanel leaderCardsPanel = new JPanel(new GridBagLayout());
                leaderCardsPanel.setOpaque(false);

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
                leaderCardsPanel.add(leaderCardLabel1text, c);

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
                leaderCardsPanel.add(leaderCardLabel1text1, c);

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
                leaderCardsPanel.add(leaderCardLabel2text, c);

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
                leaderCardsPanel.add(leaderCardLabel2text2, c);


                leaderCardLabel1 = new JLabel();
                leaderCardLabel1.setLayout(null);
                leaderCardLabel1.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
                ImageIcon t1 = new ImageIcon("resources/cardsBack/BACK (1).png");
                t1 = scaleImage(t1, 300, 300);
                leaderCardLabel1.setIcon(t1);
                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 2;
                c.weighty = 0.1;
                c.weightx = 0.5;
                c.anchor = GridBagConstraints.PAGE_START;
                c.insets = new Insets(0, 0, 0, 0);
                leaderCardsPanel.add(leaderCardLabel1, c);

                extraResource1_LeaderCard1_Label = new JLabel();
                extraResource1_LeaderCard1_Label.setBounds(40, 234, 50, 50);
                leaderCardLabel1.add(extraResource1_LeaderCard1_Label);

                extraResource2_LeaderCard1_Label = new JLabel();
                extraResource2_LeaderCard1_Label.setBounds(115, 234, 50, 50);
                leaderCardLabel1.add(extraResource2_LeaderCard1_Label);

                labelUnderLeaderCard1 = new JLabel("not picked");
                labelUnderLeaderCard1.setOpaque(false);
                labelUnderLeaderCard1.setHorizontalAlignment(SwingConstants.CENTER);
                labelUnderLeaderCard1.setFont(new Font(PAP, Font.BOLD, 22));
                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 3;
                c.anchor = GridBagConstraints.PAGE_START;
                leaderCardsPanel.add(labelUnderLeaderCard1, c);

                leaderCardLabel2 = new JLabel();
                leaderCardLabel2.setLayout(null);
                leaderCardLabel2.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
                ImageIcon t2 = new ImageIcon("resources/cardsBack/BACK (1).png");
                t2 = scaleImage(t2, 300, 300);
                leaderCardLabel2.setIcon(t2);
                c = new GridBagConstraints();
                c.gridx = 2;
                c.gridy = 2;
                c.weighty = 0.1;
                c.weightx = 0.5;
                c.anchor = GridBagConstraints.PAGE_START;
                c.insets = new Insets(0, 0, 0, 0);
                leaderCardsPanel.add(leaderCardLabel2, c);

                extraResource1_LeaderCard2_Label = new JLabel();
                extraResource1_LeaderCard2_Label.setBounds(40, 234, 50, 50);
                leaderCardLabel2.add(extraResource1_LeaderCard2_Label, c);

                extraResource2_LeaderCard2_Label = new JLabel();
                extraResource2_LeaderCard2_Label.setBounds(115, 234, 50, 50);
                leaderCardLabel2.add(extraResource2_LeaderCard2_Label, c);

                labelUnderLeaderCard2 = new JLabel("not picked");
                labelUnderLeaderCard2.setOpaque(false);
                labelUnderLeaderCard2.setHorizontalAlignment(SwingConstants.CENTER);
                labelUnderLeaderCard2.setFont(new Font(PAP, Font.BOLD, 22));
                c = new GridBagConstraints();
                c.gridx = 2;
                c.gridy = 3;
                c.anchor = GridBagConstraints.PAGE_START;
                leaderCardsPanel.add(labelUnderLeaderCard2, c);

                JLabel spacer = new JLabel("");
                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 0;
                c.weighty = 0.1;
                c.weightx = 0.5;
                c.gridheight = 3;
                c.anchor = GridBagConstraints.PAGE_START;
                c.insets = new Insets(0, 10, 0, 10);
                leaderCardsPanel.add(spacer, c);

                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 4;
                c.weighty = 0.1;
                c.gridwidth = 2;
                c.gridheight = 1;
                c.anchor = GridBagConstraints.PAGE_START;
                c.insets = new Insets(10, 0, 0, 0);
                this.add(leaderCardsPanel, c);
            } //leaderCardsPanel

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

    class BottomPanel extends JPanel {
        public BottomPanel() {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBackground(new Color(215, 200, 145));
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9),1));


            JLabel paddingVertical = new JLabel();
            paddingVertical.setText("");
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 1;
            c.gridheight = 3; // <- max rows
            c.insets = new Insets(160,0, 0,0);
            c.insets = new Insets(158,0, 0,0);
            this.add(paddingVertical, c);


            JLabel paddingHorizontal = new JLabel();
            paddingHorizontal.setText("");
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 6; // <- max columns
            c.gridheight = 1;
            c.insets = new Insets(0,1680, 0,0);
            c.insets = new Insets(0,1678, 0,0);
            this.add(paddingHorizontal, c);

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

    class PlayersRecapPanel extends JPanel
    {
        Map<String, java.util.List> map;

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

                    Map<Resource,Integer> resources = player.getResources();

                    JLabel coinLabel = new JLabel("Coins: "+resources.get(Resource.COIN));
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
                    c.insets = new Insets(1,20,1,1);
                    playerPanel.add(coinLabel,c);

                    JLabel shieldLabel = new JLabel("Shields: "+resources.get(Resource.SHIELD));
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
                    c.insets = new Insets(1,20,1,1);
                    playerPanel.add(shieldLabel,c);

                    JLabel stoneLabel = new JLabel("Stones: "+resources.get(Resource.STONE));
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
                    c.insets = new Insets(1,20,1,1);
                    playerPanel.add(stoneLabel,c);

                    JLabel servantLabel = new JLabel("Servants: "+resources.get(Resource.SERVANT));
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
                    c.insets = new Insets(1,20,3,1);
                    playerPanel.add(servantLabel,c);


                    showPlayerButton button = new showPlayerButton("show", player.getNickname());
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

                    map.put(player.getNickname(), labelList);
                    c = new GridBagConstraints();
                    c.fill = GridBagConstraints.BOTH;
                    c.gridx = i;
                    c.weightx = 0.5;
                    c.weighty = 0.5;
                    this.add(playerPanel, c);

                }
            }
        }
    }

    class showPlayerButton extends JButton {
        private final String playerName;
        public showPlayerButton(String text, String playerName)
        {
            super(text);
            this.playerName = playerName;
        }

        public String getPlayerName() { return this.playerName; }
    }

    class TopPanel extends JPanel {
        private Image image;
        public TopPanel()
        {
            try {
                image = ImageIO.read(new File("resources/images/upper_board.png"));
            } catch (IOException e) {}
            this.setLayout(new GridBagLayout());

            JLabel el = new JLabel();
            el.setText("");
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(239,1221, 0,0);
            this.add(el, c);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }

    class CentralLeftPanel extends JPanel {
        private Image image;
        public CentralLeftPanel()
        {
            try {
                image = ImageIO.read(new File("resources/images/left_board.png"));
            } catch (IOException e) {}

            cardLayoutLeft = new CardLayout();
            this.setLayout(cardLayoutLeft);
            this.setLayout(new GridBagLayout()); //delete this


            //java.util.List<String> depotCards = Ark.game.getPlayerSimplifiedList().stream().map(PlayerSimplified::getPlayerNumber).map(x -> ""+x).collect(Collectors.toList());


            JLabel el = new JLabel();
            el.setText("");
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(601,273, 0,0);
            this.add(el, c);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }

    class depotPanel extends JPanel {
        //many labels
        public depotPanel() {
            this.setOpaque(false);
            this.setLayout(new GridBagLayout());


        }
    }

    class CentralRightPanel extends JPanel {
        public CentralRightPanel() {
            cardLayoutRight = new CardLayout();
            this.setLayout(cardLayoutRight);
            this.setOpaque(false);

            Card1_Board card1_board = new Card1_Board();
            this.add(card1_board, BOARD);
        }

    }

    class Card1_Board extends JPanel {

        private Image image;

        public Card1_Board()
        {
            this.setLayout(new GridBagLayout());
            try {
                image = ImageIO.read(new File("resources/images/right_board.png"));
            } catch (IOException e) {}

            JLabel el = new JLabel();
            el.setOpaque(false);
            el.setText("");
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(601,948, 0,0);
            this.add(el,c);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }

    public ImageIcon scaleImage(ImageIcon icon, int w, int h)
    {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();
        if(icon.getIconWidth() > w) {
            nw = w;
            nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }
        if(nh > h) {
            nh = h;
            nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }
        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }

    //ACTION LISTENERS
    ActionListener quit_Button_actionListener = e -> {
        String string;
        string = notificationsArea.getText();
        notificationsArea.setText(string+"\nword");
        ImageIcon t = new ImageIcon(Resource.COIN.getPathLittle());
        t = scaleImage(t, 50, 50);
        extraResource1_LeaderCard1_Label.setIcon(t);
        labelUnderLeaderCard1.setText("ENABLED!");
        leaderCardLabel1.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));

        labelUnderLeaderCard2.setText("DISCARDED!");
        t = new ImageIcon("resources/cardsFront/LFRONT (7).png");
        t = scaleImage(t, 300, 300);
        leaderCardLabel2.setIcon(t);
        leaderCardLabel2.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
    };

    ActionListener show_DevDeck_Button_actionListener = e -> {

    };

    ActionListener show_Market_Button_actionListener = e -> {

    };

    ActionListener activate_LeaderCards_Button_actionListener = e -> {

    };

    ActionListener change_Depot_Config_Button_actionListener = e -> {

    };

    ActionListener get_MarketResource_Button_actionListener = e -> {

    };

    ActionListener discard_LeaderCard_Button_actionListener = e -> {

    };

    ActionListener buy_DevCard_Button_actionListener = e -> {

    };

    ActionListener activate_Production_Button_actionListener = e -> {

    };

    ActionListener endTurn_Button_actionListener = e -> {

    };

}
