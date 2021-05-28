package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_OK_REJOIN extends Message implements Serializable {

    private final String nickname;

    /**
     * MSG_OK_REJOIN is sent by the ClientHandler to the Client.
     * It indicates the positive result of the REJOIN routine.
     * @param nickname The assigned nickname by the Lobby.
     */
    public MSG_OK_REJOIN(String nickname) {
        super(MessageType.MSG_OK_REJOIN);

        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
    }
}