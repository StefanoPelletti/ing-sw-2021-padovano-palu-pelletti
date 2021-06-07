package it.polimi.ingsw.networking.message.updateMessages;

import it.polimi.ingsw.client.cli.UpdateHandler;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.model.DevelopmentCard;

import java.io.Serializable;

public class MSG_UPD_DevDeck extends UpdateMessage implements Serializable {

    private final DevelopmentCard[][] cards;

    /**
     * MSG_UPD_DevDeck is sent by the ClientHandler to the Client.
     * It is generated my a model.DevelopmentCardsDeck generateMessage().
     * It contains the internal status of the DevelopmentCardsDeck, but just the Visible Cards.
     *
     * @param cards A 3x4 matrix which represents the Visible Cards on the DevelopmentCardsDeck.
     */
    public MSG_UPD_DevDeck(DevelopmentCard[][] cards) {
        super(MessageType.MSG_UPD_DevDeck);

        this.cards = new DevelopmentCard[3][4];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(cards[i], 0, this.cards[i], 0, 4);
        }
    }

    public DevelopmentCard[][] getCards() {
        return this.cards;
    }

    @Override
    public void executeCLI(UpdateHandler updateHandler) {
        updateHandler.updateDevDeck(this);
    }

    @Override
    public void executeGUI() {

    }
}