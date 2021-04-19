package it.polimi.ingsw.Server.Model;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;

import java.util.*;


public class Strongbox {
    private Map<Resource, Integer> resources;

    public Strongbox() {
        this.resources = new HashMap<>();
    }

    public boolean addResource(Resource resource, int quantity) {
        if(resource==Resource.EXTRA || resource==Resource.NONE || resource==Resource.FAITH ||resource==null || quantity<0)  return false;
        Integer tmp = resources.get(resource);
        if ( tmp == null ) {
            resources.put(resource, quantity);
        }
        else
        {
            resources.put(resource, tmp + quantity);
        }
        return true;
    }

    public boolean remove(Resource resource, int quantity) {
        Integer tmp = resources.get(resource);
        if ( tmp == null || quantity<0) {
            return false;
        }
        else
        {
            if( (tmp - quantity) > 0) /*there will be resources after the remove */
                {
                resources.put(resource, tmp - quantity);
                //notify()
                return true;
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

    @Override
    public boolean equals( Object obj )
    {
        if(obj == this) return true;
        if(!(obj instanceof Strongbox)) return false;
        Strongbox o = (Strongbox) obj;
        return (this.resources.equals(((Strongbox) obj).resources));
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
