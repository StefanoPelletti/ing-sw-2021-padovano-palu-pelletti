package it.polimi.ingsw.Server.Model.SpecialAbilities;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;

public class DiscountResource implements SpecialAbility {
    private Resource resource;

    public DiscountResource(Resource resource) {
        this.resource = resource;
    }

    public Resource getDiscountedResource() {
        return resource;
    }

    public boolean isProduction()
    {
        return false;
    }
    public boolean isMarketResource()
    {
        return false;
    }
    public boolean isExtraDepot()
    {
        return false;
    }
    public boolean isDiscountResource()
    {
        return true;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == this) return true;
        if(!(obj instanceof DiscountResource)) return false;
        DiscountResource o = (DiscountResource) obj;
        return (this.resource.equals(o.resource));
    }

    @Override
    public String toString()
    {
        return "Hear my plea! Discount this resource: "+getDiscountedResource();
    }
}
