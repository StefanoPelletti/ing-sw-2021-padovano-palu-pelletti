package it.polimi.ingsw.server.model.specialAbilities;

import java.io.Serializable;

/**
 * SpecialAbility implements a Strategy Pattern.
 * A Leader Card can have 1 out of 4 Special Abilities.
 * In base of which Ability the Card have, different approaches can be made, while securely Casting the Special Ability as a specific Ability.
 * The logic behind it is still implemented in the action Manager.
 */
public interface SpecialAbility extends Serializable {
    boolean isProduction();

    boolean isMarketResource();

    boolean isExtraDepot();

    boolean isDiscountResource();
}