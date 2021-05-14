package it.polimi.ingsw.Networking.Message;

import java.io.Serializable;

public class MSG_ACTION_GET_MARKET_RESOURCES extends Message implements Serializable {

    private final boolean column;
    private final int number;

    public MSG_ACTION_GET_MARKET_RESOURCES(boolean column, int number)
    {
        super(MessageType.MSG_ACTION_GET_MARKET_RESOURCES);

        if(column && (number <0 || number > 3 )) throw new IllegalArgumentException();
        if(!column && (number <0 || number > 2)) throw new IllegalArgumentException();

        this.column = column;
        this.number = number;
    }

    public boolean getColumn() { return this.column; }
    public int getNumber() { return this.number; }

    public MessageType getMessageType() { return super.getMessageType();}
}
