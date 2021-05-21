package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_INIT_CHOOSE_LEADERCARDS extends Message implements Serializable {

    private final int firstCard;
    private final int secondCard;

    public MSG_INIT_CHOOSE_LEADERCARDS(int firstCard, int secondCard) {
        super(MessageType.MSG_INIT_CHOOSE_LEADERCARDS);

        if (firstCard < 0 || firstCard > 3) throw new IllegalArgumentException();
        if (secondCard < 0 || secondCard > 3) throw new IllegalArgumentException();
        if (firstCard == secondCard) throw new IllegalArgumentException();

        this.firstCard = firstCard;
        this.secondCard = secondCard;
    }

    public int getFirstCard() {
        return this.firstCard;
    }

    public int getSecondCard() {
        return this.secondCard;
    }
}