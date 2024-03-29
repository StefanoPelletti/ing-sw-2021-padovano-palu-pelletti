package it.polimi.ingsw.server.model;

import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_Strongbox;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.ModelObservable;

import java.util.HashMap;
import java.util.Map;


public class Strongbox extends ModelObservable {
    private final Map<Resource, Integer> resources;

    /**
     * CONSTRUCTOR
     */
    public Strongbox() {
        this.resources = new HashMap<>();
    }

    /**
     * Tries do add a given quantity of a specified Resource in the Strongbox.
     * If the operation went well, notifies observers.
     *
     * @param resource The resource to add in the strongbox
     * @param quantity The quantity of resource to add in the strongbox
     * @return True if and only if the quantity of resource is added. False otherwise.
     */
    public boolean addResource(Resource resource, int quantity) {
        if (resource == Resource.EXTRA || resource == Resource.NONE || resource == Resource.FAITH || resource == null || quantity < 0)
            return false;
        resources.merge(resource, quantity, Integer::sum);
        notifyObservers();
        return true;
    }

    /**
     * Tries to remove a given quantity of a specified Resource from the Strongbox.
     * If the operation went well, notifies observers.
     *
     * @param resource The resource to remove from the Strongbox.
     * @param quantity The quantity of resource to remove from the Strongbox.
     * @return True if and only if the quantity of resource is removed. False otherwise.
     */
    public boolean remove(Resource resource, int quantity) {
        Integer tmp = resources.get(resource);
        if (tmp == null || quantity < 0) {
            return false;
        } else {
            if ((tmp - quantity) >= 0) /*there will be resources after the remove */ {
                resources.put(resource, tmp - quantity);
                if (resources.get(resource) == 0) resources.remove(resource);
                notifyObservers();
                return true;
            } else //note : tmp-quantity SHOULD BE ZERO, not negative. I cannot ask to remove MORE than what the strongbox has to offer.
            {
                return false;
            }

        }
    }

    /**
     * Returns the amount of the given Resource in the Strongbox.
     *
     * @param resource The specified type of Resource.
     * @return The amount of the given Resource in the Strongbox, or null if there was none.
     */
    public Integer getQuantity(Resource resource) {
        return resources.get(resource);
    }

    /**
     * Returns the total amount of resources in the Strongbox.
     *
     * @return The total amount of resources in the Strongbox.
     */
    public Integer getTotal() {
        return resources.values().stream().reduce(0, Integer::sum);
    }

    /**
     * Creates a message using generateMessage() and notifies observers.
     *
     * @see #generateMessage()
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     * Returns a MSG_UPD_Strongbox representing the current state of the Strongbox.
     *
     * @return A MSG_UPD_Strongbox representing the current state of the Strongbox.
     */
    public MSG_UPD_Strongbox generateMessage() {
        return new MSG_UPD_Strongbox(
                resources
        );
    }
}