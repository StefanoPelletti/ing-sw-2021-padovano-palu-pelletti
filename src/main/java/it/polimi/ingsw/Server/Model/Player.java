package it.polimi.ingsw.Server.Model;


import java.util.*;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Model.SpecialAbilities.ExtraDepot;

public class Player {
    private final String nickname;
    private int VP;
    private int position;   //used for FaithTrack tracking
    private int playerNumber;

    private final Strongbox strongbox;
    private final WarehouseDepot warehouseDepot;
    private LeaderCard[] leaderCards;
    private final DevelopmentSlot developmentSlot;

    public Player( String nickname, int playerNumber )
    {
        this.nickname = nickname;
        this.VP = 0;
        this.position = 1;
        this.playerNumber = playerNumber;
        this.leaderCards = new LeaderCard[2];
        this.strongbox = new Strongbox();
        this.warehouseDepot = new WarehouseDepot();
        this.developmentSlot = new DevelopmentSlot();

    }

    public String getNickname() { return nickname; }
    public int getVP() { return VP; }

    public boolean setVP(int VP) {
        if(VP<0) return false;
        this.VP = VP;
        return true;
    }

    public boolean addVP(int VP) {
        if(VP<0) return false;
        this.VP = this.VP + VP;
        return true;
    }
    public int getPosition() { return position; }
    public boolean setPosition(int position) {
        if(position<0) return false;
        this.position = position;
        return true;
    }
    public int getPlayerNumber() { return playerNumber; }
    public void setPlayerNumber(int playerNumber) { this.playerNumber = playerNumber; }

    public Strongbox getStrongbox() { return this.strongbox; }
    public WarehouseDepot getWarehouseDepot() { return this.warehouseDepot; }

    public LeaderCard[] getLeaderCards() {
        LeaderCard[] result = new LeaderCard[2];
        result[0] = leaderCards[0];
        result[1] = leaderCards[1];
        return result;
    }
    public DevelopmentSlot getDevelopmentSlot() { return this.developmentSlot; }

    public void associateLeaderCards(LeaderCard L1, LeaderCard L2)
    {
        leaderCards[0] = L1;
        leaderCards[1] = L2;
    }

    public Map<Resource, Integer> getResources(){
        Map<Resource, Integer> resources= this.warehouseDepot.getResources();
        for(Resource resource: Resource.values()){
            if(this.strongbox.getQuantity(resource)!=null) {
                resources.put(resource, resources.get(resource) + this.strongbox.getQuantity(resource));
            }
        }
        for(LeaderCard l: this.getCardsWithExtraDepotAbility()){
            ExtraDepot depotAbility = (ExtraDepot) l.getSpecialAbility();
            Resource extraResource = depotAbility.getResourceType();
            resources.put(extraResource, resources.get(extraResource) + depotAbility.getNumber());
        }
        return resources;
    }

    public ArrayList<LeaderCard> getCardsWithDiscountResourceAbility()
    {
        ArrayList<LeaderCard> result = new ArrayList<>();
        if (leaderCards[0].getSpecialAbility().isDiscountResource() && leaderCards[0].getEnable())
            result.add(leaderCards[0]);
        if (leaderCards[1].getSpecialAbility().isDiscountResource() && leaderCards[1].getEnable())
            result.add(leaderCards[1]);
        return result;
    }

    public ArrayList<LeaderCard> getCardsWithExtraDepotAbility()
    {
        ArrayList<LeaderCard> result = new ArrayList<>();
        if (leaderCards[0].getSpecialAbility().isExtraDepot() && leaderCards[0].getEnable())
            result.add(leaderCards[0]);
        if (leaderCards[1].getSpecialAbility().isExtraDepot() && leaderCards[1].getEnable())
            result.add(leaderCards[1]);
        return result;
    }
    public ArrayList<LeaderCard> getCardsWithProductionAbility()
    {
        ArrayList<LeaderCard> result = new ArrayList<>();
        if (leaderCards[0].getSpecialAbility().isProduction() && leaderCards[0].getEnable())
            result.add(leaderCards[0]);
        if (leaderCards[1].getSpecialAbility().isProduction() && leaderCards[1].getEnable())
            result.add(leaderCards[1]);
        return result;
    }
    public ArrayList<LeaderCard> getCardsWithMarketResourceAbility()
    {
        ArrayList<LeaderCard> result = new ArrayList<>();
        if (leaderCards[0].getSpecialAbility().isMarketResource() && leaderCards[0].getEnable())
            result.add(leaderCards[0]);
        if (leaderCards[1].getSpecialAbility().isMarketResource() && leaderCards[1].getEnable())
            result.add(leaderCards[1]);
        return result;
    }
}