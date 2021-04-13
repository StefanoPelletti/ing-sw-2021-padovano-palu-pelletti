package it.polimi.ingsw.Server.Model.Requirements;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;

import java.io.Serializable;
import java.util.HashMap;

public class ResourceRequirements implements Requirement, Serializable {
    private HashMap<Resource, Integer> requirements;

    public ResourceRequirements(HashMap<Resource, Integer> requirements) {
        this.requirements = requirements;
    }

    public HashMap<Resource, Integer> getRequirements() {
        return requirements;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == this) return true;
        if(!(obj instanceof ResourceRequirements)) return false;
        ResourceRequirements o = (ResourceRequirements) obj;
        return (o.getRequirements().equals(this.requirements));
    }
}
