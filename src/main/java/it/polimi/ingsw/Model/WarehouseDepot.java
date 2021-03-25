package it.polimi.ingsw.Model;

import java.util.*;

public class WarehouseDepot {
    private Map<Resource, Integer> depot;

    public WarehouseDepot() { this.depot = new HashMap<>(); }
    public WarehouseDepot(Map<Resource, Integer> depot) {
        this.depot = depot;
    }

    public boolean validateConfig(Map<Resource, Integer> test) {
        return (8==8);
    }

    public static boolean validateNewConf(Resource[][] dep, Resource ris) {
        return true;
    }

    //private? Perchè?
    private boolean consume(ArrayList<Resource> resources) {
        return true;
    }

    public Map<Resource, Integer> getResources() {
        return depot;
    }

    public int getTotal() {
        return 1;
    }
}
