package it.polimi.ingsw.server.model.specialAbilities;

import java.io.Serializable;

/**
 * SpecialAbility implements a Strategy Pattern.
 * A Leader Card can have 1 out of 4 Special Abilities.
 * In base of which Ability the Card have, different approaches can be made, while securely Casting the Special Ability as a specific Ability.
 * The logic behind it is still implemented in the action Manager.
 */
public interface SpecialAbility extends Serializable {
    /**
     * Returns True if this SpecialAbility is a Production Ability.
     * A Production Ability ables the LeaderCard to produce a Faith and some other free choice Resource, by consuming one other Resource, as specified by the attributes of the Special Ability itself.
     *
     * @return True if this SpecialAbility is a Production Ability, False otherwise.
     */
    boolean isProduction();

    /**
     * Returns True if this SpecialAbility is a Market Ability.
     * A Market Ability ables the LeaderCard to convert a White Marble into a Resource, as specified by the attributes of the Special Ability itself.
     * If a Player owns two Market Ability Leader Cards, he may choose what to transform a White Marble into.
     *
     * @return True if this SpecialAbility is a Market Ability, False otherwise.
     */
    boolean isMarketResource();

    /**
     * Returns True if this SpecialAbility is a ExtraDepot Ability.
     * A ExtraDepot Ability ables the LeaderCard to store up to 2 Resources, the type of is specified by the attributes of the Special Ability itself.
     * The ExtraDepot is the only type of Special Ability to be ModelObservable.
     *
     * @return True if this SpecialAbility is a ExtraDepot Ability, False otherwise.
     */
    boolean isExtraDepot();

    /**
     * Returns True if this SpecialAbility is a Discount Resource Ability.
     * A Discount Resource Ability ables the LeaderCard to discount a certain Resource when buying a Development Card. The type of the Resource is specified by the attributes of the Special Ability itself.
     *
     * @return True if this SpecialAbility is a Discount Resource Ability, False otherwise.
     */
    boolean isDiscountResource();
}