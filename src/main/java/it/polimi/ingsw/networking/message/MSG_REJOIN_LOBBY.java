package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_REJOIN_LOBBY extends Message implements Serializable {

    private final String nickname;
    private final int lobbyNumber;

    /**
     * MSG_REJOIN_LOBBY is sent by the Client to a ClientHandler
     *  It requests the ClientHandler to reconnect a Client to a specific Lobby.
     * see the ClientHandler run() method, FIRST BLOCK, REJOIN part
     * @param nickname the nickname of the player requesting to be reconnected
     * @param lobbyNumber the number of the Lobby specified by the player
     * note: a player may reconnect without specifying the (i) after his name
     */
    public MSG_REJOIN_LOBBY(String nickname, int lobbyNumber) {
        super(MessageType.MSG_REJOIN_LOBBY);

        this.nickname = nickname;
        this.lobbyNumber = lobbyNumber;
    }

    public String getNickname() {
        return this.nickname;
    }

    public int getLobbyNumber() {
        return this.lobbyNumber;
    }
}