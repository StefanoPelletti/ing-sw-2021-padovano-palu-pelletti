package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_GAME_BUY_DEVELOPMENT_CARD_REQUEST extends Message implements Serializable {


    public MSG_GAME_BUY_DEVELOPMENT_CARD_REQUEST()
    {
        super(MessageType.MSG_GAME_BUY_DEVELOPMENT_CARD_REQUEST);
    }

    public MessageType getMessageType() { return super.getMessageType();}
}
