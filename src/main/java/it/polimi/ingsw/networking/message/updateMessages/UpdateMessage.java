package it.polimi.ingsw.networking.message.updateMessages;

import it.polimi.ingsw.client.cli.UpdateHandler;
import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;

public abstract class UpdateMessage extends Message {

    public UpdateMessage(MessageType messageType) {
        super(messageType);
    }

    public abstract void executeCLI(UpdateHandler updateHandler);

    public abstract void executeGUI();
}
