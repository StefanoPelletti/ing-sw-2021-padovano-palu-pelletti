package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.cli.PhaseClient;
import it.polimi.ingsw.networking.Server;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class StartingPrompt implements Runnable {
    JFrame mainFrame;
    Dimension frameDimension;

    JButton confirm_Button;
    JButton quit_Button;

    JRadioButton clientGUI_radioButton;
    JRadioButton clientCLI_radioButton;
    JRadioButton server_radioButton;

    //fonts
    static final String TIMES = "Times New Roman";
    static final String PAP = "Papyrus";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new StartingPrompt());
    }

    public void run() { }



    public StartingPrompt() {
        mainFrame = new JFrame("MdR Launcher");
        mainFrame.setContentPane(new mainPanel());

        //PROMPT
        //operation flags
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setResizable(false);

        //pack to generate the result
        mainFrame.pack();


        //operation must be done after pack
        frameDimension = new Dimension(493, 304);
        mainFrame.setMinimumSize(frameDimension);
        mainFrame.setLocationRelativeTo(null); //this must be done after setting the minimum size
        //always last action
        mainFrame.setVisible(true);

        //ACTION LISTENERS
        confirm_Button.addActionListener(confirm_ButtonActionListener);
        quit_Button.addActionListener(quit_ButtonActionListener);
    }

    //CUSTOM CONTENT PANE
    class mainPanel extends JPanel {

        private Image image;

        public mainPanel() {
            try {
                image = ImageIO.read(new File("resources/images/BackgroundPrompt1.png"));
            } catch (IOException e) {}


            GridBagConstraints c = new GridBagConstraints();
            this.setLayout(new GridBagLayout());


            //TOP LABEL
            JLabel topLabel = new JLabel();
            topLabel.setFont(new Font(PAP, Font.BOLD, 26));
            topLabel.setHorizontalAlignment(SwingConstants.CENTER);
            topLabel.setText("Please select something");
            c.insets = new Insets(40, 90, 20, 90);
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 1;
            c.weighty = 0.5;
            c.gridwidth = 2;
            c.fill = GridBagConstraints.HORIZONTAL;
            this.add(topLabel, c);

            //RADIO BUTTONS

            clientGUI_radioButton = new JRadioButton("  Start GUI    ", true);
            clientGUI_radioButton.setFont(new Font(TIMES, Font.PLAIN, 18));
            clientGUI_radioButton.setSize(100, 80);
            clientGUI_radioButton.setOpaque(false);
            c = new GridBagConstraints();
            c.insets = new Insets(0, 50, 0, 30);
            c.gridx = 0;
            c.gridy = 1;
            c.weightx = 1;
            c.weighty = 0.05;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.LINE_START;
            this.add(clientGUI_radioButton, c);

            clientCLI_radioButton = new JRadioButton("  Start CLI    ");
            clientCLI_radioButton.setFont(new Font(TIMES, Font.PLAIN, 18));
            clientCLI_radioButton.setSize(100, 80);
            clientCLI_radioButton.setOpaque(false);
            c = new GridBagConstraints();
            c.insets = new Insets(0, 50, 0, 30);
            c.gridx = 0;
            c.gridy = 2;
            c.weightx = 1;
            c.weighty = 0.05;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.LINE_START;
            this.add(clientCLI_radioButton, c);

            server_radioButton = new JRadioButton("  Start Server ");
            server_radioButton.setFont(new Font(TIMES, Font.PLAIN, 18));
            server_radioButton.setSize(100, 80);
            server_radioButton.setOpaque(false);
            c = new GridBagConstraints();
            c.insets = new Insets(0, 50, 0, 30);
            c.gridx = 0;
            c.gridy = 3;
            c.weightx = 1;
            c.weighty = 0.05;
            c.gridwidth = 2;
            c.anchor = GridBagConstraints.LINE_START;
            this.add(server_radioButton, c);

            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(clientGUI_radioButton);
            buttonGroup.add(clientCLI_radioButton);
            buttonGroup.add(server_radioButton);

            //BOTTOM BUTTONS

            confirm_Button = new JButton("Confirm");
            confirm_Button.setFont(new Font(PAP, Font.BOLD, 20));
            confirm_Button.setPreferredSize(new Dimension(120, 40));
            confirm_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.insets = new Insets(20, 5, 15, 5);
            c.gridx = 0;
            c.gridy = 4;
            c.weightx = 1;
            c.weighty = 0.5;
            c.anchor = GridBagConstraints.FIRST_LINE_END;
            this.add(confirm_Button, c);

            quit_Button = new JButton("Quit");
            quit_Button.setFont(new Font(PAP, Font.BOLD, 20));
            quit_Button.setPreferredSize(new Dimension(120, 40));
            quit_Button.setBackground(new Color(231, 210, 181));
            c = new GridBagConstraints();
            c.insets = new Insets(20, 5, 15, 5);
            c.gridx = 1;
            c.gridy = 4;
            c.weightx = 1;
            c.weighty = 0.5;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            this.add(quit_Button, c);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }


    //ACTION LISTENER
    ActionListener confirm_ButtonActionListener = e -> {
        if (clientGUI_radioButton.isSelected()) {
            SwingUtilities.invokeLater(new MainMenu());
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