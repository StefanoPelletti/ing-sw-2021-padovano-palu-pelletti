package it.polimi.ingsw.Model;

import java.util.HashMap;

public class ResourceRequirements extends Requirement {
    private HashMap<Resource, Integer> requirements;

    public ResourceRequirements(HashMap<Resource, Integer> requirements) {
        this.requirements = requirements;
    }

    public HashMap<Resource, Integer> getRequirements() {
        return requirements;
    }
}
