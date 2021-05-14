package it.polimi.ingsw.Networking.Message.UpdateMessages;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Model.LeaderCard;

import java.io.Serializable;

public class Ping extends Message implements Serializable {

    public Ping()
    {
        super(MessageType.Ping);
    }

    public MessageType getMessageType() { return super.getMessageType();}
}
