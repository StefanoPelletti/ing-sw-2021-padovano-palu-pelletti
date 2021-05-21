package it.polimi.ingsw.server.model.requirements;

import it.polimi.ingsw.server.model.enumerators.Resource;

import java.io.Serializable;
import java.util.Map;

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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        Integer numOfResources = requirements.values().stream().reduce(0, Integer::sum);
        result.append("\u001B[35m" + "   REQUIREMENTS: " + "\u001B[0m").append("\n");
        result.append("    You need ").append(numOfResources);
        if (numOfResources > 1)
            result.append(" resources, in this way: ").append("\n");
        else
            result.append(" resource, which is: ").append("\n");

        for (Resource r : requirements.keySet()) {
            //   STONE : numOf
            result.append("     ").append(requirements.get(r)).append(": ").append(r).append("\n");
        }
        return result.toString();

        /*
         Requirement:
          You need 2 resources, in this way:
           STONE: 2
           SHIELD: 1
         */
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ResourceRequirements)) return false;
        ResourceRequirements o = (ResourceRequirements) obj;
        return (o.getRequirements().equals(this.requirements));
    }
}