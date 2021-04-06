package it.polimi.ingsw.Model;
import it.polimi.ingsw.Model.ActionTokens.*;
import it.polimi.ingsw.Model.Enumerators.Color;

import java.util.*;

public class ActionTokenStack {
    private ArrayList<ActionToken> stack;

    public ActionTokenStack() {
        this.stack=new ArrayList<>();
        this.stack.add(new RemoverToken(Color.BLUE));
        this.stack.add(new RemoverToken(Color.YELLOW));
        this.stack.add(new RemoverToken(Color.GREEN));
        this.stack.add(new RemoverToken(Color.PURPLE));
        this.stack.add(new Forward2Token());
        this.stack.add(new ForwardAndShuffleToken());
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
