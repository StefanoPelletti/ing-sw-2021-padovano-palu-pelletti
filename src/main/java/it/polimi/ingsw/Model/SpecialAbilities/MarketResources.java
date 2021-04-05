package it.polimi.ingsw.Model.SpecialAbilities;

import it.polimi.ingsw.Model.Enumerators.Resource;

public class MarketResources implements SpecialAbility {
    private Resource resource;

    public MarketResources(Resource resource) {
        this.resource = resource;
    }

    public Resource getConvertedResource() {
        return resource;
    }

    @Override
    public String toString()
    {
        return "I have the ability to transform white marbles in "+getConvertedResource()+", do I remind you of someone else?";
    }
}
