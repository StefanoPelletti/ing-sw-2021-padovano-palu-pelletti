package it.polimi.ingsw.server.model.specialAbilities;

import it.polimi.ingsw.server.model.enumerators.Resource;

import java.io.Serializable;
import java.util.Objects;

public class DiscountResource implements SpecialAbility, Serializable {
    private final Resource resource;

    public DiscountResource(Resource resource) {
        this.resource = resource;
    }

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
        result.append("\u001B[31m" + "   SPECIAL ABILITY: " + "\u001B[0m").append("\n");
        result.append("    Discount this resource: ").append(resource).append("\n");
        return result.toString();
    }
}