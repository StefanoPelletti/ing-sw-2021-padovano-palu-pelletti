package it.polimi.ingsw.Server;

import it.polimi.ingsw.Networking.*;
import it.polimi.ingsw.Server.Controller.*;
import it.polimi.ingsw.Networking.Message.*;
import it.polimi.ingsw.Server.Model.LeaderCard;
import it.polimi.ingsw.Server.Model.Player;

import java.util.*;

import java.net.*;
import java.io.*;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final OutputStream outputStream; // = socket.getOutputStream();
    private final ObjectOutputStream objectOutputStream;// = new ObjectOutputStream(outputStream);
    private final InputStream inputStream;// = socket.getInputStream();
    private final ObjectInputStream objectInputStream;// = new ObjectInputStream(inputStream);

    private final GameManager gameManager;
    private final ActionManager actionManager;
    private final Lobby lobby;
    private final String nickname;
    private Player player;




    public ClientHandler( Socket clientSocket, GameManager gameManager, Lobby lobby, String nickname,
                          OutputStream outputStream, ObjectOutputStream objectOutputStream,
                          InputStream inputStream, ObjectInputStream objectInputStream)
    {
        this.clientSocket = clientSocket;
        this.gameManager = gameManager;
        this.actionManager = gameManager.getActionManager();
        this.lobby = lobby;
        this.nickname=nickname;
        this.player=null;
        this.outputStream = outputStream;
        this.objectOutputStream = objectOutputStream;
        this.inputStream = inputStream;
        this.objectInputStream = objectInputStream;
    }

    public void run() {

        ArrayList<LeaderCard> cards = gameManager.pickFourLeaderCards();
        try
        {
            MSG_INIT_LEADERCARDS_REQUEST message = new MSG_INIT_LEADERCARDS_REQUEST(cards);
            objectOutputStream.writeObject(message);

            Message msg = (Message) objectInputStream.readObject();
            MSG_INIT_CHOOSE_LEADERCARDS mesage = (MSG_INIT_CHOOSE_LEADERCARDS) msg;
            gameManager.setLeaderCards(mesage.getCards(), this.nickname);

            if ( gameManager.getGame().getPlayer(this.nickname).getPlayerNumber() == 2)
            {

            }

        }
        catch(IOException | ClassNotFoundException e)
        {

        }
    }

    public void update(Message message)
    {

    }

    public boolean callController(Message message)
    {
        if(player == null) getPlayer();

        gameManager.resetErrorObject();

        switch (message.getMessageType())
        {
            case MSG_INIT_CHOOSE_RESOURCE:
                return actionManager.chooseResource(player, (MSG_INIT_CHOOSE_RESOURCE) message);
            case MSG_INIT_CHOOSE_LEADERCARDS:
                return actionManager.chooseLeaderCard(player, (MSG_INIT_CHOOSE_LEADERCARDS) message);
            case MSG_ACTION_ACTIVATE_LEADERCARD:
                return actionManager.activateLeaderCard(player, (MSG_ACTION_ACTIVATE_LEADERCARD) message);
            case MSG_ACTION_DISCARD_LEADERCARD:
                return actionManager.discardLeaderCard(player, (MSG_ACTION_DISCARD_LEADERCARD) message);
            case MSG_ACTION_CHANGE_DEPOT_CONFIG:
                return actionManager.changeDepotConfig(player, (MSG_ACTION_CHANGE_DEPOT_CONFIG) message);
            case MSG_ACTION_ACTIVATE_PRODUCTION:
                return actionManager.activateProduction(player, (MSG_ACTION_ACTIVATE_PRODUCTION) message);
            case MSG_ACTION_BUY_DEVELOPMENT_CARD:
                return actionManager.buyDevelopmentCard(player);
            case MSG_ACTION_GET_MARKET_RESOURCES:
                return actionManager.getMarketResources(player, (MSG_ACTION_GET_MARKET_RESOURCES) message);
            case MSG_ACTION_MARKET_CHOICE:
                return actionManager.newChoiceMarket(player, (MSG_ACTION_MARKET_CHOICE) message);
            case MSG_ACTION_CHOOSE_DEVELOPMENT_CARD:
                return actionManager.chooseDevelopmentCard(player, (MSG_ACTION_CHOOSE_DEVELOPMENT_CARD) message );
            case MSG_ACTION_ENDTURN:
                gameManager.endTurn();
                return true;
        }
        return false;
    }

    private void getPlayer()
    {
        this.player = gameManager.getGame().getPlayer(this.nickname);
    }
}
