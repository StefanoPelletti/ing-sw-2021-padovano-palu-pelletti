package it.polimi.ingsw.Model;

public class Production extends SpecialAbility {
    private Resource input;

    public Production(Resource input) {
        this.input = input;
    }

    public Resource getInput() {
        return input;
    }
}
