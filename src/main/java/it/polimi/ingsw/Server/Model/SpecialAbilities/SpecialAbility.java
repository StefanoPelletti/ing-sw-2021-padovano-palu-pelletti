package it.polimi.ingsw.Server.Model.SpecialAbilities;

import java.io.Serializable;

public interface SpecialAbility extends Serializable {

    public boolean isProduction();
    public boolean isMarketResource();
    public boolean isExtraDepot();
    public boolean isDiscountResource();
}
