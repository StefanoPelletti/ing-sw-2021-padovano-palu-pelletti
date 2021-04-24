package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Networking.Message.*;
import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.Model.ActionTokens.*;
import it.polimi.ingsw.Server.Model.Enumerators.*;
import it.polimi.ingsw.Server.Model.Marbles.MarketMarble;
import it.polimi.ingsw.Server.Model.Marbles.RedMarbleException;
import it.polimi.ingsw.Server.Model.Middles.DevelopmentCardsVendor;
import it.polimi.ingsw.Server.Model.Middles.LeaderCardsObject;
import it.polimi.ingsw.Server.Model.Middles.MarketHelper;
import it.polimi.ingsw.Server.Model.Middles.ResourceObject;
import it.polimi.ingsw.Server.Model.Requirements.*;
import it.polimi.ingsw.Server.Model.SpecialAbilities.*;


import java.util.*;


public class ActionManager {
    private final GameManager gameManager;
    private final FaithTrackManager faithTrackManager;
    private final Game game;

    public ActionManager(GameManager gameManager, FaithTrackManager faithTrackManager, Game game)
    {
        this.gameManager = gameManager;
        this.faithTrackManager = faithTrackManager;
        this.game = game;
    }

    public boolean chooseLeaderCard(Player player, MSG_INIT_CHOOSE_LEADERCARDS message) {
        ArrayList<LeaderCard> cards = message.getCards();
        LeaderCardsObject leaderCardsObject = game.getLeaderCardsObject();
        ResourceObject resourceObject = game.getResourceObject();

        if(!leaderCardsObject.isEnabled()) //how the hell did he get in here?
        {
            gameManager.setErrorObject("Errore! è stato chiamato il metodo chooseLeaderCards senza oggetto LeaderCardsObject attivo nel model!");
            return false;
        }

        player.associateLeaderCards(cards);
        if(game.getStatus()==Status.SOLO)
        {
            leaderCardsObject.setEnabled(false);
        }
        else {
            if(game.getCurrentPlayerInt() == gameManager.getLobbyMaxPlayers()) // then all the players have already chosen their cards
            {
                leaderCardsObject.setEnabled(false);
                resourceObject.setNumOfResources(1);
                resourceObject.setEnabled(true);
                game.setCurrentPlayer(2);
            }
            else
            {
                leaderCardsObject.setCards(game.getLeaderCardsDeck().pickFourCards());
                endTurn(player);
            }
        }
        return true;
    }
    public boolean chooseResource(Player player, MSG_INIT_CHOOSE_RESOURCE message) {
        ResourceObject resourceObject = game.getResourceObject();
        Resource resource = message.getResource();

        if(!resourceObject.isEnabled()) //how the hell did he get in here?
        {
            gameManager.setErrorObject("Errore! è stato chiamato il metodo chooseResources senza oggetto ResourceObject attivo nel model!");
            return false;
        }
        player.getWarehouseDepot().add(resource);

        if(resourceObject.getNumOfResources() == 2)
            player.getWarehouseDepot().swapRow(1,2);
        resourceObject.decNumOfResources();
        if(resourceObject.getNumOfResources() == 0)
        {
            if(game.getCurrentPlayerInt()==gameManager.getLobbyMaxPlayers())
            {
                resourceObject.setEnabled(false);
            }
            else
            {
                if(game.getCurrentPlayerInt()==3)
                    resourceObject.setNumOfResources(2);
                else
                    resourceObject.setNumOfResources(1);
            }
            endTurn(player);
            game.setTurn(1);
        }
        return true;
    }

