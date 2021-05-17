package it.polimi.ingsw.Server.Model.Enumerators;

import java.io.Serializable;

public enum Resource implements Serializable {
    SHIELD, COIN, SERVANT, STONE, NONE, EXTRA, FAITH;

    public String toString() {
        switch (this) {
            case SHIELD:
                return "\u001B[34m" + "SHIELD" + "\u001B[0m";
            case COIN:
                return "\u001B[33m" + "COIN" + "\u001B[0m";
            case SERVANT:
                return "\u001B[35m" + "SERVANT" + "\u001B[0m";
            case STONE:
                return "STONE";
            case EXTRA:
                return "\u001B[37m" + "EXTRA" + "\u001B[0m";
            case FAITH:
                return "\u001B[31m" + "FAITH" + "\u001B[0m";
            default:
                return "NONE";
        }
    }
}
