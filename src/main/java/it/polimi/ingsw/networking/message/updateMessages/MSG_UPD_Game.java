package it.polimi.ingsw.networking.message.updateMessages;

import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;

import java.io.Serializable;

public class MSG_UPD_Game extends Message implements Serializable {

    private final int turn;
    private final int currentPlayer;
    private final int blackCrossPosition;

    public MSG_UPD_Game(int turn, int currentPlayer, int blackCrossPosition) {
        super(MessageType.MSG_UPD_Game);

        this.turn = turn;
        this.blackCrossPosition = blackCrossPosition;
        this.currentPlayer = currentPlayer;
    }

    public int getTurn() {
        return turn;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public int getBlackCrossPosition() {
        return blackCrossPosition;
    }
}