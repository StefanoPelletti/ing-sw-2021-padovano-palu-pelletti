package it.polimi.ingsw.Server.Model.SpecialAbilities;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;

import java.io.Serializable;

public class MarketResources implements SpecialAbility, Serializable {
    private Resource resource;

    public MarketResources(Resource resource) {
        this.resource = resource;
    }

    public Resource getConvertedResource() {
        return resource;
    }

    public boolean isProduction() {
        return false;
    }

    public boolean isMarketResource() {
        return true;
    }

    public boolean isExtraDepot() {
        return false;
    }

    public boolean isDiscountResource() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof MarketResources)) return false;
        MarketResources o = (MarketResources) obj;
        return (this.resource.equals(o.resource));
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\u001B[31m" + "   SPECIAL ABILITY: " + "\u001B[0m").append("\n");
        result.append("    You can transform any white marble in ").append(resource).append("!").append("\n");
        return result.toString();
    }
}