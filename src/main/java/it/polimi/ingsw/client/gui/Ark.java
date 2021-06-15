package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.modelSimplified.GameSimplified;
import it.polimi.ingsw.client.modelSimplified.PlayerSimplified;
import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.actionMessages.ActionMessage;
import it.polimi.ingsw.server.controller.ActionManager;
import it.polimi.ingsw.server.controller.GameManager;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class Ark {
    static String defaultAddress = "localhost";
    static int defaultPort = 43210;
    static String nickname = System.getProperty("user.name");
    static int myPlayerNumber;

    static Socket socket;
    static OutputStream outputStream;
    static ObjectOutputStream objectOutputStream;
    static InputStream inputStream;
    static ObjectInputStream objectInputStream;

    static boolean solo;
    static boolean local;
    static boolean yourTurn;
    static boolean reconnected;
    static boolean action;
    static boolean triedAction;


    static boolean goneWell;
    static String string;

    static GameSimplified game;
    static PlayerSimplified myPlayerRef;


    static GameManager gameManager;
    static ActionManager actionManager;


    public static void sweep() {
        //shared var
        myPlayerNumber = 0;
        nickname = System.getProperty("user.name");;
        yourTurn = false;
        local = false;
        action = false;
        triedAction = false;

        //online var
        game = null;
        myPlayerRef = null;
        reconnected = false;
        Ark.closeStreams();

        //local ref
        gameManager = null;
        actionManager = null;
    }

    /**
     * Closes the Streams and the Socket.
     * Catches and prints the Stack Trace of an eventual IOException.
     */
    public static void closeStreams() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
                outputStream.close();
                inputStream.close();
                objectOutputStream.close();
                objectInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send(Message message) throws IOException {

        if(!Ark.local) {
            Ark.objectOutputStream.reset();
            Ark.objectOutputStream.writeObject(message);
            Ark.objectOutputStream.flush();
        }
        else {
            Ark.actionManager.onMessage((ActionMessage) message);
        }
    }


    public static void addPadding(JComponent object, int height, int width, int maximumWidth, int maximumHeight) {
        GridBagConstraints c;

        c = new GridBagConstraints(); //padding horizontal
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = 1;
        c.gridwidth = maximumWidth;
        object.add(Box.createHorizontalStrut(width),c);

        c = new GridBagConstraints(); //padding vertical
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.gridheight = maximumHeight;
        c.gridwidth = 1;
        object.add(Box.createVerticalStrut(height),c);
    }
}
