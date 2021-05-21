package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_OK_REJOIN extends Message implements Serializable {

    private final String nickname;

    public MSG_OK_REJOIN(String nickname) {
        super(MessageType.MSG_OK_REJOIN);

        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
    }
}