package it.polimi.ingsw.Model;

public enum Color {
    YELLOW("Y"), BLUE("B"), GREY("G"), PURPLE("P"), RED("R"), WHITE("W"), GREEN("G");

    private final String abbreviation;

    Color(String abbreviation)
    {
        this.abbreviation = abbreviation;
    }

    public String toAbbreviation() {
        return abbreviation;
    }
}
