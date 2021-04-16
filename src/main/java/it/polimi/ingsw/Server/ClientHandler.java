package it.polimi.ingsw.Server;

import it.polimi.ingsw.Networking.*;
import it.polimi.ingsw.Server.Controller.*;
import it.polimi.ingsw.Networking.Message.*;
import it.polimi.ingsw.Server.Model.LeaderCard;

import java.lang.reflect.Array;
import java.util.*;

import java.net.*;
import java.io.*;

public class ClientHandler implements Runnable {

    private Socket clientSocket;

    private GameManager gameManager;
    private ActionManager actionManager;
    private Lobby lobby;
    private String nickname;
    OutputStream outputStream; // = socket.getOutputStream();
    ObjectOutputStream objectOutputStream;// = new ObjectOutputStream(outputStream);
    InputStream inputStream;// = socket.getInputStream();
    ObjectInputStream objectInputStream;// = new ObjectInputStream(inputStream);

    public ClientHandler( Socket clientSocket, GameManager gameManager, Lobby lobby, String nickname)
    {
        this.clientSocket = clientSocket;
        this.gameManager = gameManager;
        this.actionManager = gameManager.getActionManager();
        this.lobby = lobby;
        this.nickname=nickname;
    }

    public void update(){

    }

    public void run() {

        ArrayList<LeaderCard> cards = gameManager.pickFourLeaderCards();
        try
        {
            outputStream  = clientSocket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            inputStream = clientSocket.getInputStream();
            objectInputStream = new ObjectInputStream(inputStream);

            MSG_INIT_LEADERCARDS_REQUEST message = new MSG_INIT_LEADERCARDS_REQUEST(cards);
            objectOutputStream.writeObject(message);

            Message msg = (Message) objectInputStream.readObject();
            MSG_INIT_LEADERCARDS_REPLY mesage = (MSG_INIT_LEADERCARDS_REPLY) msg;
            gameManager.setLeaderCards(mesage.getCards(), this.nickname);

            if ( gameManager.getGame().getPlayer(this.nickname).getPlayerNumber() == 2)
            {

            }

        }
        catch(IOException | ClassNotFoundException e)
        {

        }

    }
}
