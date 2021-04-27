package it.polimi.ingsw.Client.ModelSimplified;


import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.UpdateMessages.*;
import it.polimi.ingsw.Client.ModelSimplified.Middles.*;
import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.*;
import it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate.MSG_UPD_DevSlot;
import it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate.MSG_UPD_Extradepot;
import it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate.MSG_UPD_Strongbox;
import it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate.MSG_UPD_WarehouseDepot;
import it.polimi.ingsw.Server.Model.Player;

import java.util.*;

public class GameSimplified {
    private int turn;
    private int currentPlayer;
    private int blackCrossPosition;

    MarketSimplified market;
    DevelopmentCardsDeckSimplified devDeck;
    FaithTrackSimplified faithTrack;
    List<PlayerSimplified> playerSimplifiedList;

    DevelopmentCardsVendor developmentCardsVendor;
    LeaderCardsObject leaderCardsObject;
    MarketHelper marketHelper;
    ResourceObject resourceObject;
    LeaderBoard leaderBoard;

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
        leaderBoard = new LeaderBoard();
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

    public void updateGame(MSG_UPD_Game message)
    {
        int newTurn = message.getTurn();
        int newCurrentPlayer = message.getCurrentPlayer();
        int newBlackCrossPosition = message.getBlackCrossPosition();

        this.turn = newTurn;
        this.currentPlayer = newCurrentPlayer;
        this.blackCrossPosition = newBlackCrossPosition;
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

    public void updateDevelopmentCardsVendor(MSG_UPD_DevCardsVendor message) {
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

    public void updateLeaderBoard(MSG_UPD_LeaderBoard message) { this.leaderBoard.update(message);}

    // absolutely needs testing
    public void updateAll(MSG_UPD_Full message)
    {
        this.updateGame(message.getGame());

        this.developmentCardsVendor.update(message.getDevCardsVendor());
        this.leaderCardsObject.update(message.getLeaderCardsObject());
        this.marketHelper.update(message.getMarketHelper());
        this.resourceObject.update(message.getResourceObject());

        this.market.update(message.getMarket());
        this.devDeck.update(message.getDevDeck());
        this.faithTrack.update(message.getFaithTrack());

        Map<Integer, List<Message>> map = message.getPlayerList();
        List<Message> list;

        playerSimplifiedList = new ArrayList<>();
        PlayerSimplified player;

        for ( int i=1 ; i<=map.size(); i++)
        {
            list = map.get(i);
            if(list == null) break;
            player = new PlayerSimplified(i);
            for( Message m : list)
            {
                switch(m.getMessageType())
                {
                    case MSG_UPD_Player:
                        player.update((MSG_UPD_Player) m);
                        break;
                    case MSG_UPD_DevSlot:
                        player.updateDevelopmentSlot((MSG_UPD_DevSlot) m);
                        break;
                    case MSG_UPD_Extradepot:
                        player.updateExtradepot((MSG_UPD_Extradepot) m);
                        break;
                    case MSG_UPD_Strongbox:
                        player.updateStrongbox((MSG_UPD_Strongbox) m);
                        break;
                    case MSG_UPD_WarehouseDepot:
                        player.updateWarehouseDepot((MSG_UPD_WarehouseDepot) m);
                        break;
                }
            }
            playerSimplifiedList.add(player);
        }
    }

    //?? 100% would not work for some reason.
    public void updateCurrentPlayer(Message message) {
        switch(message.getMessageType())
        {
            case MSG_UPD_Player:
                playerSimplifiedList.get(currentPlayer).update((MSG_UPD_Player) message);
                break;
            case MSG_UPD_WarehouseDepot:
                playerSimplifiedList.get(currentPlayer).updateWarehouseDepot((MSG_UPD_WarehouseDepot) message);
                break;
            case MSG_UPD_DevSlot:
                playerSimplifiedList.get(currentPlayer).updateDevelopmentSlot((MSG_UPD_DevSlot) message);
                break;
            case MSG_UPD_Extradepot:
                playerSimplifiedList.get(currentPlayer).updateExtradepot((MSG_UPD_Extradepot) message);
                break;
            case MSG_UPD_Strongbox:
                playerSimplifiedList.get(currentPlayer).updateStrongbox((MSG_UPD_Strongbox) message);
                break;
        }
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
