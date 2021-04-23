package it.polimi.ingsw.Server.Model.Requirements;

import it.polimi.ingsw.Server.Model.Enumerators.Color;


import java.io.Serializable;
import java.util.HashMap;

public class CardRequirements implements Requirement, Serializable {
    private HashMap<Color, Integer[]> requirements;

    public CardRequirements(HashMap<Color, Integer[]> requirements) {
        this.requirements = requirements;
    }

    public HashMap<Color, Integer[]> getRequirements() {
        return requirements;
    }



    public boolean isCardRequirement() { return true; }
    public boolean isResourceRequirement() { return false; }

    @Override
    public String toString()
    {
        StringBuilder result= new StringBuilder();
        Integer numOfCards = requirements.values().stream().map(integers -> integers[0]).reduce(0, Integer::sum);
        result.append("   You need ").append(numOfCards).append(" card(s) with this stats:   \n");
        for ( Color c : requirements.keySet())
        {
            result.append(requirements.get(c)[0]).append(" ").append(c.toString()).append(" card(s)");
            if(requirements.get(c)[1] != -1)
                result.append(" at level ").append(requirements.get(c)[1]);
        }
        return result.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == this) return true;
        if(!(obj instanceof CardRequirements)) return false;
        CardRequirements o = (CardRequirements) obj;
        return (o.getRequirements().equals(this.requirements));
    }
}

