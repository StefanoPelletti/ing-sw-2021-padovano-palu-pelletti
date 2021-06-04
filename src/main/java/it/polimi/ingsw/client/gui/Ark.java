package it.polimi.ingsw.client.gui;

import it.polimi.ingsw.client.modelSimplified.GameSimplified;
import it.polimi.ingsw.client.modelSimplified.PlayerSimplified;
import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.server.controller.ActionManager;
import it.polimi.ingsw.server.controller.GameManager;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;

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

    static GameSimplified game;
    static PlayerSimplified myPlayerRef;


    static GameManager gameManager;
    static ActionManager actionManager;


    public static void sweep() {
        //shared var
        myPlayerNumber = 0;

        //online var

        //local ref

    }

    public static void send(Message message) throws IOException {
        Ark.objectOutputStream.reset();
        Ark.objectOutputStream.writeObject(message);
        Ark.objectOutputStream.flush();
    }
}
