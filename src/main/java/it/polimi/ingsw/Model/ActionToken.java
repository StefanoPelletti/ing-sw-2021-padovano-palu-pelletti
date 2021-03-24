package it.polimi.ingsw.Model;

public class ActionToken {
    private ActionType type;

    public ActionToken(ActionType type) {
        this.type = type;
    }

    public ActionType getType() {
        return type;
    }
}
