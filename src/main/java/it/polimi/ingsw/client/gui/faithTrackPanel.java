package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.server.model.FaithTrack;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class faithTrackPanel {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        faithTrackPane p = new faithTrackPane();
        frame.setContentPane(p);
        frame.pack();
        frame.setSize(new Dimension(1237,278));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}

class faithTrackPane extends JPanel {

    private Image image;

    CellPanel[] positions;
    JPanel[] zones;

    public faithTrackPane() {

        this.positions = new CellPanel[25];
        this.zones = new JPanel[3];


        GridBagConstraints c;
        try {
            image = ImageIO.read(new File("resources/images/upper_board.png"));
        } catch (IOException e) {
        }
        this.setLayout(new GridBagLayout());

        c = new GridBagConstraints(); //left padding
        c.weightx = 0;
        c.weighty = 0;
        c.gridx = 0;
        c.gridy = 1;
        this.add(Box.createHorizontalStrut(44),c);

        c = new GridBagConstraints(); //right padding
        c.weightx = 0;
        c.weighty = 0;
        c.gridx = 2;
        c.gridy = 1;
        this.add(Box.createHorizontalStrut(40),c);

        c = new GridBagConstraints(); //top padding
        c.weightx = 0;
        c.weighty = 0;
        c.gridx = 1;
        c.gridy = 0;
        this.add(Box.createVerticalStrut(32),c);

        c = new GridBagConstraints(); //bottom padding
        c.weightx = 1;
        c.weighty = 0;
        c.gridx = 1;
        c.gridy = 2;
        this.add(Box.createVerticalStrut(25),c);

        JPanel internalPane = new JPanel();
        internalPane.setLayout(new GridBagLayout());
        internalPane.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
        internalPane.setOpaque(false);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 0.5;
        c.weightx = 0.5;
        this.add(internalPane,c);


        CellPanel cellPanel;
        ZonePanel zonePanel;

        for(int row=0; row<=2; row++) {
            for(int col=0; col<=3; col++) {

                if(isUseful(row,col)) {
                    cellPanel = new CellPanel(true);
                    this.positions[pos(row,col)] = cellPanel;
                }
                else
                    cellPanel = new CellPanel(false);

                c = new GridBagConstraints();
                c.gridx = col;
                c.gridy = row;
                c.weightx = 0.3;
                c.weighty = 0.3;
                c.insets = new Insets(2,2,2,2);
                internalPane.add(cellPanel,c);


            }
        }

        for(int col=4; col<=5; col++) {
            cellPanel = new CellPanel(true);
            this.positions[pos(0,col)] = cellPanel;
            c = new GridBagConstraints();
            c.gridx = col;
            c.gridy = 0;
            c.weightx = 0.3;
            c.weighty = 0.3;
            c.insets = new Insets(2,2,2,2);
            internalPane.add(cellPanel,c);
        }

        zonePanel = new ZonePanel(0);
        this.zones[0] = zonePanel;
        c = new GridBagConstraints();
        c.gridx = 4;
        c.gridy = 1;
        c.weightx = 0.3;
        c.weighty = 0.3;
        c.gridwidth = 2;
        c.gridheight = 2;
        c.insets = new Insets(2,2,2,2);
        internalPane.add(zonePanel,c);

        for(int row = 0; row<=2; row++) {
            for(int col = 6; col<=8; col++) {

                if(isUseful(row,col)) {
                    cellPanel = new CellPanel(true);
                    this.positions[pos(row,col)] = cellPanel;
                }
                else
                    cellPanel = new CellPanel(false);

                c = new GridBagConstraints();
                c.gridx = col;
                c.gridy = row;
                c.weightx = 0.3;
                c.weighty = 0.3;
                c.insets = new Insets(2,2,2,2);
                internalPane.add(cellPanel,c);
            }
        }

        for(int col=9; col<=10; col++) {
            cellPanel = new CellPanel(true);
            this.positions[pos(2,col)] = cellPanel;
            c = new GridBagConstraints();
            c.gridx = col;
            c.gridy = 2;
            c.weightx = 0.3;
            c.weighty = 0.3;
            c.insets = new Insets(2,2,2,2);
            internalPane.add(cellPanel,c);
        }

        zonePanel = new ZonePanel(1);
        this.zones[1] = zonePanel;
        c = new GridBagConstraints();
        c.gridx = 9;
        c.gridy = 0;
        c.weightx = 0.3;
        c.weighty = 0.3;
        c.gridwidth = 2;
        c.gridheight = 2;
        c.insets = new Insets(2,2,2,2);
        internalPane.add(zonePanel,c);

        for(int row = 0; row<=2; row++) {
            for(int col = 11; col<=14; col++) {

                if(isUseful(row,col)) {
                    cellPanel = new CellPanel(true);
                    this.positions[pos(row,col)] = cellPanel;
                }
                else
                    cellPanel = new CellPanel(false);

                c = new GridBagConstraints();
                c.gridx = col;
                c.gridy = row;
                c.weightx = 0.3;
                c.weighty = 0.3;
                c.insets = new Insets(2,2,2,2);
                internalPane.add(cellPanel,c);
            }
        }

        for(int col=15; col<=16; col++) {
            cellPanel = new CellPanel(true);
            this.positions[pos(0,col)] = cellPanel;
            c = new GridBagConstraints();
            c.gridx = col;
            c.gridy = 0;
            c.weightx = 0.3;
            c.weighty = 0.3;
            c.insets = new Insets(2,2,2,2);
            internalPane.add(cellPanel,c);
        }

        zonePanel = new ZonePanel(2);
        this.zones[2] = zonePanel;
        c = new GridBagConstraints();
        c.gridx = 15;
        c.gridy = 1;
        c.weightx = 0.3;
        c.weighty = 0.3;
        c.gridwidth = 2;
        c.gridheight = 2;
        c.insets = new Insets(2,2,2,2);
        internalPane.add(zonePanel,c);

        for(int row = 0; row<=2; row++) {
            for(int col = 17; col<=18; col++) {

                if(isUseful(row,col)) {
                    cellPanel = new CellPanel(true);
                    this.positions[pos(row,col)] = cellPanel;
                }
                else
                    cellPanel = new CellPanel(false);

                c = new GridBagConstraints();
                c.gridx = col;
                c.gridy = row;
                c.weightx = 0.3;
                c.weighty = 0.3;
                c.insets = new Insets(2,2,2,2);
                internalPane.add(cellPanel,c);
            }
        }

    }

