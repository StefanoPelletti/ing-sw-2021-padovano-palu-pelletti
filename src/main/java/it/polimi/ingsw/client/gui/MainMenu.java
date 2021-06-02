package it.polimi.ingsw.client.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MainMenu {
    CardLayout cl;
    JFrame mainFrame;
    JPanel cardPanel;
    Dimension frameDimension;

    //shared objects
    JLabel top_Update_Label;
    JLabel bottom_Update_Label;
    //card 1
    JButton online_Menu_Button;
    JButton local_Menu_Button;
    JButton settings_Menu_Button;
    JButton quit_Menu_Button;
    //card 2
    JTextField ipAddress_Settings_Field;
    JTextField port_Settings_Field;
    JTextField nickname_Settings_Field;
    JButton apply_Settings_Button;
    JButton back_Settings_Button;
    //card 3
    JButton create_Online_button;
    JButton join_Online_Button;
    JButton rejoin_Online_Button;
    JButton back_Online_Button;

    static final String MENU = "Main Menu";
    static final String SETTINGS = "Settings";
    static final String ONLINE = "Online Game";
    static final String LOCAL = "Local Game";

    //fonts
    static final String TIMES = "Times New Roman";

    public static void main(String[] args) {
        new MainMenu().run();
    }

    public void run() {
        //you can write here shortcuts, like going directly to the Settings and opening multiple frames

        //new MainMenu();
        //  cl.show(mainFrame.getContentPane(), Settings);
    }

    public MainMenu() {
        mainFrame = new JFrame(" Maestri del Rinascimento! GUI-edition! ");
        mainFrame.setContentPane(new mainPanel());


        back_Settings_Button.addActionListener(back_Settings_Button_actionListener);
        settings_Menu_Button.addActionListener(Settings_Menu_Button_actionListener);
        apply_Settings_Button.addActionListener(apply_Settings_Button_actionListener);
        quit_Menu_Button.addActionListener(quit_Menu_Button_actionListener);
        online_Menu_Button.addActionListener(online_Menu_Button_actionListener);
        back_Online_Button.addActionListener(back_Online_Button_actionListener);

        mainFrame.pack();
        //frameDimension = new Dimension(mainFrame.getWidth(), mainFrame.getHeight());
        frameDimension = new Dimension(1280,962);
        mainFrame.setMinimumSize(frameDimension);
        mainFrame.setResizable(false);
        System.out.println("w: " + frameDimension.getWidth() + "p h:" + frameDimension.getHeight() + "p");

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private JPanel getCardMagic() {
        cardPanel = new JPanel();
        cardPanel.setLayout(cl);

        GridBagConstraints c;

        /*
            card 1: Main Menu things.

         */
        JPanel card1 = new JPanel(new GridBagLayout());
        online_Menu_Button = new JButton(ONLINE);
        online_Menu_Button.setPreferredSize(new Dimension(180, 50));
        online_Menu_Button.setFont(new Font(TIMES, Font.PLAIN, 20));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.01;
        c.gridwidth = 2;
        c.insets = new Insets(0, 0, 5, 0);
        card1.add(online_Menu_Button, c);

        local_Menu_Button = new JButton(LOCAL);
        local_Menu_Button.setPreferredSize(new Dimension(180, 50));
        local_Menu_Button.setFont(new Font(TIMES, Font.PLAIN, 20));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0.01;
        c.gridwidth = 2;
        c.insets = new Insets(5, 0, 5, 0);
        card1.add(local_Menu_Button, c);

        JLabel emptyLabelc1 = new JLabel();
        emptyLabelc1.setText("");
        c= new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 160, 0,160 );
        c.gridwidth = 2;
        c.weighty = 1;
        card1.add(emptyLabelc1, c);

        quit_Menu_Button = new JButton("Quit");
        quit_Menu_Button.setPreferredSize(new Dimension(120, 40));
        quit_Menu_Button.setFont(new Font(TIMES, Font.PLAIN, 20));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.weighty = 0.01;
        c.insets = new Insets(100, 0, 0, 20);
        c.anchor = GridBagConstraints.LAST_LINE_START;
        card1.add(quit_Menu_Button, c);

        settings_Menu_Button = new JButton(SETTINGS);
        settings_Menu_Button.setPreferredSize(new Dimension(120, 40));
        settings_Menu_Button.setFont(new Font(TIMES, Font.PLAIN, 20));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.weighty = 0.01;
        c.insets = new Insets(100, 20, 0, 0);
        c.anchor = GridBagConstraints.LAST_LINE_END;
        card1.add(settings_Menu_Button, c);

        card1.setOpaque(false);
        cardPanel.add(card1, MENU);

        /*
            card 2: Settings

         */
        JPanel card2 = new JPanel(new GridBagLayout());

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
        ipAddress_Settings_Field.setFont(new Font(TIMES, Font.PLAIN, 20));
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
        port_Settings_Field.setFont(new Font(TIMES, Font.PLAIN, 20));
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
        nickname_Settings_Field.setFont(new Font(TIMES, Font.PLAIN, 20));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        c.weighty = 0.3;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 3, 5, 15);
        card2.add(nickname_Settings_Field, c);

        JLabel emptyLabelc2 = new JLabel();
        emptyLabelc2.setText("");
        c= new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 160, 0,160 );
        c.gridwidth =2;
        c.weighty=1;
        card2.add(emptyLabelc2, c);

        back_Settings_Button = new JButton("Back");
        back_Settings_Button.setPreferredSize(new Dimension(120, 40));
        back_Settings_Button.setFont(new Font(TIMES, Font.PLAIN, 20));
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
        apply_Settings_Button.setFont(new Font(TIMES, Font.PLAIN, 20));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 4;
        c.weighty = 0.01;
        c.gridwidth = 1;
        c.insets = new Insets(100, 20, 0, 0);
        c.anchor = GridBagConstraints.LAST_LINE_END;
        card2.add(apply_Settings_Button, c);

        card2.setOpaque(false);
        cardPanel.add(card2, SETTINGS);

        /*
            card 3: online game

         */

        JPanel card3 = new JPanel(new GridBagLayout());

        create_Online_button = new JButton("Create a new Lobby");
        create_Online_button.setPreferredSize(new Dimension(220, 50));
        create_Online_button.setFont(new Font(TIMES, Font.PLAIN, 20));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.3;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(0, 0, 0, 0);
        card3.add(create_Online_button, c);

        join_Online_Button = new JButton("Join an existing Lobby");
        join_Online_Button.setPreferredSize(new Dimension(220, 50));
        join_Online_Button.setFont(new Font(TIMES, Font.PLAIN, 20));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0.3;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(5, 0, 0, 0);
        card3.add(join_Online_Button, c);

        rejoin_Online_Button = new JButton("Reconnect to a game");
        rejoin_Online_Button.setPreferredSize(new Dimension(220, 50));
        rejoin_Online_Button.setFont(new Font(TIMES, Font.PLAIN, 20));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 0.3;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(5, 0, 0, 0);
        card3.add(rejoin_Online_Button, c);

        JLabel emptyLabelc3 = new JLabel();
        emptyLabelc3.setText("");
        c= new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 160, 0,160 );
        c.weighty=1;
        card3.add(emptyLabelc3, c);

        back_Online_Button = new JButton("Back");
        back_Online_Button.setPreferredSize(new Dimension(120, 40));
        back_Online_Button.setFont(new Font(TIMES, Font.PLAIN, 20));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.weighty = 0.01;
        c.insets = new Insets(100, 0, 0, 180);
        c.anchor = GridBagConstraints.LAST_LINE_START;
        card3.add(back_Online_Button, c);

        card3.setOpaque(false);
        cardPanel.add(card3, ONLINE);

        cardPanel.setOpaque(false);
        return cardPanel;
    }

    //ACTION LISTENERS
    ActionListener back_Online_Button_actionListener = e -> {
        cl.show(cardPanel, MENU);
        top_Update_Label.setText(" welcome " + Ark.nickname);
        bottom_Update_Label.setText(" Please don't judge me ");
    };

    ActionListener online_Menu_Button_actionListener = e -> {
        cl.show(cardPanel, ONLINE);
        top_Update_Label.setText(ONLINE);
        bottom_Update_Label.setText("Is this Requiem?");
    };

    ActionListener back_Settings_Button_actionListener = e -> {
        cl.show(cardPanel, MENU);
        top_Update_Label.setText(" welcome " + Ark.nickname);
    };

    ActionListener Settings_Menu_Button_actionListener = e -> {
        cl.show(cardPanel, SETTINGS);
        top_Update_Label.setText(SETTINGS);
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
                }
                else
                {
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
            bottom_Update_Label.setText("changes were saved!");
            top_Update_Label.setText(" welcome " + Ark.nickname);
            cl.show(cardPanel, MENU);
        }
    };

    ActionListener quit_Menu_Button_actionListener = e -> {
        mainFrame.dispose();
    };

    //HELPER METHODS
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

    class mainPanel extends JPanel {

        private Image image;

        public mainPanel()
        {
            try {
                image = ImageIO.read(new File("resources/images/main_menu_bg.jpg"));
            } catch (IOException e) {}

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
            top_Update_Label.setText(" Welcome " + Ark.nickname+ " ");
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
            bottom_Update_Label.setFont(new Font("Papyrus", Font.ITALIC, 24));
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
            bottomMenuLabel.setFont(new Font(TIMES, Font.ITALIC, 30));
            bottomMenuLabel.setForeground(Color.YELLOW);
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
}