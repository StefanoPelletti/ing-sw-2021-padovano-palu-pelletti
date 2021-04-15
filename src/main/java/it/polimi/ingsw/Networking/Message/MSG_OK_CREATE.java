package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_OK_CREATE extends Message implements Serializable {
    private final int lobbyNumber;

    public MSG_OK_CREATE(int lobbyNumber)
    {
        super(MessageType.MSG_OK_CREATE);
        this.lobbyNumber=lobbyNumber;
    }

    public MessageType getMessageType() { return super.getMessageType();}
    public int getLobbyNumber() { return this.lobbyNumber; }
}
