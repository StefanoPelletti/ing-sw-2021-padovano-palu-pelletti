package it.polimi.ingsw.client.modelSimplified;

import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_Strongbox;
import it.polimi.ingsw.server.model.Strongbox;
import it.polimi.ingsw.server.model.enumerators.Resource;

import java.util.HashMap;
import java.util.Map;

public class StrongboxSimplified {
    private Map<Resource, Integer> resources;

    public void update(MSG_UPD_Strongbox message) {
        Map<Resource, Integer> map = message.getResources();
        this.resources = new HashMap<>(map);
    }

    @Override
    public String toString() {
        return Strongbox.toString(this.resources);
    }
}