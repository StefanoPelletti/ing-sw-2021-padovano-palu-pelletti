package it.polimi.ingsw.Server.Model.Enumerators;

import java.io.Serializable;

public enum Color implements Serializable {

    YELLOW("Y"), BLUE("B"), GREY("G"), PURPLE("P"), RED("R"), WHITE("W"), GREEN("G");

    private final String abbreviation;

    Color(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String toAbbreviation() {
        return abbreviation;
    }

    @Override
    public String toString() {
        switch (this) {
            case YELLOW:
                return "\u001B[33m" + "YELLOW" + "\u001B[0m";
            case BLUE:
                return "\u001B[34m" + "BLUE" + "\u001B[0m";
            case PURPLE:
                return "\u001B[35m" + "PURPLE" + "\u001B[0m";
            case RED:
                return "\u001B[31m" + "RED" + "\u001B[0m";
            case WHITE:
                return "WHITE";
            case GREEN:
                return "\u001B[32m" + "GREEN" + "\u001B[0m";
            default:
                return "GREY";
        }
    }
}