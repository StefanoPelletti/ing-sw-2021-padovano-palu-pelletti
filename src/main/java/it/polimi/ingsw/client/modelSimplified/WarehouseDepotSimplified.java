package it.polimi.ingsw.client.modelSimplified;

import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_WarehouseDepot;
import it.polimi.ingsw.server.model.WarehouseDepot;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.A;

public class WarehouseDepotSimplified {
    private Resource shelf1;
    private Resource[] shelf2;
    private Resource[] shelf3;

    public WarehouseDepotSimplified() {
        this.shelf1 = Resource.NONE;
        this.shelf2 = null;
        this.shelf3 = null;
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
     * @return A String representing the current state of the WarehouseDepot.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("               WAREHOUSE DEPOT:").append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append(A.CYAN + "  Shelf 1:  " + A.RESET).append(shelf1).append("\n");
        result.append(A.CYAN + "  Shelf 2:  " + A.RESET).append(shelf2[0]).append(" - ").append(shelf2[1]).append("\n");
        result.append(A.CYAN + "  Shelf 3:  " + A.RESET).append(shelf3[0]).append(" - ").append(shelf3[1]).append(" - ").append(shelf3[2]).append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        return result.toString();
    }
}