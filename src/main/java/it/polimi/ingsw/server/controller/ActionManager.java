package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.networking.message.actionMessages.*;
import it.polimi.ingsw.server.model.*;
import it.polimi.ingsw.server.model.actionTokens.ActionToken;
import it.polimi.ingsw.server.model.actionTokens.RemoverToken;
import it.polimi.ingsw.server.model.enumerators.Color;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.model.enumerators.Status;
import it.polimi.ingsw.server.model.marbles.MarketMarble;
import it.polimi.ingsw.server.model.marbles.RedMarbleException;
import it.polimi.ingsw.server.model.middles.*;
import it.polimi.ingsw.server.model.requirements.CardRequirements;
import it.polimi.ingsw.server.model.requirements.ReqValue;
import it.polimi.ingsw.server.model.requirements.Requirement;
import it.polimi.ingsw.server.model.requirements.ResourceRequirements;
import it.polimi.ingsw.server.model.specialAbilities.DiscountResource;
import it.polimi.ingsw.server.model.specialAbilities.ExtraDepot;
import it.polimi.ingsw.server.model.specialAbilities.MarketResources;
import it.polimi.ingsw.server.model.specialAbilities.Production;

import java.util.*;


public class ActionManager {
    /**
     * the reference to the GameManager
     */
    private final GameManager gameManager;
    /**
     * the reference to the faithTrackManager
     */
    private final FaithTrackManager faithTrackManager;
    /**
     * the reference to the model entry point
     */
    private final Game game;
    /**
     * the reference to the messageHelper, which provides many broadcasting services
     */
    private final MessageHelper messageHelper;

    /**
     * CONSTRUCTOR
     */
    public ActionManager(GameManager gameManager, FaithTrackManager faithTrackManager, Game game) {
        this.gameManager = gameManager;
        this.faithTrackManager = faithTrackManager;
        this.game = game;
        this.messageHelper = game.getMessageHelper();
    }

    /**
     * Passes a specified Message to the ActionManager, which then executes the correct requested action.
     * The Player performing the action is always the Current Player.
     * Objects calling this method should check that they're handling the current Player before calling this filter.
     * If the selected method returns true, it sends a message to notify the Players that the action is concluded, so they do not expect other messages related to the performed action.
     *
     * @param message The message that a player sent, it is the action that he wants to perform.
     */
    public void onMessage(ActionMessage message) {
        gameManager.resetErrorObject();
        boolean result;
        result = message.execute(this);
        if (result) messageHelper.setUpdateEnd();
    }

    /**
     * Associates the chosen initial Leader Cards to the Current Player.
     * For the given player, it associates two leaderCards contained in the LeaderCardsPicker, using the two indexes contained in the message.
     * If an error occurs, the action is cancelled and the error is notified. Nothing else changes in the model.
     * ERRORS:
     * <ul>
     *      <li> the message has invalid values in its fields: the indexes are not between 1 and 4. </li>
     *      <li> the message contains two indexes that have the same value. </li>
     *      <li> the method is invoked while LeaderCardsPicker middle-object was not enabled. </li>
     * </ul>
     * If the association of cards is correct:
     * <ul>
     *      <li> if the game is in solo mode, the initialization phase is over and standard turns begin. </li>
     *      <li> if the game is in multiplayer and the player is not the last, 4 new LeaderCards are offered to the next player. </li>
     *      <li> if the game is in multiplayer and the player is the last, the initial resources are offered to the players or the standard turn begins. </li>
     * </ul>
     *
     * @param player  The reference to the Current Player.
     * @param message The message that the player sent.
     * @return True if and only if the Resource are associated to the Players without errors.
     * @see MSG_INIT_CHOOSE_LEADERCARDS
     * @see LeaderCardsPicker
     */
    public boolean chooseLeaderCard(Player player, MSG_INIT_CHOOSE_LEADERCARDS message) {
        int firstCard = message.getFirstCard();
        int secondCard = message.getSecondCard();
        List<LeaderCard> cards = new ArrayList<>();
        LeaderCardsPicker leaderCardsPicker = game.getLeaderCardsPicker();
        ResourcePicker resourcePicker = game.getResourcePicker();

//MESSAGE VALIDATION    //impossible to test
        if (firstCard < 0 || firstCard > 3) {
            gameManager.setErrorObject("Error! First Card number must be between 1 and 4!");
            return false;
        }
        if (secondCard < 0 || secondCard > 3) {
            gameManager.setErrorObject("Error! Second Card number must be between 1 and 4!");
            return false;
        }
        if (firstCard == secondCard) {
            gameManager.setErrorObject("Error! Message not well formatted: card #1 == card #2!");
            return false;
        }
//VALIDATION
        if (!leaderCardsPicker.isEnabled()) {
            gameManager.setErrorObject("Error! Method chooseLeaderCards was somehow invoked while LeaderCardsPicker middle-object was not enabled!");
            return false;
        }

        cards.add(leaderCardsPicker.getCards().get(firstCard));
        cards.add(leaderCardsPicker.getCards().get(secondCard));
        messageHelper.setNotificationMessage(player.getNickname(), message);
        player.associateLeaderCards(cards); //notifies player

        if (gameManager.getSolo()) {
            leaderCardsPicker.setEnabled(false);
            game.changeStatus(Status.STANDARD_TURN);
        } else //if MULTIPLAYER
        {
            player.setDisconnectedBeforeLeaderCard(false); //player has chosen his card.

            if (game.getStatus() == Status.INIT_1) {
                endTurn(player, false); //if STATUS is DISTRIBUTING CARDS, advance.
            } else //if status == Status.STANDARD_TURN (can't be INIT_2), maybe the player was disconnected and has to choose the resource.
            {
                leaderCardsPicker.setEnabled(false);
                if (player.isDisconnectedBeforeResource()) {
                    if (player.getPlayerNumber() != 1) {
                        resourcePicker.setNumOfResources(player.getStartingResources());
                        resourcePicker.setEnabled(true);
                    }
                }
            }
        }
        return true;
    }

