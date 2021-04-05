package it.polimi.ingsw.Model;
import it.polimi.ingsw.Model.Enumerators.Resource;

import java.util.*;


public class Strongbox {
    private Map<Resource, Integer> resources;

    public Strongbox() {
        this.resources = new HashMap<>();
    }

    public boolean addResource(Resource resource, int quantity) {
        Integer tmp = resources.get(resource);
        if ( tmp == null ) {
            resources.put(resource, quantity);
            return true;
        }
        else
        {
            resources.put(resource, tmp + quantity);
            return false;
        }
    }

    public boolean remove(Resource resource, int quantity) {
        Integer tmp = resources.get(resource);
        if ( tmp == null ) {
            return true;
        }
        else
        {
            if( (tmp - quantity) > 0) /*there will be resources after the remove */
                {
                resources.put(resource, tmp - quantity);
                return false;
            }
            else //note : tmp-quantity SHOULD BE ZERO, not negative. I cannot ask to remove MORE than what the strongbox has to offer.
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
}