    public boolean activateLeaderCard(Player player, MSG_ACTION_ACTIVATE_LEADERCARD message){
        int cardNumber = message.getCardNumber();

        if(player.getLeaderCards()[cardNumber].getEnable())
        {
            gameManager.setErrorObject("Errore! Questa carta è già attivata!");
            return false;
        }

        LeaderCard l = player.getLeaderCards()[cardNumber];
        Requirement requirement = l.getRequirement();
        //player.addVP(l.getVP());

        if(requirement.isResourceRequirement()){
            ResourceRequirements resourceRequirements= (ResourceRequirements) requirement;
            Map<Resource, Integer> requiredResources = resourceRequirements.getRequirements();

            Map<Resource, Integer> resources = player.getResources();

            for(Resource resource: requiredResources.keySet()){
                resources.put(resource, resources.get(resource)-requiredResources.get(resource));
                if(resources.get(resource)<0)
                {
                    gameManager.setErrorObject("Errore! Non hai abbastanza risorse!");
                    return false;
                }
            }
            player.getLeaderCards()[cardNumber].setEnable(true);
            return true;
        }

        if(requirement.isCardRequirement()){
            CardRequirements cardRequirements = (CardRequirements) requirement;
            Map<Color, Integer[]> requiredCards = cardRequirements.getRequirements();
            ArrayList<DevelopmentCard> playerCards = player.getDevelopmentSlot().getCards();
            int requiredNumCard, requiredLevelCard;
            for(Color color: requiredCards.keySet()){
                requiredNumCard = requiredCards.get(color)[0];
                requiredLevelCard = requiredCards.get(color)[1];
                for(DevelopmentCard card : playerCards ){
                    if(card.getColor()==color){
                        if(requiredLevelCard==-1 || requiredLevelCard==card.getLevel()){
                            requiredNumCard--;
                        }
                    }
                }
                if(requiredNumCard>0)
                {
                    gameManager.setErrorObject("Errore! Non disponi delle carte corrette!");
                    return false;
                }
            }
            player.setLeaderCards(cardNumber, true);
            return true;
        }

        //should never reach this position
        return false;
    }
    public boolean discardLeaderCard(Player player, MSG_ACTION_DISCARD_LEADERCARD message){
        int cardNumber = message.getCardNumber();
//VALIDATION
        if(player.getLeaderCards()[cardNumber].getEnable())
        {
            gameManager.setErrorObject("Errore! Non puoi disattivare una carta già attivata!");
            return false;
        }
//MODEL UPDATE
        player.setLeaderCards(cardNumber, false);
        //notify???
        faithTrackManager.advance(player);
        return true;
    }

    public boolean activateProduction(Player player, MSG_ACTION_ACTIVATE_PRODUCTION message) {
        Map<Resource, Integer> initialResources = player.getResources();
        boolean[] standardProduction = message.getStandardProduction();
        boolean baseProduction = message.isBasicProduction();
        boolean[] leaderProduction = message.getLeaderProduction();
        Resource[] possibleResources = new Resource[]{Resource.COIN, Resource.STONE, Resource.SHIELD, Resource.SERVANT};

        Map<Resource, Integer> requiredResources = new HashMap<>();
        Map<Resource, Integer> newResources = new HashMap<>();

//VALIDATION
        //initializing requiredResources
        for(Resource r : possibleResources){
            requiredResources.put(r, 0);
        }

        //getting costs for standard Productions
        ArrayList<DevelopmentCard> topCards = player.getDevelopmentSlot().getTopCards();
        for(int i=0; i<3; i++){
            if(standardProduction[i]){
                Map<Resource, Integer> input = topCards.get(i).getPowerInput();
                for(Resource r : input.keySet()){
                    requiredResources.put(r, requiredResources.get(r)+input.get(r));
                }
                Map<Resource, Integer> output = topCards.get(i).getPowerOutput();
                for(Resource r : output.keySet()){
                    newResources.put(r, requiredResources.get(r)+output.get(r));
                }
            }
        }

        //getting cost for base Production
        if(baseProduction) {
            for (Resource r : message.getBasicInput()) {
                requiredResources.put(r, requiredResources.get(r) + 1);
            }
            newResources.put(message.getBasicOutput(), newResources.get(message.getBasicOutput())+1);
        }

        //getting cost for leader Production
        //first LeaderCard
        if(leaderProduction[0]){
            Production ability = (Production) player.getLeaderCards()[0].getSpecialAbility();
            Resource input = ability.getInput();
            requiredResources.put(input, requiredResources.get(input) + 1);
            newResources.put(message.getLeaderOutput1(), newResources.get(message.getLeaderOutput1())+1);
        }

        //secondLeaderCard
        if(leaderProduction[1]){
            Production ability = (Production) player.getLeaderCards()[1].getSpecialAbility();
            Resource input = ability.getInput();
            requiredResources.put(input, requiredResources.get(input) + 1);
            newResources.put(message.getLeaderOutput2(), newResources.get(message.getLeaderOutput2())+1);
        }

        //check if the player has enough resources
        for(Resource r : requiredResources.keySet()){
            if(initialResources.get(r) < requiredResources.get(r)) {
                gameManager.setErrorObject("Errore! Ti mancano le risorse per attivare queste produzioni!");
                return false;
            }
        }
//MODEL UPDATE
        //now we must consume the required resources
        consumeResources(player, requiredResources);

        //now we add output resources to the player's strongbox
        Strongbox playerStrongbox = player.getStrongbox();
        for(Resource r: newResources.keySet()){
            playerStrongbox.addResource(r, newResources.get(r));
        }

        //for each leaderProduction enabled, player receives one Faith Point
        for(boolean enabled : leaderProduction){
            if(enabled) {
                faithTrackManager.advance(player);
            }
        }
        return true;
    }

