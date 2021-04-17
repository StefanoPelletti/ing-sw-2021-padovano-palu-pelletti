package it.polimi.ingsw.Networking.Message;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;

public class MSG_ACTION_MARKET_REQUEST extends Message {
    private boolean[] choices;
    private boolean isNormalChoice;
    private Resource[] extraResourceChoice;

    public MSG_ACTION_MARKET_REQUEST(boolean choices[], boolean isNormalChoice, Resource[] extraResourceChoice){
        super(MessageType.MSG_ACTION_MARKET_REQUEST);
        this.choices = choices.clone();
        this.extraResourceChoice = extraResourceChoice.clone();
        this.isNormalChoice = isNormalChoice;
    }

    @Override
    public MessageType getMessageType() {
        return super.getMessageType();
    }

    public boolean[] getChoice(){
        return choices;
    }

    public boolean isNormalChoice(){
        return isNormalChoice;
    }

    public Resource[] getResourceChoice(){
        return this.extraResourceChoice.clone();
    }
}
