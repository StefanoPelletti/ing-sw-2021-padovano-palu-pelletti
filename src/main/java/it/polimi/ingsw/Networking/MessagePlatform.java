package it.polimi.ingsw.Networking;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Server.Utils.ModelObserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessagePlatform implements ModelObserver {

    List<Message> list;
    int activePlayers;
    int t;
    boolean grabber[];

    public MessagePlatform(int activePlayers)
    {
        list = new ArrayList<>();
        this.activePlayers = activePlayers;
        t = activePlayers;
        grabber = new boolean[activePlayers];
        //grabber is set to false
    }

    public synchronized Message waitForLatestMessage(int playerNumber) throws InterruptedException {

        while( didThreadFetch(playerNumber) ) {
            System.out.println("["+Thread.currentThread().getName()+"] going to sleep");
            this.wait();

        }

        Message msg = list.get(0);

        grabber[playerNumber-1] = true;
        t--;
        if(t==0)
        {
            list.remove(0);
            t = activePlayers;
            regenerate();
            this.notifyAll();
        }

        return msg;
    }

    private void regenerate() {
        Arrays.fill(grabber, false);
    }

    private boolean didThreadFetch(int playerNumber) {
        return ( grabber[playerNumber - 1] || list.isEmpty() );
    }

    public synchronized void update(Message message)
    {
        if(list.isEmpty()) {
            this.notifyAll();
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