    public boolean endTurn(Player player) {
        gameManager.setNextPlayer();
        return true;
    }

    // if player has two LeaderCards with ExtraDepot SpecialAbility and only one is activated, only firstExtraDepot is considered
    // if player has two LeaderCards with ExtraDepot SpecialAbility and both are activated, firstExtraDepot is referred to leaderCard[0] and secondExtraDepot is referred to leaderCard[1]
    public boolean changeDepotConfig(Player player, MSG_ACTION_CHANGE_DEPOT_CONFIG message) {
        Resource slot1 = message.getSlot1();
        Resource[] slot2 = message.getSlot2();
        Resource[] slot3 = message.getSlot3();
        int firstExtraDepot = message.getFirstExtraDepot();
        int secondExtraDepot = message.getSecondExtraDepot();
        Map<Resource, Integer> initialResources = player.getDepotAndExtraDepotResources();
        ArrayList<LeaderCard> playerLeaderCards = player.getCardsWithExtraDepotAbility();
//VALIDATION
        Resource[] possibleResources = new Resource[]{Resource.COIN, Resource.STONE, Resource.SHIELD, Resource.SERVANT};
        Map<Resource, Integer> newResources;
        WarehouseDepot demoDepot = new WarehouseDepot();
        ExtraDepot demoExtraDepot = new ExtraDepot(Resource.COIN);
        if(!demoDepot.setConfig(slot1, slot2, slot3)) {
            gameManager.setErrorObject("Errore! Non puoi mettere le cose così nel deposito!");
            return false;
        }
        if(!demoExtraDepot.setResource(firstExtraDepot)) {
            gameManager.setErrorObject("Errore! Non puoi mettere le cose così nel primo deposito extra!");
            return false;
        }
        if(!demoExtraDepot.setResource(secondExtraDepot)) {
            gameManager.setErrorObject("Errore! Non puoi mettere le cose così nel secondo deposito extra!");
            return false;
        }

        //let's check if the new configuration has the same resources as the one before!
        newResources = demoDepot.getResources();

        if(playerLeaderCards.size()>0 && firstExtraDepot>0){
            ExtraDepot ability = (ExtraDepot) playerLeaderCards.get(0).getSpecialAbility();
            Resource resource = ability.getResourceType();
            newResources.put(resource, newResources.get(resource) + firstExtraDepot);
        }

        if(playerLeaderCards.size()==2 && secondExtraDepot>0){
            ExtraDepot ability = (ExtraDepot) playerLeaderCards.get(1).getSpecialAbility();
            Resource resource = ability.getResourceType();
            newResources.put(resource, newResources.get(resource) + secondExtraDepot);
        }

        for(Resource resToControl : possibleResources){
            if(!initialResources.get(resToControl).equals(newResources.get(resToControl)))
            {
                gameManager.setErrorObject("Errore! Il numero di risorse non combacia!");
                return false;
            }

        }
//MODEL UPDATE
        //after all those controls, player really deserves a new depot!
        player.getWarehouseDepot().setConfig(slot1, slot2, slot3);
        if(playerLeaderCards.size()>0){
            ExtraDepot ability1 = (ExtraDepot) playerLeaderCards.get(0).getSpecialAbility();
            ability1.setResource(firstExtraDepot);
        }
        if(playerLeaderCards.size()==2){
            ExtraDepot ability2 = (ExtraDepot) playerLeaderCards.get(1).getSpecialAbility();
            ability2.setResource(secondExtraDepot);
        }
        return true;
    }

