package it.polimi.ingsw.Server.Model.SpecialAbilities;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;

import java.io.Serializable;

public class ExtraDepot implements SpecialAbility, Serializable {
    private Resource resource;
    private int number;

    public ExtraDepot(Resource resource) {
        this.resource = resource;
        this.number = 0;
    }

    public Resource getResourceType() {
        return resource;
    }
    public int getNumber() { return number; }

    public boolean addResource(int numAdd) {
        if ( numAdd > 2 || numAdd <= 0 ) return false;
        if ( numAdd == 2 && number != 0 ) return false;
        if ( numAdd == 1 && number == 2 ) return false;
        number = number+numAdd;
        return true;
    }

    public boolean removeResource(int numRem) {
        if ( number == 0 ) return false;
        if ( numRem <= 0 || numRem > 2 ) return false;
        if ( numRem == 2 && number == 1 ) return false;
        number = number - numRem;
        return true;
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
        return true;
    }
    public boolean isDiscountResource()
    {
        return false;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == this) return true;
        if(!(obj instanceof ExtraDepot)) return false;
        ExtraDepot o = (ExtraDepot) obj;
        return (this.resource.equals(o.resource) && this.number==o.number);
    }

    @Override
    public String toString()
    {
        return "I have "+getNumber()+" "+getResourceType()+ " in my belly! ";
    }

}
