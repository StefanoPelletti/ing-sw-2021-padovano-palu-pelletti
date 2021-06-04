package it.polimi.ingsw.networking.message.actionMessages;

import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.controller.ActionManager;

import java.io.Serializable;

public class MSG_INIT_CHOOSE_LEADERCARDS extends ActionMessage implements Serializable {

    private final int firstCard;
    private final int secondCard;

    /**
     * MSG_INIT_CHOOSE_LEADERCARDS is sent by the Client to the ClientHandler.
     * Contains two integers representing two cards, and is operated by the middles.LeaderCardsPicker object.
     * @param firstCard The number of the first card chosen.
     * @param secondCard The number of the second card chosen.
     * @throws IllegalArgumentException If the message is built with: <ul>
     * <li> firstCard is not between 0 and 3 (included)
     * <li> secondCard is not between 0 and 3 (included)
     * <li> firstCard is the same as secondCard.
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

    @Override
    public boolean execute(ActionManager actionManager) {
        return actionManager.chooseLeaderCard( actionManager.getGame().getCurrentPlayer(), this);
    }
}