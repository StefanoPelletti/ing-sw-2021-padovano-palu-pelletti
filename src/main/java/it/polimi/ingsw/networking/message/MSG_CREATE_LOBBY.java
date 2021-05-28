package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_CREATE_LOBBY extends Message implements Serializable {

    private final int numOfPlayers;
    private final String nickname;

    /**
     * MSG_CREATE_LOBBY is sent by the Client to the ClientHandler
     *  It requests the ClientHandler to create a new Lobby with specified capacity
     * see the ClientHandler run() method, FIRST BLOCK, CREATE part
     * @param numOfPlayers the Lobby capacity
     * @param nickname the nickname of the player requesting to create a new Lobby
     * @throws IllegalArgumentException if the message is build with:
     *      - numOfPlayers is not between 1 and 4 (included)
     *      - nickname is null
     */
    public MSG_CREATE_LOBBY(int numOfPlayers, String nickname) {
        super(MessageType.MSG_CREATE_LOBBY);

        if (numOfPlayers <= 0 || numOfPlayers > 4)
            throw new IllegalArgumentException();
        if (nickname == null)
            throw new IllegalArgumentException();

        this.numOfPlayers = numOfPlayers;
        this.nickname = nickname;
    }

    public int getNumOfPlayers() {
        return this.numOfPlayers;
    }

    public String getNickname() {
        return this.nickname;
    }
}
