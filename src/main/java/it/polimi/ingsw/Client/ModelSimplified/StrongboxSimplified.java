package it.polimi.ingsw.Client.ModelSimplified;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate.*;
import it.polimi.ingsw.Server.Utils.Displayer;

import java.util.*;

public class StrongboxSimplified {
    private Map<Resource, Integer> resources;

    public void update(MSG_UPD_Strongbox message) {
        Map<Resource, Integer> map = message.getResources();
        this.resources = new HashMap<>(map);
    }

    @Override
    public String toString() {
        return Displayer.strongboxToString(this.resources);
    }
}