    /**
     * Adds the chosen initial Resource to the Current Player.
     * Only in Multiplayer Mode.
     * For the given player, it adds the resource contained in the message in his depot.
     * <p>
     * If an error occurs, the action is cancelled and the error is notified. Nothing else changes in the model.
     * ERRORS:
     * <ul>
     *      <li> the message has invalid values in its fields: the resource is not among the permitted values Resource.STONE, Resource.SERVANT, Resource.SHIELD, Resource.COIN. </li>
     *      <li> the method is invoked while LeaderCardsPicker middle-object was not enabled. </li>
     * </ul>
     * If the resource is added:
     * <ul>
     *      <li> if the player is not the last, prepare the ResourcePicker for the next player, which must choose his initial resources. </li>
     *      <li> if the player is the last one, standard turns begin. </li>
     * </ul>
     *
     * @param player  The reference to the Current Player.
     * @param message The message that the player sent.
     * @return trTrue if and only if the resource is added to the depot of the player without errors.
     * @see MSG_INIT_CHOOSE_RESOURCE
     * @see ResourcePicker
     */
    public boolean chooseResource(Player player, MSG_INIT_CHOOSE_RESOURCE message) {
        Resource resource = message.getResource();

        ResourcePicker resourcePicker = game.getResourcePicker();

//MESSAGE VALIDATION //impossible to test
        if (resource != Resource.COIN && resource != Resource.SERVANT && resource != Resource.STONE && resource != Resource.SHIELD) {
            gameManager.setErrorObject("Error! Message not well formatted: resource not a default one!");
            return false;
        }
//VALIDATION
        if (!resourcePicker.isEnabled()) //how the h did he get in here?
        {
            gameManager.setErrorObject("Error! Method chooseResource was somehow invoked while ResourcePicker middle-object was not enabled!");
            return false;
        }

        this.messageHelper.setNotificationMessage(player.getNickname(), message);
        player.getWarehouseDepot().add(resource); //notifies Warehouse


        if (resourcePicker.getNumOfResources() == 2)
            player.getWarehouseDepot().swapRow(1, 2);
        player.decrementStartingResource();
        resourcePicker.decNumOfResources();

        if (game.getStatus() == Status.INIT_2) //so it is distributing the resources
        {
            if (resourcePicker.getNumOfResources() == 0) {
                endTurn(player, false);
                player.setDisconnectedBeforeResource(false);
            } else return true;
        } else //status == StandardTurn
        {
            if (resourcePicker.getNumOfResources() == 0) {
                player.setDisconnectedBeforeResource(false);
                resourcePicker.setEnabled(false);
            } else return true;
        }
        return true;
    }

    /**
     * Activates the chosen Leader Card of the Current Player.
     * For the given player, it checks if the leaderCard that has been passed can be activated by controlling the resources or the cards owned.
     * If the requirements are all fulfilled then it activates the leaderCard and notifies the player.
     * <p>
     * If an error occurs, the action is cancelled and the error is notified. Nothing else changes in the model.
     * ERRORS:
     * <ul>
     *     <li> the message did not contain cardNumber as 0 or 1. </li>
     *     <li> the selected leaderCard was discarded. </li>
     *     <li> the selected leaderCard was already enabled. </li>
     *     <li> the resources for the activation are not enough (ResourceRequirement). </li>
     *     <li> the cards owned are not enough or are not the ones needed (CardRequirement). </li>
     * </ul>
     *
     * @param player  The reference to the Current Player.
     * @param message The message that the player sent.
     * @return True if and only if the Leader Card has been activated correctly.
     */
    public boolean activateLeaderCard(Player player, MSG_ACTION_ACTIVATE_LEADERCARD message) {
        int cardNumber = message.getCardNumber();

//MESSAGE VALIDATION        //impossible to test
        if (cardNumber != 0 && cardNumber != 1) {
            gameManager.setErrorObject("Error! Message not well formatted: not 0 or 1!");
            return false;
        }
//VALIDATION
        if (player.getLeaderCards()[cardNumber] == null) {
            gameManager.setErrorObject("Error! This card is already discarded!");
            return false;
        }

        if (player.getLeaderCards()[cardNumber].isEnabled()) {
            gameManager.setErrorObject("Error! This card was already enabled!");
            return false;
        }

        LeaderCard l = player.getLeaderCards()[cardNumber];
        Requirement requirement = l.getRequirement();

        if (requirement.isResourceRequirement()) {
            ResourceRequirements resourceRequirements = (ResourceRequirements) requirement;
            Map<Resource, Integer> requiredResources = resourceRequirements.getRequirements();

            Map<Resource, Integer> resources = player.getResources();

            for (Resource resource : requiredResources.keySet()) {
                resources.put(resource, resources.get(resource) - requiredResources.get(resource));
                if (resources.get(resource) < 0) {
                    gameManager.setErrorObject("Error! You don't have enough resources!");
                    return false;
                }
            }
            messageHelper.setNotificationMessage(player.getNickname(), message);
            player.setLeaderCards(cardNumber, true);
            return true;
        } else { //if (requirement.isCardRequirement()) {
            CardRequirements cardRequirements = (CardRequirements) requirement;
            Map<Color, ReqValue> requiredCards = cardRequirements.getRequirements();
            List<DevelopmentCard> playerCards = player.getDevelopmentSlot().getCards();
            int requiredNumCard;
            int requiredLevelCard;
            for (Color color : requiredCards.keySet()) {
                requiredNumCard = requiredCards.get(color).getReqNumCard();
                requiredLevelCard = requiredCards.get(color).getReqLvlCard();
                for (DevelopmentCard card : playerCards) {
                    if (card.getColor() == color) {
                        if (requiredLevelCard == -1 || requiredLevelCard == card.getLevel()) {
                            requiredNumCard--;
                        }
                    }
                }
                if (requiredNumCard > 0) {
                    gameManager.setErrorObject("Error! You don't own the correct cards!");
                    return false;
                }
            }
            messageHelper.setNotificationMessage(player.getNickname(), message);
            player.setLeaderCards(cardNumber, true);
            return true;
        }
    }

    /**
     * Discard the chosen Leader Card of the Current Player.
     * For the given player, it checks if the leaderCard that has been passed is already discarded, if not, it discards it, otherwise it notifies an error.
     * ERRORS:
     * <ul>
     *     <li> The cardNumber in the message is not a 0 or a 1. </li>
     *     <li> The leaderCard is already discarded. </li>
     *     <li> The leaderCard has been enabled and can't be discarded anymore. </li>
     * </ul>
     * If the leaderCard is discarded correctly, it moves the player's position on the FaithTrack by one.
     *
     * @param player  The reference to the current player.
     * @param message The message that the player sent.
     * @return True if and only if the Leader Card has been discarded correctly.
     */
    public boolean discardLeaderCard(Player player, MSG_ACTION_DISCARD_LEADERCARD message) {
        int cardNumber = message.getCardNumber();

//MESSAGE VALIDATION    //impossible to test
        if (cardNumber != 0 && cardNumber != 1) {
            gameManager.setErrorObject("Error! Message not well formatted: cardNumber is not a 0 or a 1. ");
            return false; //impossible?  cannot build a message like that // still a layer of defense.
        }
//VALIDATION
        if (player.getLeaderCards()[cardNumber] == null) {
            gameManager.setErrorObject("Error! You already discarded that card!");
            return false;
        }
        if (player.getLeaderCards()[cardNumber].isEnabled()) {
            gameManager.setErrorObject("Error! You can't discard a card that has already been enabled!");
            return false;
        }
//MODEL UPDATE
        this.messageHelper.setNotificationMessage(player.getNickname(), message);
        player.setLeaderCards(cardNumber, false);
        faithTrackManager.advance(player);
        return true;
    }

