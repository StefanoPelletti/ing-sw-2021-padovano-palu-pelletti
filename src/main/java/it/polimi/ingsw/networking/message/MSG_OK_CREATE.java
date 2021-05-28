package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_OK_CREATE extends Message implements Serializable {

    private final int lobbyNumber;

    /**
     * MSG_OK_CREATE is sent by the ClientHandler to the Client
     *  to indicate the correcting result of the CREATE routine
     * @param lobbyNumber the assigned nickname by the Game
     * @throws IllegalArgumentException if lobbyNumber is less than 0
     * note: assignedNickname may be the same nickname requested by the player
     */
    public MSG_OK_CREATE(int lobbyNumber) {
        super(MessageType.MSG_OK_CREATE);

        if (lobbyNumber < 0)
            throw new IllegalArgumentException();

        this.lobbyNumber = lobbyNumber;
    }

    public int getLobbyNumber() {
        return this.lobbyNumber;
    }
}