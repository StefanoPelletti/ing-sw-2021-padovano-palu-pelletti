package it.polimi.ingsw.server.model.enumerators;

import it.polimi.ingsw.server.utils.A;

import java.io.Serializable;

public enum Resource implements Serializable {
    SHIELD("resources/punchboard/shield.png","resources/punchboard/shield2.png"),
    COIN("resources/punchboard/coin.png","resources/punchboard/coin2.png"),
    SERVANT("resources/punchboard/servant.png","resources/punchboard/servant2.png"),
    STONE("resources/punchboard/stone.png","resources/punchboard/stone2.png"),
    NONE("resources/punchboard/none.png","resources/punchboard/none.png"),
    EXTRA("resources/punchboard/extra.png","resources/punchboard/extra.png"),
    FAITH("resources/punchboard/faith.png","resources/punchboard/faith.png");


    private final String pathLittle;
    private final String pathBig;

    Resource(String pathLittle, String pathBig) {
        this.pathLittle = pathLittle;
        this.pathBig = pathBig;
    }

    public String getPathLittle() { return this.pathLittle; }
    public String getPathBig() { return this.pathBig; }

    /**
     * Returns the Enumerator Value, Colored in a proper manner.
     * @return A Colored String.
     */
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
