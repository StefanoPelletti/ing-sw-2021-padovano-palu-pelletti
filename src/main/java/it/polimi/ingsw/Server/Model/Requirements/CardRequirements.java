package it.polimi.ingsw.Server.Model.Requirements;

import it.polimi.ingsw.Server.Model.Enumerators.Color;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class CardRequirements implements Requirement, Serializable {
    private Map<Color, Integer[]> requirements;

    public CardRequirements(Map<Color, Integer[]> requirements) {
        this.requirements = requirements;
    }

    public Map<Color, Integer[]> getRequirements() {
        return requirements;
    }

    public boolean isCardRequirement() { return true; }
    public boolean isResourceRequirement() { return false; }

    public Set<Color> getColor(){
        return requirements.keySet();
    }

    @Override
    public String toString()
    {
        StringBuilder result= new StringBuilder();
        Integer numOfCards = requirements.values().stream().map(integers -> integers[0]).reduce(0, Integer::sum);
        result.append("   Requirement: ").append("\n");
        result.append("    You need ").append(numOfCards);
        if(numOfCards>1)
            result.append(" cards, with these stats: ").append("\n");
        else
            result.append(" card, with these stats: ").append("\n");

        for ( Color c : requirements.keySet())
        {
            result.append("     ").append(requirements.get(c)[0]).append(" ").append(c.toString()).append(" card(s)");
            if(requirements.get(c)[1] != -1)
                result.append(" at level ").append(requirements.get(c)[1]);
            result.append("\n");
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

