package it.polimi.ingsw.Model.Requirements;

import it.polimi.ingsw.Model.Color;
import it.polimi.ingsw.Model.Requirements.Requirement;

import java.util.HashMap;

public class CardRequirements implements Requirement {
    private HashMap<Color, Integer[]> requirements;

    public CardRequirements(HashMap<Color, Integer[]> requirements) {
        this.requirements = requirements;
    }

    public HashMap<Color, Integer[]> getRequirements() {
        return requirements;
    }
}
