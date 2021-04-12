package it.polimi.ingsw.Server.Model.SpecialAbilities;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;

public class Production implements SpecialAbility {
    private Resource input;

    public Production(Resource input) {
        this.input = input;
    }

    public Resource getInput() {
        return input;
    }

    public boolean isProduction()
    {
        return true;
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
        return false;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == this) return true;
        if(!(obj instanceof Production)) return false;
        Production o = (Production) obj;
        return (this.input.equals(o.input));
    }

    @Override
    public String toString()
    {
        return "Give me that "+getInput()+", I'll transform it in Faith and something you wish!";
    }
}