    public boolean buyDevelopmentCard(Player player) {
        DevelopmentCard[][] possibleCards = game.getDevelopmentCardsDeck().getVisible();
        Map<DevelopmentCard, boolean[]> finalCards = new HashMap<>();

        Map<Resource, Integer> playerResources = player.getResources();

        ArrayList<DevelopmentCard> cards = new ArrayList<>();
        for(int r = 0; r < 3; r++) {
            for( int c = 0 ; c < 4; c++)
            {
                if(possibleCards[r][c]!=null)
                   cards.add(possibleCards[r][c]);
            }
        }

        ArrayList<LeaderCard> specialAb = player.getCardsWithDiscountResourceAbility();
        ArrayList<Resource> discountedResources = new ArrayList<>();
        if(specialAb.size() != 0) {
            for(LeaderCard leaderCard : specialAb) {
                discountedResources.add(((DiscountResource) leaderCard.getSpecialAbility()).getDiscountedResource());
            }
        }

        ArrayList<DevelopmentCard> tmp = new ArrayList<>(cards);
        //first check: all the cards that the player can't buy are removed from the ones that are on top.
        for(int i = 0; i < tmp.size(); i++) {
            HashMap<Resource, Integer> cost = tmp.get(i).getCost();
            for(Resource rs : discountedResources) {
                if(cost.get(rs) != null) {
                    cost.replace(rs, cost.get(rs)-1);
                }
            }
            for (Resource resource : cost.keySet()) {
                if (playerResources.get(resource) < cost.get(resource)) {
                    cards.remove(tmp.get(i));
                    break;
                }
            }
        }

        if(cards.size() == 0) {
            gameManager.setErrorObject("Ao! Va che non puoi comprare nemmeno una carta!");
            return false;
        }

        //second check: all the cards that the player can't put in his slot are removed from the ones selected before.
        for(int i = 0; i < cards.size(); i++) {
            boolean[] pos = new boolean[3];
            for(int j = 0; j < 3; j++) {
                if(player.getDevelopmentSlot().validateNewCard(cards.get(i), j)) {
                    pos[j] = true;
                }
            }
            if(pos[0] || pos[1] || pos[2]){
                finalCards.put(cards.get(i), pos);
            }
        }

        if(finalCards.size()==0)
        {
            gameManager.setErrorObject("Ao! Va che puoi comprarle, ma non puoi metterle da nessuna parte!");
            return false;
        }

        DevelopmentCardsVendor developmentCardsVendor = game.getDevelopmentCardsVendor();
        developmentCardsVendor.setCards(finalCards);
        developmentCardsVendor.setEnabled(true);
        return true;
       /*
        Scanner myInput = new Scanner(System.in);
        for(DevelopmentCard dc : finalCards.keySet()) {
            System.out.println(dc);
            int k = 1;
            for(boolean b : finalCards.get(dc)) {
                if(b) {
                    System.out.println("Puo' essere inserito nello slot: "+k);
                }
                k++;
            }
        }

        return true;

        */
    }

    public boolean chooseDevelopmentCard(Player player, MSG_ACTION_CHOOSE_DEVELOPMENT_CARD message) {
        int cardNumber = message.getCardNumber();
        int slotNumber = message.getSlotNumber();

        DevelopmentCardsVendor developmentCardsVendor = game.getDevelopmentCardsVendor();
        if(!developmentCardsVendor.isEnabled()) //how the hell did he get in here?
        {
            gameManager.setErrorObject("Errore! è stato chiamato il metodo chooseDevelopmentCard (2/2) senza oggetto vendor attivo nel model!");
            return false;
        }

        DevelopmentCard dc = (new ArrayList<DevelopmentCard>(developmentCardsVendor.getCards().keySet())).get(cardNumber-1);

        player.getDevelopmentSlot().addCard(dc, slotNumber);
        DevelopmentCard[][] visibleCards = game.getDevelopmentCardsDeck().getVisible();
        boolean found = false;
        int r = 0;
        int c = 0;
        for(; r < 3; r++) {
            for(; c < 4; c++) {
                if(dc.equals(visibleCards[r][c])) {
                    found = true;
                    break;
                }
            }
            if(found) {
                break;
            }
        }

        game.getDevelopmentCardsDeck().removeCard(r,c);


        ArrayList<LeaderCard> specialAb = player.getCardsWithDiscountResourceAbility();
        HashMap<Resource, Integer> cost = dc.getCost();

        for(LeaderCard l: specialAb) {
            DiscountResource d = (DiscountResource) l.getSpecialAbility();
            if (cost.get(d.getDiscountedResource()) != null) {
                cost.replace(d.getDiscountedResource(), cost.get(d.getDiscountedResource()) - 1);
            }
        }

        consumeResources(player, cost);

        developmentCardsVendor.setEnabled(false);
        if(player.getDevelopmentSlot().getNumOfCards()==7) gameManager.setStatus(Status.LAST_TURN);
        return true;
    }

