package it.polimi.ingsw.server.model.enumerators;

import it.polimi.ingsw.server.utils.A;

import java.io.Serializable;

public enum Resource implements Serializable {
    SHIELD("/punchboard/shield.png", "/punchboard/shield2.png"),
    COIN("/punchboard/coin.png", "/punchboard/coin2.png"),
    SERVANT("/punchboard/servant.png", "/punchboard/servant2.png"),
    STONE("/punchboard/stone.png", "/punchboard/stone2.png"),
    NONE("/punchboard/none.png", "/punchboard/none.png"),
    EXTRA("/punchboard/extra.png", "/punchboard/extra.png"),
    FAITH("/punchboard/faith.png", "/punchboard/faith.png");

    private final String pathLittle;
    private final String pathBig;

    Resource(String pathLittle, String pathBig) {
        this.pathLittle = pathLittle;
        this.pathBig = pathBig;
    }

    public String getPathLittle() {
        return this.pathLittle;
    }

    public String getPathBig() {
        return this.pathBig;
    }

    /**
     * Returns the Enumerator Value, Colored in a proper manner.
     *
     * @return A Colored String.
     */
    public String toStringColored() {
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
