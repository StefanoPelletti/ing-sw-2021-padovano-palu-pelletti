package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_ACTION_GET_MARKET_RESOURCES extends Message implements Serializable {

    private final boolean column;
    private final int number;

    /**
     * MSG_ACTION_GET_MARKET_RESOURCES is sent by the Client to the ClientHandler
     *  It requests the ClientHandler to perform the Controller getMarketResource() method
     * Contains a boolean representing the choice of row/column and the corresponding number
     * @param column which is true if a column was chosen, false if a row was chosen
     * @param number the corresponding number of column/row
     * @throws IllegalArgumentException if the message is built with:
     *      - column chosen and number not between 0 and 3 (included)
     *      - row chosen and number not between 0 and 2 (included)
     */
    public MSG_ACTION_GET_MARKET_RESOURCES(boolean column, int number) {
        super(MessageType.MSG_ACTION_GET_MARKET_RESOURCES);

        if (column && (number < 0 || number > 3))
            throw new IllegalArgumentException();
        if (!column && (number < 0 || number > 2))
            throw new IllegalArgumentException();

        this.column = column;
        this.number = number;
    }

    public boolean getColumn() {
        return this.column;
    }

    public int getNumber() {
        return this.number;
    }
}
