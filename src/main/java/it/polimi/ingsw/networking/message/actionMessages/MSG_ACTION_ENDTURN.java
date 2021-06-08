package it.polimi.ingsw.networking.message.actionMessages;

import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.controller.ActionManager;
import it.polimi.ingsw.server.model.middles.MessageHelper;

import java.io.Serializable;

public class MSG_ACTION_ENDTURN extends ActionMessage implements Serializable {

    /**
     * MSG_ACTION_ENDTURN is sent by the Client to the ClientHandler.
     * It requests the ClientHandler to perform the Controller endTurn() method.
     */
    public MSG_ACTION_ENDTURN() {
        super(MessageType.MSG_ACTION_ENDTURN);
    }

    @Override
    public boolean execute(ActionManager actionManager) {
        return actionManager.endTurn(actionManager.getGame().getCurrentPlayer(), true);
    }

    @Override
    public String notifyAction(String nickname, MessageHelper messageHelper) {
        return messageHelper.action_endTurn(nickname, this);
    }
}