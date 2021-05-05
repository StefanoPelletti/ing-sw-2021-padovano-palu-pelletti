package it.polimi.ingsw.Networking;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Server.Utils.ModelObserver;

import java.util.ArrayList;
import java.util.List;

public class MessagePlatform implements ModelObserver {

    List<Message> list;
    int activePlayers;
    int t;

    public MessagePlatform(int activePlayers)
    {
        list = new ArrayList<Message>();
        this.activePlayers = activePlayers;
        t = activePlayers;
    }

    public synchronized Message waitForLatestMessage() throws InterruptedException {
        while( list.isEmpty() ) this.wait();

        Message msg = list.get(0);

        t--;
        if(t==0)
        {
            list.remove(0);
            t = activePlayers;
        }

        return msg;
    }

    public synchronized void update(Message message)
    {
        if(list.isEmpty()) {
            this.notify();
        }
        list.add(message);
    }

    public synchronized void decrementActivePlayers()
    {
        activePlayers--;
        if( t == activePlayers -t -1) // this should migrate che unfortunate case in which the other threads are
                                            //waiting for a disconnected thread to fetch the message
        {
            list.remove(0);
            t = activePlayers;
        }

    }

    public synchronized void incrementActivePlayers()
    {
        activePlayers++;
    }
}