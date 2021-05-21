package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_ACTION_MARKET_CHOICE extends Message implements Serializable {

    private final int choice;

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