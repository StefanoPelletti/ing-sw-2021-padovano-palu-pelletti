package it.polimi.ingsw.networking.message.updateMessages.playerUpdate;

import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.model.enumerators.Resource;

import java.io.Serializable;

public class MSG_UPD_WarehouseDepot extends Message implements Serializable {

    private final Resource shelf1;
    private final Resource[] shelf2;
    private final Resource[] shelf3;

    public MSG_UPD_WarehouseDepot(Resource shelf1, Resource[] shelf2, Resource[] shelf3) {
        super(MessageType.MSG_UPD_WarehouseDepot);

        this.shelf1 = shelf1;
        this.shelf2 = new Resource[2];
        this.shelf2[0] = shelf2[0];
        this.shelf2[1] = shelf2[1];
        this.shelf3 = new Resource[3];
        this.shelf3[0] = shelf3[0];
        this.shelf3[1] = shelf3[1];
        this.shelf3[2] = shelf3[2];
    }

    public Resource getShelf1() {
        return shelf1;
    }

    public Resource[] getShelf2() {
        return shelf2;
    }

    public Resource[] getShelf3() {
        return shelf3;
    }
}