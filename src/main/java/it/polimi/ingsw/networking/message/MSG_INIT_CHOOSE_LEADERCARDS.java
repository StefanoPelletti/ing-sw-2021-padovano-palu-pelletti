package it.polimi.ingsw.networking.message;

import java.io.Serializable;

public class MSG_INIT_CHOOSE_LEADERCARDS extends Message implements Serializable {

    private final int firstCard;
    private final int secondCard;

    /**
     * MSG_INIT_CHOOSE_LEADERCARDS is sent by the Client to the ClientHandler
     *  Contains two integers representing two cards, and is operated by the middles.LeaderCardsPicker object
     * @param firstCard the number of the first card chosen
     * @param secondCard the number of the second card chosen
     * @throws IllegalArgumentException if the message is built with:
     *      - firstCard is not between 0 and 3 (included)
     *      - secondCard is not between 0 and 3 (included)
     *      - firstCard is the same as secondCard
     */
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