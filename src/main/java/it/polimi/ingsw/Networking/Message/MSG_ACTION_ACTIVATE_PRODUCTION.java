package it.polimi.ingsw.Networking.Message;

import it.polimi.ingsw.Networking.OBJ_PRODUCTION;

import java.io.Serializable;

public class MSG_ACTION_ACTIVATE_PRODUCTION extends Message implements Serializable {


    private final boolean[] standardProduction; //refers to the DevelopmentSlot
    private final boolean basicProduction; //refers to the basic Development skill
    private final boolean[] leaderProduction; //refers to the choices made for the leaderCards with a Production ability
    private final OBJ_PRODUCTION basicProductionObject;
    private final OBJ_PRODUCTION[] leaderProductionObject;

    public MSG_ACTION_ACTIVATE_PRODUCTION(boolean[] standardProduction, boolean basicProduction, boolean[] leaderProduction, OBJ_PRODUCTION basicProductionObject, OBJ_PRODUCTION[] leaderProductionObject)
    {
        super(MessageType.MSG_ACTION_ACTIVATE_PRODUCTION);

        this.standardProduction = new boolean[3];
        System.arraycopy(standardProduction, 0, this.standardProduction, 0, 3);
        this.basicProduction=basicProduction;
        this.leaderProduction = new boolean[2];
        System.arraycopy(leaderProduction, 0, this.leaderProduction, 0, 2);
        this.basicProductionObject=basicProductionObject;
        this.leaderProductionObject = new OBJ_PRODUCTION[2];
        System.arraycopy(leaderProductionObject, 0, this.leaderProductionObject, 0, 2);
    }

    public boolean[] getStandardProduction() {
        return standardProduction;
    }

    public boolean isBasicProduction() {
        return basicProduction;
    }

    public boolean[] getLeaderProduction() {
        return leaderProduction;
    }

    public OBJ_PRODUCTION getBasicProductionObject() {
        return basicProductionObject;
    }

    public OBJ_PRODUCTION[] getLeaderProductionObject() {
        return leaderProductionObject;
    }



    public MessageType getMessageType() { return super.getMessageType();}
}
