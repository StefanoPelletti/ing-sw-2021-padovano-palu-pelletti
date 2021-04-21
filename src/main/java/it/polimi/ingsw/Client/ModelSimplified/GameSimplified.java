package it.polimi.ingsw.Client.ModelSimplified;


import it.polimi.ingsw.Networking.Message.UpdateMessages.*;
import it.polimi.ingsw.Client.ModelSimplified.Middles.*;
import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.*;

import java.util.*;

public class GameSimplified {
    private int turn;
    private int currentPlayer;



    MarketSimplified market;// = new MarketSimplified();
    DevelopmentCardsDeckSimplified devDeck;// = new DevelopmentCardsDeckSimplified();
    FaithTrackSimplified faithTrack;// = new FaithTrackSimplified(this);
    List<PlayerSimplified> playerSimplifiedList;// = new ArrayList<>();

    DevelopmentCardsVendor developmentCardsVendor;
    LeaderCardsObject leaderCardsObject;
    MarketHelper marketHelper;
    ResourceObject resourceObject;

    public GameSimplified()
    {
        this.turn = -1;
        this.currentPlayer = -1;

        market = new MarketSimplified();
        devDeck = new DevelopmentCardsDeckSimplified();
        faithTrack = new FaithTrackSimplified(this);
        playerSimplifiedList = new ArrayList<>();

         developmentCardsVendor= new DevelopmentCardsVendor();
         leaderCardsObject= new LeaderCardsObject();
         marketHelper = new MarketHelper();
         resourceObject =  new ResourceObject();
    }

    public boolean isDevelopmentCardsVendorEnabled()
    {
        return this.developmentCardsVendor.isEnabled();
    }

    public boolean isLeaderCardsObjectEnabled()
    {
        return this.leaderCardsObject.isEnabled();
    }

    public boolean isMarketHelperEnabled()
    {
        return this.marketHelper.isEnabled();
    }

    public boolean isResourceObjectEnabled()
    {
        return this.resourceObject.isEnabled();
    }

    public void updateMarket(MSG_UPD_Market message)
    {
        this.market.update(message);
    }

    public void updateDevelopmentCardsDeck(MSG_UPD_DevDeck message)
    {
        this.devDeck.update(message);
    }

    public void updateFaithTrack(MSG_UPD_FaithTrack message)
    {
        this.faithTrack.update(message);
    }

    public void updateDevelopmentCardsVendor(MSG_UPD_DevCardsVendor message)
    {
        this.developmentCardsVendor.update(message);
    }

    public void updateLeaderCardsObject(MSG_UPD_LeaderCardsObject message)
    {
        this.leaderCardsObject.update(message);
    }

    public void updateMarketHelper(MSG_UPD_MarketHelper message)
    {
        this.marketHelper.update(message);
    }

    public void updateResourceObject(MSG_UPD_ResourceObject message)
    {
        this.resourceObject.update(message);
    }

    public void updateTurn()
    {

    }


    public void updateCurrentPlayer(MSG_UPD_Player message) {

    }



    public DevelopmentCardsVendor getDevelopmentCardsVendor() { return this.developmentCardsVendor;}
    public LeaderCardsObject getLeaderCardsObject() { return this.leaderCardsObject;}
    public MarketHelper getMarketHelper() { return this.marketHelper;}
    public ResourceObject getResourceObject() { return this.resourceObject;}

    public List<PlayerSimplified> getPlayerList() { return this.playerSimplifiedList; }
    public int getCurrentPlayer() { return this.currentPlayer;}
    public int getTurn() { return this.turn;}
    public PlayerSimplified getCurrentPlayerRef() {
        Optional<PlayerSimplified> result = playerSimplifiedList.stream().filter(p->p.getPlayerNumber()==currentPlayer).findFirst();
        return result.orElse(null);
    }
}
