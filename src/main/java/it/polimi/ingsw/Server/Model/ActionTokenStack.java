package it.polimi.ingsw.Server.Model;
import it.polimi.ingsw.Networking.Message.MSG_NOTIFICATION;
import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_FaithTrack;
import it.polimi.ingsw.Server.Model.ActionTokens.*;
import it.polimi.ingsw.Server.Model.Enumerators.Color;
import it.polimi.ingsw.Server.Utils.ModelObservable;

import java.util.*;

public class ActionTokenStack extends ModelObservable {
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


        if (result.isForward2())
            notifyObservers(" Lorenzo has moved Twice on the Faith Track!");
        if (result.isRemover())
        {
            String message = "";
            message = message + " Lorenzo has removed "+((RemoverToken) result).getColor()
                    + " cards from Column number "+(((RemoverToken) result).getColumn()+1);
            notifyObservers(message);
        }
        if(result.isForwardAndShuffle())
            notifyObservers(" Lorenzo has moved One space on the Faith Track and has shuffled his action stack!");

        return result;
        //Controller has instruction: result.doAction();
    }

    private void notifyObservers(String message){
        this.notifyObservers(generateMessage(message));
    }

    public MSG_NOTIFICATION generateMessage(String message)
    {
        return new MSG_NOTIFICATION(message);
    }
}
