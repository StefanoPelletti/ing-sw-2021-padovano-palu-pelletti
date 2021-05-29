package it.polimi.ingsw.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
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
    static final String APPLE = "Apple Chancery";

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
        cl = new CardLayout();
        GridBagConstraints c;


        mainFrame.setLayout(new GridBagLayout());

        // title label
        JLabel titleLabel = new JLabel();
        titleLabel.setFont(new Font(APPLE, Font.BOLD | Font.ITALIC, 26));
        titleLabel.setText(" Maestri del Rinascimento ");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.3;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(30, 50, 10, 50);
        mainFrame.add(titleLabel, c);

        top_Update_Label = new JLabel();
        top_Update_Label.setFont(new Font(APPLE, Font.BOLD | Font.ITALIC, 20));
        top_Update_Label.setText(" welcome " + Ark.nickname);
        top_Update_Label.setHorizontalAlignment(SwingConstants.CENTER);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0.2;
        c.insets = new Insets(10, 50, 10, 50);
        mainFrame.add(top_Update_Label, c);


        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 0.8;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(10, 10, 10, 10);
        mainFrame.add(getCardMagic(), c);


        //updating label
        bottom_Update_Label = new JLabel();
        bottom_Update_Label.setFont(new Font(APPLE, Font.ITALIC, 20));
        bottom_Update_Label.setHorizontalAlignment(SwingConstants.CENTER);
        bottom_Update_Label.setText(" hello world ");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.weighty = 0.2;
        c.insets = new Insets(20, 0, 20, 15);
        mainFrame.add(bottom_Update_Label, c);

        //bottom label
        JLabel bottomMenuLabel = new JLabel();
        bottomMenuLabel.setFont(new Font(APPLE, Font.ITALIC, 20));
        bottomMenuLabel.setText(" GC31 ");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.weighty = 0.2;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        c.insets = new Insets(20, 0, 20, 15);
        mainFrame.add(bottomMenuLabel, c);


        back_Settings_Button.addActionListener(back_Settings_Button_actionListener);
        settings_Menu_Button.addActionListener(Settings_Menu_Button_actionListener);
        apply_Settings_Button.addActionListener(apply_Settings_Button_actionListener);
        quit_Menu_Button.addActionListener(quit_Menu_Button_actionListener);
        online_Menu_Button.addActionListener(online_Menu_Button_actionListener);
        back_Online_Button.addActionListener(back_Online_Button_actionListener);

        mainFrame.pack();
        frameDimension = new Dimension(mainFrame.getWidth(), mainFrame.getHeight());
        mainFrame.setMinimumSize(frameDimension);
        System.out.println("w: " + mainFrame.getContentPane().getWidth() + "p h:" + mainFrame.getContentPane().getHeight() + "p");

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
        online_Menu_Button.setPreferredSize(new Dimension(150, 40));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.01;
        c.gridwidth = 2;
        c.insets = new Insets(80, 0, 5, 0);
        card1.add(online_Menu_Button, c);

        local_Menu_Button = new JButton(LOCAL);
        local_Menu_Button.setPreferredSize(new Dimension(150, 40));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0.01;
        c.gridwidth = 2;
        c.insets = new Insets(5, 0, 5, 0);
        card1.add(local_Menu_Button, c);

        quit_Menu_Button = new JButton("Quit");
        quit_Menu_Button.setPreferredSize(new Dimension(130, 30));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 0.1;
        c.insets = new Insets(100, 15, 5, 20);
        c.anchor = GridBagConstraints.LAST_LINE_START;
        card1.add(quit_Menu_Button, c);

        settings_Menu_Button = new JButton(SETTINGS);
        settings_Menu_Button.setPreferredSize(new Dimension(130, 30));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.weighty = 0.1;
        c.insets = new Insets(100, 20, 5, 15);
        c.anchor = GridBagConstraints.LAST_LINE_END;
        card1.add(settings_Menu_Button, c);

        cardPanel.add(card1, MENU);

        /*
            card 2: Settings

         */
        JPanel card2 = new JPanel(new GridBagLayout());

        JLabel ipLabel = new JLabel();
        ipLabel.setText("current IP is:");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weighty = 0.01;
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(100, 15, 5, 3);
        card2.add(ipLabel, c);

        ipAddress_Settings_Field = new JTextField(10);
        ipAddress_Settings_Field.setText(Ark.defaultAddress);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weighty = 0.01;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(100, 3, 5, 15);
        card2.add(ipAddress_Settings_Field, c);

        JLabel portLabel = new JLabel();
        portLabel.setText("current port is:");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weighty = 0.01;
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(3, 15, 5, 3);
        card2.add(portLabel, c);

        port_Settings_Field = new JTextField(10);
        port_Settings_Field.setText("" + Ark.defaultPort);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weighty = 0.01;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(3, 3, 5, 15);
        card2.add(port_Settings_Field, c);

        JLabel nameLabel = new JLabel();
        nameLabel.setText("current name is:");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.weighty = 0.01;
        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(3, 15, 5, 3);
        card2.add(nameLabel, c);

        nickname_Settings_Field = new JTextField(10);
        nickname_Settings_Field.setText("" + Ark.nickname);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        c.weighty = 0.01;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(3, 3, 5, 15);
        card2.add(nickname_Settings_Field, c);

        back_Settings_Button = new JButton("Back");
        back_Settings_Button.setPreferredSize(new Dimension(130, 30));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.insets = new Insets(100, 15, 5, 20);
        c.anchor = GridBagConstraints.LAST_LINE_START;
        card2.add(back_Settings_Button, c);

        apply_Settings_Button = new JButton("Apply");
        apply_Settings_Button.setPreferredSize(new Dimension(130, 30));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.insets = new Insets(100, 20, 5, 15);
        c.anchor = GridBagConstraints.LAST_LINE_END;
        card2.add(apply_Settings_Button, c);

        cardPanel.add(card2, SETTINGS);

        /*
            card 3: online game

         */

        JPanel card3 = new JPanel(new GridBagLayout());

        create_Online_button = new JButton("Create a new Lobby");
        create_Online_button.setPreferredSize(new Dimension(180, 40));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.01;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(80, 0, 5, 0);
        card3.add(create_Online_button, c);

        join_Online_Button = new JButton("Join an existing Lobby");
        join_Online_Button.setPreferredSize(new Dimension(180, 40));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0.01;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(5, 0, 5, 0);
        card3.add(join_Online_Button, c);

        rejoin_Online_Button = new JButton("Reconnect to a game");
        rejoin_Online_Button.setPreferredSize(new Dimension(180, 40));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 0.01;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(5, 0, 5, 0);
        card3.add(rejoin_Online_Button, c);

        back_Online_Button = new JButton("Back");
        back_Online_Button.setPreferredSize(new Dimension(130, 30));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.weighty = 0.8;
        c.insets = new Insets(100, 10, 6, 180);
        c.anchor = GridBagConstraints.LAST_LINE_START;
        card3.add(back_Online_Button, c);

        cardPanel.add(card3, ONLINE);
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
}