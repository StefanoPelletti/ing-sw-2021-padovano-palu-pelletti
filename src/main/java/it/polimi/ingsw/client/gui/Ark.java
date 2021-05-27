package it.polimi.ingsw.client.gui;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Ark {
    static String defaultAddress = "localhost";
    static int defaultPort = 43210;
    static String nickname = System.getProperty("user.name");
    static int playerNumber;

    static Socket socket;
    static OutputStream outputStream;
    static ObjectOutputStream objectOutputStream;
    static InputStream inputStream;
    static ObjectInputStream objectInputStream;

}
