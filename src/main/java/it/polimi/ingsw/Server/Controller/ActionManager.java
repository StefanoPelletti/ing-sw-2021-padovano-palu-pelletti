package it.polimi.ingsw.Server.Controller;

public class ActionManager {
    private final GameManager gameManager;
    private final FaithTrackManager faithTrackManager;
    private final Game game;

    public ActionManager(GameManager gameManager, FaithTrackManager faithTrackManager, Game game)
    {
        this.gameManager=gameManager;
        this.faithTrackManager=faithTrackManager;
        this.game = game;
    }

    public boolean gameMarketResourcesAction(Player p, boolean column, int number) {
        if(column && number >=0 && number <= 4) return false;
        if(!column && number >= 0 && number <= 3) return false;

        ArrayList<MarketMarble> temp;
        WarehouseDepot depot = p.getWarehouseDepot();

        Scanner myInput = new Scanner(System.in);

        if(column)
            temp = game.getMarket().getColumn(number);
        else
            temp = game.getMarket().getRow(number);

        ArrayList<Resource> R = new ArrayList<>();
        for( MarketMarble m : temp ) {
            try {
                m.addResource(R);
            } catch (RedMarbleException e) {
                faithTrackManager.advance(p);
            }
        }

        int i = 0;
        while(R.size()>0) {
            Resource r = R.get(i);

            if (r == Resource.EXTRA) {
                ArrayList<LeaderCard> a = p.getCardsWithMarketResourceAbility();
                if (a.size() == 0) {
                    R.remove(i); //Player cannot use the White Marble
                } else if (a.size() == 1) {
                    R.set(i, ((MarketResources) a.get(0).getSpecialAbility()).getConvertedResource());
                } else if (a.size() == 2) {
                    Resource r1 = ((MarketResources) a.get(0).getSpecialAbility()).getConvertedResource();
                    Resource r2 = ((MarketResources) a.get(1).getSpecialAbility()).getConvertedResource();
                    Resource chosenResource;
//MYSTERY FROM THIS POINT ONWARD -------------------------------------------------------------------------------------------------------------------
                    boolean done = false;
                    int x = 0;


                    while (!done) {
                        System.out.println("hai una risorsa extra! che vuoi?");
                        System.out.println("1: " + r1);
                        System.out.println("2: " + r2);
                        x = myInput.nextInt();
                        if (x == 1 || x == 2) done = true;

                    }
                    //askForResource ???

                    if (x == 1) chosenResource = r1;
                    else chosenResource = r2;
//END OF THE MYSTERY! -------------------------------------------------------------------------------------------------------------------
                    R.set(i, chosenResource);
                }
            }
            r = R.get(i);

            if (r == Resource.COIN || r == Resource.STONE || r == Resource.SHIELD || r == Resource.SERVANT)
            {
                boolean[] choices = new boolean[7];
                choices[0] = choices[1] = choices[2] = choices[3] = choices[4] = choices[5] = choices[6] = false;

                //choice 0: put in depot
                if (depot.isAddable(r)) choices[0] = true;

                //choice 1: put in extra depot
                ArrayList<LeaderCard> list = p.getCardsWithExtraDepotAbility();
                ExtraDepot e = null;
                if (list.size() == 0) {
                    choices[1] = false;
                } else if (list.size() == 1) {
                    e = ((ExtraDepot) list.get(0).getSpecialAbility());
                    if (e.getResourceType()==r && e.getNumber() < 2)
                        choices[1] = true;
                } else if (list.size() == 2) {
                    e = ((ExtraDepot) list.get(0).getSpecialAbility());
                    if (e.getResourceType() != r) {
                        e = ((ExtraDepot) list.get(1).getSpecialAbility());
                        if (e.getResourceType() != r) {
                            e = null;
                        }
                    }
                    if (e != null) {
                        if (e.getNumber() < 2)
                            choices[1] = true;
                    }
                }

                //choice 2: discard resource (only available if the player cannot use the depot or the extra depot
                if (!choices[0] && !choices[1]) choices[2] = true;

                //choice 3-4-5: swap rows
                if (depot.getShelf2ResourceNumber() < 2) choices[3] = true; //swap 1-2
                if (depot.getShelf3ResourceNumber() < 2) choices[4] = true; //swap 1-3
                if (depot.getShelf3ResourceNumber() < 3) choices[5] = true; //swap 2-3

                //choice 6: skip this resource
                if ( R.size() > 1 ) {
                    choices[6] = true;
                }


//MYSTERY FROM THIS POINT ONWARD -------------------------------------------------------------------------------------------------------------------

                boolean done = false;
                int x = 0;

                while (!done) {
                    if(choices[0]) System.out.println("1: METTI NEL DEPOSITO");
                    if(choices[1]) System.out.println("2: PUT IN EXTRA DEPOSITO");
                    if(choices[2]) System.out.println("3: DISCARD RESOURCE");
                    if(choices[3]) System.out.println("4: SWAP RIGA 1-2 DEPOT");
                    if(choices[4]) System.out.println("5: SWAP RIGA 1-3 DEPOT");
                    if(choices[5]) System.out.println("6: SWAP RIGA 2-3 DEPOT");
                    if(choices[6]) System.out.println("7: SKIPPA THIS RISOURSA");
                    x = myInput.nextInt();
                    if ((x==1&&choices[0]) || (x==2&&choices[1]) || (x==3&&choices[2]) || (x==4&&choices[3]) || (x==5&&choices[4]) || (x==6&&choices[5]) || (x==7)&&choices[6]) done = true;

                }
                //askForCHOICE?

                switch (x)
                {
                    case 1:
                        depot.add(r);
                        R.remove(i);
                        break;
                    case 2:
                        e.addResource(1);
                        R.remove(i);
                        break;
                    case 3:
                        faithTrackManager.advanceAllExcept(p);
                        R.remove(i);
                        break;
                    case 4:
                        depot.swapRow(1,2);
                        break;
                    case 5:
                        depot.swapRow(1,3);
                        break;
                    case 6:
                        depot.swapRow(2,3);
                        break;
                    case 7:
                        i = (i+1)%R.size();
                        break;
                }
//END OF THE MYSTERY! -------------------------------------------------------------------------------------------------------------------

            }
        }
        return true;
    }

