package it.polimi.ingsw.server.model.requirements;

import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.A;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class ResourceRequirements implements Requirement, Serializable {
    private final Map<Resource, Integer> requirements;

    public ResourceRequirements(Map<Resource, Integer> requirements) {
        this.requirements = requirements;
    }

    public Map<Resource, Integer> getRequirements() {
        return requirements;
    }

    public boolean isCardRequirement() {
        return false;
    }

    public boolean isResourceRequirement() {
        return true;
    }

    /**
     * Returns a String representing the Resource Requirements.
     *
     * @return A String representing the Resource Requirements.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        Integer numOfResources = requirements.values().stream().reduce(0, Integer::sum);
        result.append(A.RED + "   REQUIREMENTS: " + A.RESET).append("\n");
        result.append("    You need ").append(numOfResources);
        if (numOfResources > 1)
            result.append(" resources, in this way: ").append("\n");
        else
            result.append(" resource, which is: ").append("\n");

        for (Resource r : requirements.keySet()) {
            //   STONE : numOf
            result.append("     ").append(requirements.get(r)).append(": ").append(r.toStringColored()).append("\n");
        }
        return result.toString();

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ResourceRequirements)) return false;
        ResourceRequirements o = (ResourceRequirements) obj;
        return (o.getRequirements().equals(this.requirements));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.requirements);
    }
}