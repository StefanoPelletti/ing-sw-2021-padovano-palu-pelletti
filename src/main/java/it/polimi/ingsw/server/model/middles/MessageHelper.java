package it.polimi.ingsw.server.model.middles;

import it.polimi.ingsw.networking.message.*;
import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_End;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.ModelObservable;

public class MessageHelper extends ModelObservable {
    private String helperMessage;

    /**
     * CONSTRUCTOR
     */
    public MessageHelper() {
        this.helperMessage = "";
    }

    /**
     * Used only in solo mode
     * Saves what Lorenzo did in his action to forward it to the player
     * Also notifies the observers
     * @param action the action that Lorenzo did
     */
    public synchronized void setLorenzoNotificationMessage(int action) {
        if (action == 0) helperMessage = "Lorenzo destroyed two cards on the devCardDeck!";
        if (action == 1) helperMessage = "Lorenzo gained one faithPoint and shuffled the ActionToken Stack!";
        if (action == 2) helperMessage = "Lorenzo gained two faithPoints!";
        notifyObservers();
    }

    /**
     * Called at the end of an action by a player, generates a MSG_UPD_End and notifies the observers
     */
    public synchronized void setUpdateEnd() {
        notifyObservers(new MSG_UPD_End());
    }

    /**
     * Saves a generic message to forward to the players
     * Also notifies the observers
     * @param message the message to forward to players
     */
    public synchronized void setNewMessage(String message) {
        helperMessage = message;
        notifyObservers();
    }

    /**
     * Saves an action notification to forward to the players
     * Also notifies the observers
     * @param nickname the player who did the action
     * @param message the action of the player
     */
    public synchronized void setNotificationMessage(String nickname, Message message) {
        switch (message.getMessageType()) {
            case MSG_ACTION_ACTIVATE_LEADERCARD:
                helperMessage = nickname + " activated his leadercard number " + (((MSG_ACTION_ACTIVATE_LEADERCARD) message).getCardNumber() + 1);
                break;
            case MSG_INIT_CHOOSE_LEADERCARDS:
                helperMessage = nickname + " has chosen his leadercards.";
                break;
            case MSG_INIT_CHOOSE_RESOURCE:
                Resource r1 = ((MSG_INIT_CHOOSE_RESOURCE) message).getResource();
                helperMessage = nickname + " has chosen an initial resource: " + r1;
                break;
            case MSG_ACTION_DISCARD_LEADERCARD:
                helperMessage = nickname + " has discarded his leadercard number " + (((MSG_ACTION_DISCARD_LEADERCARD) message).getCardNumber() + 1);
                break;
            case MSG_ACTION_CHANGE_DEPOT_CONFIG:
                helperMessage = nickname + " has changed the configuration of his depot";
                break;
            case MSG_ACTION_ACTIVATE_PRODUCTION:
                MSG_ACTION_ACTIVATE_PRODUCTION msg = (MSG_ACTION_ACTIVATE_PRODUCTION) message;
                helperMessage = nickname + " activated production. Here's what he chose to activate:\n";
                if (msg.isBasicProduction()) {
                    helperMessage += "Basic power\n   input | output  : "+msg.getBasicInput()+" | "+msg.getBasicOutput();
                }
                boolean[] choices = msg.getStandardProduction();
                if (choices[0]) helperMessage += "Standard production of the card visible in his devSlot 1\n";
                if (choices[1]) helperMessage += "Standard production of the card visible in his devSlot 2\n";
                if (choices[2]) helperMessage += "Standard production of the card visible in his devSlot 3\n";
                choices = msg.getLeaderProduction();
                if (choices[0]) helperMessage += "Leader Card production of his leaderCard 1, he got a "+msg.getLeaderOutput1();
                if (choices[1]) helperMessage += "Leader Card production of his leaderCard 2, he got a "+msg.getLeaderOutput2();
                break;
            case MSG_ACTION_GET_MARKET_RESOURCES:
                helperMessage = nickname + " has decided to go to the market";
                int num = ((MSG_ACTION_GET_MARKET_RESOURCES) message).getNumber() + 1;
                if (((MSG_ACTION_GET_MARKET_RESOURCES) message).getColumn())
                    helperMessage += " and picked the column " + num;
                else
                    helperMessage += " and picked the row " + num;
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

    /**
     * Creates a message and notifies observers
     * @see #generateMessage()
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     *
     * @return a MSG_UPD_MarketHelper representing the current state of the LeaderCardsPicker
     */
    public MSG_NOTIFICATION generateMessage() {
        return new MSG_NOTIFICATION(this.helperMessage);
    }
}