    public boolean getMarketResources(Player player, MSG_ACTION_GET_MARKET_RESOURCES message){
        MarketHelper marketHelper = game.getMarketHelper();
        boolean column = message.getColumn();
        int number = message.getNumber();

        if(column &&( number <0 || number >=4)) return false; //should be impossible, would throw an exception when building the message
        if(!column &&( number < 0 || number >= 3)) return false; // same as above

        if(marketHelper.isEnabled())
        {
            gameManager.setErrorObject("Errore in getMarketResource()! Oggetto marketHelper attivo nel model!");
            return false;
        }

        Market market = game.getMarket();
        ArrayList<MarketMarble> selectedMarbles;
        if(column) selectedMarbles = market.pushColumn(number);
        else selectedMarbles = market.pushRow(number);

        //System.out.println("You got these marbles: ");
        //selectedMarbles.stream().forEach(System.out::println);

        //Requires NO VALIDATION (except for input)
        ArrayList<Resource> resources = new ArrayList<>();
        for( MarketMarble m : selectedMarbles ) {
            try {
                m.addResource(resources);
            } catch (RedMarbleException e) {
                faithTrackManager.advance(player);
                //System.out.println("Risorsa fede! Corro ad aggiungerti un punto fede!");
            }
        }

        //check on the leaderCards with Market Ability
        ArrayList<LeaderCard> marblesCards = player.getCardsWithMarketResourceAbility();
        for(int i=0; i<resources.size(); i++){
            if(resources.get(i)==Resource.EXTRA){
                if(marblesCards.size()==0){
                    resources.remove(i);
                    i--;
                }
                else if(marblesCards.size()==1){
                    resources.set(i, ((MarketResources) marblesCards.get(0).getSpecialAbility()).getConvertedResource());
                }
            }
        }

        if(resources.size()==0)
            return true;
        marketHelper.setResources(resources);
        setNextResourceOptions(player);
        return true;
    }
    public boolean newChoiceMarket(Player player, MSG_ACTION_MARKET_CHOICE message){
        MarketHelper marketHelper = game.getMarketHelper();
        boolean[] choice = message.getChoice();

        if(!marketHelper.isEnabled())
        {
            gameManager.setErrorObject("Errore! Oggetto marketHelper NON attivo ed è stato chiamato il metodo newMarketChoice!");
            return false;
        }

        Resource currentResource = marketHelper.getCurrentResource();
        WarehouseDepot depot = player.getWarehouseDepot();
        ArrayList<LeaderCard> extraDepotCards = player.getCardsWithExtraDepotAbility();

        if(!message.isNormalChoice()){
            marketHelper.setResource(message.getResourceChoice());
            System.out.println("Risorsa cambiata!");
            setNextResourceOptions(player);
            return true;
        }
        if(choice[0]){
            depot.add(currentResource);
            marketHelper.removeResource();
            System.out.println("\nRisorsa aggiunta al deposito!");
        }

        else if(choice[1]){
            for(LeaderCard l : extraDepotCards) {
                ExtraDepot extraDepot = (ExtraDepot) l.getSpecialAbility();
                if (extraDepot.getResourceType() == currentResource) {
                    extraDepot.addResource(1);
                    marketHelper.removeResource();
                    break;
                }
            }
            System.out.println("\nRisorsa aggiunta nel deposito extra!");
        }

        else if(choice[2]){
            marketHelper.removeResource();
            faithTrackManager.advanceAllExcept(player);
            System.out.println("\nRisorsa scartata!");
        }

        else if(choice[3]){
            depot.swapRow(1,2);
            System.out.println("\nHo scambiato le righe 1 e 2!");
        }

        else if(choice[4]){
            depot.swapRow(1,3);
            System.out.println("\nHo scambiato le righe 1 e 3!");
        }

        else if(choice[5]){
            depot.swapRow(2,3);
            System.out.println("\nHo scambiato le righe 2 e 3!");
        }

        else if(choice[6]){
            marketHelper.skipForward();
            System.out.println("\nRisorsa skippata! Non le volevi bene?");
        }

        else if(choice[7]){
            marketHelper.skipBackward();
            System.out.println("\nTorniamo indietro!");
        }

        if(marketHelper.getResources().size()>0) {
            setNextResourceOptions(player);
        }
        else
        {
            marketHelper.setEnabled(false);
        }
        return true;
    }

