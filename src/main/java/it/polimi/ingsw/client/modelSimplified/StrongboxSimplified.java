package it.polimi.ingsw.client.modelSimplified;

import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_Strongbox;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.A;

import java.util.HashMap;
import java.util.Map;

public class StrongboxSimplified {
    private Map<Resource, Integer> resources;

    /**
     * Updates this Strongbox to reflect the same status inside the server model.
     *
     * @param message the UpdateMessage that will update this Object internal status.
     */
    public void update(MSG_UPD_Strongbox message) {
        Map<Resource, Integer> map = message.getResources();
        this.resources = new HashMap<>(map);
    }

    /**
     * Returns the representation of the current state of a given Strongbox.
     *
     * @return A String representing the current state of the Strongbox.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append("               STRONGBOX").append("\n");
        if (resources.isEmpty()) {
            result.append("      The Strongbox is empty. ").append("\n");
        } else {
            for (Resource r : resources.keySet()) {
                result.append("      " + resources.get(r)).append(" of ").append(r.toStringColored());
                result.append("\n");
            }
        }
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        return result.toString();
    }

    public Integer getQuantity(Resource resource) {
        return resources.get(resource);
    }
}