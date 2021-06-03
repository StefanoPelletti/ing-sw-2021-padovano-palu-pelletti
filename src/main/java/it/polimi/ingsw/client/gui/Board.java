package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.server.model.enumerators.Resource;

import javax.swing.*;
import java.awt.*;

public class Board implements Runnable {

    JFrame mainFrame;
    MainPanel mainPanel;
    Dimension frameDimension;


    public static void main(String[] args) {
        //you can write here shortcuts, like going directly to the Settings and opening multiple frames
        //only if called by the main, otherwise it must be empty
        //new MainMenu();
        // cl.show(cardPanel, CREATE);

        SwingUtilities.invokeLater(new Board());
    }

    public void run() {

    }

    public Board()
    {
        mainFrame = new JFrame("Title");
        mainPanel = new MainPanel();
        mainFrame.setContentPane(mainPanel);


        mainFrame.pack();

        frameDimension = new Dimension(1630, 1030);
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mainFrame.setMinimumSize(frameDimension);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    class MainPanel extends JPanel {
        public MainPanel()
        {
            this.setLayout(new FlowLayout());
            this.add(new JLabel("Success"));
            ImageIcon icon = new ImageIcon(Resource.COIN.getPathLittle());
            JLabel label = new JLabel();
            label.setIcon(icon);
            this.add(label);
        }


    }
}
