package it.polimi.ingsw.server.model.specialAbilities;

import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.A;

import java.io.Serializable;
import java.util.Objects;

public class DiscountResource implements SpecialAbility, Serializable {
    private final Resource resource;

    public DiscountResource(Resource resource) {
        this.resource = resource;
    }

    /**
     * Returns the type of Resource this Card discounts.
     *
     * @return The type of Resource this Card discounts.
     */
    public Resource getDiscountedResource() {
        return resource;
    }

    public boolean isProduction() {
        return false;
    }

    public boolean isMarketResource() {
        return false;
    }

    public boolean isExtraDepot() {
        return false;
    }

    public boolean isDiscountResource() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof DiscountResource)) return false;
        DiscountResource o = (DiscountResource) obj;
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
        result.append("    Discount this resource: ").append(resource.toStringColored()).append("\n");
        return result.toString();
    }
}