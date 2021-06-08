package it.polimi.ingsw.networking.message.actionMessages;

import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.controller.ActionManager;
import it.polimi.ingsw.server.model.middles.MessageHelper;

import java.io.Serializable;

public class MSG_ACTION_DISCARD_LEADERCARD extends ActionMessage implements Serializable {

    private final int cardNumber;

    /**
     * MSG_ACTION_DISCARD_LEADERCARD is sent by the Client to the ClientHandler.
     * It requests the ClientHandler to perform the Controller discardLeaderCard() method.
     * Contains a number representing the card to discard.
     *
     * @param cardNumber The desired card number.
     * @throws IllegalArgumentException If cardNumber is not 0 or 1.
     */
    public MSG_ACTION_DISCARD_LEADERCARD(int cardNumber) {
        super(MessageType.MSG_ACTION_DISCARD_LEADERCARD);

        if (cardNumber != 0 && cardNumber != 1)
            throw new IllegalArgumentException();

        this.cardNumber = cardNumber;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    @Override
    public boolean execute(ActionManager actionManager) {
        return actionManager.discardLeaderCard(actionManager.getGame().getCurrentPlayer(), this);
    }

    @Override
    public String notifyAction(String nickname, MessageHelper messageHelper) {
        return messageHelper.action_discardLeaderCards(nickname, this);
    }
}