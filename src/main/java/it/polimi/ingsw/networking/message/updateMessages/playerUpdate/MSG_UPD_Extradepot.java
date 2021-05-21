package it.polimi.ingsw.networking.message.updateMessages.playerUpdate;

import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.model.enumerators.Resource;

import java.io.Serializable;

public class MSG_UPD_Extradepot extends Message implements Serializable {

    private final Resource resource;
    private final int number;

    public MSG_UPD_Extradepot(Resource resource, int number) {
        super(MessageType.MSG_UPD_Extradepot);

        this.resource = resource;
        this.number = number;
    }

    public Resource getResource() {
        return this.resource;
    }

    public int getNumber() {
        return this.number;
    }
}