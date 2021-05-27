package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.cli.PhaseClient;
import it.polimi.ingsw.server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class StartingPrompt {
    JFrame mainFrame;

    JButton confirm_Button;
    JButton quit_Button;

    JRadioButton clientGUI_radioButton;
    JRadioButton clientCLI_radioButton;
    JRadioButton server_radioButton;


    public static void main(String[] args) {
        new StartingPrompt();
    }

    public StartingPrompt() {
        mainFrame = new JFrame("MdR Launcher");
        GridBagConstraints c = new GridBagConstraints();
        mainFrame.setLayout(new GridBagLayout());

        /*

        +-------------------------------+
        +    topLabel   +   topLabel    +
        +-------------------------------+
        +  +-------------------------+  +
        +  +      radio1             +  +
        +  +-------------------------+  +
        +  +      radio2             +  +
        +  +-------------------------+  +
        +  +      radio3             +  +
        +  +-------------------------+  +
        +-------------------------------+
        +  +-------------------------+  +
        +  +     button1+button2     +  +
        +  +-------------------------+  +
        +-------------------------------+

         */


        //TOP LABEL
        JLabel topLabel = new JLabel();
        topLabel.setFont(new Font("Apple Chancery", Font.BOLD, 15));
        topLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topLabel.setText(" Please choose something and confirm ");
        c.insets = new Insets(20, 30, 20, 150);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;
        mainFrame.add(topLabel, c);

        //RADIO BUTTONS PANEL
        JPanel midPanel = new JPanel();
        midPanel.setLayout(new GridBagLayout());

        c = new GridBagConstraints();
        clientGUI_radioButton = new JRadioButton(" Start GUI ", true);
        clientGUI_radioButton.setSize(100, 80);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        midPanel.add(clientGUI_radioButton, c);

        clientCLI_radioButton = new JRadioButton(" Start CLI ");
        clientCLI_radioButton.setSize(100, 80);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        midPanel.add(clientCLI_radioButton, c);

        server_radioButton = new JRadioButton(" Start Server ");
        server_radioButton.setSize(100, 80);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.LINE_START;
        midPanel.add(server_radioButton, c);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(clientGUI_radioButton);
        buttonGroup.add(clientCLI_radioButton);
        buttonGroup.add(server_radioButton);

        c = new GridBagConstraints();
        c.insets = new Insets(0, 50, 0, 30);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.LINE_START;
        mainFrame.add(midPanel, c);

        //BOTTOM PANEL
        JPanel botPanel = new JPanel(new GridBagLayout());

        confirm_Button = new JButton("Confirm");
        confirm_Button.setPreferredSize(new Dimension(100, 40));
        c = new GridBagConstraints();
        c.insets = new Insets(0, 3, 0, 3);
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;
        botPanel.add(confirm_Button, c);

        quit_Button = new JButton("Quit");
        quit_Button.setPreferredSize(new Dimension(100, 40));
        c.insets = new Insets(0, 3, 0, 3);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        botPanel.add(quit_Button, c);


        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.insets = new Insets(20, 0, 15, 0);
        mainFrame.add(botPanel, c);


        //PROMPT
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        //mainFrame.setResizable(false);
        mainFrame.pack();
        int x = mainFrame.getWidth();
        int y = mainFrame.getHeight();
        mainFrame.setMinimumSize(new Dimension(x, y));
        mainFrame.setVisible(true);


        //ACTION LISTENERS
        confirm_Button.addActionListener(confirm_ButtonActionListener);
        quit_Button.addActionListener(quit_ButtonActionListener);
    }


    //ACTION LISTENER
    ActionListener confirm_ButtonActionListener = e -> {
        if (clientGUI_radioButton.isSelected()) {
            new MainMenuV2();
            mainFrame.dispose();
        } else if (clientCLI_radioButton.isSelected()) {
            JOptionPane.showMessageDialog(mainFrame, " Client in CLI started! \n Check your JAVA Console!");

            PhaseClient client = new PhaseClient();
            mainFrame.dispose();
            client.run();
        } else if (server_radioButton.isSelected()) {
            JOptionPane.showMessageDialog(mainFrame, " Server Started! \n Check your JAVA Console!");

            Server server = new Server(43210);
            mainFrame.dispose();
            server.run();
        }
    };

    ActionListener quit_ButtonActionListener = e -> mainFrame.dispose();
}