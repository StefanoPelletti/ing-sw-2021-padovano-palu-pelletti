package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class WarehouseDepot {
    private Resource[][] depot;

    public WarehouseDepot(Resource[][] depot) {
        this.depot = depot;
    }

    public boolean move(Resource[][] dep) {
        return true;
    }

    public static boolean validateNewConf(Resource[][] dep, Resource ris) {
        return true;
    }

    //private? Perchè?
    private boolean consume(ArrayList<Resource> resources) {
        return true;
    }

    public Resource[][] getResources() {
        return depot;
    }

    public int getTotal() {
        return 1;
    }
}
