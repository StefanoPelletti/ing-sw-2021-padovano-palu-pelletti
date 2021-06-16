package it.polimi.ingsw.networking.message.actionMessages;

import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.controller.ActionManager;
import it.polimi.ingsw.server.model.middles.MessageHelper;


/**
 * The ActionMessage abstract class categorizes the Messages that the ActionManager uses to decide which specific method to invoke.
 * <p>
 * Possible methods are the actions_ methods and init_ methods.
 * No further requirement is asked.
 * This is a consequence of OOP programming.
 * </p>
 */
public abstract class ActionMessage extends Message {

    public ActionMessage(MessageType messageType) {
        super(messageType);
    }

    /**
     * Executes one of the possible Action in the ActionManager class.
     *
     * @param actionManager the reference to the ActionManager
     * @return the boolean value of the consequentially invoked method.
     */
    public abstract boolean execute(ActionManager actionManager);

    /**
     * Returns a String recapping the Action that was just performed on the Action Manager.
     *
     * @param nickname      the nickname of the Player who just performed the Action
     * @param messageHelper the reference to the messageHelper model object
     * @return a recap of the Action
     */
    public abstract String notifyAction(String nickname, MessageHelper messageHelper);
}
