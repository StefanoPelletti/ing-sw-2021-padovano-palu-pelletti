package it.polimi.ingsw.Server.Utils;

public enum A {

    RESET, BLACK, RED, GREEN, YELLOW, BLUE, PURPLE, CYAN, WHITE, UL;

    @Override
    public String toString() {
        switch (this) {
            case RESET:
                return "\u001B[0m";
            case BLACK:
                return "\u001B[30m";
            case RED:
                return "\u001B[31m"; //FAITH
            case GREEN:
                return "\u001B[32m"; //POSITIVE
            case YELLOW:
                return "\u001B[33m"; //NOTIFICATIONS AND COINS
            case BLUE:
                return "\u001B[34m"; //SHIELD
            case PURPLE:
                return "\u001B[35m"; //PARAMETERS, SERVANTS
            case CYAN:
                return "\u001B[36m"; //COMMANDS
            case WHITE:
                return "\u001B[37m"; //DEFAULT
            case UL:
                return "\u001B[4m"; //REQUESTS,
            default:
                return "";
        }
    }
}