    /**
     * Tries to activate all the chosen Production for the Current Player.
     * Checks if the selected type of productions can be done, if not it notifies an error (see below).
     * If the player can do all the selected productions, it gets the costs for the standard production, the basic production and the leader production.
     * It then checks if the player has enough resources for everything, if so it sets a main action as done, notifies all the players and removes the resources from depot and strongbox.
     * If the player has not enough resources it gives an error message.
     * Finally, the produced resources are added in the strongbox and the faith points are added (the player advances on the Faith Track).
     * <p>
     * ERRORS:
     * <ul>
     *     <li> one of the parameters 'standardproduction' or 'leaderproduction' is null. </li>
     *     <li> baseProduction is true but basicInput is null or not 2. </li>
     *     <li> one of the two leaderCards is being used for the production but the output is null (not specified). </li>
     *     <li> the player has already done a main action. </li>
     *     <li> the leaderCard selected (first or second) has been discarded or is not enabled or its power is not production. </li>
     *     <li> the player has chosen a slot with no cards. </li>
     *     <li> the resource for the basic production can't be produced because it is not valid. </li>
     *     <li> the resource for the leaderCard production can't be produced because it is not valid. </li>
     *     <li> the player as not enough resources for all the selected productions. </li>
     * </ul>
     *
     * @param player  The player that wants to activate the production.
     * @param message The message that the player sent.
     * @return True if and only if the production has been done correctly.
     */
    public boolean activateProduction(Player player, MSG_ACTION_ACTIVATE_PRODUCTION message) {
        Map<Resource, Integer> initialResources = player.getResources();
        boolean[] standardProduction = message.getStandardProduction();
        boolean baseProduction = message.isBasicProduction();
        boolean[] leaderProduction = message.getLeaderProduction();
        Resource[] possibleResources = new Resource[]{Resource.COIN, Resource.STONE, Resource.SHIELD, Resource.SERVANT};
        List<Resource> basicInput = message.getBasicInput();
        Resource basicOutput = message.getBasicOutput();
        Resource leaderOutput1 = message.getLeaderOutput1();
        Resource leaderOutput2 = message.getLeaderOutput2();

//MESSAGE VALIDATION    //impossible to test
        if (standardProduction == null || leaderProduction == null) {
            gameManager.setErrorObject("Error! Message not well formatted: standardProduction or leaderProduction == null");
            return false;
        }
        if (baseProduction && (basicInput == null || basicInput.size() != 2)) {
            gameManager.setErrorObject("Error! Message not well formatted: baseProduction && (basicInput==null || basicInput.size()!=2)");
            return false;
        }
        if (leaderProduction[0] && leaderOutput1 == null) {
            gameManager.setErrorObject("Error! Message not well formatted: leaderProduction[0] && leaderOutput1==null");
            return false;
        }
        if (leaderProduction[1] && leaderOutput2 == null) {
            gameManager.setErrorObject("Error! Message not well formatted: leaderProduction[1] && leaderOutput2==null");
            return false;
        }
//VALIDATION
        if (player.getAction()) {
            gameManager.setErrorObject("Error! You already performed a main action");
            return false;
        }

        if (leaderProduction[0]) {
            LeaderCard l1 = player.getLeaderCards()[0];
            if (l1 == null || !l1.isEnabled() || !l1.getSpecialAbility().isProduction()) {
                gameManager.setErrorObject("You cannot produce with the first leader card!");
                return false;
            }
        }

        if (leaderProduction[1]) {
            LeaderCard l2 = player.getLeaderCards()[1];
            if (l2 == null || !l2.isEnabled() || !l2.getSpecialAbility().isProduction()) {
                gameManager.setErrorObject("You cannot produce with the second leader card!");
                return false;
            }
        }

        DevelopmentCard[] top = player.getDevelopmentSlot().getOnTop();
        for (int i = 0; i < 3; i++) {
            if (top[i] == null && standardProduction[i]) {
                gameManager.setErrorObject("You chose a slot with no cards!");
                return false;
            }
        }

        if (baseProduction && Arrays.stream(possibleResources).noneMatch(r -> r == message.getBasicOutput())) {
            gameManager.setErrorObject("You cannot produce that resource with that the Basic Power!");
            return false;
        }

        if (leaderProduction[0] && Arrays.stream(possibleResources).noneMatch(r -> r == message.getLeaderOutput1())) {
            gameManager.setErrorObject("You cannot produce that resource with that Leader Cards!");
            return false;
        }
        if (leaderProduction[1] && Arrays.stream(possibleResources).noneMatch(r -> r == message.getLeaderOutput2())) {
            gameManager.setErrorObject("You cannot produce that resource with that Leader Cards!");
            return false;
        }

        Map<Resource, Integer> requiredResources = new HashMap<>();
        Map<Resource, Integer> newResources = new HashMap<>();

//VALIDATION
        //initializing requiredResources
        for (Resource r : possibleResources) {
            requiredResources.put(r, 0);
            newResources.put(r, 0);
        }

        newResources.put(Resource.FAITH, 0);

        //getting costs for standard Productions
        DevelopmentCard[] topCards = player.getDevelopmentSlot().getOnTop();
        for (int i = 0; i < 3; i++) {
            if (standardProduction[i]) {
                Map<Resource, Integer> input = topCards[i].getPowerInput();
                for (Resource r : input.keySet()) {
                    requiredResources.merge(r, input.get(r), Integer::sum);
                }
                Map<Resource, Integer> output = topCards[i].getPowerOutput();
                for (Resource r : output.keySet()) {
                    newResources.merge(r, output.get(r), Integer::sum);
                }
            }
        }

        //getting cost for base Production
        if (baseProduction) {
            for (Resource r : basicInput) {
                requiredResources.merge(r, 1, Integer::sum);
            }
            newResources.merge(basicOutput, 1, Integer::sum);
        }

        //getting cost for leader Production
        //first LeaderCard
        if (leaderProduction[0]) {
            Production ability = (Production) player.getLeaderCards()[0].getSpecialAbility();
            Resource input = ability.getInput();
            requiredResources.put(input, requiredResources.get(input) + 1);
            newResources.put(leaderOutput1, newResources.get(leaderOutput1) + 1);
        }

        //secondLeaderCard
        if (leaderProduction[1]) {
            Production ability = (Production) player.getLeaderCards()[1].getSpecialAbility();
            Resource input = ability.getInput();
            requiredResources.put(input, requiredResources.get(input) + 1);
            newResources.put(leaderOutput2, newResources.get(leaderOutput2) + 1);
        }

        //check if the player has enough resources
        for (Resource r : requiredResources.keySet()) {
            if (initialResources.get(r) < requiredResources.get(r)) {
                gameManager.setErrorObject("Error! You miss the resource to get this production done!");
                return false;
            }
        }
//MODEL UPDATE
        player.setAction();
        messageHelper.setNotificationMessage(player.getNickname(), message);
        //now we must consume the required resources
        consumeResources(player, requiredResources);
        //now we add output resources to the player's strongbox (and the faith points)
        Strongbox playerStrongbox = player.getStrongbox();
        for (Resource r : newResources.keySet()) {
            if (r == Resource.FAITH) {
                for (int i = 0; i < newResources.get(Resource.FAITH); i++) {
                    faithTrackManager.advance(player);
                }
            } else if (newResources.get(r) > 0) playerStrongbox.addResource(r, newResources.get(r));
        }

        //for each leaderProduction enabled, player receives one Faith Point
        for (boolean enabled : leaderProduction) {
            if (enabled) {
                faithTrackManager.advance(player);
            }
        }
        return true;
    }

