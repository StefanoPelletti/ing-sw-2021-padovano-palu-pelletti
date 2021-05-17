package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_JOIN_LOBBY extends Message implements Serializable {

    private final String nickname;
    private final int lobbyNumber;

    public MSG_JOIN_LOBBY(String nickname, int lobbyNumber) {
        super(MessageType.MSG_JOIN_LOBBY);

        if (lobbyNumber < 0)
            throw new IllegalArgumentException();
        if (nickname == null)
            throw new IllegalArgumentException();

        this.nickname = nickname;
        this.lobbyNumber = lobbyNumber;
    }

    public String getNickname() {
        return this.nickname;
    }

    public int getLobbyNumber() {
        return this.lobbyNumber;
    }

    public MessageType getMessageType() {
        return super.getMessageType();
    }
}
