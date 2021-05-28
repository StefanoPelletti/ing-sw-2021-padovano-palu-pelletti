package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_ACTION_MARKET_CHOICE extends Message implements Serializable {

    private final int choice;

    /**
     * MSG_ACTION_MARKET_CHOICE is sent by the Client to the ClientHandler
     *  It requests the ClientHandler to perform the Controller newMarketChoice() method.
     * Contains a integer representing the choice
     * @param choice the desired choice
     * @throws IllegalArgumentException if choice is not between 0 and 8 (included)
     */
    public MSG_ACTION_MARKET_CHOICE(int choice) {
        super(MessageType.MSG_ACTION_MARKET_CHOICE);

        if (choice < 0 || choice > 8)
            throw new IllegalArgumentException();

        this.choice = choice;
    }

    public int getChoice() {
        return choice;
    }
}