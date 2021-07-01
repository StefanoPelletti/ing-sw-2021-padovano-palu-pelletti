package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.modelSimplified.GameSimplified;
import it.polimi.ingsw.networking.message.*;
import it.polimi.ingsw.networking.message.initMessages.MSG_CREATE_LOBBY;
import it.polimi.ingsw.networking.message.initMessages.MSG_JOIN_LOBBY;
import it.polimi.ingsw.networking.message.initMessages.MSG_REJOIN_LOBBY;
import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_Full;
import it.polimi.ingsw.server.controller.GameManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class MainMenu implements Runnable {

    //card names
    static final String MENU = "Main Menu";
    static final String SETTINGS = "Settings";
    static final String ONLINE = "Online Game";
    static final String LOCAL = "Local Game";
    static final String CREATE = "Create Lobby";
    static final String JOIN = "Join Lobby";
    static final String REJOIN = "Reconnect to Lobby";
    static final String LOADING = "Loading";

    /**
     * Min is the minimum number of Players. It should be 1 by design.
     */
    static final int MIN = 1;
    /**
     * Max is the maximum number of Players. It should be 4 by design.
     */
    static final int MAX = 4;
    /**
     * Default is the default number of Players that is set in the Slider, Create page.
     */
    static final int DEFAULT = 3;
    //fonts
    static final String TIMES = "Times New Roman";
    static final String PAPYRUS = "Papyrus";

    /**
     * Reference to the Card Layout that manages the cardPanel.
     */
    CardLayout cl;
    /**
     * Reference to the cardPanel, which uses a CardLayout that is stored in the variable cl.
     */
    JPanel cardPanel;

    /**
     * Reference to the MainFrame.
     */
    JFrame mainFrame;

    /**
     * Dimensions of the MainFrame's contentPane.
     */
    Dimension frameDimension;

    //shared objects
    JLabel top_Update_Label;
    JLabel bottom_Update_Label;
    //card 1 (menu card)
    //card 2 (settings card)
    JTextField ipAddress_Settings_Field;
    JTextField port_Settings_Field;
    JTextField nickname_Settings_Field;
    //card 3 (online game card)
    //card 4 (create card)
    JLabel recap1_Create_Label;
    JLabel recap2_Create_Label;
    JLabel recap3_Create_Label;
    JSlider numberOfPlayersSlider;
    //card 5 (join card)
    JLabel recap1_Join_Label;
    JLabel recap2_Join_Label;
    JLabel recap3_Join_Label;
    JTextField lobbyNumber_Join_Field;
    //card 6 (rejoin card)
    JLabel recap1_Rejoin_Label;
    JLabel recap2_Rejoin_Label;
    JLabel recap3_Rejoin_Label;
    JTextField lobbyNumber_Rejoin_Field;
    //card 7 (loading screen)
    JLabel progressLabel;
    JLabel messageLabel;
    JProgressBar progressBar;
    JButton back_Loading_Button;

    /**
     * The Last Valid Card of the CardLayout.
     */
    private String lastCard = MENU;

    /**
     * Used in the card 1 (MENU). Closes the mainFrame.
     */
    ActionListener quit_Menu_Button_actionListener = e -> {
        mainFrame.dispose();
    };

    /**
     * Used in the card 1. Switches to the ONLINE Card.
     */
    ActionListener online_Menu_Button_actionListener = e -> {
        cl.show(cardPanel, ONLINE);
        lastCard = MENU;
        top_Update_Label.setText(ONLINE);
        bottom_Update_Label.setText("Lorenzo, Lorenzo");
    };

    /**
     * Used in the card 1 (MENU). Loads a Local Game instance and then closes this MainFrame.
     */
    ActionListener local_Menu_Button_actionListener = e -> {
        cl.show(cardPanel, LOADING);
        lastCard = MENU;

        progressLabel.setText("starting");
        messageLabel.setText("local mode");
        top_Update_Label.setText("writing game");
        bottom_Update_Label.setText("hold on");

        SwingUtilities.invokeLater(new LocalThread());
    };

    /**
     * Used in the card 1 (MENU). Switches to the SETTINGS Card.
     */
    ActionListener Settings_Menu_Button_actionListener = e -> {
        cl.show(cardPanel, SETTINGS);
        lastCard = MENU;
        top_Update_Label.setText(SETTINGS);
        bottom_Update_Label.setText("First time here?");
    };

    /**
     * Used in the card 2 (SETTINGS). Switches back to the MENU Card.
     */
    ActionListener back_Settings_Button_actionListener = e -> {
        if (lastCard.equals(MENU))
            top_Update_Label.setText("Welcome " + Ark.nickname + " ");
        else
            top_Update_Label.setText(lastCard);
        cl.show(cardPanel, lastCard);
    };

    /**
     * Used in the card 2 (SETTINGS). Tries to write the new set contents and then goes back to the Last Valid Card.
     */
    ActionListener apply_Settings_Button_actionListener = e -> {
        boolean error = false;
        int newPort = 0;
        String newNickname = "";
        if (isValidInet4Address(ipAddress_Settings_Field.getText()) || ipAddress_Settings_Field.getText().equals("localhost")) {
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
                top_Update_Label.setText("Welcome " + Ark.nickname + " ");
            else
                top_Update_Label.setText(lastCard);
            cl.show(cardPanel, lastCard);
        }
    };

    /**
     * Used in the card 3 (ONLINE). Switches to the CREATE (online) Card.
     */
    ActionListener create_Online_Button_actionListener = e -> {
        cl.show(cardPanel, CREATE);
        lastCard = ONLINE;
        top_Update_Label.setText(CREATE);
        bottom_Update_Label.setText("Ok?");
    };

    /**
     * Used in the card 3 (ONLINE). Switches to the JOIN (online) Card.
     */
    ActionListener join_Online_Button_actionListener = e -> {
        cl.show(cardPanel, JOIN);
        lastCard = ONLINE;
        top_Update_Label.setText(JOIN);
        bottom_Update_Label.setText("Ready!");
    };

    /**
     * Used in the card 3 (ONLINE). Switches to the REJOIN (online) Card.
     */
    ActionListener rejoin_Online_Button_actionListener = e -> {
        cl.show(cardPanel, REJOIN);
        lastCard = ONLINE;
        top_Update_Label.setText(REJOIN);
        bottom_Update_Label.setText("Oh no, you crashed?");
    };

    /**
     * Used in the card 3 (ONLINE). Switches back to the MENU Card.
     */
    ActionListener back_Online_Button_actionListener = e -> {
        cl.show(cardPanel, MENU);
        lastCard = ONLINE;
        top_Update_Label.setText("Welcome " + Ark.nickname + " ");
        bottom_Update_Label.setText(" Please don't judge me ");
    };

    /**
     * Used in the card 4 (CREATE). Invokes the procedure to connect this Client to the Server and creates a new Lobby with the specified parameters.
     *
     * @see it.polimi.ingsw.client.gui.MainMenu.CreateThread
     */
    ActionListener confirm_Create_Button_actionListener = e -> {
        String nickname = Ark.nickname;
        String address = Ark.defaultAddress;
        int port = Ark.defaultPort;
        int numberOfPlayers = numberOfPlayersSlider.getValue();
        boolean solo = numberOfPlayers == 1;

        lastCard = CREATE;
        cl.show(cardPanel, LOADING);
        top_Update_Label.setText(LOADING);
        bottom_Update_Label.setText("Sit tight");
        progressBar.setValue(10);

        SwingUtilities.invokeLater(new CreateThread(address, port, nickname, numberOfPlayers, solo));
    };

    /**
     * Used in the card 4 (CREATE). Shortcut for the SETTINGS Card.
     */
    ActionListener settings_Create_Button_actionListener = e -> {
        cl.show(cardPanel, SETTINGS);
        lastCard = CREATE;
        top_Update_Label.setText(SETTINGS);
        bottom_Update_Label.setText(" Here we go again ");
    };

    /**
     * Used in the card 4 (CREATE). Switches back to the ONLINE Card.
     */
    ActionListener back_Create_Button_actionListener = e -> {
        cl.show(cardPanel, ONLINE);
        lastCard = CREATE;
        top_Update_Label.setText(ONLINE);
        bottom_Update_Label.setText("Ok");
    };

    /**
     * Used in the card 5 (JOIN). Invokes the procedure to connect this Client to the Server and join an existing Lobby with the specified parameters.
     *
     * @see it.polimi.ingsw.client.gui.MainMenu.JoinThread
     */
    ActionListener confirm_Join_Button_actionListener = e -> {
        int lobbyNumber;
        try {
            lobbyNumber = Integer.parseInt(lobbyNumber_Join_Field.getText());
            if (lobbyNumber < 0)
                JOptionPane.showMessageDialog(mainFrame, "Lobby number must be positive");
            if (lobbyNumber > 500)
                JOptionPane.showMessageDialog(mainFrame, "Lobby number must be less than 500");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Lobby number must be a number");
            return;
        }

        String nickname = Ark.nickname;
        String address = Ark.defaultAddress;
        int port = Ark.defaultPort;

        lastCard = JOIN;
        cl.show(cardPanel, LOADING);
        top_Update_Label.setText(LOADING);
        bottom_Update_Label.setText("Sit tight");

        SwingUtilities.invokeLater(new JoinThread(address, port, nickname, lobbyNumber));
    };

    /**
     * Used in the card 5 (JOIN). Shortcut for the SETTINGS Card.
     */
    ActionListener settings_Join_Button_actionListener = e -> {
        cl.show(cardPanel, SETTINGS);
        lastCard = JOIN;
        top_Update_Label.setText(SETTINGS);
        bottom_Update_Label.setText("Is this Lorenzo?");
    };

    /**
     * Used in the card 5 (JOIN). Switches back to the ONLINE Card.
     */
    ActionListener back_Join_Button_actionListener = e -> {
        cl.show(cardPanel, ONLINE);
        lastCard = JOIN;
        top_Update_Label.setText(ONLINE);
        bottom_Update_Label.setText("Roger that");
    };

    /**
     * Used in the card 6 (REJOIN). Invokes the procedure to connect this [disconnected] Client to the Server and join an existing Lobby with the specified parameters.
     *
     * @see it.polimi.ingsw.client.gui.MainMenu.RejoinThread
     */
    ActionListener confirm_Rejoin_Button_actionListener = e -> {
        int lobbyNumber;
        try {
            lobbyNumber = Integer.parseInt(lobbyNumber_Rejoin_Field.getText());
            if (lobbyNumber < 0)
                JOptionPane.showMessageDialog(mainFrame, "Lobby number must be positive");
            if (lobbyNumber > 500)
                JOptionPane.showMessageDialog(mainFrame, "Lobby number must be less than 500");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(mainFrame, "Lobby number must be a number");
            return;
        }

        String nickname = Ark.nickname;
        String address = Ark.defaultAddress;
        int port = Ark.defaultPort;

        lastCard = REJOIN;
        cl.show(cardPanel, LOADING);
        top_Update_Label.setText(LOADING);
        bottom_Update_Label.setText("Sit tight");

        SwingUtilities.invokeLater(new RejoinThread(address, port, nickname, lobbyNumber));
    };

    /**
     * Used in the card 6 (REJOIN). Shortcut for the SETTINGS Card.
     */
    ActionListener settings_Rejoin_Button_actionListener = e -> {
        cl.show(cardPanel, SETTINGS);
        lastCard = REJOIN;
        top_Update_Label.setText(SETTINGS);
        bottom_Update_Label.setText("What now?");
    };

    /**
     * Used in the card 6 (REJOIN). Switches back to the ONLINE Card.
     */
    ActionListener back_Rejoin_Button_actionListener = e -> {
        cl.show(cardPanel, ONLINE);
        lastCard = REJOIN;
        top_Update_Label.setText(ONLINE);
        bottom_Update_Label.setText("Aye aye");
    };

    /**
     * Used in the card 7 (LOADING). Only used when an error was generated. Switches back to the Last Valid Card.
     */
    ActionListener back_Loading_Button_actionListener = e -> {
        cl.show(cardPanel, lastCard);
        top_Update_Label.setText(lastCard);
        bottom_Update_Label.setText("I'm sorry");
        progressBar.setValue(0);
        progressLabel.setText("initiating request");
        messageLabel.setText("creating message");
        back_Loading_Button.setEnabled(false);
        progressLabel.setText("Again here?");
    };

    /**
     * Class that implements a Thread that initializes a Local Game instance and consequentially launches the Board.
     */
    class LocalThread implements Runnable {

        @Override
        public void run() {
            Ark.nickname = "Player";
            Ark.solo = true;
            Ark.local = true;
            Ark.reconnected = true;

            Ark.myPlayerNumber = 1;

            Ark.gameManager = new GameManager(1);
            Ark.actionManager = Ark.gameManager.getActionManager();

            Ark.gameManager.getGame().addPlayer(Ark.nickname, Ark.myPlayerNumber);

            Ark.gameManager.getGame().setLeaderCardsPickerCards(Ark.gameManager.getGame().getCurrentPlayerStartingCards());
            Ark.gameManager.getGame().setLeaderCardsPickerEnabled(true);

            MSG_UPD_Full message = Ark.gameManager.getFullModel();
            Ark.game = new GameSimplified();
            Ark.game.updateAll(message);
            Ark.myPlayerRef = Ark.game.getCurrentPlayerRef();

            Board board = new Board();
            board.changeRightCard(Board.LEADERCARDSPICKER);
            board.lastRightCard = Board.LEADERCARDSPICKER;
            board.lastLeftCard = Ark.nickname;
            board.disableBottomButtons();

            UpdateHandlerGUI updateHandler = new UpdateHandlerGUI(board);
            Ark.yourTurn = true;

            Ark.gameManager.addAllObserver(updateHandler);

            SwingUtilities.invokeLater(board);
            mainFrame.dispose();
        }
    }

    /**
     * Class that implements a Thread that connects to a Server and sends a CREATE Request.
     */
    class CreateThread implements Runnable {
        private final String address;
        private final int port;
        private final String nickname;
        private final int numberOfPlayers;
        private final boolean solo;

        public CreateThread(String address, int port, String nickname, int numberOfPlayers, boolean solo) {
            this.address = address;
            this.port = port;
            this.nickname = nickname;
            this.numberOfPlayers = numberOfPlayers;
            this.solo = solo;
        }

        @Override
        public void run() {
            try {
                Ark.socket = new Socket(address, port);

                Ark.outputStream = Ark.socket.getOutputStream();
                Ark.objectOutputStream = new ObjectOutputStream(Ark.outputStream);
                Ark.inputStream = Ark.socket.getInputStream();
                Ark.objectInputStream = new ObjectInputStream(Ark.inputStream);

                Message message = new MSG_CREATE_LOBBY(numberOfPlayers, nickname);

                Ark.send(message);

                message = (Message) Ark.objectInputStream.readObject();

                if (message.getMessageType() == MessageType.MSG_OK_CREATE) {
                    MSG_OK_CREATE msg = (MSG_OK_CREATE) message;

                    Ark.solo = solo;
                    Ark.local = false;
                    Ark.nickname = msg.getAssignedNickname();
                    Ark.reconnected = false;

                    progressBar.setIndeterminate(true);
                    progressLabel.setText("connected");
                    bottom_Update_Label.setText("waiting for players");
                    messageLabel.setText("Lobby number is: " + msg.getLobbyNumber());

                    new Thread(new SecondPartThread()).start();
                    // SwingUtilities.invokeLater(new SecondPartThread());

                } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                    SwingUtilities.invokeLater(new ErrorThread(((MSG_ERROR) message).getErrorMessage()));
                }
            } catch (IOException | ClassNotFoundException ex) {
                SwingUtilities.invokeLater(new ErrorThread("network error occurred"));
            } catch (IllegalArgumentException ex) {
                SwingUtilities.invokeLater(new ErrorThread("failed to build the message"));
            }
        }

    }

    /**
     * Class that implements a Thread that connects to a Server and sends a JOIN Request.
     */
    class JoinThread implements Runnable {
        private final String address;
        private final int port;
        private final String nickname;
        private final int lobbyNumber;

        public JoinThread(String address, int port, String nickname, int lobbyNumber) {
            this.address = address;
            this.port = port;
            this.nickname = nickname;
            this.lobbyNumber = lobbyNumber;
        }

        @Override
        public void run() {
            try {
                Ark.socket = new Socket(address, port);

                Ark.outputStream = Ark.socket.getOutputStream();
                Ark.objectOutputStream = new ObjectOutputStream(Ark.outputStream);
                Ark.inputStream = Ark.socket.getInputStream();
                Ark.objectInputStream = new ObjectInputStream(Ark.inputStream);

                Message message = new MSG_JOIN_LOBBY(nickname, lobbyNumber);

                Ark.send(message);

                messageLabel.setText("waiting for response");

                message = (Message) Ark.objectInputStream.readObject();

                if (message.getMessageType() == MessageType.MSG_OK_JOIN) {
                    MSG_OK_JOIN msg = (MSG_OK_JOIN) message;

                    Ark.nickname = msg.getAssignedNickname();
                    Ark.solo = false;
                    Ark.local = false;
                    Ark.reconnected = false;

                    progressBar.setIndeterminate(true);
                    progressLabel.setText("connected");
                    bottom_Update_Label.setText("waiting for players");
                    messageLabel.setText("you'll enter as: " + Ark.nickname);
                    back_Loading_Button.setEnabled(true);

                    new Thread(new SecondPartThread()).start();
                    // SwingUtilities.invokeLater(new SecondPartThread());

                } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                    SwingUtilities.invokeLater(new ErrorThread(((MSG_ERROR) message).getErrorMessage()));
                }
            } catch (IOException | ClassNotFoundException ex) {
                SwingUtilities.invokeLater(new ErrorThread("network error occurred"));
            } catch (IllegalArgumentException ex) {
                SwingUtilities.invokeLater(new ErrorThread("failed to build the message"));
            }
        }
    }

    /**
     * Class that implements a Thread that connects to a Server and sends a REJOIN Request.
     */
    class RejoinThread implements Runnable {
        private final String address;
        private final int port;
        private final String nickname;
        private final int lobbyNumber;

        public RejoinThread(String address, int port, String nickname, int lobbyNumber) {
            this.address = address;
            this.port = port;
            this.nickname = nickname;
            this.lobbyNumber = lobbyNumber;
        }

        @Override
        public void run() {
            try {
                Ark.socket = new Socket(address, port);

                Ark.outputStream = Ark.socket.getOutputStream();
                Ark.objectOutputStream = new ObjectOutputStream(Ark.outputStream);
                Ark.inputStream = Ark.socket.getInputStream();
                Ark.objectInputStream = new ObjectInputStream(Ark.inputStream);

                Message message = new MSG_REJOIN_LOBBY(nickname, lobbyNumber);

                Ark.send(message);

                messageLabel.setText("waiting for response");

                message = (Message) Ark.objectInputStream.readObject();

                if (message.getMessageType() == MessageType.MSG_OK_REJOIN) {
                    MSG_OK_REJOIN msg = (MSG_OK_REJOIN) message;

                    Ark.nickname = msg.getAssignedNickname();
                    Ark.solo = false;
                    Ark.local = false;
                    Ark.reconnected = true;

                    progressBar.setIndeterminate(true);
                    progressLabel.setText("connected");
                    bottom_Update_Label.setText("waiting");
                    messageLabel.setText("you'll enter as: " + Ark.nickname);

                    new Thread(new SecondPartThread()).start();
                    // SwingUtilities.invokeLater(new SecondPartThread());

                } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                    SwingUtilities.invokeLater(new ErrorThread(((MSG_ERROR) message).getErrorMessage()));
                }
            } catch (IOException | ClassNotFoundException ex) {
                SwingUtilities.invokeLater(new ErrorThread("network error occurred"));
            } catch (IllegalArgumentException ex) {
                SwingUtilities.invokeLater(new ErrorThread("failed to build the message"));
            }
        }
    }

    /**
     * Class that implements a Thread that receives an answer (OK or not) from the Server.
     * <p> Necessary for Swing to update the Progress Bar and let the paint() method complete.</p>
     */
    class SecondPartThread implements Runnable {
        @Override
        public void run() {
            Message message;
            try {
                message = (Message) Ark.objectInputStream.readObject();
                if (message.getMessageType() == MessageType.MSG_UPD_Full) {

                    Ark.game = new GameSimplified();
                    Ark.game.updateAll((MSG_UPD_Full) message);
                    Ark.myPlayerRef = Ark.game.getPlayerRef(Ark.nickname);
                    Ark.myPlayerNumber = Ark.game.getMyPlayerNumber(Ark.nickname);
                    Ark.yourTurn = Ark.game.isMyTurn(Ark.myPlayerNumber);

                    if (Ark.myPlayerNumber == 0)
                        SwingUtilities.invokeLater(new ErrorThread("corrupted game was written"));

                    Board board = new Board();

                    new Thread(new UpdateHandlerGUI(board)).start();
                    SwingUtilities.invokeLater(board);

                    mainFrame.dispose();
                } else if (message.getMessageType() == MessageType.MSG_ERROR) {
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(100);
                    progressLabel.setText("error");
                    messageLabel.setText(((MSG_ERROR) message).getErrorMessage());
                    bottom_Update_Label.setText("we're sorry");
                    back_Loading_Button.setEnabled(true);
                    SwingUtilities.invokeLater(new ErrorThread(((MSG_ERROR) message).getErrorMessage()));
                } else {
                    SwingUtilities.invokeLater(new ErrorThread("received unexpected message"));
                }
            } catch (IOException | ClassNotFoundException ex) {
                SwingUtilities.invokeLater(new ErrorThread("network error occurred"));
            } catch (IllegalArgumentException ex) {
                SwingUtilities.invokeLater(new ErrorThread("failed to build the message"));
            }
        }
    }

    /**
     * Class that implements a Thread writes the error condition that was generated by the connection threads.
     * <p> Necessary for Swing to update the Progress Bar and let the paint() method complete.</p>
     */
    class ErrorThread implements Runnable {
        private final String reason;

        public ErrorThread(String reason) {
            this.reason = reason;
        }

        @Override
        public void run() {
            progressBar.setIndeterminate(false);
            progressBar.setValue(100);
            progressLabel.setText("error");
            messageLabel.setText(this.reason);
            bottom_Update_Label.setText("we're sorry");
            back_Loading_Button.setEnabled(true);
        }
    }

    /**
     * Constructor of the MainMenu Class.
     * <p> Builds a new Frame and sets it visible. </p>
     */
    public MainMenu() {
        mainFrame = new JFrame(" Masters of Renaissance - Main Menu ");
        mainFrame.setContentPane(new mainPanel());

        mainFrame.pack();
        frameDimension = new Dimension(1290, 980);
        mainFrame.setMinimumSize(frameDimension);
        mainFrame.setResizable(false);

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    /**
     * Shortcut for launching the MainMenu instance. Testing purposes only.
     *
     * @param args argument list
     */
    public static void main(String[] args) {
        // you can write here shortcuts, like going directly to the Settings and opening multiple frames
        // only if called by the main, otherwise it must be empty
        // new MainMenu();
        // cl.show(cardPanel, CREATE);
        SwingUtilities.invokeLater(new MainMenu());
    }

    @Override
    public void run() {
        //method is empty to override Runnable interface method
    }

    /**
     * The mainPanel Class is used as the new ContentPane for the MainFrame.
     */
    class mainPanel extends JPanel {

        private Image image;

        public mainPanel() {
            image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/main_menu_bg.jpg"));

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
            top_Update_Label.setText("Welcome " + Ark.nickname + " ");
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

    /**
     * Builds the Card Layout Panel and returns it to the caller.
     *
     * @return a Panel which uses the CardLayout cl and contains the Cards 1->7
     */
    private JPanel getCardMagic() {
        cardPanel = new JPanel();
        cardPanel.setLayout(cl);
        cardPanel.setOpaque(false);

        cardPanel.add(getCard1(), MENU);

        cardPanel.add(getCard2(), SETTINGS);

        cardPanel.add(getCard3(), ONLINE);

        cardPanel.add(getCard4(), CREATE);

        cardPanel.add(getCard5(), JOIN);

        cardPanel.add(getCard6(), REJOIN);

        cardPanel.add(getCard7(), LOADING);

        return cardPanel;
    }

    /**
     * Builds the Card 1 (MENU) and returns it to the caller.
     * <p> Allows to get to SETTINGS, ONLINE, LOCAL Cards (or quit). </p>
     *
     * @return the MENU Card
     */
    private JPanel getCard1() {
        GridBagConstraints c;
        /*
            card 1: Main Menu things.
         */
        JPanel card1 = new JPanel(new GridBagLayout());
        card1.setOpaque(false);

        JButton online_Menu_Button = new JButton(ONLINE);
        online_Menu_Button.addActionListener(online_Menu_Button_actionListener);
        online_Menu_Button.setPreferredSize(new Dimension(200, 50));
        online_Menu_Button.setFont(new Font(PAPYRUS, Font.BOLD, 20));
        online_Menu_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.01;
        c.gridwidth = 2;
        c.insets = new Insets(0, 0, 5, 0);
        card1.add(online_Menu_Button, c);

        JButton local_Menu_Button = new JButton(LOCAL);
        local_Menu_Button.addActionListener(local_Menu_Button_actionListener);
        local_Menu_Button.setPreferredSize(new Dimension(200, 50));
        local_Menu_Button.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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

        JButton quit_Menu_Button = new JButton("Quit");
        quit_Menu_Button.addActionListener(quit_Menu_Button_actionListener);
        quit_Menu_Button.setPreferredSize(new Dimension(120, 40));
        quit_Menu_Button.setFont(new Font(PAPYRUS, Font.BOLD, 20));
        quit_Menu_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.weighty = 0.01;
        c.insets = new Insets(100, 0, 0, 20);
        c.anchor = GridBagConstraints.LAST_LINE_START;
        card1.add(quit_Menu_Button, c);

        JButton settings_Menu_Button = new JButton(SETTINGS);
        settings_Menu_Button.addActionListener(Settings_Menu_Button_actionListener);
        settings_Menu_Button.setPreferredSize(new Dimension(120, 40));
        settings_Menu_Button.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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

    /**
     * Builds the Card 2 (SETTINGS) and returns it to the caller.
     * <p> Allows the User to modify the defaultAddress, defaultPort, and nickname. Those new Settings can be applied or not. </p>
     *
     * @return the SETTINGS Card
     */
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

        JButton back_Settings_Button = new JButton("Back");
        back_Settings_Button.addActionListener(back_Settings_Button_actionListener);
        back_Settings_Button.setPreferredSize(new Dimension(120, 40));
        back_Settings_Button.setFont(new Font(PAPYRUS, Font.BOLD, 20));
        back_Settings_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.weighty = 0.01;
        c.gridwidth = 1;
        c.insets = new Insets(100, 0, 0, 20);
        c.anchor = GridBagConstraints.LAST_LINE_START;
        card2.add(back_Settings_Button, c);

        JButton apply_Settings_Button = new JButton("Apply");
        apply_Settings_Button.addActionListener(apply_Settings_Button_actionListener);
        apply_Settings_Button.setPreferredSize(new Dimension(120, 40));
        apply_Settings_Button.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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

    /**
     * Builds the Card 3 (ONLINE) and returns it to the caller.
     * <p> Allows to get to CREATE, JOIN, REJOIN Cards (or go back). </p>
     *
     * @return the ONLINE Card
     */
    private JPanel getCard3() {
        GridBagConstraints c;
        /*
            card 3: online game
         */

        JPanel card3 = new JPanel(new GridBagLayout());
        card3.setOpaque(false);

        JButton create_Online_Button = new JButton("Create a new Lobby");
        create_Online_Button.addActionListener(create_Online_Button_actionListener);
        create_Online_Button.setPreferredSize(new Dimension(250, 50));
        create_Online_Button.setFont(new Font(PAPYRUS, Font.BOLD, 20));
        create_Online_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.3;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(0, 0, 0, 0);
        card3.add(create_Online_Button, c);

        JButton join_Online_Button = new JButton("Join an existing Lobby");
        join_Online_Button.addActionListener(join_Online_Button_actionListener);
        join_Online_Button.setPreferredSize(new Dimension(250, 50));
        join_Online_Button.setFont(new Font(PAPYRUS, Font.BOLD, 20));
        join_Online_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0.3;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(5, 0, 0, 0);
        card3.add(join_Online_Button, c);

        JButton rejoin_Online_Button = new JButton("Reconnect to a game");
        rejoin_Online_Button.addActionListener(rejoin_Online_Button_actionListener);
        rejoin_Online_Button.setPreferredSize(new Dimension(250, 50));
        rejoin_Online_Button.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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

        JButton back_Online_Button = new JButton("Back");
        back_Online_Button.addActionListener(back_Online_Button_actionListener);
        back_Online_Button.setPreferredSize(new Dimension(120, 40));
        back_Online_Button.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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

    /**
     * Builds the Card 4 (CREATE) and returns it to the caller.
     * <p> Shows the user a Recap of the settings, and allows the Player to confirm those settings and go ahead to connect to the server. </p>
     *
     * @return the CREATE Card
     */
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
        label.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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

        recap1_Create_Label = new JLabel();
        recap1_Create_Label.setText(Ark.nickname);
        recap1_Create_Label.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        card4.add(recap1_Create_Label, c);

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

        recap2_Create_Label = new JLabel();
        recap2_Create_Label.setText(Ark.defaultAddress);
        recap2_Create_Label.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        card4.add(recap2_Create_Label, c);

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

        recap3_Create_Label = new JLabel();
        recap3_Create_Label.setText("" + Ark.defaultPort);
        recap3_Create_Label.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        card4.add(recap3_Create_Label, c);

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

        JButton settings_Create_Button = new JButton("Settings");
        settings_Create_Button.addActionListener(settings_Create_Button_actionListener);
        settings_Create_Button.setPreferredSize(new Dimension(120, 40));
        settings_Create_Button.setFont(new Font(PAPYRUS, Font.BOLD, 20));
        settings_Create_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 5;
        c.weighty = 0.01;
        c.gridwidth = 1;
        c.insets = new Insets(15, 25, 0, 0);
        c.anchor = GridBagConstraints.LINE_START;
        card4.add(settings_Create_Button, c);

        JButton confirm_Create_Button = new JButton("Confirm");
        confirm_Create_Button.addActionListener(confirm_Create_Button_actionListener);
        confirm_Create_Button.setPreferredSize(new Dimension(120, 40));
        confirm_Create_Button.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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

        JButton back_Create_Button = new JButton("Back");
        back_Create_Button.addActionListener(back_Create_Button_actionListener);
        back_Create_Button.setPreferredSize(new Dimension(120, 40));
        back_Create_Button.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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

    /**
     * Builds the Card 5 (JOIN) and returns it to the caller.
     * <p> Shows the user a Recap of the settings, and allows the Player to confirm those settings and go ahead to connect to the server. </p>
     *
     * @return the JOIN Card
     */
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
        label.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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


        recap1_Join_Label = new JLabel();
        recap1_Join_Label.setText(Ark.nickname);
        recap1_Join_Label.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        card5.add(recap1_Join_Label, c);

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

        recap2_Join_Label = new JLabel();
        recap2_Join_Label.setText(Ark.defaultAddress);
        recap2_Join_Label.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        card5.add(recap2_Join_Label, c);

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

        recap3_Join_Label = new JLabel();
        recap3_Join_Label.setText("" + Ark.defaultPort);
        recap3_Join_Label.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        card5.add(recap3_Join_Label, c);

        JLabel lobbyNumberLabel = new JLabel();
        lobbyNumberLabel.setText("Lobby number:");
        lobbyNumberLabel.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.weighty = 0.1;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        card5.add(lobbyNumberLabel, c);

        lobbyNumber_Join_Field = new JTextField(4);
        lobbyNumber_Join_Field.setText("343");
        lobbyNumber_Join_Field.setFont(new Font(TIMES, Font.PLAIN, 20));
        lobbyNumber_Join_Field.setHorizontalAlignment(SwingConstants.CENTER);
        lobbyNumber_Join_Field.setOpaque(false);
        lobbyNumber_Join_Field.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 35, 0);
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 1;
        c.gridy = 4;
        c.weighty = 0.1;
        c.gridwidth = 1;
        card5.add(lobbyNumber_Join_Field, c);

        JButton settings_Join_Button = new JButton("Settings");
        settings_Join_Button.addActionListener(settings_Join_Button_actionListener);
        settings_Join_Button.setPreferredSize(new Dimension(120, 40));
        settings_Join_Button.setFont(new Font(PAPYRUS, Font.BOLD, 20));
        settings_Join_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 5;
        c.weighty = 0.01;
        c.gridwidth = 1;
        c.insets = new Insets(15, 25, 0, 0);
        c.anchor = GridBagConstraints.LINE_START;
        card5.add(settings_Join_Button, c);

        JButton confirm_Join_Button = new JButton("Confirm");
        confirm_Join_Button.addActionListener(confirm_Join_Button_actionListener);
        confirm_Join_Button.setPreferredSize(new Dimension(120, 40));
        confirm_Join_Button.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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

        JButton back_Join_Button = new JButton("Back");
        back_Join_Button.addActionListener(back_Join_Button_actionListener);
        back_Join_Button.setPreferredSize(new Dimension(120, 40));
        back_Join_Button.setFont(new Font(PAPYRUS, Font.BOLD, 20));
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

    /**
     * Builds the Card 6 (REJOIN) and returns it to the caller.
     * <p> Shows the user a Recap of the settings, and allows the Player to confirm those settings and go ahead to connect to the server. </p>
     *
     * @return the REJOIN Card
     */
    private JPanel getCard6() {
        GridBagConstraints c;
         /*
            card 6: rejoin
         */
        JPanel card6 = new JPanel(new GridBagLayout());
        card6.setOpaque(false);

        JLabel label = new JLabel();
        label.setText(" Continue with these settings? ");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font(PAPYRUS, Font.BOLD, 20));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.1;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.PAGE_END;
        card6.add(label, c);

        JLabel nameLabel = new JLabel();
        nameLabel.setText("name:");
        nameLabel.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.LINE_END;
        card6.add(nameLabel, c);


        recap1_Rejoin_Label = new JLabel();
        recap1_Rejoin_Label.setText(Ark.nickname);
        recap1_Rejoin_Label.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        card6.add(recap1_Rejoin_Label, c);

        JLabel addressLabel = new JLabel();
        addressLabel.setText("ip:");
        addressLabel.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.LINE_END;
        card6.add(addressLabel, c);

        recap2_Rejoin_Label = new JLabel();
        recap2_Rejoin_Label.setText(Ark.defaultAddress);
        recap2_Rejoin_Label.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        card6.add(recap2_Rejoin_Label, c);

        JLabel portLabel = new JLabel();
        portLabel.setText("port:");
        portLabel.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.LINE_END;
        card6.add(portLabel, c);

        recap3_Rejoin_Label = new JLabel();
        recap3_Rejoin_Label.setText("" + Ark.defaultPort);
        recap3_Rejoin_Label.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.weighty = 0.1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        card6.add(recap3_Rejoin_Label, c);

        JLabel lobbyNumberLabel = new JLabel();
        lobbyNumberLabel.setText("Lobby number:");
        lobbyNumberLabel.setFont(new Font(TIMES, Font.PLAIN, 18));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.weighty = 0.1;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        card6.add(lobbyNumberLabel, c);

        lobbyNumber_Rejoin_Field = new JTextField(4);
        lobbyNumber_Rejoin_Field.setText("343");
        lobbyNumber_Rejoin_Field.setFont(new Font(TIMES, Font.PLAIN, 20));
        lobbyNumber_Rejoin_Field.setHorizontalAlignment(SwingConstants.CENTER);
        lobbyNumber_Rejoin_Field.setOpaque(false);
        lobbyNumber_Rejoin_Field.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 35, 0);
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 1;
        c.gridy = 4;
        c.weighty = 0.1;
        c.gridwidth = 1;
        card6.add(lobbyNumber_Rejoin_Field, c);

        JButton settings_Rejoin_Button = new JButton("Settings");
        settings_Rejoin_Button.addActionListener(settings_Rejoin_Button_actionListener);
        settings_Rejoin_Button.setPreferredSize(new Dimension(120, 40));
        settings_Rejoin_Button.setFont(new Font(PAPYRUS, Font.BOLD, 20));
        settings_Rejoin_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 5;
        c.weighty = 0.01;
        c.gridwidth = 1;
        c.insets = new Insets(15, 25, 0, 0);
        c.anchor = GridBagConstraints.LINE_START;
        card6.add(settings_Rejoin_Button, c);

        JButton confirm_Rejoin_Button = new JButton("Confirm");
        confirm_Rejoin_Button.addActionListener(confirm_Rejoin_Button_actionListener);
        confirm_Rejoin_Button.setPreferredSize(new Dimension(120, 40));
        confirm_Rejoin_Button.setFont(new Font(PAPYRUS, Font.BOLD, 20));
        confirm_Rejoin_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 5;
        c.weighty = 0.01;
        c.gridwidth = 1;
        c.insets = new Insets(15, 0, 0, 25);
        c.anchor = GridBagConstraints.LINE_END;
        card6.add(confirm_Rejoin_Button, c);


        JLabel emptyLabel = new JLabel("");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 6;
        c.weighty = 1;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0, 160, 0, 160);
        card6.add(emptyLabel, c);

        JButton back_Rejoin_Button = new JButton("Back");
        back_Rejoin_Button.addActionListener(back_Rejoin_Button_actionListener);
        back_Rejoin_Button.setPreferredSize(new Dimension(120, 40));
        back_Rejoin_Button.setFont(new Font(PAPYRUS, Font.BOLD, 20));
        back_Rejoin_Button.setBackground(new Color(231, 210, 181));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 7;
        c.weighty = 0.1;
        c.insets = new Insets(4, 0, 0, 0);
        c.anchor = GridBagConstraints.LAST_LINE_START;
        card6.add(back_Rejoin_Button, c);

        return card6;
    }

    /**
     * Builds the Card 7 (LOADING) and returns it to the caller.
     * <p> By default it shows a Progress Bar. If Any error occurred, a Back button will be unlocked. </p>
     *
     * @return the LOADING Card
     */
    private JPanel getCard7() {
        GridBagConstraints c;
         /*
            card 6: rejoin
         */
        JPanel card7 = new JPanel(new GridBagLayout());
        card7.setOpaque(false);

        progressLabel = new JLabel();
        progressLabel.setText("initiating request");
        progressLabel.setHorizontalAlignment(SwingConstants.CENTER);
        progressLabel.setFont(new Font(PAPYRUS, Font.BOLD, 24));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weighty = 0.2;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        card7.add(progressLabel, c);

        messageLabel = new JLabel("creating message");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setFont(new Font(PAPYRUS, Font.BOLD, 24));
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weighty = 0.2;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        card7.add(messageLabel, c);

        progressBar = new JProgressBar();
        progressBar.setOrientation(JProgressBar.HORIZONTAL);
        progressBar.setPreferredSize(new Dimension(200, 20));
        progressBar.setForeground(new Color(178, 49, 35));
        progressBar.setOpaque(false);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.weighty = 0.5;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        card7.add(progressBar, c);

        JLabel emptyLabel = new JLabel("");
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0, 160, 0, 160);
        card7.add(emptyLabel, c);

        back_Loading_Button = new JButton("Back");
        back_Loading_Button.addActionListener(back_Loading_Button_actionListener);
        back_Loading_Button.setPreferredSize(new Dimension(120, 40));
        back_Loading_Button.setFont(new Font(PAPYRUS, Font.BOLD, 20));
        back_Loading_Button.setBackground(new Color(231, 210, 181));
        back_Loading_Button.setEnabled(false);
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 4;
        c.weighty = 0.1;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.LAST_LINE_START;
        card7.add(back_Loading_Button, c);

        return card7;
    }

    /**
     * Updates the Recap labels in the CREATE, JOIN and REJOIN Cards.
     */
    private void updateCreateRecapLabel() {
        recap1_Create_Label.setText(Ark.nickname);
        recap2_Create_Label.setText(Ark.defaultAddress);
        recap3_Create_Label.setText("" + Ark.defaultPort);

        recap1_Join_Label.setText(Ark.nickname);
        recap2_Join_Label.setText(Ark.defaultAddress);
        recap3_Join_Label.setText("" + Ark.defaultPort);

        recap1_Rejoin_Label.setText(Ark.nickname);
        recap2_Rejoin_Label.setText(Ark.defaultAddress);
        recap3_Rejoin_Label.setText("" + Ark.defaultPort);
    }

    /**
     * Checks if a given String is a Valid IP address, between 0.0.0.0 and 255.255.255.255
     *
     * @param ip the input String
     * @return True if the given String is a valid IP address, False otherwise
     */
    private static boolean isValidInet4Address(String ip) {
        String[] groups = ip.split("\\.");

        if (groups.length != 4) {
            return false;
        }

        try {
            return Arrays.stream(groups)
                    .filter(s -> s.length() >= 1)
                    .map(Integer::parseInt)
                    .filter(i -> (i >= 0 && i <= 255))
                    .count() == 4;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}