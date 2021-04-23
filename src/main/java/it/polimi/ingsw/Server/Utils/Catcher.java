package it.polimi.ingsw.Server.Utils;

import it.polimi.ingsw.Networking.Message.Message;

import java.util.ArrayList;
import java.util.List;


// basicly gives you a public list of intercepted Model messages.
public class Catcher implements ModelObserver {
    public List<Message> messages;

    public Catcher() {
        this.messages = new ArrayList<>();
    }

    public void update(Message message) {
        messages.add(message);
    }

    public void printQueueHeaders()
    {
        for( int i = 0; i < messages.size(); i++)
        {
            System.out.println(" Received ["+i+"] : "+messages.get(i).getMessageType());
        }
    }

    public void emptyQueue()
    {
        this.messages = new ArrayList<>();
    }
}
