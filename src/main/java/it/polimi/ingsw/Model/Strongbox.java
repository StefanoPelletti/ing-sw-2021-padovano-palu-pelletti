package it.polimi.ingsw.Model;
import java.util.Map;

public class Strongbox {
    private Map<Resource, Integer> resources;

    public Strongbox(Map<Resource, Integer> resources) {
        this.resources = resources;
    }

    public boolean addResource(Resource ris, int i) {
        resources.replace(ris, resources.get(ris)+i);
        return true;
    }

    //trying functional
    public boolean addResource(Map<Resource,Integer> ris){
        resources.replaceAll((r,q)->resources.replace(r, q+ris.get(r)));
        return true;
    }

    //if there aren't enough resources of the specified type returns false
    public boolean remove(Resource ris, int i) {
        if(resources.get(ris)-i<0) return false;
        else resources.replace(ris, resources.get(ris)-i);
        return true;
    }

    public int getQuantity(Resource ris) {
        return resources.get(ris);
    }

    public int getTotal() {
        int result=0;
        for(Resource r: resources.keySet()){
            result+=getQuantity(r);
        }
        return result;
    }
}
