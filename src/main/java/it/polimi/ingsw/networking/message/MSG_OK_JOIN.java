package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_OK_JOIN extends Message implements Serializable {

    private final String assignedNickname;

    /**
     * MSG_OK_JOIN is sent by the ClientHandler to the Client
     *  to indicate the correcting result of the JOIN routine
     * @param assignedNickname the assigned nickname by the Game
     * note: assignedNickname may be the same nickname requested by the player
     */
    public MSG_OK_JOIN(String assignedNickname) {
        super(MessageType.MSG_OK_JOIN);

        this.assignedNickname = assignedNickname;
    }

    public String getAssignedNickname() {
        return this.assignedNickname;
    }
}