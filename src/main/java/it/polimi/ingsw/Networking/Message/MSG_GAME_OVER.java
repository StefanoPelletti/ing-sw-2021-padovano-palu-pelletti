package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_GAME_OVER extends Message implements Serializable {


    public MSG_GAME_OVER()
    {
        super(MessageType.MSG_GAME_OVER);
    }

    public MessageType getMessageType() { return super.getMessageType();}
}
