package it.polimi.ingsw.networking.message.actionMessages;

import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.controller.ActionManager;

import java.io.Serializable;

public class MSG_ACTION_BUY_DEVELOPMENT_CARD extends ActionMessage implements Serializable {

    /**
     * MSG_ACTION_BUY_DEVELOPMENT_CARD is sent by the Client to the ClientHandler.
     * It requests the ClientHandler to perform the Controller buyDevelopmentCard() method.
     */
    public MSG_ACTION_BUY_DEVELOPMENT_CARD() {
        super(MessageType.MSG_ACTION_BUY_DEVELOPMENT_CARD);
    }

    @Override
    public boolean execute(ActionManager actionManager) {
        return actionManager.buyDevelopmentCard(actionManager.getGame().getCurrentPlayer());
    }
}