package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Model.ActionTokens.ActionToken;
import it.polimi.ingsw.Server.Model.ActionTokens.Forward2Token;
import it.polimi.ingsw.Server.Model.ActionTokens.ForwardAndShuffleToken;
import it.polimi.ingsw.Server.Model.ActionTokens.RemoverToken;
import it.polimi.ingsw.Server.Model.Enumerators.Color;

import java.util.ArrayList;
import java.util.Collections;

public class ActionTokenStack {
    private final ArrayList<ActionToken> stack;

    public ActionTokenStack() {
        this.stack = new ArrayList<>();
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
        ActionToken result = stack.get(0);
        for (int i = 0; i < stack.size() - 1; i++) {
            stack.set(i, stack.get(i + 1));
        }
        stack.set(stack.size() - 1, result);

        return result;
    }
}