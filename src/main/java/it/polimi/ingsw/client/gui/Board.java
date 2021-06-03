package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.server.model.enumerators.Resource;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

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

        //frameDimension = new Dimension(1630, 1030);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //mainFrame.setMinimumSize(frameDimension);

        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    class MainPanel extends JPanel {
        public MainPanel()
        {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());

            JPanel leftPanel = new JPanel(new GridBagLayout());
            leftPanel.setBackground(new Color(215, 200, 145));
            leftPanel.setBorder(BorderFactory.createMatteBorder(1,1,0,1,Color.BLACK));

            JLabel el1 = new JLabel();
            el1.setText("");
            c = new GridBagConstraints();
            c.insets = new Insets(840,459, 0,0);
            c.insets = new Insets(839,457, 0,0);
            leftPanel.add(el1, c);

            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 1;
            c.gridheight = 2;
            this.add(leftPanel, c);



            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 0;
            c.gridwidth = 2;
            c.gridheight = 1;
            this.add(new TopPanel(), c);


            JPanel bottomPanel = new JPanel(new GridBagLayout());
            bottomPanel.setBackground(new Color(215, 200, 145));
            bottomPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
            JLabel el3 = new JLabel();
            el3.setText("");
            c = new GridBagConstraints();
            c.insets = new Insets(210,1680, 0,0);
            c.insets = new Insets(208,1678, 0,0);
            bottomPanel.add(el3, c);

            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 2;
            c.gridwidth = 3;
            c.gridheight = 1;
            this.add(bottomPanel, c);



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
            this.add(new CentralRightPanel(), c);

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
        private Image image;
        public CentralRightPanel() {
            try {
                image = ImageIO.read(new File("resources/images/right_board.png"));
            } catch (IOException e) {}

            this.setLayout(new GridBagLayout());
            JLabel el = new JLabel();
            el.setText("");
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(601,948, 0,0);
            this.add(el, c);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }
}
