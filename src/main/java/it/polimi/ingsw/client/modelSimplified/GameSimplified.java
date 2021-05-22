package it.polimi.ingsw.client.modelSimplified;

import it.polimi.ingsw.client.modelSimplified.middles.*;
import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.updateMessages.*;
import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.*;
import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_DevSlot;
import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_Extradepot;
import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_Strongbox;
import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_WarehouseDepot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GameSimplified {
    private final MarketSimplified market;
    private final DevelopmentCardsDeckSimplified devDeck;
    private final FaithTrackSimplified faithTrack;
    private final DevelopmentCardsVendorSimplified developmentCardsVendorSimplified;
    private final LeaderCardsPickerSimplified leaderCardsPickerSimplified;
    private final MarketHelperSimplified marketHelperSimplified;
    private final ResourceObjectSimplified resourceObjectSimplified;
    private final LeaderboardSimplified leaderboardSimplified;
    private int turn;
    private int currentPlayer;
    private int blackCrossPosition;
    private List<PlayerSimplified> playerSimplifiedList;

    public GameSimplified() {
        this.turn = -1;
        this.currentPlayer = -1;

        market = new MarketSimplified();
        devDeck = new DevelopmentCardsDeckSimplified();
        faithTrack = new FaithTrackSimplified(this);
        playerSimplifiedList = new ArrayList<>();

        developmentCardsVendorSimplified = new DevelopmentCardsVendorSimplified();
        leaderCardsPickerSimplified = new LeaderCardsPickerSimplified();
        marketHelperSimplified = new MarketHelperSimplified();
        resourceObjectSimplified = new ResourceObjectSimplified();
        leaderboardSimplified = new LeaderboardSimplified();
    }

    public boolean isDevelopmentCardsVendorEnabled() {
        return this.developmentCardsVendorSimplified.isEnabled();
    }

    public boolean isLeaderCardsObjectEnabled() {
        return this.leaderCardsPickerSimplified.isEnabled();
    }

    public boolean isMarketHelperEnabled() {
        return this.marketHelperSimplified.isEnabled();
    }

    public boolean isResourceObjectEnabled() {
        return this.resourceObjectSimplified.isEnabled();
    }

    public void updateGame(MSG_UPD_Game message) {
        int newTurn = message.getTurn();
        int newCurrentPlayer = message.getCurrentPlayer();
        int newBlackCrossPosition = message.getBlackCrossPosition();

        this.turn = newTurn;
        this.currentPlayer = newCurrentPlayer;
        this.blackCrossPosition = newBlackCrossPosition;
    }

    public void updateMarket(MSG_UPD_Market message) {
        this.market.update(message);
    }

    public void updateDevelopmentCardsDeck(MSG_UPD_DevDeck message) {
        this.devDeck.update(message);
    }

    public void updateFaithTrack(MSG_UPD_FaithTrack message) {
        this.faithTrack.update(message);
    }

    public void updateDevelopmentCardsVendor(MSG_UPD_DevCardsVendor message) {
        this.developmentCardsVendorSimplified.update(message);
    }

    public void updateLeaderCardsObject(MSG_UPD_LeaderCardsObject message) {
        this.leaderCardsPickerSimplified.update(message);
    }

    public void updateMarketHelper(MSG_UPD_MarketHelper message) {
        this.marketHelperSimplified.update(message);
    }

    public void updateResourceObject(MSG_UPD_ResourceObject message) {
        this.resourceObjectSimplified.update(message);
    }

    public void updateLeaderBoard(MSG_UPD_LeaderBoard message) {
        this.leaderboardSimplified.update(message);
    }

    // absolutely needs testing
    public void updateAll(MSG_UPD_Full message) {
        this.updateGame(message.getGame());

        this.developmentCardsVendorSimplified.update(message.getDevCardsVendor());
        this.leaderCardsPickerSimplified.update(message.getLeaderCardsObject());
        this.marketHelperSimplified.update(message.getMarketHelper());
        this.resourceObjectSimplified.update(message.getResourceObject());

        this.market.update(message.getMarket());
        this.devDeck.update(message.getDevDeck());
        this.faithTrack.update(message.getFaithTrack());

        Map<Integer, List<Message>> map = message.getPlayerList();
        List<Message> list;

        playerSimplifiedList = new ArrayList<>();
        PlayerSimplified player;

        for (int i = 1; i <= map.size(); i++) {
            list = map.get(i);
            if (list == null) break;
            player = new PlayerSimplified(i);
            for (Message m : list) {
                switch (m.getMessageType()) {
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
        switch (message.getMessageType()) {
            case MSG_UPD_Player:
                getCurrentPlayerRef().update((MSG_UPD_Player) message);
                break;
            case MSG_UPD_WarehouseDepot:
                getCurrentPlayerRef().updateWarehouseDepot((MSG_UPD_WarehouseDepot) message);
                break;
            case MSG_UPD_DevSlot:
                getCurrentPlayerRef().updateDevelopmentSlot((MSG_UPD_DevSlot) message);
                break;
            case MSG_UPD_Extradepot:
                getCurrentPlayerRef().updateExtradepot((MSG_UPD_Extradepot) message);
                break;
            case MSG_UPD_Strongbox:
                getCurrentPlayerRef().updateStrongbox((MSG_UPD_Strongbox) message);
                break;
        }
    }


    public DevelopmentCardsVendorSimplified getDevelopmentCardsVendor() {
        return this.developmentCardsVendorSimplified;
    }

    public LeaderCardsPickerSimplified getLeaderCardsObject() {
        return this.leaderCardsPickerSimplified;
    }

    public MarketHelperSimplified getMarketHelper() {
        return this.marketHelperSimplified;
    }

    public ResourceObjectSimplified getResourceObject() {
        return this.resourceObjectSimplified;
    }

    public int getBlackCrossPosition() {
        return blackCrossPosition;
    }

    public MarketSimplified getMarket() {
        return market;
    }

    public DevelopmentCardsDeckSimplified getDevDeck() {
        return devDeck;
    }

    public FaithTrackSimplified getFaithTrack() {
        return faithTrack;
    }

    public List<PlayerSimplified> getPlayerSimplifiedList() {
        return playerSimplifiedList;
    }

    public LeaderboardSimplified getLeaderBoard() {
        return leaderboardSimplified;
    }

    public List<PlayerSimplified> getPlayerList() {
        return this.playerSimplifiedList;
    }

    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    public int getTurn() {
        return this.turn;
    }

    public PlayerSimplified getCurrentPlayerRef() {
        Optional<PlayerSimplified> result = playerSimplifiedList.stream().filter(p -> p.getPlayerNumber() == currentPlayer).findFirst();
        return result.orElse(null);
    }


    public int getMyPlayerNumber(String Nickname) {
        for (PlayerSimplified p : playerSimplifiedList) {
            if (p.getNickname().equals(Nickname)) return p.getPlayerNumber();
        }
        return 0;
    }

    public PlayerSimplified getPlayerRef(int playerNumber) {
        Optional<PlayerSimplified> result = playerSimplifiedList.stream().filter(p -> p.getPlayerNumber() == playerNumber).findFirst();
        return result.orElse(null);
    }

    public PlayerSimplified getPlayerRef(String nickname) {
        Optional<PlayerSimplified> result = playerSimplifiedList.stream().filter(p -> p.getNickname().equals(nickname)).findFirst();
        return result.orElse(null);
    }

    public boolean isMyTurn(int myPlayerNumber) {
        return (this.currentPlayer == myPlayerNumber);
    }

    public void updatePlayer(Message message) {
        MSG_UPD_Player msg = (MSG_UPD_Player) message;
        int playerNumber = msg.getPlayerNumber();

        Optional<PlayerSimplified> result = playerSimplifiedList.stream().filter(p -> p.getPlayerNumber() == playerNumber).findFirst();

        result.ifPresentOrElse(player -> player.update(msg), () -> System.out.println(" Game Simplified R259 Player not found. ???????"));
    }

    public boolean isMiddleActive() {
        return isLeaderCardsObjectEnabled() || isDevelopmentCardsVendorEnabled() || isMarketHelperEnabled() || isLeaderBoardEnabled() || isResourceObjectEnabled();
    }

    public boolean isLeaderBoardEnabled() {
        return this.leaderboardSimplified.isEnabled();
    }
}