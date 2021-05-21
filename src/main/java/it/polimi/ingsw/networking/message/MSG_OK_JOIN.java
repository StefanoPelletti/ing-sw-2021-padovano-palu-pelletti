package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_OK_JOIN extends Message implements Serializable {

    private final String assignedNickname;

    public MSG_OK_JOIN(String assignedNickname) {
        super(MessageType.MSG_OK_JOIN);

        this.assignedNickname = assignedNickname;
    }

    public String getAssignedNickname() {
        return this.assignedNickname;
    }
}