    /**
     * Tries to set a new WarehouseDepot and ExtraDepots configuration for the Current Player.
     * Checks if the new configuration of the resources owned is valid, if so it swaps the resources and notifies the other players.
     * If not, it gives one of the errors below.
     * ERRORS:
     * <ul>
     *      <li> the resource in the first slot is not a valid resource. </li>
     *      <li> the second and third slot are null. </li>
     *      <li> the extra depot leaderCards are not enabled or the number of resources that the player has tried to put in them is not valid. </li>
     *      <li> the player has tried to put resources in a leaderCard that has not an extra depot ability. </li>
     *      <li> the configuration for a slot (any of the three) is not valid. </li>
     *      <li> the configuration for an extra depot (any of the two) is not valid. </li>
     *      <li> the number of resources of the new configuration do not match the one for the previous configuration. </li>
     * </ul>
     * NOTE: if the configuration is the same as the one before it just returns true (no notifications for the players).
     * NOTE: for the errors related to the slots:
     *       @see WarehouseDepot
     *
     * @param player  The reference to the Current Player.
     * @param message The message that the player has sent.
     * @return True if and only if the configuration has changed correctly or if it is the same as before.
     */
    public boolean changeDepotConfig(Player player, MSG_ACTION_CHANGE_DEPOT_CONFIG message) {
        Resource slot1 = message.getSlot1();
        Resource[] slot2 = message.getSlot2();
        Resource[] slot3 = message.getSlot3();
        int firstExtraDepot = message.getFirstExtraDepot();
        int secondExtraDepot = message.getSecondExtraDepot();
        Map<Resource, Integer> initialResources = player.getDepotAndExtraDepotResources();
        LeaderCard[] playerLeaderCards = player.getLeaderCards();


//MESSAGE VALIDATION    //impossible to test
        if (slot1 != Resource.COIN && slot1 != Resource.SERVANT && slot1 != Resource.SHIELD && slot1 != Resource.STONE && slot1 != Resource.NONE) {
            gameManager.setErrorObject("Error! Message not well formatted: slot1 not default resources!");
            return false;
        }
        if (slot2 == null || slot3 == null) {
            gameManager.setErrorObject("Error! Message not well formatted: slot 2 or 3 are null!");
            return false;
        }
        if (firstExtraDepot != -1 && firstExtraDepot != 0 && firstExtraDepot != 1 && firstExtraDepot != 2) {
            gameManager.setErrorObject("Error! Message not well formatted: extraDepot 1 value is not -1 0 1 2!");
            return false;
        }
        if (secondExtraDepot != -1 && secondExtraDepot != 0 && secondExtraDepot != 1 && secondExtraDepot != 2) {
            gameManager.setErrorObject("Error! Message not well formatted: extraDepot 2 value is not -1 0 1 2!");
            return false;
        }

//VALIDATION
        if (firstExtraDepot >= 0 && !playerLeaderCards[0].getSpecialAbility().isExtraDepot()) {
            gameManager.setErrorObject("Error! Your Leader Card number 1 does not have an Extra Depot!");
            return false;
        }

        if (secondExtraDepot >= 0 && !playerLeaderCards[1].getSpecialAbility().isExtraDepot()) {
            gameManager.setErrorObject("Error! Your Leader Card number 2 does not have an Extra Depot!");
            return false;
        }

        Resource[] possibleResources = new Resource[]{Resource.COIN, Resource.STONE, Resource.SHIELD, Resource.SERVANT};
        Map<Resource, Integer> newResources;
        WarehouseDepot demoDepot = new WarehouseDepot();
        ExtraDepot demoExtraDepot = new ExtraDepot(Resource.COIN);
        if (!demoDepot.setConfig(slot1, slot2, slot3)) {
            gameManager.setErrorObject("Error! You cannot put the resources like that in the depot!");
            return false;
        }
        if (firstExtraDepot != -1 && !demoExtraDepot.setResource(firstExtraDepot)) {
            gameManager.setErrorObject("Error! You cannot put the resources like that in the first Extra Depot!");
            return false; //impossible? Cannot build message like that
        }
        if (secondExtraDepot != -1 && !demoExtraDepot.setResource(secondExtraDepot)) {
            gameManager.setErrorObject("Error! You cannot put the resources like that in the second Extra Depot!");
            return false; //impossible? Cannot build message like that
        }

        //let's check if the new configuration has the same resources as the one before!
        newResources = demoDepot.getResources();

        if (firstExtraDepot >= 0) {
            ExtraDepot ability = (ExtraDepot) playerLeaderCards[0].getSpecialAbility();
            Resource resource = ability.getResourceType();
            newResources.merge(resource, firstExtraDepot, Integer::sum);
        }

        if (secondExtraDepot >= 0) {
            ExtraDepot ability = (ExtraDepot) playerLeaderCards[1].getSpecialAbility();
            Resource resource = ability.getResourceType();
            newResources.merge(resource, secondExtraDepot, Integer::sum);
        }

        for (Resource resToControl : possibleResources) {
            if (!initialResources.get(resToControl).equals(newResources.get(resToControl))) {
                gameManager.setErrorObject("Error! The number of resources does not match!");
                return false;
            }
        }

        if (demoDepot.equals(player.getWarehouseDepot()))
            return true;

//MODEL UPDATE
        messageHelper.setNotificationMessage(player.getNickname(), message);
        //after all those controls, player really deserves a new depot!
        player.getWarehouseDepot().setConfig(slot1, slot2, slot3);
        if (firstExtraDepot >= 0) {
            ExtraDepot ability1 = (ExtraDepot) playerLeaderCards[0].getSpecialAbility();
            if (firstExtraDepot == 0 && ability1.getNumber() == 0) {
            } else
                ability1.setResource(firstExtraDepot);
        }
        if (secondExtraDepot >= 0) {
            ExtraDepot ability2 = (ExtraDepot) playerLeaderCards[1].getSpecialAbility();
            if (secondExtraDepot == 0 && ability2.getNumber() == 0) {
            } else
                ability2.setResource(secondExtraDepot);
        }
        return true;
    }

