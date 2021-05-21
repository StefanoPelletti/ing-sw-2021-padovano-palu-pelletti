package it.polimi.ingsw.server.model.actionTokens;

import it.polimi.ingsw.server.model.enumerators.Color;

public class RemoverToken implements ActionToken {
    private final Color color;

    public RemoverToken(Color color) {
        if (color != Color.GREEN && color != Color.BLUE && color != Color.YELLOW && color != Color.PURPLE)
            throw new IllegalArgumentException();
        this.color = color;
    }

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