package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_CREATE_LOBBY extends Message implements Serializable {

    private final int numOfPlayers;
    private final String nickname;

    public MSG_CREATE_LOBBY(int numOfPlayers, String nickname) {
        super(MessageType.MSG_CREATE_LOBBY);

        if (numOfPlayers <= 0 || numOfPlayers > 4)
            throw new IllegalArgumentException();
        if (nickname == null)
            throw new IllegalArgumentException();

        this.numOfPlayers = numOfPlayers;
        this.nickname = nickname;
    }

    public int getNumOfPlayers() {
        return this.numOfPlayers;
    }

    public String getNickname() {
        return this.nickname;
    }
}
