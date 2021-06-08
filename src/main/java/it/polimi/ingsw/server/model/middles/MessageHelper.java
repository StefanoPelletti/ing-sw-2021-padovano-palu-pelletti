package it.polimi.ingsw.server.model.middles;

import it.polimi.ingsw.networking.message.MSG_NOTIFICATION;
import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.actionMessages.*;
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
     * Used only in Solo mode.
     * Saves what Lorenzo did in his action to forward it to the player.
     * Also notifies the observers.
     *
     * @param action the action that Lorenzo did
     */
    public synchronized void setLorenzoNotificationMessage(int action) {
        if (action == 0) helperMessage = "Lorenzo destroyed two cards on the devCardDeck!";
        if (action == 1) helperMessage = "Lorenzo gained one faithPoint and shuffled the ActionToken Stack!";
        if (action == 2) helperMessage = "Lorenzo gained two faithPoints!";
        notifyObservers();
    }

    /**
     * Called at the end of an action by a player, generates a MSG_UPD_End and notifies the observers.
     */
    public synchronized void setUpdateEnd() {
        notifyObservers(new MSG_UPD_End());
    }

    /**
     * Saves a generic message to forward to the players.
     * Also notifies the observers.
     *
     * @param message The message to forward to players.
     */
    public synchronized void setNewMessage(String message) {
        helperMessage = message;
        notifyObservers();
    }

    /**
     * Saves an action notification to forward to the players.
     * Also notifies the observers.
     *
     * @param nickname The player who did the action.
     * @param message  The action of the player.
     */
    public synchronized void setNotificationMessage(String nickname, ActionMessage message) {
        helperMessage = message.notifyAction(nickname, this);
        notifyObservers();
    }

    public String init_choose_LeaderCards(String nickname, MSG_INIT_CHOOSE_LEADERCARDS message) {
        return nickname + " has chosen his leadercards.";
    }

    public String init_choose_Resources(String nickname, MSG_INIT_CHOOSE_RESOURCE message) {
        return nickname + " has chosen an initial resource: " + message.getResource().toString();
    }

    public String action_activateLeaderCards(String nickname, MSG_ACTION_ACTIVATE_LEADERCARD message) {
        return nickname + " activated his leadercard number " + (message.getCardNumber() + 1);
    }

    public String action_discardLeaderCards(String nickname, MSG_ACTION_DISCARD_LEADERCARD message) {
        return nickname + " has discarded his leadercard number " + (message.getCardNumber() + 1);
    }

    public String action_changeDepotConfig(String nickname, MSG_ACTION_CHANGE_DEPOT_CONFIG message) {
        return nickname + " has changed the configuration of his depot";
    }

    public String action_activateProduction(String nickname, MSG_ACTION_ACTIVATE_PRODUCTION message) {
        String result = nickname + " activated production. Here's what he chose to activate:\n";
        if (message.isBasicProduction()) {
            result += "Basic power\n   input -> output  : " + message.getBasicInput() + " -> " + message.getBasicOutput() + "\n";
        }
        boolean[] choices = message.getStandardProduction();
        if (choices[0]) result += "Standard production of the card visible in his devSlot 1\n";
        if (choices[1]) result += "Standard production of the card visible in his devSlot 2\n";
        if (choices[2]) result += "Standard production of the card visible in his devSlot 3\n";
        choices = message.getLeaderProduction();
        if (choices[0])
            result += "Leader Card production of his leaderCard 1, he got a " + message.getLeaderOutput1();
        if (choices[1])
            result += "Leader Card production of his leaderCard 2, he got a " + message.getLeaderOutput2();
        return result;
    }

    public String action_buyDevelopmentCard(String nickname, MSG_ACTION_BUY_DEVELOPMENT_CARD message) {
        return nickname + " has decided that it is time to buy a new card, let's see which one he wants";
    }

    public String action_chooseDevelopmentCard(String nickname, MSG_ACTION_CHOOSE_DEVELOPMENT_CARD message) {
        return nickname + " has chosen a card to buy";
    }

    public String action_getMarketResources(String nickname, MSG_ACTION_GET_MARKET_RESOURCES message) {
        String result = nickname + " has decided to go to the market";
        int num = message.getNumber() + 1;
        if (message.getColumn())
            result += " and picked the column " + num;
        else
            result += " and picked the row " + num;
        return result;
    }

    public String action_endTurn(String nickname, MSG_ACTION_ENDTURN message) {
        return nickname + " has ended its turn";
    }

    /**
     * Creates a message using generateMessage() and notifies observers.
     *
     * @see #generateMessage()
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     * Returns a MSG_NOTIFICATION containing the message String previously set.
     *
     * @return A MSG_NOTIFICATION containing the message String previously set.
     */
    public MSG_NOTIFICATION generateMessage() {
        return new MSG_NOTIFICATION(this.helperMessage);
    }
}