package it.polimi.ingsw.Server.Model.Middles;

import it.polimi.ingsw.Networking.Message.MSG_NOTIFICATION;
import it.polimi.ingsw.Server.Utils.ModelObservable;
import it.polimi.ingsw.Networking.Message.*;
import it.polimi.ingsw.Server.Model.Enumerators.*;

public class ActionHelper extends ModelObservable {

    String actionMessage;

    public ActionHelper() {
        this.actionMessage = "";
    }

    public void setLorenzoNotificationMessage(int number){
        if(number == 0) actionMessage = "Lorenzo destroyed two cards on the devCardDeck!";
        if(number == 1) actionMessage = "Lorenzo gained one faithPoint and shuffled the ActionToken Stack!";
        if(number == 2) actionMessage = "Lorenzo gained two faithPoints!";
        //notifyObservers();
    }

    public void setNotificationMessage(String nickname, Message message){
        switch(message.getMessageType()){
            case MSG_ACTION_ACTIVATE_LEADERCARD:
                actionMessage = nickname + " activated his leadercard number "+ ((MSG_ACTION_ACTIVATE_LEADERCARD) message).getCardNumber();
                break;
            case MSG_INIT_CHOOSE_LEADERCARDS:
                actionMessage = nickname + " has chosen his leadercards.";
                break;
            case MSG_INIT_CHOOSE_RESOURCE:
                Resource r1 = ((MSG_INIT_CHOOSE_RESOURCE) message).getResource();
                actionMessage = nickname + " has chosen an inital resource: " + r1;
                break;
            case MSG_ACTION_DISCARD_LEADERCARD:
                actionMessage = nickname + " has discarded his leadercard number "+ ((MSG_ACTION_DISCARD_LEADERCARD) message).getCardNumber();
                break;
            case MSG_ACTION_CHANGE_DEPOT_CONFIG:
                actionMessage = nickname + " has changed the configuration of his depot";
                break;
            case MSG_ACTION_ACTIVATE_PRODUCTION:
                MSG_ACTION_ACTIVATE_PRODUCTION msg = (MSG_ACTION_ACTIVATE_PRODUCTION) message;
                actionMessage = nickname + " activated production. Here's what he chose to activate:\n";
                if(msg.isBasicProduction()){
                    actionMessage+= "Basic power\n";
                }
                boolean[] choices = msg.getStandardProduction();
                if(choices[0]) actionMessage+= "Standard production of the card visible in his devSlot 1\n";
                if(choices[1]) actionMessage+= "Standard production of the card visible in his devSlot 2\n";
                if(choices[2]) actionMessage+= "Standard production of the card visible in his devSlot 3\n";
                choices = msg.getLeaderProduction();
                if(choices[0]) actionMessage+= "Leader Card production of his leaderCard 1\n";
                if(choices[1]) actionMessage+= "Leader Card production of his leaderCard 2\n";
                break;
            case MSG_ACTION_GET_MARKET_RESOURCES:
                actionMessage = nickname + " has decided to go to the market";
                if(((MSG_ACTION_GET_MARKET_RESOURCES) message).getColumn()) actionMessage+=" and picked the column "+((MSG_ACTION_GET_MARKET_RESOURCES) message).getNumber();
                else  actionMessage+=" and picked the row "+((MSG_ACTION_GET_MARKET_RESOURCES) message).getNumber();
                break;
            case MSG_ACTION_MARKET_CHOICE:
                actionMessage = nickname +" has done something on a resource grabbed in the market";
                break;
            case MSG_ACTION_BUY_DEVELOPMENT_CARD:
                actionMessage = nickname +" has decided that it is time to buy a new card, let's see which one he wants";
                break;
            case MSG_ACTION_CHOOSE_DEVELOPMENT_CARD:
                actionMessage = nickname + " has chosen a card to buy";
                break;
            case MSG_ACTION_ENDTURN:
                actionMessage = nickname+ " has ended is turn";
                break;
        }
        //notifyObservers();
    }

    private void notifyObservers(){
        this.notifyObservers(generateMessage());
    }

    public MSG_NOTIFICATION generateMessage()
    {
        return new MSG_NOTIFICATION(this.actionMessage);
        }
}
