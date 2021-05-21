package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Model.Requirements.Requirement;
import it.polimi.ingsw.Server.Model.SpecialAbilities.SpecialAbility;
import it.polimi.ingsw.Server.Utils.A;

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

    public int getVP() {
        return VP;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public SpecialAbility getSpecialAbility() {
        return specialAbility;
    }

    public boolean getEnable() {
        return enabled;
    }

    public void setEnable(boolean enable) {
// does not need to notify(), the players updates it automatically
        this.enabled = enable;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append("                 LEADER CARD! ");
        if (enabled)
            result.append(" The card is ENABLED");

        result.append("\n").append(A.YELLOW + "   VP: " + A.RESET).append(this.VP).append("\n");

        result.append(this.requirement);
        result.append(this.specialAbility);
        result.append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        return result.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof LeaderCard)) return false;
        LeaderCard o = (LeaderCard) obj;
        return (this.VP == o.VP &&
                this.requirement.equals(o.requirement) &&
                this.specialAbility.equals(o.specialAbility));

    }
}