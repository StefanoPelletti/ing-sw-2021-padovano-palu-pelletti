package it.polimi.ingsw.Networking.Message.UpdateMessages;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Model.DevelopmentCard;

import java.io.Serializable;

public class MSG_UPD_DevDeck extends Message implements Serializable {

    DevelopmentCard[][] cards;
    public MSG_UPD_DevDeck(DevelopmentCard[][] cards)
    {
        super(MessageType.MSG_UPD_DevDeck);
        this.cards = new DevelopmentCard[3][4];
        for(int i=0; i<3; i++)
        {
            for(int j=0; j<4; j++)
            {
                this.cards[i][j] = cards[i][j];
            }
        }
    }
    public MessageType getMessageType() { return super.getMessageType();}
    public DevelopmentCard[][] getCards() { return this.cards; }

}
