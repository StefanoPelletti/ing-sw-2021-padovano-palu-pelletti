package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_OK_CREATE extends Message implements Serializable {

    private final int lobbyNumber;

    public MSG_OK_CREATE(int lobbyNumber) {
        super(MessageType.MSG_OK_CREATE);

        if (lobbyNumber < 0)
            throw new IllegalArgumentException();

        this.lobbyNumber = lobbyNumber;
    }

    public int getLobbyNumber() {
        return this.lobbyNumber;
    }

    public MessageType getMessageType() {
        return super.getMessageType();
    }
}