    public boolean activateLeaderCard(Player player, int cardNumber){
        if(player.getLeaderCards()[cardNumber].getEnable()) return false;
        LeaderCard l = player.getLeaderCards()[cardNumber];
        Requirement requirement = l.getRequirement();

        if(requirement.isResourceRequirement()){
            ResourceRequirements resourceRequirements= (ResourceRequirements) requirement;
            Map<Resource, Integer> requiredResources = resourceRequirements.getRequirements();

            Map<Resource, Integer> resources = player.getResources();

            for(Resource resource: requiredResources.keySet()){
                resources.put(resource, resources.get(resource)-requiredResources.get(resource));
                if(resources.get(resource)<0) return false;
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
                if(requiredNumCard>0) return false;
            }
            player.getLeaderCards()[cardNumber].setEnable(true);
            return true;
        }

        //should never reach this position
        return false;
    }

    public boolean ActivateProduction() {
        return true;
    }

    public boolean endTurn() {
        return true;
    }

    public boolean changeDepotConfig(Resource slot1, Resource[] slot2, Resource[] slot3, int i, int j) {
        return true;
    }

    public boolean buyDevelopmentCard(Player p) {
        DevelopmentCard[][] possibleCards = game.getDevelopmentCardsDeck().getVisible();
        Map<DevelopmentCard, boolean[]> finalCards = new HashMap<>();

        Map<Resource, Integer> playerResources = p.getResources();

        ArrayList<DevelopmentCard> cards = new ArrayList<>();
        for(int r = 0; r < 2; r++) {
            for( int c = 0 ; c < 3; c++)
            {
                if(possibleCards[r][c]!=null)
                   cards.add(possibleCards[r][c]);
            }
        }

        ArrayList<DevelopmentCard> tmp = new ArrayList<>(cards);
        //first check: all the cards that the player can't buy are removed from the ones that are on top.
        for(int i = 0; i < tmp.size(); i++)
        {
            HashMap<Resource, Integer> cost = tmp.get(i).getCost();
            for (Resource resource : cost.keySet()) {
                if (playerResources.get(resource) < cost.get(resource)) {
                    cards.remove(i);
                }
            }
        }

        //second check: all the cards that the player can't put in his slot are removed from the ones selected before.
        for(int i = 0; i < cards.size(); i++) {
            boolean[] pos = new boolean[3];
            for(int j = 0; j < 3; j++) {
                if(p.getDevelopmentSlot().validateNewCard(cards.get(i), j)) {
                    pos[j] = true;
                }
                else {
                    pos[j] = false;
                }
            }
            if(pos[0] || pos[1] || pos[2]){
                finalCards.put(cards.get(i), pos);
            }
        }
        return false;
    }
}
