package it.polimi.ingsw.Networking;

import java.io.Serializable;

public class MSG_OK_CREATE extends Message implements Serializable {
    private MessageType messageType;
    private final int lobbyNumber;

    public MSG_OK_CREATE(int lobbyNumber)
    {
        super(MessageType.MSG_OK_CREATE);
        this.lobbyNumber=lobbyNumber;
    }

    public MessageType getMessageType() { return messageType; }
    public int getNumOfPlayers() { return this.lobbyNumber; }
}
