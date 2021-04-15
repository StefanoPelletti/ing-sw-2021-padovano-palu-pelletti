package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Networking.Message.Message;

import java.util.*;

public class GameManager {

    List<Message> broadcastList;

    public GameManager()
    {
        broadcastList = new LinkedList<>();
    }

    public boolean addBroadcastMessage(Message message)
    {
        if ( message == null ) return false;
        broadcastList.add(message);
        return true;
    }

    public boolean INIT()
    {
        return true;
    }
}
