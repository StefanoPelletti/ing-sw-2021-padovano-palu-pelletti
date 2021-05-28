package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_OK_JOIN extends Message implements Serializable {

    private final String assignedNickname;

    /**
     * MSG_OK_JOIN is sent by the ClientHandler to the Client.
     * it indicates the positive result of the JOIN routine.
     * Note: assignedNickname may be the same nickname requested by the player.
     * @param assignedNickname The assigned nickname by the Lobby.
     */
    public MSG_OK_JOIN(String assignedNickname) {
        super(MessageType.MSG_OK_JOIN);

        this.assignedNickname = assignedNickname;
    }

    public String getAssignedNickname() {
        return this.assignedNickname;
    }
}