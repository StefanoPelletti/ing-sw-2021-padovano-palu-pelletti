package it.polimi.ingsw.Networking.Message.UpdateMessages;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;

import java.io.Serializable;

public class MSG_UPD_Game extends Message implements Serializable {
    private final int turn;
    private final int currentPlayer;
    private final int blackCrossPosition;

    public MSG_UPD_Game(int turn, int currentPlayer, int blackCrossPosition)
    {
        super(MessageType.MSG_UPD_Game);
        this.turn = turn;
        this.blackCrossPosition = blackCrossPosition;
        this.currentPlayer = currentPlayer;
    }
    public MessageType getMessageType() { return super.getMessageType();}
}
