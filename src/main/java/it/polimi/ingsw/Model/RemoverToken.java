package it.polimi.ingsw.Model;

public class RemoverToken extends ActionToken {
    private Color color;

    public RemoverToken(ActionType type, Color color) {
        super(type);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