    class ZonePanel extends JPanel {

        static final int boxSize = 112;
        private final int zoneNumber;

        public ZonePanel(int zoneNumber) {
            this.zoneNumber = zoneNumber;
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());


            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 0;
            c.weightx = 0;
            c.weighty = 0;
            this.add(Box.createHorizontalStrut(boxSize),c);

            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 1;
            c.weightx = 0;
            c.weighty = 0;
            this.add(Box.createVerticalStrut(boxSize),c);
        }

        public int getZoneNumber() { return zoneNumber; }
    }

    class CellPanel extends JPanel {

        static final int boxSize = 54;

        public CellPanel(boolean useful) {
            GridBagConstraints c;
            this.setLayout(new GridBagLayout());

            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 0;
            c.weightx = 0;
            c.weighty = 0;
            this.add(Box.createHorizontalStrut(boxSize),c);

            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 1;
            c.weightx = 0;
            c.weighty = 0;
            this.add(Box.createVerticalStrut(boxSize),c);

            if (useful) {
                JButton button = new JButton();
                c = new GridBagConstraints();
                c.gridx = 1;
                c.gridy = 1;
                c.weightx = 0.5;
                c.weighty = 0.5;
                c.fill = GridBagConstraints.BOTH;
                this.add(button,c);
            }

        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }

    public boolean isUseful(int row, int col) {
        switch (row) {
            case 0:
                switch (col) {
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 18:
                        return true;
                    default:
                        return false;
                }
            case 1:
                switch (col) {
                    case 2:
                    case 7:
                    case 12:
                        return true;
                    default:
                        return false;
                }
            case 2:
                switch (col) {
                    case 0:
                    case 1:
                    case 2:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                        return true;
                    default:
                        return false;
                }
            default:
                return false;
        }
    }

    public int pos(int row, int col ) {
        switch (row) {
            case 0:
                switch (col) {
                    case 2:  return 4;
                    case 3:  return 5;
                    case 4:  return 6;
                    case 5:  return 7;
                    case 6:  return 8;
                    case 7:  return 9;
                    case 12: return 18;
                    case 13: return 19;
                    case 14: return 20;
                    case 15: return 21;
                    case 16: return 22;
                    case 17: return 23;
                    case 18: return 24;
                    default: return -1;
                }
            case 1:
                switch (col) {
                    case 2:  return 3;
                    case 7:  return 10;
                    case 12: return 17;
                    default: return -1;
                }
            case 2:
                switch (col) {
                    case 0:  return 0;
                    case 1:  return 1;
                    case 2:  return 2;
                    case 7:  return 11;
                    case 8:  return 12;
                    case 9:  return 13;
                    case 10: return 14;
                    case 11: return 15;
                    case 12: return 16;
                    default: return -1;
                }
            default:
                return -1;
        }
    }

    public static void addPadding(JComponent object, int height, int width, int maxColumns, int maxRows) {
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
        c.insets = new Insets(height, 0, 0, 0);
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
        c.insets = new Insets(0, width, 0, 0);
        object.add(paddingHorizontal, c);
    }

}