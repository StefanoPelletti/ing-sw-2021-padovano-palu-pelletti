package it.polimi.ingsw.Networking.Message.UpdateMessages;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;

import java.io.Serializable;

public class MSG_UPD_Game extends Message implements Serializable {
    private int turn;
    private int currentPlayer;
    private int blackCrossPosition;

    public MSG_UPD_Game()
    {
        super(MessageType.MSG_UPD_Game);
    }
    public MessageType getMessageType() { return super.getMessageType();}
}
