package it.polimi.ingsw.client.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MainMenu implements Runnable {
    CardLayout cl;
    JFrame mainFrame;
    JPanel cardPanel;
    Dimension frameDimension;

    //shared objects
    JLabel top_Update_Label;
    JLabel bottom_Update_Label;
    //card 1 (menu card)
    JButton online_Menu_Button;
    JButton local_Menu_Button;
    JButton settings_Menu_Button;
    JButton quit_Menu_Button;
    //card 2 (settings card)
    JTextField ipAddress_Settings_Field;
    JTextField port_Settings_Field;
    JTextField nickname_Settings_Field;
    JButton apply_Settings_Button;
    JButton back_Settings_Button;
    //card 3 (online game card)
    JButton create_Online_Button;
    JButton join_Online_Button;
    JButton rejoin_Online_Button;
    JButton back_Online_Button;
    //card 4 (create card)
    JButton confirm_Create_Button;
    JButton settings_Create_Button;
    JButton back_Create_Button;
    JLabel recapLabel1;
    JLabel recapLabel2;
    JLabel recapLabel3;
    JSlider numberOfPlayersSlider;
    //card 5 (join card)
    JButton confirm_Join_Button;
    JButton settings_Join_Button;
    JButton back_Join_Button;
    JLabel recapLabel4;
    JLabel recapLabel5;
    JLabel recapLabel6;
    JTextField lobbyNumberField;


    private String lastCard = MENU;

    static final String MENU = "Main Menu";
    static final String SETTINGS = "Settings";
    static final String ONLINE = "Online Game";
    static final String LOCAL = "Local Game";
    static final String CREATE = "Create Lobby";
    static final String JOIN = "Join Lobby";
    static final String REJOIN = "Reconnect to Lobby";

    static final int MIN = 1;
    static final int MAX = 4;
    static final int DEFAULT = 3;

    //fonts
    static final String TIMES = "Times New Roman";
    static final String PAP = "Papyrus";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MainMenu());
    }

    public void run() {
        //you can write here shortcuts, like going directly to the Settings and opening multiple frames
        new MainMenu();
        // cl.show(cardPanel, CREATE);
        //  cl.show(mainFrame.getContentPane(), Settings);
    }

    public MainMenu() {
        mainFrame = new JFrame(" Maestri del Rinascimento! GUI-edition! ");
        mainFrame.setContentPane(new mainPanel());

        //ACTION LISTENERS assignment
        //card 1 (menu)
        online_Menu_Button.addActionListener(online_Menu_Button_actionListener);
        local_Menu_Button.addActionListener(local_Menu_Button_actionListener);
        settings_Menu_Button.addActionListener(Settings_Menu_Button_actionListener);
        quit_Menu_Button.addActionListener(quit_Menu_Button_actionListener);

        //card2 (settings)
        back_Settings_Button.addActionListener(back_Settings_Button_actionListener);
        apply_Settings_Button.addActionListener(apply_Settings_Button_actionListener);

        //card 3 (online)
        create_Online_Button.addActionListener(create_Online_Button_actionListener);
        join_Online_Button.addActionListener(join_Online_Button_actionListener);
        rejoin_Online_Button.addActionListener(rejoin_Online_Button_actionListener);
        back_Online_Button.addActionListener(back_Online_Button_actionListener);

        //card 4 (create)
        confirm_Create_Button.addActionListener(confirm_Create_Button_actionListener);
        settings_Create_Button.addActionListener(settings_Create_Button_actionListener);
        back_Create_Button.addActionListener(back_Create_Button_actionListener);

        //card 5 (join)
        confirm_Join_Button.addActionListener(confirm_Join_Button_actionListener);
        settings_Join_Button.addActionListener(settings_Join_Button_actionListener);
        back_Join_Button.addActionListener(back_Join_Button_actionListener);


        mainFrame.pack();
        //frameDimension = new Dimension(mainFrame.getWidth(), mainFrame.getHeight());
        frameDimension = new Dimension(1290, 980);
        mainFrame.setMinimumSize(frameDimension);
        mainFrame.setResizable(false);
        System.out.println("w: " + frameDimension.getWidth() + "p h:" + frameDimension.getHeight() + "p");

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    //mainFrame.contentPane class, this is the main panel
    class mainPanel extends JPanel {

        private Image image;

        public mainPanel() {
            try {
                image = ImageIO.read(new File("resources/images/main_menu_bg.jpg"));
            } catch (IOException e) {
            }

            cl = new CardLayout();
            GridBagConstraints c;


            this.setLayout(new GridBagLayout());

            // title label
            JLabel titleLabel = new JLabel();
            titleLabel.setFont(new Font(TIMES, Font.BOLD | Font.ITALIC, 26));
            titleLabel.setText("");
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.weighty = 1;
            c.fill = GridBagConstraints.BOTH;
            c.anchor = GridBagConstraints.PAGE_START;
            c.insets = new Insets(0, 640, 330, 640);
            this.add(titleLabel, c);

            top_Update_Label = new JLabel();
            top_Update_Label.setFont(new Font("Papyrus", Font.BOLD | Font.ITALIC, 36));
            top_Update_Label.setText(" Welcome " + Ark.nickname + " ");
            top_Update_Label.setHorizontalAlignment(SwingConstants.CENTER);
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 1;
            c.weighty = 0.2;
            c.insets = new Insets(0, 50, 40, 50);
            this.add(top_Update_Label, c);


            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 2;
            c.weighty = 0.8;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(0, 10, 0, 10);
            this.add(getCardMagic(), c);


            //updating label
            bottom_Update_Label = new JLabel();
            bottom_Update_Label.setFont(new Font("Papyrus", Font.BOLD, 24));
            bottom_Update_Label.setHorizontalAlignment(SwingConstants.CENTER);
            bottom_Update_Label.setText(" ");
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 3;
            c.weighty = 0.2;
            c.insets = new Insets(25, 0, 0, 0);
            this.add(bottom_Update_Label, c);

            //bottom label
            JLabel bottomMenuLabel = new JLabel();
            bottomMenuLabel.setFont(new Font(TIMES, Font.BOLD, 30));
            bottomMenuLabel.setForeground(Color.WHITE);
            bottomMenuLabel.setText("GC31");
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 4;
            c.weighty = 0.2;
            c.anchor = GridBagConstraints.LAST_LINE_END;
            c.insets = new Insets(115, 0, 20, 15);
            this.add(bottomMenuLabel, c);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }

    private JPanel getCardMagic() {
        cardPanel = new JPanel();
        cardPanel.setLayout(cl);

        cardPanel.add(getCard1(), MENU);

        cardPanel.add(getCard2(), SETTINGS);

        cardPanel.add(getCard3(), ONLINE);

        cardPanel.add(getCard4(), CREATE);

        cardPanel.add(getCard5(), JOIN);

        cardPanel.setOpaque(false);
        return cardPanel;
    }

    private JPanel getCard1() {
        GridBagConstraints c;
        /*
            card 1: Main Menu things.

         */
        JPanel card1 = new JPanel(new GridBagLayout());
        card1.setOpaque(false);

        online_Menu_Button = new JButton(ONLINE);
        online_Menu_Button.setPreferredSize(new Dimension(200, 50));
        online_Menu_Button.setFont(new Font(PAP, Font.BOLD, 20));
        online_Menu_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.01;
        c.gridwidth = 2;
        c.insets = new Insets(0, 0, 5, 0);
        card1.add(online_Menu_Button, c);

        local_Menu_Button = new JButton(LOCAL);
        local_Menu_Button.setPreferredSize(new Dimension(200, 50));
        local_Menu_Button.setFont(new Font(PAP, Font.BOLD, 20));
        local_Menu_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0.01;
        c.gridwidth = 2;
        c.insets = new Insets(5, 0, 5, 0);
        card1.add(local_Menu_Button, c);

        JLabel emptyLabel = new JLabel();
        emptyLabel.setText("");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 160, 0, 160);
        c.gridwidth = 2;
        c.weighty = 1;
        card1.add(emptyLabel, c);

        quit_Menu_Button = new JButton("Quit");
        quit_Menu_Button.setPreferredSize(new Dimension(120, 40));
        quit_Menu_Button.setFont(new Font(PAP, Font.BOLD, 20));
        quit_Menu_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.weighty = 0.01;
        c.insets = new Insets(100, 0, 0, 20);
        c.anchor = GridBagConstraints.LAST_LINE_START;
        card1.add(quit_Menu_Button, c);

        settings_Menu_Button = new JButton(SETTINGS);
        settings_Menu_Button.setPreferredSize(new Dimension(120, 40));
        settings_Menu_Button.setFont(new Font(PAP, Font.BOLD, 20));
        settings_Menu_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.weighty = 0.01;
        c.insets = new Insets(100, 20, 0, 0);
        c.anchor = GridBagConstraints.LAST_LINE_END;
        card1.add(settings_Menu_Button, c);

        card1.setOpaque(false);
        return card1;
    }

    private JPanel getCard2() {
        GridBagConstraints c;

        /*
            card 2: Settings

         */
        JPanel card2 = new JPanel(new GridBagLayout());
        card2.setOpaque(false);

        JLabel ipLabel = new JLabel();
        ipLabel.setText("current IP is:");
        ipLabel.setFont(new Font(TIMES, Font.PLAIN, 20));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weighty = 0.3;
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(0, 15, 5, 3);
        card2.add(ipLabel, c);

        ipAddress_Settings_Field = new JTextField(10);
        ipAddress_Settings_Field.setText(Ark.defaultAddress);
        ipAddress_Settings_Field.setHorizontalAlignment(SwingConstants.CENTER);
        ipAddress_Settings_Field.setFont(new Font(TIMES, Font.PLAIN, 20));
        ipAddress_Settings_Field.setOpaque(false);
        ipAddress_Settings_Field.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weighty = 0.3;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 3, 5, 15);
        card2.add(ipAddress_Settings_Field, c);

        JLabel portLabel = new JLabel();
        portLabel.setText("current port is:");
        portLabel.setFont(new Font(TIMES, Font.PLAIN, 20));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weighty = 0.3;
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(0, 15, 5, 3);
        card2.add(portLabel, c);

        port_Settings_Field = new JTextField(10);
        port_Settings_Field.setText("" + Ark.defaultPort);
        port_Settings_Field.setHorizontalAlignment(SwingConstants.CENTER);
        port_Settings_Field.setFont(new Font(TIMES, Font.PLAIN, 20));
        port_Settings_Field.setOpaque(false);
        port_Settings_Field.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weighty = 0.3;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 3, 5, 15);
        card2.add(port_Settings_Field, c);

        JLabel nameLabel = new JLabel();
        nameLabel.setText("current name is:");
        nameLabel.setFont(new Font(TIMES, Font.PLAIN, 20));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.weighty = 0.3;
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(0, 15, 5, 3);
        card2.add(nameLabel, c);

        nickname_Settings_Field = new JTextField(10);
        nickname_Settings_Field.setText("" + Ark.nickname);
        nickname_Settings_Field.setHorizontalAlignment(SwingConstants.CENTER);
        nickname_Settings_Field.setFont(new Font(TIMES, Font.PLAIN, 20));
        nickname_Settings_Field.setOpaque(false);
        nickname_Settings_Field.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        c.weighty = 0.3;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 3, 5, 15);
        card2.add(nickname_Settings_Field, c);

        JLabel emptyLabel = new JLabel();
        emptyLabel.setText("");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 160, 0, 160);
        c.gridwidth = 2;
        c.weighty = 1;
        card2.add(emptyLabel, c);

        back_Settings_Button = new JButton("Back");
        back_Settings_Button.setPreferredSize(new Dimension(120, 40));
        back_Settings_Button.setFont(new Font(PAP, Font.BOLD, 20));
        back_Settings_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.weighty = 0.01;
        c.gridwidth = 1;
        c.insets = new Insets(100, 0, 0, 20);
        c.anchor = GridBagConstraints.LAST_LINE_START;
        card2.add(back_Settings_Button, c);

        apply_Settings_Button = new JButton("Apply");
        apply_Settings_Button.setPreferredSize(new Dimension(120, 40));
        apply_Settings_Button.setFont(new Font(PAP, Font.BOLD, 20));
        apply_Settings_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 4;
        c.weighty = 0.01;
        c.gridwidth = 1;
        c.insets = new Insets(100, 20, 0, 0);
        c.anchor = GridBagConstraints.LAST_LINE_END;
        card2.add(apply_Settings_Button, c);

        card2.setOpaque(false);
        return card2;
    }

    private JPanel getCard3() {
        GridBagConstraints c;
        /*
            card 3: online game

         */

        JPanel card3 = new JPanel(new GridBagLayout());
        card3.setOpaque(false);

        create_Online_Button = new JButton("Create a new Lobby");
        create_Online_Button.setPreferredSize(new Dimension(250, 50));
        create_Online_Button.setFont(new Font(PAP, Font.BOLD, 20));
        create_Online_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.3;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(0, 0, 0, 0);
        card3.add(create_Online_Button, c);

        join_Online_Button = new JButton("Join an existing Lobby");
        join_Online_Button.setPreferredSize(new Dimension(250, 50));
        join_Online_Button.setFont(new Font(PAP, Font.BOLD, 20));
        join_Online_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0.3;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(5, 0, 0, 0);
        card3.add(join_Online_Button, c);

        rejoin_Online_Button = new JButton("Reconnect to a game");
        rejoin_Online_Button.setPreferredSize(new Dimension(250, 50));
        rejoin_Online_Button.setFont(new Font(PAP, Font.BOLD, 20));
        rejoin_Online_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 0.3;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(5, 0, 0, 0);
        card3.add(rejoin_Online_Button, c);

        JLabel emptyLabel = new JLabel();
        emptyLabel.setText("");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 160, 0, 160);
        c.weighty = 1;
        card3.add(emptyLabel, c);

        back_Online_Button = new JButton("Back");
        back_Online_Button.setPreferredSize(new Dimension(120, 40));
        back_Online_Button.setFont(new Font(PAP, Font.BOLD, 20));
        back_Online_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.weighty = 0.01;
        c.insets = new Insets(100, 0, 0, 180);
        c.anchor = GridBagConstraints.LAST_LINE_START;
        card3.add(back_Online_Button, c);

        card3.setOpaque(false);
        return card3;
    }

    private JPanel getCard4() {
        GridBagConstraints c;
        /*
            card 4 create
         */

        JPanel card4 = new JPanel(new GridBagLayout());
        card4.setOpaque(false);

        JLabel label = new JLabel();
        label.setText(" Continue with these settings? ");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font(PAP, Font.BOLD, 20));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.1;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.PAGE_END;
        card4.add(label, c);

        JLabel nameLabel = new JLabel();
        nameLabel.setText("name:");
        nameLabel.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.LINE_END;
        card4.add(nameLabel, c);

        recapLabel1 = new JLabel();
        recapLabel1.setText(Ark.nickname);
        recapLabel1.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        card4.add(recapLabel1, c);

        JLabel addressLabel = new JLabel();
        addressLabel.setText("ip:");
        addressLabel.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.LINE_END;
        card4.add(addressLabel, c);

        recapLabel2 = new JLabel();
        recapLabel2.setText(Ark.defaultAddress);
        recapLabel2.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        card4.add(recapLabel2, c);

        JLabel portLabel = new JLabel();
        portLabel.setText("port:");
        portLabel.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.LINE_END;
        card4.add(portLabel, c);

        recapLabel3 = new JLabel();
        recapLabel3.setText("" + Ark.defaultPort);
        recapLabel3.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        card4.add(recapLabel3, c);

        JLabel numberOfPlayersLabel = new JLabel();
        numberOfPlayersLabel.setText("number of players:");
        numberOfPlayersLabel.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.weighty = 0.1;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        card4.add(numberOfPlayersLabel, c);

        numberOfPlayersSlider = new JSlider(JSlider.HORIZONTAL, MIN, MAX, DEFAULT);
        numberOfPlayersSlider.setPreferredSize(new Dimension(120, 70));
        numberOfPlayersSlider.setOpaque(false);
        numberOfPlayersSlider.setFont(new Font(TIMES, Font.PLAIN, 18));
        numberOfPlayersSlider.setMajorTickSpacing(1);
        numberOfPlayersSlider.setMinorTickSpacing(1);
        numberOfPlayersSlider.setPaintTicks(true);
        numberOfPlayersSlider.setPaintLabels(true);
        numberOfPlayersSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 1;
        c.gridy = 4;
        c.weighty = 0.1;
        c.gridwidth = 1;
        card4.add(numberOfPlayersSlider, c);

        settings_Create_Button = new JButton("Settings");
        settings_Create_Button.setPreferredSize(new Dimension(120, 40));
        settings_Create_Button.setFont(new Font(PAP, Font.BOLD, 20));
        settings_Create_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 5;
        c.weighty = 0.01;
        c.gridwidth = 1;
        c.insets = new Insets(15, 25, 0, 0);
        c.anchor = GridBagConstraints.LINE_START;
        card4.add(settings_Create_Button, c);

        confirm_Create_Button = new JButton("Confirm");
        confirm_Create_Button.setPreferredSize(new Dimension(120, 40));
        confirm_Create_Button.setFont(new Font(PAP, Font.BOLD, 20));
        confirm_Create_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 5;
        c.weighty = 0.01;
        c.gridwidth = 1;
        c.insets = new Insets(15, 0, 0, 25);
        c.anchor = GridBagConstraints.LINE_END;
        card4.add(confirm_Create_Button, c);


        JLabel emptyLabel = new JLabel("");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 6;
        c.weighty = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0, 160, 0, 160);
        card4.add(emptyLabel, c);

        back_Create_Button = new JButton("Back");
        back_Create_Button.setPreferredSize(new Dimension(120, 40));
        back_Create_Button.setFont(new Font(PAP, Font.BOLD, 20));
        back_Create_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 7;
        c.weighty = 0.1;
        c.insets = new Insets(4, 0, 0, 0);
        c.anchor = GridBagConstraints.LAST_LINE_START;
        card4.add(back_Create_Button, c);


        card4.setOpaque(false);
        return card4;
    }

    private JPanel getCard5() {
        GridBagConstraints c;
         /*
            card 5: join

         */
        JPanel card5 = new JPanel(new GridBagLayout());
        card5.setOpaque(false);

        JLabel label = new JLabel();
        label.setText(" Continue with these settings? ");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font(PAP, Font.BOLD, 20));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.1;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.PAGE_END;
        card5.add(label, c);

        JLabel nameLabel = new JLabel();
        nameLabel.setText("name:");
        nameLabel.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.LINE_END;
        card5.add(nameLabel, c);


        recapLabel4 = new JLabel();
        recapLabel4.setText(Ark.nickname);
        recapLabel4.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        card5.add(recapLabel4, c);

        JLabel addressLabel = new JLabel();
        addressLabel.setText("ip:");
        addressLabel.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.LINE_END;
        card5.add(addressLabel, c);

        recapLabel5 = new JLabel();
        recapLabel5.setText(Ark.defaultAddress);
        recapLabel5.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        card5.add(recapLabel5, c);

        JLabel portLabel = new JLabel();
        portLabel.setText("port:");
        portLabel.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.LINE_END;
        card5.add(portLabel, c);

        recapLabel6 = new JLabel();
        recapLabel6.setText("" + Ark.defaultPort);
        recapLabel6.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        card5.add(recapLabel6, c);

        JLabel lobbyNumberLabel = new JLabel();
        lobbyNumberLabel.setText("Lobby number:");
        lobbyNumberLabel.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.weighty = 0.1;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        card5.add(lobbyNumberLabel, c);

        lobbyNumberField = new JTextField(4);
        lobbyNumberField.setText("343");
        lobbyNumberField.setFont(new Font(TIMES, Font.PLAIN, 20));
        lobbyNumberField.setHorizontalAlignment(SwingConstants.CENTER);
        lobbyNumberField.setOpaque(false);
        lobbyNumberField.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        c = new GridBagConstraints();
         c.insets = new Insets(0, 0, 35, 0);
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 1;
        c.gridy = 4;
        c.weighty = 0.1;
        c.gridwidth = 1;
        card5.add(lobbyNumberField, c);

        settings_Join_Button = new JButton("Settings");
        settings_Join_Button.setPreferredSize(new Dimension(120, 40));
        settings_Join_Button.setFont(new Font(PAP, Font.BOLD, 20));
        settings_Join_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 5;
        c.weighty = 0.01;
        c.gridwidth = 1;
        c.insets = new Insets(15, 25, 0, 0);
        c.anchor = GridBagConstraints.LINE_START;
        card5.add(settings_Join_Button, c);

        confirm_Join_Button = new JButton("Confirm");
        confirm_Join_Button.setPreferredSize(new Dimension(120, 40));
        confirm_Join_Button.setFont(new Font(PAP, Font.BOLD, 20));
        confirm_Join_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 5;
        c.weighty = 0.01;
        c.gridwidth = 1;
        c.insets = new Insets(15, 0, 0, 25);
        c.anchor = GridBagConstraints.LINE_END;
        card5.add(confirm_Join_Button, c);


        JLabel emptyLabel = new JLabel("");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 6;
        c.weighty = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0, 160, 0, 160);
        card5.add(emptyLabel, c);

        back_Join_Button = new JButton("Back");
        back_Join_Button.setPreferredSize(new Dimension(120, 40));
        back_Join_Button.setFont(new Font(PAP, Font.BOLD, 20));
        back_Join_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 7;
        c.weighty = 0.1;
        c.insets = new Insets(4, 0, 0, 0);
        c.anchor = GridBagConstraints.LAST_LINE_START;
        card5.add(back_Join_Button, c);

        return card5;
    }

    //HELPER METHODS (graphics)
    private void updateCreateRecapLabel() {
        recapLabel1.setText(Ark.nickname);
        recapLabel2.setText(Ark.defaultAddress);
        recapLabel3.setText("" + Ark.defaultPort);

        recapLabel4.setText(Ark.nickname);
        recapLabel5.setText(Ark.defaultAddress);
        recapLabel6.setText("" + Ark.defaultPort);
    }

    //HELPER METHODS (non graphic)
    private static boolean isValidInet4Address(String ip) {
        String[] groups = ip.split("\\.");

        if (groups.length != 4) {
            return false;
        }

        try {
            return Arrays.stream(groups)
                    .filter(s -> s.length() > 1)
                    .map(Integer::parseInt)
                    .filter(i -> (i >= 0 && i <= 255))
                    .count() == 4;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    //ACTION LISTENERS
    //card 1 (menu)
    ActionListener online_Menu_Button_actionListener = e -> {
        cl.show(cardPanel, ONLINE);
        lastCard = MENU;
        top_Update_Label.setText(ONLINE);
        bottom_Update_Label.setText("Lorenzo, Lorenzo");
    };

    ActionListener local_Menu_Button_actionListener = e -> {

        //load directly the game?
        lastCard = MENU;
    };

    ActionListener Settings_Menu_Button_actionListener = e -> {
        cl.show(cardPanel, SETTINGS);
        lastCard = MENU;
        top_Update_Label.setText(SETTINGS);
    };

    ActionListener quit_Menu_Button_actionListener = e -> {
        mainFrame.dispose();
    };

    //card2 (settings)
    ActionListener back_Settings_Button_actionListener = e -> {
        if (lastCard.equals(MENU))
            top_Update_Label.setText(" welcome " + Ark.nickname + " ");
        else
            top_Update_Label.setText(lastCard);
        cl.show(cardPanel, lastCard);
    };

    ActionListener apply_Settings_Button_actionListener = e -> {
        boolean error = false;
        int newPort = 0;
        String newNickname = "";
        if (isValidInet4Address(ipAddress_Settings_Field.getText()) || Ark.defaultAddress.equals(ipAddress_Settings_Field.getText())) {
            try {
                newPort = Integer.parseInt(port_Settings_Field.getText());
                if (newPort < 1024 || newPort > 65535) {
                    error = true;
                } else {
                    newNickname = nickname_Settings_Field.getText();
                    if (newNickname.length() == 0)
                        error = true;
                }
            } catch (NumberFormatException ex) {
                error = true;
            }
        } else
            error = true;

        if (error)
            JOptionPane.showMessageDialog(mainFrame, "Please enter valid values");
        else {
            Ark.defaultAddress = ipAddress_Settings_Field.getText();
            Ark.defaultPort = newPort;
            Ark.nickname = newNickname;

            updateCreateRecapLabel();

            bottom_Update_Label.setText("changes were saved!");
            if (lastCard.equals(MENU))
                top_Update_Label.setText(" welcome " + Ark.nickname + " ");
            else
                top_Update_Label.setText(lastCard);
            cl.show(cardPanel, lastCard);
        }
    };

    //card 3 (online)
    ActionListener create_Online_Button_actionListener = e -> {
        cl.show(cardPanel, CREATE);
        lastCard = ONLINE;
        top_Update_Label.setText(CREATE);
        bottom_Update_Label.setText("Ok?");
    };

    ActionListener join_Online_Button_actionListener = e -> {
        cl.show(cardPanel, JOIN);
        lastCard = ONLINE;
        top_Update_Label.setText(JOIN);
        bottom_Update_Label.setText("Ready!");
    };

    ActionListener rejoin_Online_Button_actionListener = e -> {
        cl.show(cardPanel, REJOIN);
        lastCard = ONLINE;
        top_Update_Label.setText(REJOIN);
        bottom_Update_Label.setText("Oh no, you crashed?");
    };

    ActionListener back_Online_Button_actionListener = e -> {
        cl.show(cardPanel, MENU);
        lastCard = ONLINE;
        top_Update_Label.setText(" welcome " + Ark.nickname + " ");
        bottom_Update_Label.setText(" Please don't judge me ");
    };

    //card 4 (create)
    ActionListener confirm_Create_Button_actionListener = e -> {

    };

    ActionListener settings_Create_Button_actionListener = e -> {
        cl.show(cardPanel, SETTINGS);
        lastCard = CREATE;
        top_Update_Label.setText(SETTINGS);
        bottom_Update_Label.setText(" Here we go again ");
    };

    ActionListener back_Create_Button_actionListener = e -> {
        cl.show(cardPanel, ONLINE);
        lastCard = CREATE;
        top_Update_Label.setText(ONLINE);
        bottom_Update_Label.setText("Ok");
    };

    //card 5 (join)
    ActionListener confirm_Join_Button_actionListener = e -> {

    };

    ActionListener settings_Join_Button_actionListener = e -> {
        cl.show(cardPanel, SETTINGS);
        lastCard = JOIN;
        top_Update_Label.setText(SETTINGS);
        bottom_Update_Label.setText(" Is this Lorenzo? ");
    };

    ActionListener back_Join_Button_actionListener = e -> {
        cl.show(cardPanel, ONLINE);
        lastCard = JOIN;
        top_Update_Label.setText(ONLINE);
        bottom_Update_Label.setText("Roger that");
    };
}