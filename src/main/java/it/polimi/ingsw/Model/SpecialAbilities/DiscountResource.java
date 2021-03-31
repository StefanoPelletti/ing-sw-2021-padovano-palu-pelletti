package it.polimi.ingsw.Model.SpecialAbilities;

import it.polimi.ingsw.Model.Resource;
import it.polimi.ingsw.Model.SpecialAbilities.SpecialAbility;

public class DiscountResource implements SpecialAbility {
    private Resource resource;

    public DiscountResource(Resource resource) {
        this.resource = resource;
    }

    public Resource getDiscountedResource() {
        return resource;
    }

    @Override
    public String toString()
    {
        return "Hear my plea! Discount this resource: "+getDiscountedResource();
    }
}
