package it.polimi.ingsw.server.model.actionTokens;

import it.polimi.ingsw.server.model.enumerators.Color;

public class RemoverToken implements ActionToken {
    private final Color color;

    /**
     * Constructs a Remover Token with an assigned Color.
     * @param color The Color of the Remover Token.
     * @throws IllegalArgumentException If the color is not GREEN, BLUE, YELLOW or PURPLE.
     */
    public RemoverToken(Color color) {
        if (color != Color.GREEN && color != Color.BLUE && color != Color.YELLOW && color != Color.PURPLE)
            throw new IllegalArgumentException();
        this.color = color;
    }

    /**
     * A color corresponds to a predefined column number.
     * Column number 0 is GREEN, column number 1 is BLUE,
     * colomn number 2 is YELLOW, column number 3 is PURPLE.
     * A Remover Token is assigned a Color, but its practical data is the column number.
     * @return The column number, from 0 to 3 (included).
     */
    public int getColumn() {
        int column = 0;
        switch (color) {
            case GREEN:
                column = 0;
                break;
            case BLUE:
                column = 1;
                break;
            case YELLOW:
                column = 2;
                break;
            case PURPLE:
                column = 3;
                break;
            default:
                break;
        }
        return column;
    }

    public boolean isRemover() {
        return true;
    }

    public boolean isForward2() {
        return false;
    }

    public boolean isForwardAndShuffle() {
        return false;
    }

    public Color getColor() {
        return this.color;
    }
}