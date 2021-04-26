package it.polimi.ingsw;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Server.Utils.ModelObserver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


// basically gives you a public list of intercepted Model messages.
public class Catcher implements ModelObserver {
    public List<Message> messages;

    public Catcher() {
        this.messages = new ArrayList<>();
    }

    public void update(Message message) {
        messages.add(message);
        System.out.println(" Received "+message.getMessageType());
    }

    public void printQueueHeaders()
    {
        if(messages.size()>0) {
            for (int i = 0; i < messages.size(); i++) {
                System.out.println(" Received [" + i + "] : " + messages.get(i).getMessageType());
            }
        }
        else
            System.out.println(" empty queue \n");
    }

    public void emptyQueue()
    {
        this.messages = new ArrayList<>();
        System.out.println(" Queue reset \n");
    }

}
