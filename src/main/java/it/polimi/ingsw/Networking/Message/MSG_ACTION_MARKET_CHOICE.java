package it.polimi.ingsw.Networking.Message;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;

import java.io.Serializable;

public class MSG_ACTION_MARKET_CHOICE extends Message implements Serializable {

    private final boolean[] choice;
    private final boolean isNormalChoice;
    private final Resource extraResourceChoice;

    public MSG_ACTION_MARKET_CHOICE(boolean[] choice, boolean isNormalChoice, Resource extraResourceChoice){
        super(MessageType.MSG_ACTION_MARKET_CHOICE);

        if(isNormalChoice) this.choice = choice.clone();
        else this.choice = null;

        this.extraResourceChoice = extraResourceChoice;
        this.isNormalChoice = isNormalChoice; //if is normalChoice is false, choice is null
    }

    public boolean[] getChoice(){
        return choice;
    }
    public boolean isNormalChoice(){
        return isNormalChoice;
    }
    public Resource getResourceChoice(){
        return this.extraResourceChoice;
    }

    public MessageType getMessageType() {
        return super.getMessageType();
    }
}
