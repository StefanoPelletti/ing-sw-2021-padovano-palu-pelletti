package it.polimi.ingsw.Networking.Message.UpdateMessages;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;

import java.io.Serializable;

public class MSG_UPD_Player extends Message implements Serializable {
    private final int VP;
    private final int playerNumber;
    private final String nickname;
    private final int position;

    public MSG_UPD_Player(int VP, int playerNumber, String nickname, int position)
    {
        super(MessageType.MSG_UPD_Player);
        this.VP = VP;
        this.playerNumber = playerNumber;
        this.nickname = nickname;
        this.position = position;
    }

    public int getVP() { return this.VP; }
    public int getPlayerNumber() { return this.playerNumber;}
    public String getNickname() { return this.nickname;}
    public int getPosition() { return this.position; }

    public MessageType getMessageType() { return super.getMessageType();}
}
