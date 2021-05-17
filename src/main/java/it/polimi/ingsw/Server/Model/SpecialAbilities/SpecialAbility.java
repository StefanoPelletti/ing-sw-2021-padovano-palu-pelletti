package it.polimi.ingsw.Server.Model.SpecialAbilities;

import java.io.Serializable;

public interface SpecialAbility extends Serializable {
    boolean isProduction();

    boolean isMarketResource();

    boolean isExtraDepot();

    boolean isDiscountResource();
}