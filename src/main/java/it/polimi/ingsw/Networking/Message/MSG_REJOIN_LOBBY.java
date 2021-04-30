package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_REJOIN_LOBBY extends Message implements Serializable {

    private final String nickname;
    private final int lobbyNumber;

    public MSG_REJOIN_LOBBY(String nickname, int lobbyNumber) {
        super(MessageType.MSG_REJOIN_LOBBY);

        this.nickname=nickname;
        this.lobbyNumber=lobbyNumber;
    }

    public String getNickname() { return this.nickname; }
    public int getLobbyNumber() { return this.lobbyNumber; }

    public MessageType getMessageType() { return super.getMessageType(); }
}