package it.polimi.ingsw.Model;

import java.util.HashMap;

public class CardRequirements extends Requirement {
    private HashMap<Color, Integer> requirements;

    public CardRequirements(HashMap<Color, Integer> requirements) {
        this.requirements = requirements;
    }

    public HashMap<Color, Integer> getRequirements() {
        return requirements;
    }
}
