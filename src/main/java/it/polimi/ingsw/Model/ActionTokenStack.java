package it.polimi.ingsw.Model;
import it.polimi.ingsw.Model.ActionTokens.*;
import java.util.*;

public class ActionTokenStack {
    private ArrayList<ActionToken> stack;

    public ActionTokenStack() {
        this.stack=new ArrayList<>();
        this.stack.add(0, new RemoverToken(Color.BLUE));
        this.stack.add(1, new RemoverToken(Color.YELLOW));
        this.stack.add(2, new RemoverToken(Color.GREEN));
        this.stack.add(3, new RemoverToken(Color.PURPLE));
        this.stack.add(4, new Forward2Token());
        this.stack.add(5, new ForwardAndShuffleToken());
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
