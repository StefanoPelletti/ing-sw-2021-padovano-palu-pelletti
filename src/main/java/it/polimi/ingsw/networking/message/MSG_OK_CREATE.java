package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_OK_CREATE extends Message implements Serializable {

    private final int lobbyNumber;
    private final String assignedNickname;

    /**
     * MSG_OK_CREATE is sent by the ClientHandler to the Client.
     * It indicates the positive result of the CREATE routine.
     * Note: assignedNickname may be the same nickname requested by the player.
     *
     * @param lobbyNumber The number of the Lobby newly created in the Server.
     * @throws IllegalArgumentException If lobbyNumber is less than 0.
     */
    public MSG_OK_CREATE(int lobbyNumber, String assignedNickname) {
        super(MessageType.MSG_OK_CREATE);

        this.lobbyNumber = lobbyNumber;
        this.assignedNickname = assignedNickname;
    }

    public int getLobbyNumber() {
        return this.lobbyNumber;
    }

    public String getAssignedNickname() {
        return this.assignedNickname;
    }
}