package it.polimi.ingsw.Networking;

import java.io.Serializable;

public class MSG_CREATE_LOBBY extends Message implements Serializable {
    private MessageType messageType;
    private final int numOfPlayers;
    private final String nickname;

    public MSG_CREATE_LOBBY(int numOfPlayers, String nickname)
    {
        super(MessageType.MSG_CREATE_LOBBY);
        this.numOfPlayers=numOfPlayers;
        this.nickname=nickname;
    }

    public MessageType getMessageType() { return messageType; }
    public int getNumOfPlayers() { return this.numOfPlayers; }
    public String getNickname() { return this.nickname; }
}
