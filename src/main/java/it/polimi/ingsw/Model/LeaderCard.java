package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Requirements.Requirement;
import it.polimi.ingsw.Model.SpecialAbilities.SpecialAbility;

public class LeaderCard {
    private int PV;
    private Requirement requirement;
    private SpecialAbility specialAbility;
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

}
