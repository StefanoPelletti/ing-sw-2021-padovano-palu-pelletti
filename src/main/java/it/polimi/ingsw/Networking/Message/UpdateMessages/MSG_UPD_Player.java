package it.polimi.ingsw.Networking.Message.UpdateMessages;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Model.LeaderCard;

import java.io.Serializable;

public class MSG_UPD_Player extends Message implements Serializable {

    private final int VP;
    private final int playerNumber;
    private final String nickname;
    private final int position;
    private final LeaderCard[] leaderCards;

    public MSG_UPD_Player(int VP, int playerNumber, String nickname, int position, LeaderCard[] leaderCards) {
        super(MessageType.MSG_UPD_Player);

        this.VP = VP;
        this.playerNumber = playerNumber;
        this.nickname = nickname;
        this.position = position;
        this.leaderCards = new LeaderCard[2];
        System.arraycopy(leaderCards, 0, this.leaderCards, 0, 2);
    }

    public int getVP() {
        return this.VP;
    }

    public int getPlayerNumber() {
        return this.playerNumber;
    }

    public String getNickname() {
        return this.nickname;
    }

    public int getPosition() {
        return this.position;
    }

    public LeaderCard[] getLeaderCards() {
        return this.leaderCards;
    }

    public MessageType getMessageType() {
        return super.getMessageType();
    }
}