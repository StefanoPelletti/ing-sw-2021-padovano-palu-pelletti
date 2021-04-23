package it.polimi.ingsw.Networking.Message;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import java.io.Serializable;
import java.util.ArrayList;

public class MSG_ACTION_ACTIVATE_PRODUCTION extends Message implements Serializable {

    private final boolean[] standardProduction; //refers to the DevelopmentSlot
    private final boolean basicProduction; //refers to the basic Development skill
    private final boolean[] leaderProduction; //refers to the choices made for the leaderCards with a Production ability
    private final ArrayList<Resource> basicInput;
    private final Resource basicOutput;
    private final Resource leaderOutput1;
    private final Resource leaderOutput2;

    public MSG_ACTION_ACTIVATE_PRODUCTION(boolean[] standardProduction, boolean basicProduction, boolean[] leaderProduction,
                                          ArrayList<Resource> basicInput,
                                          Resource basicOutput,
                                          Resource leaderOutput1,
                                          Resource leaderOutput2)
    {
        super(MessageType.MSG_ACTION_ACTIVATE_PRODUCTION);

        if( standardProduction==null || leaderProduction==null ) throw new IllegalArgumentException();
        if( basicProduction && (basicInput==null || basicInput.size()!=2)) throw new IllegalArgumentException();
        if( leaderProduction[0] && leaderOutput1==null ) throw new IllegalArgumentException();
        if( leaderProduction[1] && leaderOutput2==null ) throw new IllegalArgumentException();

        this.standardProduction = standardProduction.clone();
        this.basicProduction = basicProduction;
        this.leaderProduction = leaderProduction.clone();

        this.basicInput = new ArrayList<>(basicInput);
        this.basicOutput = basicOutput;
        this.leaderOutput1 = leaderOutput1;
        this.leaderOutput2 = leaderOutput2;
    }

    public boolean[] getStandardProduction() {
        return standardProduction.clone();
    }

    public boolean isBasicProduction() {
        return basicProduction;
    }

    public boolean[] getLeaderProduction() {
        return leaderProduction.clone();
    }

    public ArrayList<Resource> getBasicInput(){
        return (ArrayList<Resource>) this.basicInput.clone();
    }

    public Resource getBasicOutput(){
        return this.basicOutput;
    }

    public Resource getLeaderOutput1(){
        return this.leaderOutput1;
    }

    public Resource getLeaderOutput2(){
        return this.leaderOutput2;
    }


    public MessageType getMessageType() { return super.getMessageType();}
}
