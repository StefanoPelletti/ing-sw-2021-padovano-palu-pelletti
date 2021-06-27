package it.polimi.ingsw.server.utils;

public enum A {

    RESET, BLACK, RED, GREEN, YELLOW, BLUE, PURPLE, CYAN, WHITE, UL;

    @Override
    public String toString() {
        switch (this) {
            case RESET:
                return "\u001b[0m";
            case BLACK:
                return "\u001b[30m";
            case RED:
                return "\u001b[31m"; //FAITH
            case GREEN:
                return "\u001b[32m"; //POSITIVE
            case YELLOW:
                return "\u001b[33m"; //NOTIFICATIONS AND COINS
            case BLUE:
                return "\u001b[34m"; //SHIELD
            case PURPLE:
                return "\u001b[35m"; //PARAMETERS, SERVANTS
            case CYAN:
                return "\u001b[36m"; //COMMANDS
            case WHITE:
                return "\u001b[37m"; //DEFAULT
            case UL:
                return "\u001b[4m"; //REQUESTS,
            default:
                return "";
        }
    }
}
