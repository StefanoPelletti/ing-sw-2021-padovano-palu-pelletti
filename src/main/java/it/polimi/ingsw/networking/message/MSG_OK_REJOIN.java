package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_OK_REJOIN extends Message implements Serializable {

    private final String assignedNickname;

    /**
     * MSG_OK_REJOIN is sent by the ClientHandler to the Client.
     * It indicates the positive result of the REJOIN routine.
     *
     * @param assignedNickname the assigned nickname by the Lobby
     */
    public MSG_OK_REJOIN(String assignedNickname) {
        super(MessageType.MSG_OK_REJOIN);

        this.assignedNickname = assignedNickname;
    }

    public String getAssignedNickname() {
        return this.assignedNickname;
    }
}