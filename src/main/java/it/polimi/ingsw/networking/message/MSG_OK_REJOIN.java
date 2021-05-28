package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_OK_REJOIN extends Message implements Serializable {

    private final String nickname;

    /**
     * MSG_OK_REJOIN is sent by the ClientHandler to the Client
     *  to indicate the correcting result of the REJOIN routine
     * @param nickname the assigned nickname by the Game
     */
    public MSG_OK_REJOIN(String nickname) {
        super(MessageType.MSG_OK_REJOIN);

        this.nickname = nickname;
    }

    public String getNickname() {
        return this.nickname;
    }
}