package it.polimi.ingsw.Model;
import java.util.Map;

public class Strongbox {
    private Map<Resource, Integer> resources;

    public Strongbox(Map<Resource, Integer> resources) {
        this.resources = resources;
    }

    public boolean addResource(Resource ris, int i) {
        return true;
    }

    public boolean remove(Resource ris, int i) {
        return true;
    }

    public int getQuantity(Resource ris) {
        return 1;
    }

    public int getTotal() {
        return 1;
    }
}
