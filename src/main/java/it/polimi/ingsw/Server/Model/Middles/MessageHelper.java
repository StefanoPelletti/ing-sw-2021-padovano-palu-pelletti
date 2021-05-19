package it.polimi.ingsw.Server.Model.Middles;

import it.polimi.ingsw.Networking.Message.MSG_NOTIFICATION;
import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_End;
import it.polimi.ingsw.Server.Utils.ModelObservable;
import it.polimi.ingsw.Networking.Message.*;
import it.polimi.ingsw.Server.Model.Enumerators.*;

public class MessageHelper extends ModelObservable {

    String helperMessage;

    public MessageHelper() {
        this.helperMessage = "";
    }

    public synchronized void setLorenzoNotificationMessage(int number) {
        if (number == 0) helperMessage = "Lorenzo destroyed two cards on the devCardDeck!";
        if (number == 1) helperMessage = "Lorenzo gained one faithPoint and shuffled the ActionToken Stack!";
        if (number == 2) helperMessage = "Lorenzo gained two faithPoints!";
        notifyObservers();
    }

    public synchronized void setUpdateEnd() {
        notifyObservers(new MSG_UPD_End());
    }

    public synchronized void setNewMessage(String message) {
        helperMessage = message;
        notifyObservers();
    }

    public synchronized void setNotificationMessage(String nickname, Message message) {
        switch (message.getMessageType()) {
            case MSG_ACTION_ACTIVATE_LEADERCARD:
                helperMessage = nickname + " activated his leadercard number " + ((MSG_ACTION_ACTIVATE_LEADERCARD) message).getCardNumber();
                break;
            case MSG_INIT_CHOOSE_LEADERCARDS:
                helperMessage = nickname + " has chosen his leadercards.";
                break;
            case MSG_INIT_CHOOSE_RESOURCE:
                Resource r1 = ((MSG_INIT_CHOOSE_RESOURCE) message).getResource();
                helperMessage = nickname + " has chosen an initial resource: " + r1;
                break;
            case MSG_ACTION_DISCARD_LEADERCARD:
                helperMessage = nickname + " has discarded his leadercard number " + ((MSG_ACTION_DISCARD_LEADERCARD) message).getCardNumber();
                break;
            case MSG_ACTION_CHANGE_DEPOT_CONFIG:
                helperMessage = nickname + " has changed the configuration of his depot";
                break;
            case MSG_ACTION_ACTIVATE_PRODUCTION:
                MSG_ACTION_ACTIVATE_PRODUCTION msg = (MSG_ACTION_ACTIVATE_PRODUCTION) message;
                helperMessage = nickname + " activated production. Here's what he chose to activate:\n";
                if (msg.isBasicProduction()) {
                    helperMessage += "Basic power\n";
                }
                boolean[] choices = msg.getStandardProduction();
                if (choices[0]) helperMessage += "Standard production of the card visible in his devSlot 1\n";
                if (choices[1]) helperMessage += "Standard production of the card visible in his devSlot 2\n";
                if (choices[2]) helperMessage += "Standard production of the card visible in his devSlot 3\n";
                choices = msg.getLeaderProduction();
                if (choices[0]) helperMessage += "Leader Card production of his leaderCard 1\n";
                if (choices[1]) helperMessage += "Leader Card production of his leaderCard 2\n";
                break;
            case MSG_ACTION_GET_MARKET_RESOURCES:
                helperMessage = nickname + " has decided to go to the market";
                int num = ((MSG_ACTION_GET_MARKET_RESOURCES) message).getNumber() + 1;
                if (((MSG_ACTION_GET_MARKET_RESOURCES) message).getColumn())
                    helperMessage += " and picked the column " + num;
                else helperMessage += " and picked the row " + ((MSG_ACTION_GET_MARKET_RESOURCES) message).getNumber();
                break;
            case MSG_ACTION_BUY_DEVELOPMENT_CARD:
                helperMessage = nickname + " has decided that it is time to buy a new card, let's see which one he wants";
                break;
            case MSG_ACTION_CHOOSE_DEVELOPMENT_CARD:
                helperMessage = nickname + " has chosen a card to buy";
                break;
            case MSG_ACTION_ENDTURN:
                helperMessage = nickname + " has ended its turn";
                break;
        }
        notifyObservers();
    }

    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    public MSG_NOTIFICATION generateMessage() {
        return new MSG_NOTIFICATION(this.helperMessage);
    }
}