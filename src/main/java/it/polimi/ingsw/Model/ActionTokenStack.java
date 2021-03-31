package it.polimi.ingsw.Model;
import it.polimi.ingsw.Model.ActionTokens.ActionToken;

import java.util.*;

public class ActionTokenStack {
    private ArrayList<ActionToken> stack;

    public ActionTokenStack(ArrayList<ActionToken> stack) {
        this.stack = stack;
        this.shuffle();
    }

    public void shuffle() {
        Collections.shuffle(stack);
    }

    public ActionToken pickFirst() {
        ActionToken result=stack.get(0);
        for(int i=0; i<stack.size()-1; i++){
            stack.set(i, stack.get(i+1));
        }
        stack.set(stack.size()-1, result);
        return result;
        //Controller has instruction: result.doAction();
    }
}
