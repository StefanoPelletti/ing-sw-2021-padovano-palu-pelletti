package it.polimi.ingsw.Server.Model.Requirements;

import it.polimi.ingsw.Server.Model.Enumerators.Color;
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



    public boolean isCardRequirement() { return false; }
    public boolean isResourceRequirement() { return true; }

    @Override
    public String toString()
    {
        StringBuilder result= new StringBuilder();
        Integer numOfResources = requirements.values().stream().reduce(0, Integer::sum);
        result.append("   You need ").append(numOfResources).append(" resource(s) in this way:   \n");
        for ( Resource r : requirements.keySet())
        {
            result.append(requirements.get(r)).append(" ").append(r.toString()).append(r.toString());
        }
        return result.toString();
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
