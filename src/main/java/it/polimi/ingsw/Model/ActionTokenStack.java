package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class ActionTokenStack {
    private ArrayList<ActionToken> stack;

    public ActionTokenStack(ArrayList<ActionToken> stack) {
        this.stack = stack;
    }

    public void shuffle() {
    }

    public ActionToken pickFirst() {
        return stack.get(0); //si? no? -Tom
    }
}
