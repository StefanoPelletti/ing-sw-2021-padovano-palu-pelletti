package it.polimi.ingsw.Networking;

import java.io.Serializable;

public class MSG_JOIN_LOBBY extends Message implements Serializable {
    private MessageType messageType;
    private final String nickname;
    private final int lobbyNumber;

    public MSG_JOIN_LOBBY(String nickname, int lobbyNumber)
    {
        super(MessageType.MSG_JOIN_LOBBY);
        this.nickname=nickname;
        this.lobbyNumber=lobbyNumber;
    }

    public MessageType getMessageType() { return messageType; }
    public String getNickname() { return this.nickname; }
    public int getLobbyNumber() { return this.lobbyNumber; }
}
