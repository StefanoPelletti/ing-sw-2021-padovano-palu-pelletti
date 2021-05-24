package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.requirements.Requirement;
import it.polimi.ingsw.server.model.specialAbilities.SpecialAbility;
import it.polimi.ingsw.server.utils.A;

import java.io.Serializable;
import java.util.Objects;

public class LeaderCard implements Serializable {
    private final int vp;
    private final Requirement requirement;
    private final SpecialAbility specialAbility;
    private Boolean enabled;

    public LeaderCard(int vp, Requirement requirement, SpecialAbility specialAbility) {
        this.vp = vp;
        this.requirement = requirement;
        this.specialAbility = specialAbility;
        this.enabled = false;
    }

    public int getVp() {
        return vp;
    }

    public Requirement getRequirement() {
        return requirement;
    }

    public SpecialAbility getSpecialAbility() {
        return specialAbility;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
// does not need to notify(), the players updates it automatically
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append("                 LEADER CARD! ");
        if (enabled)
            result.append(" The card is ENABLED");

        result.append("\n").append(A.YELLOW + "   VP: " + A.RESET).append(this.vp).append("\n");

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
        return (this.vp == o.vp &&
                this.requirement.equals(o.requirement) &&
                this.specialAbility.equals(o.specialAbility));

    }

    @Override
    public int hashCode() {
        return Objects.hash(this.vp, this.requirement, this.specialAbility);
    }
}