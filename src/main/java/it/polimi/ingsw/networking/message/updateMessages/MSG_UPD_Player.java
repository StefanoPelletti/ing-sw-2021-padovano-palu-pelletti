package it.polimi.ingsw.networking.message.updateMessages;

import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.model.LeaderCard;

import java.io.Serializable;

public class MSG_UPD_Player extends Message implements Serializable {

    private final int vp;
    private final int playerNumber;
    private final String nickname;
    private final int position;
    private final LeaderCard[] leaderCards;
    private final boolean card1Enabled;
    private final boolean card2Enabled;

    public MSG_UPD_Player(int vp, int playerNumber, String nickname, int position, LeaderCard[] leaderCards) {
        super(MessageType.MSG_UPD_Player);

        this.card1Enabled= leaderCards[0] != null && leaderCards[0].isEnabled();
        this.card2Enabled= leaderCards[1] != null && leaderCards[1].isEnabled();

        this.vp = vp;
        this.playerNumber = playerNumber;
        this.nickname = nickname;
        this.position = position;
        this.leaderCards = new LeaderCard[2];
        System.arraycopy(leaderCards, 0, this.leaderCards, 0, 2);
    }

    public int getVp() {
        return this.vp;
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

    public boolean isCard1Enabled() { return this.card1Enabled;}

    public boolean isCard2Enabled() { return this.card2Enabled;}
}