package it.polimi.ingsw.Networking.Message;

import it.polimi.ingsw.Server.Model.LeaderCard;

import java.io.Serializable;
import java.util.ArrayList;

public class MSG_INIT_LEADERCARDS_REQUEST extends Message implements Serializable {

    private final ArrayList<LeaderCard> cards;
    public MSG_INIT_LEADERCARDS_REQUEST(ArrayList<LeaderCard> cards)
    {
        super(MessageType.MSG_INIT_LEADERCARDS_REQUEST);
        this.cards = new ArrayList<>(cards);
    }

    public MessageType getMessageType() { return super.getMessageType();}
    public ArrayList<LeaderCard> getCards() { return this.cards; }
}
