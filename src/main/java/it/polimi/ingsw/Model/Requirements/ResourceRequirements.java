package it.polimi.ingsw.Model.Requirements;

import it.polimi.ingsw.Model.Requirements.Requirement;
import it.polimi.ingsw.Model.Resource;

import java.util.HashMap;

public class ResourceRequirements implements Requirement {
    private HashMap<Resource, Integer> requirements;

    public ResourceRequirements(HashMap<Resource, Integer> requirements) {
        this.requirements = requirements;
    }

    public HashMap<Resource, Integer> getRequirements() {
        return requirements;
    }
}
