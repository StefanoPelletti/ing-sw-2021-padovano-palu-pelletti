package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_GAME_BUY_DEVELOPMENT_CARD_REPLY extends Message implements Serializable {


    public MSG_GAME_BUY_DEVELOPMENT_CARD_REPLY()
    {
        super(MessageType.MSG_GAME_BUY_DEVELOPMENT_CARD_REPLY);
    }

    public MessageType getMessageType() { return super.getMessageType();}
}