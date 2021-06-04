package it.polimi.ingsw.networking.message.initMessages;

import it.polimi.ingsw.networking.ClientHandler;
import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;

import java.io.Serializable;

public class MSG_REJOIN_LOBBY extends InitMessage implements Serializable {

    private final String nickname;
    private final int lobbyNumber;

    /**
     * MSG_REJOIN_LOBBY is sent by the Client to a ClientHandler.
     * It requests the ClientHandler to reconnect a Client to a specific Lobby.
     * See the ClientHandler run() method, FIRST BLOCK, REJOIN part.
     * Note: a player may reconnect without specifying the (number) after his name, if he was assigned so.
     * @param nickname The nickname of the player requesting to be reconnected.
     * @param lobbyNumber The number of the Lobby specified by the Player.
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

    @Override
    public boolean execute(ClientHandler clientHandler) {
        return clientHandler.rejoinLobby(this.lobbyNumber, this.nickname);
    }
}