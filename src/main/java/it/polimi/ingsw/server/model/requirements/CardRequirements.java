package it.polimi.ingsw.server.model.requirements;

import it.polimi.ingsw.server.model.enumerators.Color;
import it.polimi.ingsw.server.utils.A;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class CardRequirements implements Requirement, Serializable {
    private final Map<Color, ReqValue> requirements;

    public CardRequirements(Map<Color, ReqValue> requirements) {
        this.requirements = requirements;
    }

    public Map<Color, ReqValue> getRequirements() {
        return requirements;
    }

    public boolean isCardRequirement() {
        return true;
    }

    public boolean isResourceRequirement() {
        return false;
    }

    public Set<Color> getColor() {
        return requirements.keySet();
    }

    /**
     * Returns a String representing the Card Requirements.
     *
     * @return A String representing the Card Requirements.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        Integer numOfCards = requirements.values().stream().map(ReqValue::getReqNumCard).reduce(0, Integer::sum);

        result.append(A.RED + "   REQUIREMENTS: " + A.RESET).append("\n");
        result.append("    You need ").append(numOfCards);
        if (numOfCards > 1)
            result.append(" cards, with these stats: ").append("\n");
        else
            result.append(" card, with these stats: ").append("\n");

        for (Color c : requirements.keySet()) {
            result.append("     ").append(requirements.get(c).getReqNumCard()).append(" ").append(c.toString()).append(" card(s)");
            if (requirements.get(c).getReqLvlCard() != -1)
                result.append(" at level ").append(requirements.get(c).getReqLvlCard());
            result.append("\n");
        }
        return result.toString();

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof CardRequirements)) return false;
        CardRequirements o = (CardRequirements) obj;
        return (o.getRequirements().equals(this.requirements));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.requirements);
    }
}