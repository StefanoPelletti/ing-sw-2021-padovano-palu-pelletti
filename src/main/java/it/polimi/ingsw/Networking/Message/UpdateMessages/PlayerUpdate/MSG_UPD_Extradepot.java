package it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;

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

    public MessageType getMessageType() {
        return super.getMessageType();
    }
}