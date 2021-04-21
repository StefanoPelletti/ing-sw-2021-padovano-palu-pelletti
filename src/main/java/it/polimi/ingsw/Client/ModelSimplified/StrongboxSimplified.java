package it.polimi.ingsw.Client.ModelSimplified;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate.*;
import java.util.*;

public class StrongboxSimplified {

    private Map<Resource, Integer> resources;

    public void update (MSG_UPD_Strongbox message)
    {
        Map<Resource, Integer> map = message.getResources();
        this.resources = new HashMap<>(map);
    }


    @Override
    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append(" the Strongbox contains: ").append("\n");
        for( Resource r : resources.keySet())
        {
            result.append(resources.get(r)).append(" of ").append(r.toString());
            result.append("\n");
        }
        return result.toString();
    }
}
