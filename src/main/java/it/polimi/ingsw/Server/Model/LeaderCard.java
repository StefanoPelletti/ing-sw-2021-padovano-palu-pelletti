package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Model.Requirements.Requirement;
import it.polimi.ingsw.Server.Model.SpecialAbilities.SpecialAbility;

public class LeaderCard {
    private final int PV;
    private final Requirement requirement;
    private final SpecialAbility specialAbility;
    private boolean enabled;

    public LeaderCard(int PV, Requirement requirement, SpecialAbility specialAbility) {
        this.PV = PV;
        this.requirement = requirement;
        this.specialAbility = specialAbility;
        this.enabled = false;
    }

    public int getPV() { return PV; }

    public Requirement getRequirement() {
        return requirement;
    }

    public SpecialAbility getSpecialAbility() {
        return specialAbility;
    }

    public void setEnable(boolean new_enabled) {
        this.enabled = new_enabled;
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
        return(this.PV == o.PV &&
                this.requirement.equals(o.requirement) &&
                        this.specialAbility.equals(o.specialAbility) &&
                                this.enabled==o.enabled);

    }

}
