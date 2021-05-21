package it.polimi.ingsw.networking.message.updateMessages;

import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_DevCardsVendor;
import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_LeaderCardsObject;
import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_MarketHelper;
import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_ResourceObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MSG_UPD_Full extends Message implements Serializable {

    private final Map<Integer, List<Message>> playerList;
    private MSG_UPD_Game game;
    private MSG_UPD_DevCardsVendor devCardsVendor;
    private MSG_UPD_LeaderCardsObject leaderCardsObject;
    private MSG_UPD_MarketHelper marketHelper;
    private MSG_UPD_ResourceObject resourceObject;
    private MSG_UPD_Market market;
    private MSG_UPD_DevDeck devDeck;
    private MSG_UPD_FaithTrack faithTrack;

    public MSG_UPD_Full() {
        super(MessageType.MSG_UPD_Full);

        playerList = new HashMap<>();
    }

    // needs:
    //MSG_UPD_Player playerUpdate,
    //MSG_UPD_DevSlot devSlot,
    //MSG_UPD_Extradepot extradepot1,
    //MSG_UPD_Extradepot extradepot2,
    //MSG_UPD_Strongbox strongbox,
    //MSG_UPD_WarehouseDepot warehouseDepot
    public void addPlayerUpdate(int playerNumber, Message message) {
        List<Message> list = playerList.get(playerNumber);
        if (list == null)
            list = new ArrayList<>();

        list.add(message);
        playerList.put(playerNumber, list);
    }

    public MSG_UPD_DevCardsVendor getDevCardsVendor() {
        return devCardsVendor;
    }

    public void setDevCardsVendor(MSG_UPD_DevCardsVendor devCardsVendor) {
        this.devCardsVendor = devCardsVendor;
    }

    public MSG_UPD_LeaderCardsObject getLeaderCardsObject() {
        return leaderCardsObject;
    }

    public void setLeaderCardsObject(MSG_UPD_LeaderCardsObject leaderCardsObject) {
        this.leaderCardsObject = leaderCardsObject;
    }

    public MSG_UPD_MarketHelper getMarketHelper() {
        return marketHelper;
    }

    public void setMarketHelper(MSG_UPD_MarketHelper marketHelper) {
        this.marketHelper = marketHelper;
    }

    public MSG_UPD_ResourceObject getResourceObject() {
        return resourceObject;
    }

    public void setResourceObject(MSG_UPD_ResourceObject resourceObject) {
        this.resourceObject = resourceObject;
    }

    public MSG_UPD_Game getGame() {
        return game;
    }

    public void setGame(MSG_UPD_Game game) {
        this.game = game;
    }

    public MSG_UPD_DevDeck getDevDeck() {
        return devDeck;
    }

    public void setDevDeck(MSG_UPD_DevDeck devDeck) {
        this.devDeck = devDeck;
    }

    public MSG_UPD_FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public void setFaithTrack(MSG_UPD_FaithTrack faithTrack) {
        this.faithTrack = faithTrack;
    }

    public MSG_UPD_Market getMarket() {
        return market;
    }

    public void setMarket(MSG_UPD_Market market) {
        this.market = market;
    }

    public Map<Integer, List<Message>> getPlayerList() {
        return playerList;
    }
}