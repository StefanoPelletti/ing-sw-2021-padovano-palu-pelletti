package it.polimi.ingsw.server.model.specialAbilities;

import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.A;

import java.io.Serializable;
import java.util.Objects;

public class MarketResources implements SpecialAbility, Serializable {
    private final Resource resource;

    public MarketResources(Resource resource) {
        this.resource = resource;
    }

    /**
     * Returns the type of Resource this Card converts White Marbles into.
     *
     * @return The type of Resource this Card converts White Marbles into.
     */
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
    public int hashCode() {
        return Objects.hash(this.resource);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(A.RED + "   SPECIAL ABILITY: " + A.RESET).append("\n");
        result.append("    You can transform any white marble in ").append(resource.toStringColored()).append("!").append("\n");
        return result.toString();
    }
}