    public boolean lorenzoMove()
    {
        ActionToken token = game.getActionTokenStack().pickFirst();
        if(token.isRemover())
        {
            game.getDevelopmentCardsDeck().removeCard(((RemoverToken) token).getColumn());
            if (game.getDevelopmentCardsDeck().isOneColumnDestroyed()) gameManager.setStatus(Status.GAME_OVER);
        }
        else if(token.isForwardAndShuffle()) {
            faithTrackManager.advanceLorenzo();
            game.getActionTokenStack().shuffle();
        }
        else if(token.isForward2())
        {
            faithTrackManager.advanceLorenzo();
            faithTrackManager.advanceLorenzo();
        }
        return true;
    }

//--------------------   Helper methods   --------------------//
    public void setNextResourceOptions(Player player){
        MarketHelper marketHelper = game.getMarketHelper();
        Resource resource = marketHelper.getCurrentResource();
        boolean[] choices = new boolean[8];
        WarehouseDepot warehouseDepot = player.getWarehouseDepot();
        ArrayList<LeaderCard> extraDepotCards = player.getCardsWithExtraDepotAbility();

        if(resource!=Resource.EXTRA){
            marketHelper.setNormalChoice(true);
            //choice 0: put in depot
            if (warehouseDepot.isAddable(resource)) choices[0] = true;

            //choice 1: put in extra depot
            ExtraDepot e = null;
            if (extraDepotCards.size() == 0) {
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
            //choice 2: discard resource (only available if the player cannot use the depot or the extra depot
            /*if (!choices[0] && !choices[1])*/ choices[2] = true;
        }
        else {
            choices[0]=choices[1]=choices[2]=choices[3]= false;
            ArrayList<LeaderCard> marblesCards = player.getCardsWithMarketResourceAbility();
            Resource[] extraResources = new Resource[2];
            extraResources[0] = ((MarketResources) marblesCards.get(0).getSpecialAbility()).getConvertedResource();
            extraResources[1] = ((MarketResources) marblesCards.get(1).getSpecialAbility()).getConvertedResource();
            marketHelper.setExtraResourceChoices(extraResources);
            marketHelper.setNormalChoice(false);
        }


        //choice 3-4-5: swap rows
        if (warehouseDepot.getShelf2ResourceNumber() < 2) choices[3] = true; //swap 1-2
        if (warehouseDepot.getShelf3ResourceNumber() < 2) choices[4] = true; //swap 1-3
        if (warehouseDepot.getShelf3ResourceNumber() < 3) choices[5] = true; //swap 2-3

        //choice 6: skip this resource
        if (marketHelper.getResources().size() > 1) {
            choices[6] = true;
            choices[7] = true;
        }
        marketHelper.setChoices(choices);
        if(!marketHelper.isEnabled())
            marketHelper.setEnabled(true);
    }


    public void consumeResources(Player player, Map<Resource, Integer> cost){
        WarehouseDepot warehouseDepot = player.getWarehouseDepot();
        ArrayList<LeaderCard> extraDepotLeaderCards = player.getCardsWithExtraDepotAbility();
        int numLeaderCard = extraDepotLeaderCards.size();
        ExtraDepot ability;
        Strongbox strongbox = player.getStrongbox();
        int remainingResources;
        boolean found;

        for(Resource r : cost.keySet()){
            remainingResources = cost.get(r);
            while(remainingResources>0){
                found= false;
                if(warehouseDepot.consume(r)) found =true;
                if (!found) {
                    for (int i = 0; i < numLeaderCard && !found; i++) {
                        ability = (ExtraDepot) extraDepotLeaderCards.get(i).getSpecialAbility();
                        if (ability.getResourceType() == r) {
                            if (ability.removeResource(1)) found = true;
                        }
                    }
                }
                if(!found){
                    strongbox.remove(r, remainingResources);
                    remainingResources=0;
                }
                else remainingResources--;
            }
        }
    }

}
