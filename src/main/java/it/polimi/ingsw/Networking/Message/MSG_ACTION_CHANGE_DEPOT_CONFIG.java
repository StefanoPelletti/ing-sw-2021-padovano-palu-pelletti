package it.polimi.ingsw.Networking.Message;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;

import java.io.Serializable;

public class MSG_ACTION_CHANGE_DEPOT_CONFIG extends Message implements Serializable {

    private final Resource slot1;
    private final Resource[] slot2;
    private final Resource[] slot3;
    private final int firstExtraDepot;
    private final int secondExtraDepot;

    public MSG_ACTION_CHANGE_DEPOT_CONFIG(Resource slot1, Resource[] slot2, Resource[] slot3, int firstExtraDepot, int secondExtraDepot) {
        super(MessageType.MSG_ACTION_CHANGE_DEPOT_CONFIG);

        if (slot1 != Resource.COIN && slot1 != Resource.SERVANT && slot1 != Resource.SHIELD && slot1 != Resource.STONE && slot1 != Resource.NONE)
            throw new IllegalArgumentException();
        if (slot2 == null || slot3 == null)
            throw new IllegalArgumentException();
        if (firstExtraDepot != -1 && firstExtraDepot != 0 && firstExtraDepot != 1 && firstExtraDepot != 2)
            throw new IllegalArgumentException();
        if (secondExtraDepot != -1 && secondExtraDepot != 0 && secondExtraDepot != 1 && secondExtraDepot != 2)
            throw new IllegalArgumentException();

        this.slot1 = slot1;
        this.slot2 = new Resource[2];
        System.arraycopy(slot2, 0, this.slot2, 0, 2);
        this.slot3 = new Resource[3];
        System.arraycopy(slot3, 0, this.slot3, 0, 3);
        this.firstExtraDepot = firstExtraDepot;
        this.secondExtraDepot = secondExtraDepot;
    }

    public Resource getSlot1() {
        return this.slot1;
    }

    public Resource[] getSlot2() {
        Resource[] result = new Resource[2];
        System.arraycopy(this.slot2, 0, result, 0, 2);
        return result;
    }

    public Resource[] getSlot3() {
        Resource[] result = new Resource[3];
        System.arraycopy(this.slot3, 0, result, 0, 3);
        return result;
    }

    public int getFirstExtraDepot() {
        return firstExtraDepot;
    }

    public int getSecondExtraDepot() {
        return secondExtraDepot;
    }

    public MessageType getMessageType() {
        return super.getMessageType();
    }
}