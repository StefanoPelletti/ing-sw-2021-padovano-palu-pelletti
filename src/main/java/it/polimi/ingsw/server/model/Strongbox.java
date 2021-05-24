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

    public Strongbox() {
        this.resources = new HashMap<>();
    }

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

    public boolean addResource(Resource resource, int quantity) {
        if (resource == Resource.EXTRA || resource == Resource.NONE || resource == Resource.FAITH || resource == null || quantity < 0)
            return false;
        Integer tmp = resources.get(resource);
        if (tmp == null) {
            resources.put(resource, quantity);
        } else {
            resources.put(resource, tmp + quantity);
        }
        notifyObservers();
        return true;
    }

    public boolean remove(Resource resource, int quantity) {
        Integer tmp = resources.get(resource);
        if (tmp == null || quantity < 0) {
            return false;
        } else {
            if ((tmp - quantity) >= 0) /*there will be resources after the remove */ {
                resources.put(resource, tmp - quantity);
                notifyObservers();
                return true;
            } else //note : tmp-quantity SHOULD BE ZERO, not negative. I cannot ask to remove MORE than what the strongbox has to offer.
            {
                //resources.remove(resource);
                return false;
            }

        }
    }

    public Integer getQuantity(Resource resource) {
        return resources.get(resource);
    }

    public Integer getTotal() {
        return resources.values().stream().reduce(0, Integer::sum);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Strongbox)) return false;
        Strongbox o = (Strongbox) obj;
        return (this.resources.equals((o).resources));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.resources);
    }

    @Override
    public String toString() {
        return Strongbox.toString(this.resources);
    }

    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    public MSG_UPD_Strongbox generateMessage() {
        return new MSG_UPD_Strongbox(
                resources
        );
    }
}