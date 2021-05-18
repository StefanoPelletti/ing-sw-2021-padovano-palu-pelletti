package it.polimi.ingsw.Client.ModelSimplified;

import it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate.MSG_UPD_WarehouseDepot;
import it.polimi.ingsw.Server.Model.Enumerators.*;
import it.polimi.ingsw.Server.Utils.A;

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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("               WAREHOUSE DEPOT:").append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append("\u001B[36m" + "  Shelf 1:  " + "\u001B[0m").append(this.shelf1).append("\n");
        result.append("\u001B[36m" + "  Shelf 2:  " + "\u001B[0m").append(this.shelf2[0]).append(" - ").append(this.shelf2[1]).append("\n");
        result.append("\u001B[36m" + "  Shelf 3:  " + "\u001B[0m").append(this.shelf3[0]).append(" - ").append(this.shelf3[1]).append(" - ").append(this.shelf3[2]).append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        return result.toString();
    }
}