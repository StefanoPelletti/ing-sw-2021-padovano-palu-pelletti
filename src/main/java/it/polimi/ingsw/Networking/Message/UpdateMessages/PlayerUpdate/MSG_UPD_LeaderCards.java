package it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Model.LeaderCard;

import java.io.Serializable;

public class MSG_UPD_LeaderCards extends Message implements Serializable {

    private final LeaderCard leaderCard;

    public MSG_UPD_LeaderCards(LeaderCard leaderCard)
    {
        super(MessageType.MSG_UPD_LeaderCards);

        this.leaderCard = leaderCard;
    }

    public LeaderCard getLeaderCard() { return this.leaderCard;}

    public MessageType getMessageType() { return super.getMessageType();}
}