    /**
     * Shows the Current Player the possible acquirable Development Cards from the Development Cards Deck.
     * Takes all the cards on top of the development deck and puts them in a list of cards that can be bought.
     * It then checks which ones can be bought by the player (it does so by taking all the resources of the player and comparing them with the ones required for each card, considering also the discount ability of the enabled leaderCards).
     * The ones that cannot be bought are removed from the list.
     * After this, it checks for each of the remaining cards if they can be placed in one of the player's slots.
     * If so, it adds the cards in a list of possible cards with the slots they can be put in.
     * The final list is the one with just the cards that can be bought and placed in one of the player's slots.
     * Finally, it notifies all the players, puts the card in the developmentCardVendor and enables it.
     * <p>
     * ERRORS:
     * <ul>
     *     <li> the player has already done a main action. </li>
     *     <li> the player cannot buy any card. </li>
     *     <li> the card cannot be placed in any slot. </li>
     * </ul>
     *
     * @param player The reference of the Current Player.
     * @return True if and only if the player can buy at least one card.
     */
    public boolean buyDevelopmentCard(Player player) {
        DevelopmentCard[][] possibleCards = game.getVisibleCards();
        List<VendorCard> finalCards = new ArrayList<>();

        if (player.getAction()) {
            gameManager.setErrorObject("Error! You already performed a main action!");
            return false;
        }

        List<DevelopmentCard> cards = new ArrayList<>();

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                if (possibleCards[r][c] != null)
                    cards.add(possibleCards[r][c]);
            }
        }

        List<LeaderCard> specialAb = player.getCardsWithDiscountResourceAbility();
        List<Resource> discountedResources = new ArrayList<>();

        if (!specialAb.isEmpty()) {
            for (LeaderCard leaderCard : specialAb) {
                discountedResources.add(((DiscountResource) leaderCard.getSpecialAbility()).getDiscountedResource());
            }
        }

        Map<Resource, Integer> playerResources = player.getResources();
        List<DevelopmentCard> tmp = new ArrayList<>(cards);

        //first check: all the cards that the player can't buy are removed from the ones that are on top.
        for (int i = 0; i < tmp.size(); i++) {
            Map<Resource, Integer> cost = tmp.get(i).getCost();
            for (Resource rs : discountedResources) {
                if (cost.get(rs) != null) {
                    cost.replace(rs, cost.get(rs) - 1);
                }
            }
            for (Resource resource : cost.keySet()) {
                if (playerResources.get(resource) < cost.get(resource)) {
                    cards.remove(tmp.get(i));
                    break;
                }
            }
        }

        if (cards.isEmpty()) {
            gameManager.setErrorObject("Error! You cannot buy any card at all!");
            return false;
        }

        boolean slot1;
        boolean slot2;
        boolean slot3;

        //second check: all the cards that the player can't put in his slot are removed from the ones selected before.
        for (int i = 0; i < cards.size(); i++) {
            slot1 = false;
            slot2 = false;
            slot3 = false;
            if (player.getDevelopmentSlot().validateNewCard(cards.get(i), 0)) {
                slot1 = true;
            }
            if (player.getDevelopmentSlot().validateNewCard(cards.get(i), 1)) {
                slot2 = true;
            }
            if (player.getDevelopmentSlot().validateNewCard(cards.get(i), 2)) {
                slot3 = true;
            }
            if (slot1 || slot2 || slot3) {
                finalCards.add(new VendorCard(cards.get(i), slot1, slot2, slot3));
            }
        }

        if (finalCards.isEmpty()) {
            gameManager.setErrorObject("Error! You cannot place the cards in any slot!");
            return false;
        }

        messageHelper.setNotificationMessage(player.getNickname(), new MSG_ACTION_BUY_DEVELOPMENT_CARD());
        DevelopmentCardsVendor developmentCardsVendor = game.getDevelopmentCardsVendor();
        developmentCardsVendor.setCards(finalCards);
        developmentCardsVendor.setEnabled(true);
        return true;
    }

    /**
     * Acquires the chosen Development Card for the Current Player.
     * For the given player, it adds the chosen card in the chosen slot.
     * It then removes the card from the development card deck and the resources used to buy that card, considering also the Discount Abilities of that player.
     * Then, it notifies all the players that a card has been bought and sets the developmentCardVendor to disabled.
     * Finally, it saves that a main action has been done.
     * <p>
     * ERRORS:
     * <ul>
     *     <li> if the cardNumber and the slot number are -1 the user did not buy any card. </li>
     *     <li> the cardNumber is not valid. </li>
     *     <li> the slotNumber is not valid. </li>
     *     <li> the number of the card selected is too high. </li>
     *     <li> the player tried to put a card in a not-valid slot. </li>
     *     <li> the card was not present. </li>
     * </ul>
     * NOTE: if the card bought is the 7th for that player, it changes the game status accordingly to the game mode.
     *
     * @param player  The reference of the Current Player.
     * @param message The message that the player has sent.
     * @return True if and only if the card has been bought and placed and all the resources have been removed.
     */
    public boolean chooseDevelopmentCard(Player player, MSG_ACTION_CHOOSE_DEVELOPMENT_CARD message) {
        int cardNumber = message.getCardNumber();
        int slotNumber = message.getSlotNumber();

        DevelopmentCardsVendor developmentCardsVendor = game.getDevelopmentCardsVendor();

//MESSAGE VALIDATION    //impossible to test
        if (cardNumber == -1 && slotNumber == -1) {
            game.setDevelopmentCardsVendorEnabled(false);
            game.broadcastMessage(player.getNickname() + " did not buy any card");
            return false;
        }
        if (cardNumber < 0) {
            gameManager.setErrorObject("Error! Message not well formatted: cardNumber < 1!");
            return false;
        }
        if (slotNumber != 0 && slotNumber != 1 && slotNumber != 2) {
            gameManager.setErrorObject("Error! Message not well formatted: slotNumber != 1 2 3!");
            return false;
        }

//VALIDATION
        if (!developmentCardsVendor.isEnabled()) //how the hell did he get in here?
        {
            gameManager.setErrorObject("Error! The method chooseDevelopmentCard was somehow invoked without developmentCardsVendor middle-object enabled!");
            return false;
        }

        if (cardNumber >= developmentCardsVendor.getCards().size()) {
            gameManager.setErrorObject("Error! The cardNumber cannot be that high!");
            return false;
        }

        VendorCard vendorCard = developmentCardsVendor.getCards().get(cardNumber);

        if (!vendorCard.isSlot(slotNumber)) {
            gameManager.setErrorObject("Error! You can not put that card in the slot!");
            return false;
        }

        player.getDevelopmentSlot().addCard(vendorCard.getCard(), slotNumber);
        DevelopmentCard[][] visibleCards = game.getVisibleCards();
        int r;
        int c = 0;
        loop:
        for (r = 0; r < 3; r++) {
            for (c = 0; c < 4; c++) {
                if (vendorCard.getCard().equals(visibleCards[r][c])) {
                    break loop;
                }
            }
        }
        if (r == 3) {
            gameManager.setErrorObject("Error! Card was not present!");
            return false;
        }

        game.removeCardOnDevelopmentCardsDeck(r, c);


        List<LeaderCard> specialAb = player.getCardsWithDiscountResourceAbility();
        Map<Resource, Integer> cost = vendorCard.getCard().getCost();

        for (LeaderCard l : specialAb) {
            DiscountResource d = (DiscountResource) l.getSpecialAbility();
            if (cost.get(d.getDiscountedResource()) != null) {
                cost.replace(d.getDiscountedResource(), cost.get(d.getDiscountedResource()) - 1);
            }
        }

        messageHelper.setNotificationMessage(player.getNickname(), message);
        consumeResources(player, cost);

        developmentCardsVendor.setEnabled(false);
        if (player.getDevelopmentSlot().getNumOfCards() == 7) {
            if (gameManager.getSolo()) {
                messageHelper.setNewMessage("I mean, it's game over for Lorenzo. Go on, end the turn.");
                gameManager.setStatus(Status.GAME_OVER);
                gameManager.setSoloWinner(true);
            } else
                gameManager.setStatus(Status.LAST_TURN);
        }
        player.setAction();
        return true;
    }

    /**
     * Obtains a set of Resources from a chosen row or column from the Market.
     * For the given player, it checks if the player has selected a row or a column of the market to push and then pushes it.
     * It then notifies all the players.
     * After pushing a row or a column, it adds all the resources in a list of MarketMarble (and makes the player advance if the marble is red).
     * Finally, it saves that a main action has been done, then checks if the list of resources is empty, if so, it just returns true.
     * If not, it adds the resources in the marketHelper and calls the SetNextResourceOption method.
     * <p>
     * ERRORS:
     * <ul>
     *     <li> the player has chosen a column but the number is not correct. </li>
     *     <li> the player has chosen a row but the number is not correct. </li>
     *     <li> the player has already done a main action. </li>
     *     <li> this method was invoked with the marketHelper enabled. </li>
     * </ul>
     * NOTE: if the player has one MarketResource Ability, it converts every EXTRA Market Marble in the specified resource, if the player has no MarketResource Abilities it removes the Marble.
     * NOTE: the case where the player has two MarketResource Abilities is dealt in the SetNextResourceOption method.
     *          @see #setNextResourceOptions(Player)
     *
     * @param player  The reference to the Current Player.
     * @param message The message that the player sent.
     * @return True if and only if no error occurred.
     */
    public boolean getMarketResources(Player player, MSG_ACTION_GET_MARKET_RESOURCES message) {
        MarketHelper marketHelper = game.getMarketHelper();
        boolean column = message.getColumn();
        int number = message.getNumber();

//MESSAGE VALIDATION    //impossible to test
        if (column && (number < 0 || number > 3)) // 0 1 2 3
        {
            gameManager.setErrorObject("Error! Message not well formatted: column but not columns value!");
            return false;
        }
        if (!column && (number < 0 || number > 2)) // 0 1 2
        {
            gameManager.setErrorObject("Error! Message not well formatted: row but not rows value!");
            return false;
        }
//VALIDATION
        if (player.getAction()) {
            gameManager.setErrorObject("Error! You already performed a main action");
            return false;
        }

        if (marketHelper.isEnabled()) {
            gameManager.setErrorObject("Error! method getMarketResource was somehow invoked WITH marketHelper enabled!");
            return false;
        }

        messageHelper.setNotificationMessage(player.getNickname(), message);
        Market market = game.getMarket();
        List<MarketMarble> selectedMarbles;
        if (column) selectedMarbles = market.pushColumn(number);
        else selectedMarbles = market.pushRow(number);

        //Requires NO VALIDATION (except for input)
        ArrayList<Resource> resources = new ArrayList<>();
        for (MarketMarble m : selectedMarbles) {
            try {
                m.addResource(resources);
            } catch (RedMarbleException e) {
                faithTrackManager.advance(player);
            }
        }


        //check on the leaderCards with Market Ability
        List<LeaderCard> marblesCards = player.getCardsWithMarketResourceAbility();
        for (int i = 0; i < resources.size(); i++) {
            if (resources.get(i) == Resource.EXTRA) {
                if (marblesCards.isEmpty()) {
                    resources.remove(i);
                    i--;
                } else if (marblesCards.size() == 1) {
                    resources.set(i, ((MarketResources) marblesCards.get(0).getSpecialAbility()).getConvertedResource());
                }
            }
        }

        player.setAction();
        if (resources.isEmpty())
            return true;
        marketHelper.setResources(resources);
        setNextResourceOptions(player);
        return true;
    }

    /**
     * Performs a specific action the Current Player chose in the MarketHelper.
     * If an error occurs, the action is cancelled and the error is notified. Nothing else changes in the model.
     * ERRORS:
     * <ul>
     *     <li>the integer contained in the message (the decision of the player) is not between (included) 1 and 8</li>
     *     <li>the method was invoked when the MarketHelper was not enabled</li>
     *     <li>the choice of the player is not permitted by the server</li>
     * </ul>
     * If the resource is normal (not Resource.EXTRA), the action is based on the decision of the player (an integer in the message):
     * <ul>
     *     <li>0: put in depot </li>
     *     <li>1: put in extra depot</li>
     *     <li>2: discard the resource</li>
     *     <li>3: swap rows 1 and 2 of the WarehouseDepot</li>
     *     <li>4: swap rows 1 and 3 of the WarehouseDepot</li>
     *     <li>5: swap rows 2 and 3 of the WarehouseDepot</li>
     *     <li>6: skip the resource forward</li>
     *     <li>7: skip the resource backward</li>
     * </ul>
     * If the resource is not normal (Resource.EXTRA, the player has 2 Leader Cards with MarketResources ability):
     * <ul>
     *     <li>0: convert the resource in the one given by the Leader Card 1 of the player</li>
     *     <li>1: convert the resource in the one given by the Leader Card 2 of the player</li>
     * </ul>
     * If no error occurs, notifies the players
     *
     * @param player  The reference to the Current Player.
     * @param message The Message coming from the Player.
     * @return True if the action terminated successful, False if there was any kind of Error.
     */
    public boolean newChoiceMarket(Player player, MSG_ACTION_MARKET_CHOICE message) {
        MarketHelper marketHelper = game.getMarketHelper();
        int choice = message.getChoice();

//MESSAGE VALIDATION    //impossible to test
        if (choice < 0 || choice > 8) {
            gameManager.setErrorObject("Error! Message not well formatted: choice not between 1 and 8!");
            return false;
        }
//VALIDATION
        if (!marketHelper.isEnabled()) {
            gameManager.setErrorObject("Error! method newChoiceMarket was somehow invoked WITHOUT marketHelper enabled!");
            return false;
        }

        Resource currentResource = marketHelper.getCurrentResource();
        boolean isNormalChoice;
        boolean[] choices = marketHelper.getChoices();

        boolean error = false;

        isNormalChoice = currentResource != Resource.EXTRA;

        WarehouseDepot depot = player.getWarehouseDepot();
        List<LeaderCard> extraDepotCards = player.getCardsWithExtraDepotAbility();

        if (!isNormalChoice) {
            if (choice == 0) {
                if (choices[0]) {
                    marketHelper.setResource(marketHelper.getExtraResources()[0]);
                    game.getMessageHelper().setNewMessage(player.getNickname() + " changed his extra resource into " + marketHelper.getExtraResources()[0]);
                } else
                    error = true; //impossible to test
            } else if (choice == 1) {
                if (choices[1]) {
                    marketHelper.setResource(marketHelper.getExtraResources()[1]);
                    game.getMessageHelper().setNewMessage(player.getNickname() + " changed his extra resource into " + marketHelper.getExtraResources()[1]);
                } else
                    error = true; //impossible to test
            }

        } else {
            if (choice == 0) {
                if (choices[0]) {
                    depot.add(currentResource);
                    marketHelper.removeResource();
                    game.getMessageHelper().setNewMessage(player.getNickname() + " added a resource in his depot: " + currentResource);
                } else error = true;
            } else if (choice == 1) {
                if (choices[1]) {
                    for (LeaderCard l : extraDepotCards) {
                        ExtraDepot extraDepot = (ExtraDepot) l.getSpecialAbility();
                        if (extraDepot.getResourceType() == currentResource) {
                            extraDepot.addResource(1);
                            marketHelper.removeResource();
                            break;
                        }
                    }
                    game.getMessageHelper().setNewMessage(player.getNickname() + " added a resource in his extra depot: " + currentResource);
                } else error = true;
            }
        }

        if (choice == 2) {
            if (choices[2]) {
                marketHelper.removeResource();
                faithTrackManager.advanceAllExcept(player);
                game.broadcastMessage(player.getNickname() + " discarded a " + currentResource);
            } else error = true; //impossible to test
        } else if (choice == 3) {
            if (choices[3]) {
                depot.swapRow(1, 2);
                game.broadcastMessage(player.getNickname() + " swapped the rows 1 and 2 of his depot");
            } else error = true;
        } else if (choice == 4) {
            if (choices[4]) {
                depot.swapRow(1, 3);
                game.broadcastMessage(player.getNickname() + " swapped the rows 1 and 3 of his depot");
            } else error = true;
        } else if (choice == 5) {
            if (choices[5]) {
                depot.swapRow(2, 3);
                game.broadcastMessage(player.getNickname() + " swapped the rows 2 and 3 of his depot");
            } else error = true;
        } else if (choice == 6) {
            if (choices[6]) {
                marketHelper.skipForward();
                game.broadcastMessage(player.getNickname() + " is pondering about his market resources");
            } else error = true;
        } else if (choice == 7) {
            if (choices[7]) {
                marketHelper.skipBackward();
                game.broadcastMessage(player.getNickname() + " is pondering about his market resources");
            } else error = true;
        }


        if (error) {
            gameManager.setErrorObject("Error! You chose an invalid action!");
            game.broadcastMessage(player.getNickname() + " did not behave well");
            return false;
        } else {
            if (!marketHelper.getResources().isEmpty()) {
                setNextResourceOptions(player);
            } else {
                marketHelper.setEnabled(false);
            }
            return true;
        }
    }


    /**
     * Ends the turn for the Current Player.
     * As default this method notifies the other Players, but can be deactivated by setting the boolean parameter to False.
     * Invoking this method could result in a GameOver condition for the Game.
     * In that case, a MSG_UPD_Leaderboard will be sent followed by a MSG_Stop.
     * Otherwise, a new Player (not Idle) is selected by calling the GameManager endTurn() and the Game advances normally.
     * If the new Player was disconnected in the INIT Status, the correct Middle Object will be re-enabled for him.
     *
     * @param player The current Player reference.
     * @param notify Set to True if you want to notify other Player that 'player' has ended its turn. Set False to not.
     * @return True if the Game is continuing, False if the Game has ended by calling this method.
     * @see it.polimi.ingsw.server.controller.GameManager .endTurn()
     */
    public boolean endTurn(Player player, boolean notify) {
        player.resetPermittedAction();
        if (notify)
            messageHelper.setNotificationMessage(player.getNickname(), new MSG_ACTION_ENDTURN());
        if (gameManager.getSolo()) {
            if (game.getStatus() == Status.GAME_OVER) {
                return gameManager.endgame();
            } else  //status NOT gameover, Lorenzo has to Play
            {
                lorenzoMove();
                //after his play, check if the game is over
                if (game.getStatus() == Status.GAME_OVER && !gameManager.getSoloWinner())
                    return gameManager.endgame();
            }
        }

        Status previousStatus = game.getStatus();
        boolean result = gameManager.endTurn(); //now the game is updated to the first available player
        Player newPlayer = game.getCurrentPlayer(); // this is the firstAvailablePlayer

        if (previousStatus == Status.INIT_1 && game.getStatus() == Status.INIT_1) //so we've been called by the chooseLeadercards, and we're STILL in the distribuiting phase
        {
            //assert( game.getLeaderCardsObject().isEnabled())
            game.setLeaderCardsPickerCards(newPlayer.getStartingCards());
        } else if (previousStatus == Status.INIT_1 && game.getStatus() == Status.INIT_2) //so we've made a circle, we are now distributing resources
        {
            game.setLeaderCardsPickerEnabled(false);
            if (newPlayer.getPlayerNumber() == 1) //we have to skip him
            {
                gameManager.endTurn();
                newPlayer = game.getCurrentPlayer();
                if (game.getStatus() == Status.STANDARD_TURN) //special case: he was the only one connected and all the others were disconnected
                {
                } else //we're still in INIT_2, and the player is his successor (2, 3, 4)
                {
                    game.setResourcePickerNumOfResources(newPlayer.getStartingResources());
                    game.setResourcePickerEnabled(true);
                }
            } else //we have skipped to a newPlayer which is not the #1, so he has to choose resources
            {
                game.setResourcePickerNumOfResources(newPlayer.getStartingResources());
                game.setResourcePickerEnabled(true);
            }
        } else if (previousStatus == Status.INIT_2 && game.getStatus() == Status.INIT_2) //so we've just advanced in the distribution of resources
        {
            game.setResourcePickerNumOfResources(newPlayer.getStartingResources());
        } else if (previousStatus == Status.INIT_2 && game.getStatus() == Status.STANDARD_TURN) //so we've entered in the game, we must deactivate
        {
            game.setResourcePickerEnabled(false);
        } else if (previousStatus == Status.STANDARD_TURN && game.getStatus() == Status.STANDARD_TURN) //we've advanced in a normal situation
        {
//reconnection part
            if (newPlayer.isDisconnectedBeforeLeaderCard()) { //in that case he has to choose his leadercards
                game.setLeaderCardsPickerCards(game.getCurrentPlayerStartingCards()); //setting up the object
                game.setLeaderCardsPickerEnabled(true); //enabling the object
            } else if (newPlayer.isDisconnectedBeforeResource()) {
                game.setResourcePickerNumOfResources(newPlayer.getStartingResources());
                game.setResourcePickerEnabled(true);
            }
//end of reconnection
        }
        return result;
    }

    /**
     * Perform one of the actions that Lorenzo could do in Solo mode.
     * This method is called only after the Player has ended its turn in Solo mode and in STANDARD_TURN.
     * Works by extracting one of the Action Token, and consequentially modifying the model accordingly to the Game Rules.
     * The behavior of the Token is defined in their interface.
     *
     * @see it.polimi.ingsw.server.model.actionTokens.ActionToken
     */
    private void lorenzoMove() {
        ActionToken token = game.pickFirstActionToken();
        if (token.isRemover()) {
            messageHelper.setLorenzoNotificationMessage(0);
            game.removeColumnOnDevelopmentCardsDeck(((RemoverToken) token).getColumn());
            if (game.isOneColumnDestroyedOnTheDevelopmentCardsDeck()) {
                gameManager.setStatus(Status.GAME_OVER);
                gameManager.setSoloWinner(false);
            }
        } else if (token.isForwardAndShuffle()) {
            messageHelper.setLorenzoNotificationMessage(1);
            game.shuffleActionStack();
            faithTrackManager.advanceLorenzo();
        } else if (token.isForward2()) {
            messageHelper.setLorenzoNotificationMessage(2);
            faithTrackManager.advanceLorenzo();
            faithTrackManager.advanceLorenzo();
        }
    }

    //--------------------   Helper methods   --------------------//

    /**
     * Computates the next MarketHelper choices for the current Player.
     * The new choices are directly inserted in the MarketHelper and notified to the Players.
     * This Method should be called lastly when setting up the MarketHelper, as it enables it automatically.
     *
     * @param player The current Player reference.
     */
    public void setNextResourceOptions(Player player) {
        MarketHelper marketHelper = game.getMarketHelper();
        Resource resource = marketHelper.getCurrentResource();
        boolean[] choices = new boolean[8];
        WarehouseDepot warehouseDepot = player.getWarehouseDepot();
        List<LeaderCard> extraDepotCards = player.getCardsWithExtraDepotAbility();

        if (resource != Resource.EXTRA) {
            marketHelper.setNormalChoice(true);
            //choice 0: put in depot
            if (warehouseDepot.isAddable(resource)) choices[0] = true;

            //choice 1: put in extra depot
            ExtraDepot e;
            if (extraDepotCards.isEmpty()) {
                choices[1] = false;
            } else if (extraDepotCards.size() == 1) {
                e = ((ExtraDepot) extraDepotCards.get(0).getSpecialAbility());
                if (e.getResourceType() == resource && e.getNumber() < 2)
                    choices[1] = true;
            } else if (extraDepotCards.size() == 2) {
                e = ((ExtraDepot) extraDepotCards.get(0).getSpecialAbility());
                if (e.getResourceType() != resource) {
                    e = ((ExtraDepot) extraDepotCards.get(1).getSpecialAbility());
                    if (e.getResourceType() != resource) {
                        e = null;
                    }
                }
                if (e != null) {
                    if (e.getNumber() < 2)
                        choices[1] = true;
                }
            }
        } else {
            choices[0] = choices[1] = true;
            List<LeaderCard> marblesCards = player.getCardsWithMarketResourceAbility();
            Resource[] extraResources = new Resource[2];
            extraResources[0] = ((MarketResources) marblesCards.get(0).getSpecialAbility()).getConvertedResource();
            extraResources[1] = ((MarketResources) marblesCards.get(1).getSpecialAbility()).getConvertedResource();
            marketHelper.setExtraResourceChoices(extraResources);
            marketHelper.setNormalChoice(false);
        }
        //choice 2: discard resource
        choices[2] = true;

        //choice 3-4-5: swap rows
        if (warehouseDepot.getShelf2ResourceNumber() < 2) choices[3] = true; //swap 1-2
        if (warehouseDepot.getShelf3ResourceNumber() < 2) choices[4] = true; //swap 1-3
        if (warehouseDepot.getShelf3ResourceNumber() < 3) choices[5] = true; //swap 2-3

        //choice 6: skip this resource, 7 backwards
        if (marketHelper.getResources().size() > 1) {
            choices[6] = true;
            choices[7] = true;
        }
        marketHelper.setChoices(choices);
        if (!marketHelper.isEnabled())
            marketHelper.setEnabled(true);
    }

    /**
     * Consumes a map of resources from the WarehouseDepot, ExtraDepots and strongbox of the given player.
     * It assumes that is possible to consume all the resources in the given map.
     * For each resource to consume:
     * <ul>
     *      <li> tries to consume it from the depot. If it is not found there,</li>
     *      <li> tries to consume it from the extraDepots. If it is not found there,</li>
     *      <li> consumes the resource (and all the remaining resources of the same type in the map) from the strongbox.</li>
     * </ul>
     *
     * @param player The player who pays the cost.
     * @param cost   The map containing the resources and the relative quantity to consume.
     */
    public void consumeResources(Player player, Map<Resource, Integer> cost) {
        WarehouseDepot warehouseDepot = player.getWarehouseDepot();
        List<LeaderCard> extraDepotLeaderCards = player.getCardsWithExtraDepotAbility();
        int numLeaderCard = extraDepotLeaderCards.size();
        ExtraDepot ability;
        Strongbox strongbox = player.getStrongbox();
        int remainingResources;
        boolean found;

        for (Resource r : cost.keySet()) {
            remainingResources = cost.get(r);
            while (remainingResources > 0) {
                found = warehouseDepot.consume(r);
                if (!found) {
                    for (int i = 0; i < numLeaderCard && !found; i++) {
                        ability = (ExtraDepot) extraDepotLeaderCards.get(i).getSpecialAbility();
                        if (ability.getResourceType() == r) {
                            if (ability.removeResource(1)) found = true;
                        }
                    }
                }
                if (!found) {
                    strongbox.remove(r, remainingResources);
                    remainingResources = 0;
                } else remainingResources--;
            }
        }
    }

    /**
     * Disconnects the specified Player.
     * Notifies the observers that such Player has crashed.
     * Eventually saves his status, if he was still choosing LeaderCards or Initial Resources.
     * If the Player was the currentPlayer, advances the Game normally.
     *
     * @param player              The reference of the Player who just crashed.
     * @param currentPlayerNumber The number of the current Player.
     */
    public void disconnectPlayer(Player player, int currentPlayerNumber) {
        messageHelper.setNewMessage(" " + player.getNickname() + " has crashed! ");
        if (game.isLeaderCardsPickerEnabled()) {
            if (player.getPlayerNumber() >= currentPlayerNumber) {
                player.setDisconnectedBeforeLeaderCard(true);
                player.setDisconnectedBeforeResource(player.getPlayerNumber() != 1);
            } else {
                player.setDisconnectedBeforeLeaderCard(false);
                player.setDisconnectedBeforeResource(player.getPlayerNumber() != 1);
            }
        } else if (game.isResourcePickerEnabled()) {
            if (player.getPlayerNumber() >= currentPlayerNumber) {
                player.setDisconnectedBeforeLeaderCard(false);
                player.setDisconnectedBeforeResource(true);
            } else {
                player.setDisconnectedBeforeLeaderCard(false);
                player.setDisconnectedBeforeResource(false);
            }
        }

        if (currentPlayerNumber == player.getPlayerNumber()) {
            if (game.getStatus() == Status.STANDARD_TURN) {
                if (game.isDevelopmentCardsVendorEnabled())
                    game.setDevelopmentCardsVendorEnabled(false);
                if (game.isMarketHelperEnabled())
                    game.setMarketHelperEnabled(false);
            }
            gameManager.resetErrorObject();

            endTurn(player, false);
            messageHelper.setUpdateEnd();
        }
    }

    /**
     * Notifies other Players that someone's reconnected.
     *
     * @param nickname The name of the Player who's being reconnected.
     */
    public void notifyReconnection(String nickname) {
        messageHelper.setNewMessage(nickname + " has reconnected!");
    }

    public Game getGame() {
        return game;
    }
}