package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_OK_CREATE extends Message implements Serializable {

    private final int lobbyNumber;

    /**
     * MSG_OK_CREATE is sent by the ClientHandler to the Client.
     * It indicates the positive result of the CREATE routine.
     * Note: assignedNickname may be the same nickname requested by the player.
     * @param lobbyNumber The number of the Lobby newly created in the Server.
     * @throws IllegalArgumentException If lobbyNumber is less than 0.
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