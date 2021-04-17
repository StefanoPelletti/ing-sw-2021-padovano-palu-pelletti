package it.polimi.ingsw.Networking.Message;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;

public class MSG_ACTION_MARKET_CHOICE extends Message {
    private boolean[] choice;
    private boolean isNormalChoice;
    private Resource extraResourceChoice;

    public MSG_ACTION_MARKET_CHOICE(boolean choice[], boolean isNormalChoice, Resource extraResourceChoice){
        super(MessageType.MSG_ACTION_MARKET_CHOICE);
        if(isNormalChoice) this.choice = choice.clone();
        this.extraResourceChoice = extraResourceChoice;
        this.isNormalChoice = isNormalChoice; //if is normalChoice is false, choice is null
    }

    @Override
    public MessageType getMessageType() {
        return super.getMessageType();
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
}
