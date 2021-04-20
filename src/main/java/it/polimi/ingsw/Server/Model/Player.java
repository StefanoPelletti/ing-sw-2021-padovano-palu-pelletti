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
    private final LeaderCard[] leaderCards;
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

    public void setLeaderCards(int cardNumber, boolean enable)
    {
        if(enable)
            leaderCards[cardNumber].setEnable(true);
        else
            leaderCards[cardNumber] = null;
        //notify();
    }
    public Strongbox getStrongbox() { return this.strongbox; }
    public WarehouseDepot getWarehouseDepot() { return this.warehouseDepot; }

    public LeaderCard[] getLeaderCards() {
        LeaderCard[] result = new LeaderCard[2];
        result[0] = leaderCards[0];
        result[1] = leaderCards[1];
        return result;
    }
    public DevelopmentSlot getDevelopmentSlot() { return this.developmentSlot; }

    public void associateLeaderCards(ArrayList<LeaderCard> cards)
    {
        leaderCards[0] = cards.get(0);
        leaderCards[1] = cards.get(1);
        //notify();
    }

    public Map<Resource, Integer> getResources(){
        Map<Resource, Integer> resources= this.warehouseDepot.getResources();
        Set<Resource> possibleResources = resources.keySet();
        for(Resource resource: possibleResources){
            if(this.strongbox.getQuantity(resource)!=null) {
                resources.merge(resource, this.strongbox.getQuantity(resource), Integer::sum);
            }
        }
        for(LeaderCard l: this.getCardsWithExtraDepotAbility()){
            ExtraDepot depotAbility = (ExtraDepot) l.getSpecialAbility();
            Resource extraResource = depotAbility.getResourceType();
            resources.merge(extraResource, depotAbility.getNumber(), Integer::sum);
        }
        return resources;
    }

    public Map<Resource, Integer> getDepotAndExtraDepotResources(){
        Map<Resource, Integer> result;
        result=this.warehouseDepot.getResources();
        ArrayList<LeaderCard> playerLeaderCards = this.getCardsWithExtraDepotAbility();
        for(LeaderCard l: playerLeaderCards){
            ExtraDepot ability = (ExtraDepot) l.getSpecialAbility();
            Resource resource = ability.getResourceType();
            result.merge(resource, ability.getNumber(), Integer::sum);
        }
        return result;
    }

    public ArrayList<LeaderCard> getCardsWithDiscountResourceAbility()
    {
        ArrayList<LeaderCard> result = new ArrayList<>();
        if (leaderCards[0] != null && leaderCards[0].getSpecialAbility().isDiscountResource() && leaderCards[0].getEnable())
            result.add(leaderCards[0]);
        if (leaderCards[1] != null && leaderCards[1].getSpecialAbility().isDiscountResource() && leaderCards[1].getEnable())
            result.add(leaderCards[1]);
        return result;
    }

    public ArrayList<LeaderCard> getCardsWithExtraDepotAbility()
    {
        ArrayList<LeaderCard> result = new ArrayList<>();
        if (leaderCards[0] != null && leaderCards[0].getSpecialAbility().isExtraDepot() && leaderCards[0].getEnable())
            result.add(leaderCards[0]);
        if (leaderCards[1] != null && leaderCards[1].getSpecialAbility().isExtraDepot() && leaderCards[1].getEnable())
            result.add(leaderCards[1]);
        return result;
    }
    public ArrayList<LeaderCard> getCardsWithProductionAbility()
    {
        ArrayList<LeaderCard> result = new ArrayList<>();
        if (leaderCards[0] != null && leaderCards[0].getSpecialAbility().isProduction() && leaderCards[0].getEnable())
            result.add(leaderCards[0]);
        if (leaderCards[1] != null && leaderCards[1].getSpecialAbility().isProduction() && leaderCards[1].getEnable())
            result.add(leaderCards[1]);
        return result;
    }
    public ArrayList<LeaderCard> getCardsWithMarketResourceAbility()
    {
        ArrayList<LeaderCard> result = new ArrayList<>();
        if (leaderCards[0] != null && leaderCards[0].getSpecialAbility().isMarketResource() && leaderCards[0].getEnable())
            result.add(leaderCards[0]);
        if (leaderCards[1] != null &&leaderCards[1].getSpecialAbility().isMarketResource() && leaderCards[1].getEnable())
            result.add(leaderCards[1]);
        return result;
    }

    @Override
    public boolean equals( Object obj )
    {
        if(obj == this) return true;
        if(!(obj instanceof Player)) return false;
        Player o = (Player) obj;
        return (this.nickname.equals(o.nickname));
    }


    public boolean deepEquals( Object obj )
    {
        if(obj == this) return true;
        if(!(obj instanceof Player)) return false;
        Player o = (Player) obj;
        return ( this.nickname.equals(o.nickname) &&
                this.VP == o.VP &&
                this.position == o.position &&
                this.playerNumber == o.playerNumber &&
                Arrays.equals(this.leaderCards, o.leaderCards) &&
                this.strongbox.equals(o.strongbox) &&
                this.warehouseDepot.equals(o.warehouseDepot) &&
                this.developmentSlot.equals(o.developmentSlot));
    }
}
