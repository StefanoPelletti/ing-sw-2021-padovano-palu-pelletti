package it.polimi.ingsw.Networking;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Server.Utils.ModelObserver;

import java.util.ArrayList;
import java.util.List;

public class MessagePlatform implements ModelObserver {

    List<Message> list;
    int maxPlayerNumber;
    int t;
    public MessagePlatform(int maxPlayerNumber)
    {
        list = new ArrayList<Message>();
        this.maxPlayerNumber = maxPlayerNumber;
        t = maxPlayerNumber;
    }

    public synchronized Message waitForLatestMessage() throws InterruptedException {
        while( list.isEmpty() ) this.wait();

        Message msg = list.get(0);

        t--;
        if(t==0)
        {
            list.remove(0);
            t=maxPlayerNumber;
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
}