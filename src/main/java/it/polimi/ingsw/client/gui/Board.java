package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.server.model.enumerators.Resource;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Board implements Runnable {
    CardLayout cl;
    CentralRightPanel centralRightPanel; // <- parent for cardlayout cl

    JFrame mainFrame;
    MainPanel mainPanel;
    Dimension frameDimension;

    //STATIC PANELS VARs
    JButton quit_Button;
    JLabel turnLabel, turnOf;
    JTextArea notificationsArea;
    JLabel leaderCardLabel1, leaderCardLabel2;
    JLabel extraResource1_LeaderCard1_Label, extraResource2_LeaderCard1_Label;
    JLabel extraResource1_LeaderCard2_Label, extraResource2_LeaderCard2_Label;
    JLabel labelUnderLeaderCard1, labelUnderLeaderCard2;



    //CARDS
    static final String BOARD = "Player Board";

    //fonts
    static final String TIMES = "Times New Roman";
    static final String PAP = "Papyrus";

    public static void main(String[] args) {
        //you can write here shortcuts, like going directly to the Settings and opening multiple frames
        //only if called by the main, otherwise it must be empty
        //new MainMenu();
        // cl.show(cardPanel, CREATE);

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
            this.add(new CentralLeftPanel(), c);

            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            centralRightPanel = new CentralRightPanel();
            this.add(centralRightPanel, c);

            quit_Button.addActionListener(quit_Button_actionListener);



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
            this.setBorder(BorderFactory.createMatteBorder(1,1,0,1,new Color(62, 43, 9)));

            JLabel paddingVertical = new JLabel();
            paddingVertical.setText("");
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 1;
            c.gridheight = 5; // <- max rows
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
            notificationsArea.setText("yolo");
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
            c.insets = new Insets(0,0, 0,0);
            leaderCardsPanel.add(leaderCardLabel1text,c);

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
            c.insets = new Insets(0,0, 0,0);
            leaderCardsPanel.add(leaderCardLabel1text1,c);

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
            c.insets = new Insets(0,0, 0,0);
            leaderCardsPanel.add(leaderCardLabel2text,c);

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
            c.insets = new Insets(0,0, 0,0);
            leaderCardsPanel.add(leaderCardLabel2text2,c);


            leaderCardLabel1 = new JLabel();
            leaderCardLabel1.setLayout(new GridBagLayout());
            leaderCardLabel1.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
            ImageIcon t1 = new ImageIcon("resources/cardsFront/LFRONT (7).png");
            t1 = scaleImage(t1, 300,300);
            leaderCardLabel1.setIcon(t1);
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 2;
            c.weighty = 0.1;
            c.weightx = 0.5;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(0,0, 0,0);
            leaderCardsPanel.add(leaderCardLabel1,c);

            extraResource1_LeaderCard1_Label = new JLabel();
            ImageIcon tr1 = new ImageIcon(Resource.SHIELD.getPathLittle());
            tr1 = scaleImage(tr1, 50, 50);
            extraResource1_LeaderCard1_Label.setIcon(tr1);
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.insets = new Insets(210,5, 0,11);
            leaderCardLabel1.add(extraResource1_LeaderCard1_Label,c);

            extraResource2_LeaderCard1_Label = new JLabel();
            ImageIcon tr2 = new ImageIcon(Resource.COIN.getPathLittle());
            tr2 = scaleImage(tr1, 50, 50);
            extraResource2_LeaderCard1_Label.setIcon(tr2);
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 0;
            c.anchor = GridBagConstraints.FIRST_LINE_END;
            c.insets = new Insets(210,11, 0,5);
            leaderCardLabel1.add(extraResource2_LeaderCard1_Label,c);

            labelUnderLeaderCard1 = new JLabel("not picked");
            labelUnderLeaderCard1.setOpaque(false);
            labelUnderLeaderCard1.setHorizontalAlignment(SwingConstants.CENTER);
            labelUnderLeaderCard1.setFont(new Font(PAP, Font.BOLD, 22));
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 3;
            c.anchor = GridBagConstraints.PAGE_START;
            leaderCardsPanel.add(labelUnderLeaderCard1,c);

            labelUnderLeaderCard2 = new JLabel("not picked");
            labelUnderLeaderCard2.setOpaque(false);
            labelUnderLeaderCard2.setHorizontalAlignment(SwingConstants.CENTER);
            labelUnderLeaderCard2.setFont(new Font(PAP, Font.BOLD, 22));
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 3;
            c.anchor = GridBagConstraints.PAGE_START;
            leaderCardsPanel.add(labelUnderLeaderCard2,c);

            leaderCardLabel2 = new JLabel();
            leaderCardLabel2.setLayout(new GridBagLayout());
            leaderCardLabel2.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
            ImageIcon t2 = new ImageIcon("resources/cardsFront/LFRONT (12).png");
            t2 = scaleImage(t2, 300, 300);
            leaderCardLabel2.setIcon(t2);
            c = new GridBagConstraints();
            c.gridx = 2;
            c.gridy = 2;
            c.weighty = 0.1;
            c.weightx = 0.5;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(0,0, 0,0);
            leaderCardsPanel.add(leaderCardLabel2,c);

            extraResource1_LeaderCard2_Label = new JLabel();
            ImageIcon tr3 = new ImageIcon(Resource.SHIELD.getPathLittle());
            tr3 = scaleImage(tr3, 50, 50);
            //extraResource1_LeaderCard2_Label.setIcon(tr3);
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            c.insets = new Insets(210,5, 0,11);
            leaderCardLabel2.add(extraResource1_LeaderCard2_Label,c);

            extraResource2_LeaderCard2_Label = new JLabel();
            ImageIcon tr4 = new ImageIcon(Resource.COIN.getPathLittle());
            tr4 = scaleImage(tr4, 50, 50);
            //extraResource2_LeaderCard2_Label.setIcon(tr4);
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 0;
            c.anchor = GridBagConstraints.FIRST_LINE_END;
            c.insets = new Insets(210,11, 0,5);
            leaderCardLabel2.add(extraResource2_LeaderCard2_Label,c);

            JLabel spacer = new JLabel("");
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 0;
            c.weighty = 0.1;
            c.weightx = 0.5;
            c.gridheight=3;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(0,10, 0,10);
            leaderCardsPanel.add(spacer,c);

            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 4;
            c.weighty = 0.1;
            c.gridwidth = 2;
            c.gridheight = 1;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(10,0, 0,0);
            this.add(leaderCardsPanel, c);


        }
    }

    class BottomPanel extends JPanel {
        public BottomPanel() {
            this.setLayout(new GridBagLayout());
            this.setOpaque(false);
            this.setBackground(new Color(215, 200, 145));
            this.setBorder(BorderFactory.createLineBorder(new Color(62, 43, 9),1));
            JLabel el = new JLabel();
            el.setText("");
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(160,1680, 0,0);
            c.insets = new Insets(158,1678, 0,0);
            this.add(el, c);
        }
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

            this.setLayout(new GridBagLayout());
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

    class CentralRightPanel extends JPanel {
        public CentralRightPanel() {
            cl = new CardLayout();
            this.setLayout(cl);
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

    //ACTIONLISTENERS
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
        t = new ImageIcon("resources/cardsBack/BACK (1).png");
        t = scaleImage(t, 300, 300);
        leaderCardLabel2.setIcon(t);
        leaderCardLabel2.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
    };
}
