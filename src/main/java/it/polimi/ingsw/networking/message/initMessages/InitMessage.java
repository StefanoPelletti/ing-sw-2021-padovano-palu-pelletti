package it.polimi.ingsw.networking.message.initMessages;

import it.polimi.ingsw.networking.ClientHandler;
import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;

/**
 * The InitMessage abstract class categorizes the first Messages a ClientHandler should handle.
 * <p>
 * The consequences of the handling is seen by the ClientHandler through the returned value of the execute method.
 */
public abstract class InitMessage extends Message {

    public InitMessage(MessageType messageType) {
        super(messageType);
    }

    public abstract boolean execute(ClientHandler clientHandler);
}
