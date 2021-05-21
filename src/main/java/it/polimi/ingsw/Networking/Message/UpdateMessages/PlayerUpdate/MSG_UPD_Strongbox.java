package it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;

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

    public MessageType getMessageType() {
        return super.getMessageType();
    }
}