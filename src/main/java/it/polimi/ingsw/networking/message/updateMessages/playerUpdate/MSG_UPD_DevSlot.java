package it.polimi.ingsw.networking.message.updateMessages.playerUpdate;

import it.polimi.ingsw.client.cli.UpdateHandler;
import it.polimi.ingsw.client.gui.UpdateHandlerGUI;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.networking.message.updateMessages.UpdateMessage;
import it.polimi.ingsw.server.model.DevelopmentCard;

import java.io.Serializable;

public class MSG_UPD_DevSlot extends UpdateMessage implements Serializable {

    private final DevelopmentCard[][] cards;

    /**
     * MSG_UPD_DevSlot is sent by the ClientHandler to the Client.
     * It is generated by a model.DevelopmentSlot generateMessage().
     * It contains the internal status of a Development Slot.
     * Note: it is referred to the CurrentPlayer.
     *
     * @param cards A 3x3 DevelopmentCard matrix containing the cards.
     */
    public MSG_UPD_DevSlot(DevelopmentCard[][] cards) {
        super(MessageType.MSG_UPD_DevSlot);

        this.cards = new DevelopmentCard[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(cards[i], 0, this.cards[i], 0, 3);
        }
    }

    public DevelopmentCard[][] getCards() {
        return this.cards;
    }

    @Override
    public void executeCLI(UpdateHandler updateHandler) {
        updateHandler.updateCurrentPlayerDevSlot(this);
    }

    @Override
    public void executeGUI(UpdateHandlerGUI updateHandler) {
        updateHandler.updateCurrentPlayerDevSlot(this);
    }
}