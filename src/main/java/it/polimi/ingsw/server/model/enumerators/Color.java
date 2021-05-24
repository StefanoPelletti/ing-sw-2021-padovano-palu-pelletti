package it.polimi.ingsw.server.model.enumerators;

import it.polimi.ingsw.server.utils.A;

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
                return A.YELLOW + "YELLOW" + A.RESET;
            case BLUE:
                return A.BLUE + "BLUE" + A.RESET;
            case PURPLE:
                return A.PURPLE + "PURPLE" + A.RESET;
            case RED:
                return A.RED + "RED" + A.RESET;
            case WHITE:
                return A.WHITE + "WHITE" + A.RESET;
            case GREEN:
                return A.GREEN + "GREEN" + A.RESET;
            default:
                return A.WHITE + "GREY" + A.RESET;
        }
    }
}