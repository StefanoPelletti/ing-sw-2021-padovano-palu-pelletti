package it.polimi.ingsw;

import it.polimi.ingsw.Networking.Message.MSG_ERROR;
import it.polimi.ingsw.Networking.Message.MSG_NOTIFICATION;
import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;
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
        System.out.print("\n Received " + message.getMessageType() + " ");
        if (message.getMessageType() == MessageType.MSG_ERROR)
            System.out.print(((MSG_ERROR) message).getErrorMessage());
        else if (message.getMessageType() == MessageType.MSG_NOTIFICATION)
            System.out.print(((MSG_NOTIFICATION) message).getMessage());
    }

    public void printQueueHeaders() {
        if (messages.size() > 0) {
            for (int i = 0; i < messages.size(); i++) {
                System.out.print(" \nReceived [" + i + "] : " + messages.get(i).getMessageType() + " ");
                if (messages.get(i).getMessageType() == MessageType.MSG_ERROR)
                    System.out.print(((MSG_ERROR) messages.get(i)).getErrorMessage());
            }
        } else
            System.out.println(" empty queue \n");
    }

    public void emptyQueue() {
        this.messages = new ArrayList<>();
        System.out.println(" \nQueue reset \n");
    }
}