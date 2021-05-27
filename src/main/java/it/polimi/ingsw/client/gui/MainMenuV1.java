package it.polimi.ingsw.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class MainMenuV1 {
    CardLayout cl;
    JFrame mainFrame;



    Dimension pageDimension = new Dimension(0,0);

    JLabel updateLabel;

    JButton onlineMultiButton;
    JButton localButton;
    JButton optionsButton;
    JButton quitButton;

    JTextField ipAddressField;
    JTextField portAddressField;
    JButton applyOptionsButton;
    JButton backOptionsButton;

    final String MENU = "Main Menu";
    final String OPTIONS = "Options";

    public static void main(String[] args)
    {
        new MainMenuV1().run();
    }

    public void run(){
        new MainMenuV1();
        cl.show(mainFrame.getContentPane(), OPTIONS);
    }

    public MainMenuV1()
    {
        init();
    }

    public void init() {
        mainFrame = new JFrame("Maestri del Rinascimento! GUI-edition");
        cl = new CardLayout();
        mainFrame.setLayout(cl);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);



        //MAINFRAME
        mainFrame.add(getCard1(), MENU);
        mainFrame.add(getCard2(), OPTIONS);

       // Dimension d = new Dimension(500,600);
        //mainFrame.setSize(pageDimension);
        mainFrame.setMinimumSize(pageDimension);
        mainFrame.pack();
        mainFrame.setVisible(true);



    }

    ActionListener backOptionsButtonListener = e -> {
        cl.show(mainFrame.getContentPane(), MENU);
    };

    ActionListener optionsButtonListener = e -> {
        cl.show(mainFrame.getContentPane(), OPTIONS);
    };

    ActionListener applyButtonListener = e -> {
        boolean error = false;
        int newPort = 0;
        if( isValidInet4Address( ipAddressField.getText() ))
        {
            try
            {
                newPort = Integer.parseInt(portAddressField.getText());
                if(newPort < 1024 || newPort > 65535) error = true;
            }
            catch (NumberFormatException ex)
            {
                error = true;
            }
        }
        else
            error = true;

        if(error)
            JOptionPane.showMessageDialog(mainFrame, " Please enter valid values ");
        else
        {
            Ark.defaultAddress = ipAddressField.getText();
            Ark.defaultPort = newPort;
            updateLabel.setText(" changes were saved! ");
            cl.show(mainFrame.getContentPane(), MENU);
        }
    };

    ActionListener quitButtonListener = e -> {
        mainFrame.dispose();
    };



    private JPanel getCard1() {
        //MENU - card1
        GridBagConstraints c = new GridBagConstraints();
        JPanel card1 = new JPanel(new GridBagLayout());

        //TITLE PANEL
        JLabel welcomeTo = new JLabel();
        welcomeTo.setFont(new Font("Apple Chancery", Font.BOLD | Font.ITALIC, 20));
        welcomeTo.setText(" welcome to ");
        welcomeTo.setHorizontalAlignment(SwingConstants.CENTER);
        c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=0;
        c.weighty=0.2;
        c.gridwidth=2;
        c.anchor=GridBagConstraints.PAGE_END;
        c.insets = new Insets(30,50,1,50);
        card1.add(welcomeTo, c);

        JLabel maestriDelRinascimento = new JLabel();
        maestriDelRinascimento.setFont(new Font("Apple Chancery", Font.BOLD | Font.ITALIC, 26));
        maestriDelRinascimento.setText(" Maestri del Rinascimento ");
        maestriDelRinascimento.setHorizontalAlignment(SwingConstants.CENTER);
        c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=1;
        c.weighty=0.2;
        c.gridwidth=2;
        c.anchor=GridBagConstraints.PAGE_START;
        c.insets = new Insets(1,50,30,50);
        card1.add(maestriDelRinascimento, c);

        //BUTTONS PANEL

        onlineMultiButton = new JButton(" Online Game ");
        onlineMultiButton.setPreferredSize(new Dimension(150, 40));
        c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=2;
        c.weighty = 0.01;
        c.gridwidth=2;
        c.insets = new Insets(5,0,5,0);
        card1.add(onlineMultiButton, c);

        localButton = new JButton( " Local Game ");
        localButton.setPreferredSize(new Dimension(150, 40));
        c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=3;
        c.weighty = 0.01;
        c.gridwidth=2;
        c.insets = new Insets(5,0,5,0);
        card1.add(localButton, c);

        quitButton = new JButton(" Quit " );
        quitButton.setPreferredSize(new Dimension(130, 30));
        c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=4;
        c.weighty = 0.1;
        c.insets = new Insets(100,15,5,0);
        c.anchor=GridBagConstraints.LAST_LINE_START;
        card1.add(quitButton, c);

        optionsButton = new JButton( " Options ");
        optionsButton.setPreferredSize(new Dimension(130, 30));
        c = new GridBagConstraints();
        c.gridx=1;
        c.gridy=4;
        c.weighty = 0.1;
        c.insets = new Insets(100,0,5,15);
        c.anchor=GridBagConstraints.LAST_LINE_END;
        card1.add(optionsButton, c);

        //updating label
        updateLabel = new JLabel();
        updateLabel.setFont(new Font("Apple Chancery", Font.ITALIC, 20));
        updateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        updateLabel.setText(" ");
        c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=5;
        c.weighty = 0.8;
        c.gridwidth=2;
        c.insets = new Insets(20,0,20,15);
        card1.add(updateLabel, c);

        //BOTTOM PANEL
        JLabel bottomMenuLabel = new JLabel();
        bottomMenuLabel.setFont(new Font("Apple Chancery", Font.ITALIC, 20));
        bottomMenuLabel.setText(" GC31 ");
        c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=6;
        c.weighty = 0.2;
        c.gridwidth=2;
        c.anchor=GridBagConstraints.LAST_LINE_END;
        c.insets = new Insets(20,0,20,15);
        card1.add(bottomMenuLabel, c);

        pageDimension.width = card1.getWidth();
        pageDimension.height = card1.getHeight();

        return card1;
    }

    private JPanel getCard2() {
        //OPTIONS - card2
        GridBagConstraints c;
        JPanel card2 = new JPanel(new GridBagLayout());


        //TITLE (OPTIONS) PANEL
        JLabel optionsLabel = new JLabel();
        optionsLabel.setFont(new Font("Apple Chancery", Font.BOLD | Font.ITALIC, 26));
        optionsLabel.setText(" Options ");
        optionsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        c=new GridBagConstraints();
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=2;
        c.weighty=0.2;
        c.anchor=GridBagConstraints.PAGE_END;
        c.fill=GridBagConstraints.BOTH;
        c.insets = new Insets(30,155,30,155);
        card2.add(optionsLabel,c);

        JLabel ipLabel = new JLabel();
        ipLabel.setText("   current IP is: ");
        c=new GridBagConstraints();
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        c.weighty=0.01;
        c.anchor=GridBagConstraints.LINE_END;
        c.insets = new Insets(100,15,5,3);
        card2.add(ipLabel,c);

        ipAddressField = new JTextField(10);
        ipAddressField.setText( Ark.defaultAddress );
        c=new GridBagConstraints();
        c.gridx=1;
        c.gridy=1;
        c.gridwidth=1;
        c.weighty=0.01;
        c.anchor=GridBagConstraints.LINE_START;
        c.insets = new Insets(100,3,5,15);
        card2.add(ipAddressField,c);

        JLabel portLabel = new JLabel();
        portLabel.setText(" current port is: ");
        c=new GridBagConstraints();
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=1;
        c.weighty=0.01;
        c.anchor=GridBagConstraints.LINE_END;
        c.insets = new Insets(3,15,5,3);
        card2.add(portLabel,c);

        portAddressField = new JTextField(10);
        portAddressField.setText( ""+Ark.defaultPort );
        c=new GridBagConstraints();
        c.gridx=1;
        c.gridy=2;
        c.gridwidth=1;
        c.weighty=0.01;
        c.anchor=GridBagConstraints.LINE_START;
        c.insets = new Insets(3,3,5,15);
        card2.add(portAddressField,c);

        backOptionsButton = new JButton( " Back ");
        backOptionsButton.setPreferredSize(new Dimension(130, 30));
        c=new GridBagConstraints();
        c.gridx=0;
        c.gridy=3;
        c.weighty = 0.1;
        c.gridwidth=1;
        c.insets = new Insets(100,15,5,0);
        c.anchor=GridBagConstraints.LAST_LINE_START;
        card2.add(backOptionsButton,c);

        applyOptionsButton = new JButton(" Apply ");
        applyOptionsButton.setPreferredSize(new Dimension(130, 30));
        c=new GridBagConstraints();
        c.gridx=1;
        c.gridy=3;
        c.weighty = 0.1;
        c.gridwidth=1;
        c.insets = new Insets(100,0,5,15);
        c.anchor=GridBagConstraints.LAST_LINE_END;
        card2.add(applyOptionsButton,c);

        //BOTTOM PANEL
        JLabel bottomMenuLabel = new JLabel();
        bottomMenuLabel.setFont(new Font("Apple Chancery", Font.ITALIC, 20));
        bottomMenuLabel.setText(" GC31 ");
        c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=4;
        c.gridwidth=2;
        c.weighty = 0.8;
        c.anchor=GridBagConstraints.LAST_LINE_END;
        c.insets = new Insets(20,0,20,15);
        card2.add(bottomMenuLabel, c);
        card2.setPreferredSize(pageDimension);
        return card2;
    }

    private static boolean isValidInet4Address(String ip)
    {
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
