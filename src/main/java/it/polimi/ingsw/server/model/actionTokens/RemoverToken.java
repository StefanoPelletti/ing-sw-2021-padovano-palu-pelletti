package it.polimi.ingsw.server.model.actionTokens;

import it.polimi.ingsw.server.model.enumerators.Color;

public class RemoverToken implements ActionToken {
    private final Color color;
    private int column;

    public RemoverToken(Color color) {
        this.color = color;
        switch (color) {
            case GREEN:
                this.column = 0;
                break;
            case BLUE:
                this.column = 1;
                break;
            case YELLOW:
                this.column = 2;
                break;
            case PURPLE:
                this.column = 3;
                break;
        }
    }

    public int getColumn() {
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