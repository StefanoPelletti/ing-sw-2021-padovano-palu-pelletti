package it.polimi.ingsw.server.model;

import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_Strongbox;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.A;
import it.polimi.ingsw.server.utils.ModelObservable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class Strongbox extends ModelObservable {
    private final Map<Resource, Integer> resources;

    /**
     * CONSTRUCTOR
     */
    public Strongbox() {
        this.resources = new HashMap<>();
    }

    /**
     *
     * @param resources a map of resource contained in a strongbox
     * @return a String representing the current state of the Strongbox
     */
    public static String toString(Map<Resource, Integer> resources) {
        StringBuilder result = new StringBuilder();
        result.append("      STRONGBOX").append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        if (resources.isEmpty()) {
            result.append(" The Strongbox is empty. ").append("\n");
        } else {
            for (Resource r : resources.keySet()) {
                result.append(resources.get(r)).append(" of ").append(r.toString());
                result.append("\n");
            }
        }
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        return result.toString();
    }

    /**
     * Add a given quantity of a resource in the strongbox
     * @param resource the resource to add in the strongbox
     * @param quantity the quantity of resource to add in the strongbox
     * @return true iff the quantity of resource is added
     *         if the resource is added, notifies observers
     */
    public boolean addResource(Resource resource, int quantity) {
        if (resource == Resource.EXTRA || resource == Resource.NONE || resource == Resource.FAITH || resource == null || quantity < 0)
            return false;
        resources.merge(resource, quantity, Integer::sum);
        notifyObservers();
        return true;
    }

    /**
     * Removes a given quantity of a resource from the strongbox
     * @param resource the resource to remove from the strongbox
     * @param quantity the quantity of resource to remove from the strongbox
     * @return true iff it is possible to remove the given quantity of resource
     *         if the quantity is removed, notifies the observers
     */
    public boolean remove(Resource resource, int quantity) {
        Integer tmp = resources.get(resource);
        if (tmp == null || quantity < 0) {
            return false;
        } else {
            if ((tmp - quantity) >= 0) /*there will be resources after the remove */ {
                resources.put(resource, tmp - quantity);
                if(resources.get(resource)==0) resources.remove(resource);
                notifyObservers();
                return true;
            } else //note : tmp-quantity SHOULD BE ZERO, not negative. I cannot ask to remove MORE than what the strongbox has to offer.
            {
                return false;
            }

        }
    }

    /**
     *
     * @param resource
     * @return the amount of the given resource in the strongbox
     */
    public Integer getQuantity(Resource resource) {
        return resources.get(resource);
    }

    /**
     *
     * @return the amount of resources in the strongbox
     */
    public Integer getTotal() {
        return resources.values().stream().reduce(0, Integer::sum);
    }


    @Override
    public String toString() {
        return Strongbox.toString(this.resources);
    }

    /**
     * creates a message and notifies observers
     * @see #generateMessage()
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     *
     * @return a MSG_UPD_Strongbox containing the current state of the Strongbox
     */
    public MSG_UPD_Strongbox generateMessage() {
        return new MSG_UPD_Strongbox(
                resources
        );
    }
}