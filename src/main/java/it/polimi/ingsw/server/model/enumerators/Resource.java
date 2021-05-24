package it.polimi.ingsw.server.model.enumerators;

import it.polimi.ingsw.server.utils.A;

import java.io.Serializable;

public enum Resource implements Serializable {
    SHIELD, COIN, SERVANT, STONE, NONE, EXTRA, FAITH;

    @Override
    public String toString() {
        switch (this) {
            case SHIELD:
                return A.BLUE + "SHIELD" + A.RESET;
            case COIN:
                return A.YELLOW + "COIN" + A.RESET;
            case SERVANT:
                return A.PURPLE + "SERVANT" + A.RESET;
            case STONE:
                return A.WHITE + "STONE" + A.RESET;
            case EXTRA:
                return A.WHITE + "EXTRA" + A.RESET;
            case FAITH:
                return A.RED + "FAITH" + A.RESET;
            default:
                return "NONE";
        }
    }
}
