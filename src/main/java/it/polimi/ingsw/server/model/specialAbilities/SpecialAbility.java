package it.polimi.ingsw.server.model.specialAbilities;

import java.io.Serializable;

public interface SpecialAbility extends Serializable {
    boolean isProduction();

    boolean isMarketResource();

    boolean isExtraDepot();

    boolean isDiscountResource();
}