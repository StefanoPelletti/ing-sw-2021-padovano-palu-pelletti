package it.polimi.ingsw.Model.SpecialAbilities;

import it.polimi.ingsw.Model.Resource;
import it.polimi.ingsw.Model.SpecialAbilities.SpecialAbility;

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