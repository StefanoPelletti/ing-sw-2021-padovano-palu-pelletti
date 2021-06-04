package it.polimi.ingsw.networking.message.initMessages;

import it.polimi.ingsw.networking.ClientHandler;
import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;

import java.io.Serializable;

public class MSG_JOIN_LOBBY extends InitMessage implements Serializable {

    private final String nickname;
    private final int lobbyNumber;

    /**
     * MSG_JOIN_LOBBY is sent by the Client to the ClientHandler.
     * It requests the ClientHandler to join a Lobby with the specified number.
     * See the ClientHandler run() method, FIRST BLOCK, JOIN part.
     * @param nickname The name of the player trying to join the lobby.
     * @param lobbyNumber The number of the lobby to connect to.
     * @throws IllegalArgumentException If the message is build with: <ul>
     * <li> lobbyNumber less than 0
     * <li> nickname is null.
     */
    public MSG_JOIN_LOBBY(String nickname, int lobbyNumber) {
        super(MessageType.MSG_JOIN_LOBBY);

        if (lobbyNumber < 0)
            throw new IllegalArgumentException();
        if (nickname == null)
            throw new IllegalArgumentException();

        this.nickname = nickname;
        this.lobbyNumber = lobbyNumber;
    }

    public String getNickname() {
        return this.nickname;
    }

    public int getLobbyNumber() {
        return this.lobbyNumber;
    }

    @Override
    public boolean execute(ClientHandler clientHandler) {
        return clientHandler.joinLobby(this.lobbyNumber, this.nickname);
    }
}
