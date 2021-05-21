package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate.MSG_UPD_Strongbox;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Utils.Displayer;
import it.polimi.ingsw.Server.Utils.ModelObservable;

import java.util.HashMap;
import java.util.Map;


public class Strongbox extends ModelObservable {
    private final Map<Resource, Integer> resources;

    public Strongbox() {
        this.resources = new HashMap<>();
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
    public String toString() {
        return Displayer.strongboxToString(this.resources);
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