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

    @Override
    public boolean equals(Object obj)
    {
        if(obj == this) return true;
        if(!(obj instanceof CardRequirements)) return false;
        CardRequirements o = (CardRequirements) obj;
        return (o.getRequirements().equals(this.requirements));
    }
}
