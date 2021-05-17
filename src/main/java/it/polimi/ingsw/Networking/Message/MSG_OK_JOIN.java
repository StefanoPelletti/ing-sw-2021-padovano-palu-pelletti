package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_OK_JOIN extends Message implements Serializable {

    private final String assignedNickname;

    public MSG_OK_JOIN(String assignedNickname) {
        super(MessageType.MSG_OK_JOIN);

        this.assignedNickname = assignedNickname;
    }

    public MessageType getMessageType() {
        return super.getMessageType();
    }

    public String getAssignedNickname() {
        return this.assignedNickname;
    }
}