package it.polimi.ingsw.client.modelSimplified;

import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_WarehouseDepot;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.A;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WarehouseDepotSimplified {
    private Resource shelf1;
    private Resource[] shelf2;
    private Resource[] shelf3;

    public WarehouseDepotSimplified() {
        this.shelf1 = Resource.NONE;
        this.shelf2 = new Resource[]{Resource.NONE, Resource.NONE};
        this.shelf3 = new Resource[]{Resource.NONE, Resource.NONE, Resource.NONE};
    }

    public Resource getShelf1() {
        return this.shelf1;
    }

    public Resource[] getShelf2() {
        return this.shelf2;
    }

    public Resource[] getShelf3() {
        return this.shelf3;
    }

    public Map<Resource, Integer> getResources() {
        Map<Resource, Integer> result = new HashMap<>();
        Resource r2;
        Resource r3;
        result.put(Resource.COIN, 0);
        result.put(Resource.SERVANT, 0);
        result.put(Resource.SHIELD, 0);
        result.put(Resource.STONE, 0);
        if (this.shelf1 != Resource.NONE) {
            result.put(this.shelf1, 1);
        }
        r2 = Arrays.stream(this.shelf2).filter(r -> r != Resource.NONE).findFirst().orElse(Resource.NONE);
        r3 = Arrays.stream(this.shelf3).filter(r -> r != Resource.NONE).findFirst().orElse(Resource.NONE);
        if (r2 != Resource.NONE)
            result.put(r2, getShelf2ResourceNumber());
        if (r3 != Resource.NONE)
            result.put(r3, getShelf3ResourceNumber());
        return result;
    }

    public int getShelf2ResourceNumber() {
        int result = 0;
        if (shelf2[0] != Resource.NONE) result++;
        if (shelf2[1] != Resource.NONE) result++;
        return result;
    }

    public int getShelf3ResourceNumber() {
        int result = 0;
        if (shelf3[0] != Resource.NONE) result++;
        if (shelf3[1] != Resource.NONE) result++;
        if (shelf3[2] != Resource.NONE) result++;
        return result;
    }

    public void update(MSG_UPD_WarehouseDepot message) {
        Resource newShelf1 = message.getShelf1();
        Resource[] newShelf2 = new Resource[2];
        System.arraycopy(message.getShelf2(), 0, newShelf2, 0, 2);
        Resource[] newShelf3 = new Resource[3];
        System.arraycopy(message.getShelf3(), 0, newShelf3, 0, 3);

        this.shelf1 = newShelf1;
        this.shelf2 = new Resource[2];
        this.shelf2[0] = newShelf2[0];
        this.shelf2[1] = newShelf2[1];
        this.shelf3 = new Resource[3];
        this.shelf3[0] = newShelf3[0];
        this.shelf3[1] = newShelf3[1];
        this.shelf3[2] = newShelf3[2];
    }

    /**
     * Returns the representation of the current state of a given WarehouseDepot.
     *
     * @return A String representing the current state of the WarehouseDepot.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append("               WAREHOUSE DEPOT:").append("\n");
        result.append(A.CYAN + "  Shelf 1:  " + A.RESET).append(shelf1).append("\n");
        result.append(A.CYAN + "  Shelf 2:  " + A.RESET).append(shelf2[0]).append(" - ").append(shelf2[1]).append("\n");
        result.append(A.CYAN + "  Shelf 3:  " + A.RESET).append(shelf3[0]).append(" - ").append(shelf3[1]).append(" - ").append(shelf3[2]).append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        return result.toString();
    }
}