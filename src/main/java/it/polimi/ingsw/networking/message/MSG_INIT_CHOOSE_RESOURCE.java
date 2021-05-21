package it.polimi.ingsw.networking.message;

import it.polimi.ingsw.server.model.enumerators.Resource;

import java.io.Serializable;

public class MSG_INIT_CHOOSE_RESOURCE extends Message implements Serializable {

    private final Resource resource;

    public MSG_INIT_CHOOSE_RESOURCE(Resource resource) {
        super(MessageType.MSG_INIT_CHOOSE_RESOURCE);

        if (resource != Resource.COIN && resource != Resource.SERVANT && resource != Resource.STONE && resource != Resource.SHIELD)
            throw new IllegalArgumentException();

        this.resource = resource;
    }

    public Resource getResource() {
        return this.resource;
    }
}