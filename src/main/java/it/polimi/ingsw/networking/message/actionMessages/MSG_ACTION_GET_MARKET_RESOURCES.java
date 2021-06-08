package it.polimi.ingsw.networking.message.actionMessages;

import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.controller.ActionManager;
import it.polimi.ingsw.server.model.middles.MessageHelper;

import java.io.Serializable;

public class MSG_ACTION_GET_MARKET_RESOURCES extends ActionMessage implements Serializable {

    private final boolean column;
    private final int number;

    /**
     * MSG_ACTION_GET_MARKET_RESOURCES is sent by the Client to the ClientHandler.
     * It requests the ClientHandler to perform the Controller getMarketResource() method.
     * Contains a boolean representing the choice of row/column and the corresponding number.
     *
     * @param column True if a column was chosen, False if a row was chosen.
     * @param number The corresponding number of column/row.
     * @throws IllegalArgumentException If the message is built with: <ul>
     *                                  <li> column chosen and number not between 0 and 3 (included)
     *                                  <li> row chosen and number not between 0 and 2 (included).
     */
    public MSG_ACTION_GET_MARKET_RESOURCES(boolean column, int number) {
        super(MessageType.MSG_ACTION_GET_MARKET_RESOURCES);

        if (column && (number < 0 || number > 3))
            throw new IllegalArgumentException();
        if (!column && (number < 0 || number > 2))
            throw new IllegalArgumentException();

        this.column = column;
        this.number = number;
    }

    public boolean getColumn() {
        return this.column;
    }

    public int getNumber() {
        return this.number;
    }

    @Override
    public boolean execute(ActionManager actionManager) {
        return actionManager.getMarketResources(actionManager.getGame().getCurrentPlayer(), this);
    }

    @Override
    public String notifyAction(String nickname, MessageHelper messageHelper) {
        return messageHelper.action_getMarketResources(nickname, this);
    }
}
