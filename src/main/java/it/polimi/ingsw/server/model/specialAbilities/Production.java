package it.polimi.ingsw.server.model.specialAbilities;

import it.polimi.ingsw.server.model.enumerators.Resource;

import java.io.Serializable;
import java.util.Objects;

public class Production implements SpecialAbility, Serializable {
    private final Resource input;

    public Production(Resource input) {
        this.input = input;
    }

    /**
     * Returns the type of Resource that this Card uses to produce a Faith and a free choice Resource.
     *
     * @return The type of Resource in input for this Card.
     */
    public Resource getInput() {
        return input;
    }

    public boolean isProduction() {
        return true;
    }

    public boolean isMarketResource() {
        return false;
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
        if (!(obj instanceof Production)) return false;
        Production o = (Production) obj;
        return (this.input.equals(o.input));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.input);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\u001B[31m" + "   SPECIAL ABILITY: " + "\u001B[0m").append("\n");
        result.append("    You can transform any ").append(input).append(" in Faith and something else you wish!").append("\n");
        return result.toString();
    }
}