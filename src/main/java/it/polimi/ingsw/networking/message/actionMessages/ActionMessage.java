package it.polimi.ingsw.networking.message.actionMessages;

import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.controller.ActionManager;

public abstract class ActionMessage extends Message {


    public ActionMessage(MessageType messageType) {
        super(messageType);
    }

    public abstract boolean execute(ActionManager actionManager);
}
