package it.polimi.ingsw.Model.SpecialAbilities;

import it.polimi.ingsw.Model.Enumerators.Resource;

public class Production implements SpecialAbility {
    private Resource input;

    public Production(Resource input) {
        this.input = input;
    }

    public Resource getInput() {
        return input;
    }

    @Override
    public String toString()
    {
        return "Give me that "+getInput()+", I'll transform it in Faith and something you wish!";
    }
}
