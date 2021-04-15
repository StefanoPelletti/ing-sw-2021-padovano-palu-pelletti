package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_GAME_END_TURN extends Message implements Serializable {


    public MSG_GAME_END_TURN()
    {
        super(MessageType.MSG_GAME_END_TURN);
    }

    public MessageType getMessageType() { return super.getMessageType();}
}
