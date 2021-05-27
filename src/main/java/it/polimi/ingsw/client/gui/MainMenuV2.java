package it.polimi.ingsw.client.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class MainMenuV2 {
    CardLayout cl;
    JFrame mainFrame;
    JPanel cardPanel;
    JLabel topLabel;

    JLabel updateLabel;

    JButton onlineMultiButton;
    JButton localButton;
    JButton optionsButton;
    JButton quitButton;

    JTextField ipAddressField;
    JTextField portAddressField;
    JTextField nicknameField;
    JButton applyOptionsButton;
    JButton backOptionsButton;

    final String MENU = "Main Menu";
    final String OPTIONS = "Options";

    public static void main(String[] args)
    {
        new MainMenuV2().run();
    }

    public void run(){
        //new MainMenuV2();
      //  cl.show(mainFrame.getContentPane(), OPTIONS);
    }

    public MainMenuV2()
    {
        mainFrame = new JFrame(" Maestri del Rinascimento! GUI-edition! ");
        cl = new CardLayout();
        GridBagConstraints c = new GridBagConstraints();


        mainFrame.setLayout(new GridBagLayout());

        // title label
        JLabel titleLabel = new JLabel();
        titleLabel.setFont(new Font("Apple Chancery", Font.BOLD | Font.ITALIC, 26));
        titleLabel.setText(" Maestri del Rinascimento ");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=0;
        c.weighty=0.3;
        c.anchor=GridBagConstraints.PAGE_START;
        c.insets = new Insets(30,50,10,50);
        mainFrame.add(titleLabel,c);

        topLabel = new JLabel();
        topLabel.setFont(new Font("Apple Chancery", Font.BOLD | Font.ITALIC, 20));
        topLabel.setText(" welcome "+Ark.nickname);
        topLabel.setHorizontalAlignment(SwingConstants.CENTER);
        c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=1;
        c.weighty=0.2;
        c.insets = new Insets(10,50,10,50);
        mainFrame.add(topLabel,c);


        c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=2;
        c.weighty=0.8;
        c.fill=GridBagConstraints.BOTH;
        c.insets = new Insets(10,10,10,10);
        mainFrame.add(getCardMagic(), c);



        //updating label
        updateLabel = new JLabel();
        updateLabel.setFont(new Font("Apple Chancery", Font.ITALIC, 20));
        updateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        updateLabel.setText(" hello world ");
        c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=3;
        c.weighty = 0.2;
        c.insets = new Insets(20,0,20,15);
        mainFrame.add(updateLabel, c);

        //bottom label
        JLabel bottomMenuLabel = new JLabel();
        bottomMenuLabel.setFont(new Font("Apple Chancery", Font.ITALIC, 20));
        bottomMenuLabel.setText(" GC31 ");
        c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=4;
        c.weighty = 0.2;
        c.anchor=GridBagConstraints.LAST_LINE_END;
        c.insets = new Insets(20,0,20,15);
        mainFrame.add(bottomMenuLabel, c);


        backOptionsButton.addActionListener(backOptionsButtonListener);
        optionsButton.addActionListener(optionsButtonListener);
        applyOptionsButton.addActionListener(applyButtonListener);
        quitButton.addActionListener(quitButtonListener);

        int x = mainFrame.getWidth();
        int y = mainFrame.getHeight();
        mainFrame.setMinimumSize(new Dimension(x, y));

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private JPanel getCardMagic() {
        cardPanel = new JPanel();
        cardPanel.setLayout(cl);

        GridBagConstraints c = new GridBagConstraints();

        //card 1
        JPanel card1 = new JPanel(new GridBagLayout());
        onlineMultiButton = new JButton(" Online Game ");
        onlineMultiButton.setPreferredSize(new Dimension(150, 40));
        c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=0;
        c.weighty = 0.01;
        c.gridwidth=2;
        c.insets = new Insets(80,0,5,0);
        card1.add(onlineMultiButton, c);

        localButton = new JButton( " Local Game ");
        localButton.setPreferredSize(new Dimension(150, 40));
        c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=1;
        c.weighty = 0.01;
        c.gridwidth=2;
        c.insets = new Insets(5,0,5,0);
        card1.add(localButton, c);

        quitButton = new JButton(" Quit " );
        quitButton.setPreferredSize(new Dimension(130, 30));
        c = new GridBagConstraints();
        c.gridx=0;
        c.gridy=2;
        c.weighty = 0.1;
        c.insets = new Insets(100,15,5,20);
        c.anchor=GridBagConstraints.LAST_LINE_START;
        card1.add(quitButton, c);

        optionsButton = new JButton( " Options ");
        optionsButton.setPreferredSize(new Dimension(130, 30));
        c = new GridBagConstraints();
        c.gridx=1;
        c.gridy=2;
        c.weighty = 0.1;
        c.insets = new Insets(100,20,5,15);
        c.anchor=GridBagConstraints.LAST_LINE_END;
        card1.add(optionsButton, c);

        cardPanel.add(card1, MENU);

        //card 2
        JPanel card2 = new JPanel(new GridBagLayout());

        JLabel ipLabel = new JLabel();
        ipLabel.setText("current IP is:");
        c=new GridBagConstraints();
        c.gridx=0;
        c.gridy=0;
        c.gridwidth=1;
        c.weighty=0.01;
        c.anchor=GridBagConstraints.LINE_END;
        c.insets = new Insets(100,15,5,3);
        card2.add(ipLabel,c);

        ipAddressField = new JTextField(10);
        ipAddressField.setText( Ark.defaultAddress );
        c=new GridBagConstraints();
        c.gridx=1;
        c.gridy=0;
        c.gridwidth=1;
        c.weighty=0.01;
        c.anchor=GridBagConstraints.LINE_START;
        c.insets = new Insets(100,3,5,15);
        card2.add(ipAddressField,c);

        JLabel portLabel = new JLabel();
        portLabel.setText("current port is:");
        c=new GridBagConstraints();
        c.gridx=0;
        c.gridy=1;
        c.gridwidth=1;
        c.weighty=0.01;
        c.anchor=GridBagConstraints.LINE_END;
        c.insets = new Insets(3,15,5,3);
        card2.add(portLabel,c);

        portAddressField = new JTextField(10);
        portAddressField.setText( ""+Ark.defaultPort );
        c=new GridBagConstraints();
        c.gridx=1;
        c.gridy=1;
        c.gridwidth=1;
        c.weighty=0.01;
        c.anchor=GridBagConstraints.LINE_START;
        c.insets = new Insets(3,3,5,15);
        card2.add(portAddressField,c);

        JLabel nameLabel = new JLabel();
        nameLabel.setText("current name is:");
        c=new GridBagConstraints();
        c.gridx=0;
        c.gridy=2;
        c.gridwidth=1;
        c.weighty=0.01;
        c.anchor=GridBagConstraints.LINE_END;
        c.insets = new Insets(3,15,5,3);
        card2.add(nameLabel,c);

        nicknameField = new JTextField(10);
        nicknameField.setText( ""+Ark.nickname );
        c=new GridBagConstraints();
        c.gridx=1;
        c.gridy=2;
        c.gridwidth=1;
        c.weighty=0.01;
        c.anchor=GridBagConstraints.LINE_START;
        c.insets = new Insets(3,3,5,15);
        card2.add(nicknameField,c);

        backOptionsButton = new JButton( " Back ");
        backOptionsButton.setPreferredSize(new Dimension(130, 30));
        c=new GridBagConstraints();
        c.gridx=0;
        c.gridy=3;
        c.weighty = 0.1;
        c.gridwidth=1;
        c.insets = new Insets(100,15,5,20);
        c.anchor=GridBagConstraints.LAST_LINE_START;
        card2.add(backOptionsButton,c);

        applyOptionsButton = new JButton(" Apply ");
        applyOptionsButton.setPreferredSize(new Dimension(130, 30));
        c=new GridBagConstraints();
        c.gridx=1;
        c.gridy=3;
        c.weighty = 0.1;
        c.gridwidth=1;
        c.insets = new Insets(100,20,5,15);
        c.anchor=GridBagConstraints.LAST_LINE_END;
        card2.add(applyOptionsButton,c);

        cardPanel.add(card2, OPTIONS);

        return cardPanel;
    }

//ACTION LISTENERS
    ActionListener backOptionsButtonListener = e -> {
        cl.show(cardPanel, MENU);
        topLabel.setText(" welcome "+Ark.nickname);
    };

    ActionListener optionsButtonListener = e -> {
        cl.show(cardPanel, OPTIONS);
    };

    ActionListener applyButtonListener = e -> {
        boolean error = false;
        int newPort = 0;
        String newNickname= "";
        if( isValidInet4Address( ipAddressField.getText() ) || Ark.defaultAddress.equals(ipAddressField.getText()))
        {
            try
            {
                newPort = Integer.parseInt(portAddressField.getText());
                if(newPort < 1024 || newPort > 65535)
                {
                    error = true;
                }
                else
                {
                    newNickname = nicknameField.getText();
                    if(newNickname.length()==0)
                        error=true;
                }
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
            Ark.nickname = newNickname;
            updateLabel.setText(" changes were saved! ");
            topLabel.setText(" welcome "+Ark.nickname);
            cl.show(cardPanel, MENU);
        }
    };

    ActionListener quitButtonListener = e -> {
        mainFrame.dispose();
    };

//HELPER METHODS
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
