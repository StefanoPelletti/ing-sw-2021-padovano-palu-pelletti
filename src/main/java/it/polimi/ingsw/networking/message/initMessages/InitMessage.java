package it.polimi.ingsw.networking.message.initMessages;

import it.polimi.ingsw.networking.ClientHandler;
import it.polimi.ingsw.networking.Lobby;
import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;

public abstract class InitMessage extends Message {

    public InitMessage(MessageType messageType){
        super(messageType);
    }

    public abstract boolean execute(ClientHandler clientHandler);
}
