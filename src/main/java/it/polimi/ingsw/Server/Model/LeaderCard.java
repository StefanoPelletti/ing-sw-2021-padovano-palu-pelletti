package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Model.Requirements.Requirement;
import it.polimi.ingsw.Server.Model.SpecialAbilities.SpecialAbility;

import java.io.Serializable;

public class LeaderCard implements Serializable {
    private final int VP;
    private final Requirement requirement;
    private final SpecialAbility specialAbility;
    private boolean enabled;

    public LeaderCard(int VP, Requirement requirement, SpecialAbility specialAbility) {
        this.VP = VP;
        this.requirement = requirement;
        this.specialAbility = specialAbility;
        this.enabled = false;
    }

    public int getVP() { return VP; }

    public Requirement getRequirement() {
        return requirement;
    }

    public SpecialAbility getSpecialAbility() {
        return specialAbility;
    }

    public void setEnable(boolean enable) {
        this.enabled = enable;
    }

    public boolean getEnable() {
        return enabled;
    }

    @Override
    public boolean equals( Object obj )
    {
        if(obj == this) return true;
        if(!(obj instanceof LeaderCard)) return false;
        LeaderCard o = (LeaderCard) obj;
        return(this.VP == o.VP &&
                this.requirement.equals(o.requirement) &&
                        this.specialAbility.equals(o.specialAbility) &&
                                this.enabled==o.enabled);

    }

    @Override
    public String toString()
    {
        String result= "";
        result = result + "   LEADER CARD!   \n";
        result = result + "      VP    : "+this.VP+ "\n";
        result = result + " is enabled : "+this.enabled+ "\n";
        result = result + "\n";
        result = result + this.requirement+ "\n";
        result = result + this.specialAbility+ "\n";
        return result;
    }

}
