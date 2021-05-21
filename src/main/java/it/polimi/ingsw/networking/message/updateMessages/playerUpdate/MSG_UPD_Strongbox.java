package it.polimi.ingsw.networking.message.updateMessages.playerUpdate;

import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.model.enumerators.Resource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MSG_UPD_Strongbox extends Message implements Serializable {

    private final Map<Resource, Integer> resources;

    public MSG_UPD_Strongbox(Map<Resource, Integer> resources) {
        super(MessageType.MSG_UPD_Strongbox);

        this.resources = new HashMap<>(resources);
    }

    public Map<Resource, Integer> getResources() {
        return this.resources;
    }
}