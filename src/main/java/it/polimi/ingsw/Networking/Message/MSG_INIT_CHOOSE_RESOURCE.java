package it.polimi.ingsw.Networking.Message;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;

import java.io.Serializable;

public class MSG_INIT_CHOOSE_RESOURCE extends Message implements Serializable {

    private final Resource resource;
    public MSG_INIT_CHOOSE_RESOURCE(Resource resource)
    {
        super(MessageType.MSG_INIT_CHOOSE_RESOURCE);
        this.resource = resource;
    }
    public Resource getResource() { return this.resource; }
    public MessageType getMessageType() { return super.getMessageType();}
}
