package it.polimi.ingsw.server.model;

import it.polimi.ingsw.networking.message.MSG_NOTIFICATION;
import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_Game;
import it.polimi.ingsw.server.model.actionTokens.ActionToken;
import it.polimi.ingsw.server.model.enumerators.Status;
import it.polimi.ingsw.server.model.middles.*;
import it.polimi.ingsw.server.model.specialAbilities.ExtraDepot;
import it.polimi.ingsw.server.utils.ModelObservable;
import it.polimi.ingsw.server.utils.ModelObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Game extends ModelObservable {
    private final ActionTokenStack actionTokenStack;
    private final ArrayList<Player> playerList;
    private final LeaderCardsDeck leaderCardsDeck;
    private final Market market;
    private final FaithTrack faithTrack;
    private final DevelopmentCardsDeck developmentCardsDeck;
    private final MarketHelper marketHelper;
    private final MessageHelper messageHelper;
    private final DevelopmentCardsVendor developmentCardsVendor;
    private final ErrorObject errorObject;
    private final LeaderCardsPicker leaderCardsPicker;
    private final ResourcePicker resourcePicker;
    private final Leaderboard leaderBoard;
    private Status status;
    private Player firstPlayer;
    private int turn;
    private int blackCrossPosition;
    private int currentPlayer;

    public Game() {
        status = Status.INIT_1;
        firstPlayer = null;
        blackCrossPosition = 0;
        playerList = new ArrayList<>();
        leaderCardsDeck = new LeaderCardsDeck();
        market = new Market();
        faithTrack = new FaithTrack(this);
        developmentCardsDeck = new DevelopmentCardsDeck();
        actionTokenStack = new ActionTokenStack();
        marketHelper = new MarketHelper();
        messageHelper = new MessageHelper();
        developmentCardsVendor = new DevelopmentCardsVendor();
        errorObject = new ErrorObject();
        leaderCardsPicker = new LeaderCardsPicker();
        resourcePicker = new ResourcePicker();
        leaderBoard = new Leaderboard();
        currentPlayer = 1;
    }

    //GETTERS
    public Status getStatus() {
        return this.status;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public int getTurn() {
        return this.turn;
    }

    public int getBlackCrossPosition() {
        return this.blackCrossPosition;
    }

    //SETTERS
    public void setTurn(int turn) {
        this.turn = turn;
        notifyObservers();
    }

    public void setBlackCrossPosition(int blackCrossPosition) {
        this.blackCrossPosition = blackCrossPosition;
        notifyLorenzoMovement();
        notifyObservers();
    }

    public int getCurrentPlayerInt() {
        return currentPlayer;
    }

    public List<Player> getPlayerList() {
        return new ArrayList<>(playerList);
    }

    public LeaderCardsDeck getLeaderCardsDeck() {
        return this.leaderCardsDeck;
    }

    public Market getMarket() {
        return this.market;
    }

    public MarketHelper getMarketHelper() {
        return this.marketHelper;
    }

    public MessageHelper getMessageHelper() {
        return messageHelper;
    }

    public ErrorObject getErrorObject() {
        return this.errorObject;
    }

    public DevelopmentCardsVendor getDevelopmentCardsVendor() {
        return this.developmentCardsVendor;
    }

    public FaithTrack getFaithTrack() {
        return this.faithTrack;
    }

    public DevelopmentCardsDeck getDevelopmentCardsDeck() {
        return this.developmentCardsDeck;
    }

    public ActionTokenStack getActionTokenStack() {
        return this.actionTokenStack;
    }

    public LeaderCardsPicker getLeaderCardsPicker() {
        return this.leaderCardsPicker;
    }

    public ResourcePicker getResourcePicker() {
        return this.resourcePicker;
    }

    public Leaderboard getLeaderBoard() {
        return this.leaderBoard;
    }

    //GETTERS NON BASIC
    /**
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return getPlayer(currentPlayer);
    }

    /**
     * Sets the current player and notifies the observers that the current player has changed.
     * @param currentPlayer the current player
     */
    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
        notifyObservers();
    }

    /**
     * Given:
     * @param nickname the nickname of a player
     * @return the player
     */
    public Player getPlayer(String nickname) {
        Optional<Player> result = playerList.stream().filter(p -> p.getNickname().equals(nickname)).findFirst();
        return result.orElse(null);
    }

    /**
     * Given:
     * @param playerNumber the number of a player
     * @return the player
     */
    public Player getPlayer(int playerNumber) {
        Optional<Player> result = playerList.stream().filter(p -> p.getPlayerNumber() == playerNumber).findFirst();
        return result.orElse(null);
    }

    /**
     * Changes the status of the game. If the status is LAST TURN then notifies the observers.
     * @param status the status of the game.
     */
    public void changeStatus(Status status) {
        if (status == Status.LAST_TURN) {
            Message m = new MSG_NOTIFICATION("LAST TURN!");
            notifyObservers(m);
        }
        this.status = status;
    }

    //METHODS

    /**
     * Given:
     * @param nickname a nickname for the new player
     * @param playerNumber a number for the new player
     * @return false if the nickname already exists, true if the player is added correctly.
     * Also, the player receives his initial cards and resources and the position is initialized.
     * If the player is the number 1, he is the first player.
     */
    public boolean addPlayer(String nickname, int playerNumber) {
        if (getPlayer(nickname) != null) return false;
        Player player = new Player(nickname, playerNumber);
        player.setStartingCards(leaderCardsDeck.pickFourCards());
        player.setInitialStartingResources();
        if(playerNumber == 3 || playerNumber == 4)
            player.setPosition(1);
        if (playerNumber == 1)
            firstPlayer = player;
        if (playerNumber == 3 || playerNumber == 4)
            player.setPosition(1);
        playerList.add(player);
        return true;
    }

    /**
     * It's a method used by the Observer pattern. It links the observer to all the objects in the model.
     * @param observer
     */
    public void addAllObservers(ModelObserver observer) {
        this.addObserver(observer);
        faithTrack.addObserver(observer);
        market.addObserver(observer);
        developmentCardsDeck.addObserver(observer);
        developmentCardsVendor.addObserver(observer);
        errorObject.addObserver(observer);
        marketHelper.addObserver(observer);
        messageHelper.addObserver(observer);
        resourcePicker.addObserver(observer);
        leaderCardsPicker.addObserver(observer);
        leaderBoard.addObserver(observer);
        for (LeaderCard l : leaderCardsDeck.getCards()) {
            if (l.getSpecialAbility().isExtraDepot()) {
                ExtraDepot extraDepot = (ExtraDepot) l.getSpecialAbility();
                extraDepot.addObserver(observer);
            }
        }
        for (Player player : this.playerList) {
            player.addObserver(observer);
            player.getWarehouseDepot().addObserver(observer);
            player.getStrongbox().addObserver(observer);
            player.getDevelopmentSlot().addObserver(observer);
        }
    }

    /**
     * Given
     * @param nickname nickname of a player
     * @return true if the player is correctly removed.
     */
    public boolean removePlayer(String nickname) {
        return playerList.removeIf(x -> (x.getNickname()).equals(nickname));
    }

    /**
     * Modifies the messageHelper which notifies all the observers.
     * @param message
     */
    public void broadcastMessage(String message) {
        messageHelper.setNewMessage(message);
    }

    /**
     * @return a list of 4 cards presented to the player at the beginning of the game.
     */
    public List<LeaderCard> getCurrentPlayerStartingCards() {
        return this.getCurrentPlayer().getStartingCards();
    }

    public DevelopmentCard[][] getVisibleCards() {
        return developmentCardsDeck.getVisible();
    }

    public void removeCardOnDevelopmentCardsDeck(int row, int column) {
        developmentCardsDeck.removeCard(row, column);
    }

    public void removeColumnOnDevelopmentCardsDeck(int column) {
        developmentCardsDeck.removeCard(column);
    }

    public boolean isOneColumnDestroyedOnTheDevelopmentCardsDeck() {
        return developmentCardsDeck.isOneColumnDestroyed();
    }

    public ActionToken pickFirstActionToken() {
        return actionTokenStack.pickFirst();
    }

    public void shuffleActionStack() {
        actionTokenStack.shuffle();
    }

    public void setLeaderCardsPickerCards(List<LeaderCard> list) {
        leaderCardsPicker.setCards(list);
    }

    public void setResourcePickerNumOfResources(int value) {
        resourcePicker.setNumOfResources(value);
    }

    /**
     * @return true if one of the middles is enabled.
     */
    public boolean isMiddleActive() {
        return (this.leaderCardsPicker.isEnabled() ||
                this.resourcePicker.isEnabled() ||
                this.marketHelper.isEnabled() ||
                this.developmentCardsVendor.isEnabled());
    }

    public boolean isLeaderBoardEnabled() {
        return this.leaderBoard.isEnabled();
    }

    public boolean isLeaderCardsPickerEnabled() {
        return this.leaderCardsPicker.isEnabled();
    }

    public void setLeaderCardsPickerEnabled(boolean value) {
        leaderCardsPicker.setEnabled(value);
    }

    public boolean isResourcePickerEnabled() {
        return this.resourcePicker.isEnabled();
    }

    public void setResourcePickerEnabled(boolean value) {
        resourcePicker.setEnabled(value);
    }

    public boolean isMarketHelperEnabled() {
        return this.marketHelper.isEnabled();
    }

    public void setMarketHelperEnabled(boolean value) {
        marketHelper.setEnabled(value);
    }

    public boolean isDevelopmentCardsVendorEnabled() {
        return this.developmentCardsVendor.isEnabled();
    }

    public void setDevelopmentCardsVendorEnabled(boolean value) {
        developmentCardsVendor.setEnabled(value);
    }

    //OBSERVABLE

    /**
     * notifies the observers by sending a message that contains the actual internal status of the Game.
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     *
     * @return the actual message passed by the notifyObservers() method that contains the status of the Game (the turn, the currentPlayer and the blackCrossPosition).
     */
    public MSG_UPD_Game generateMessage() {
        return new MSG_UPD_Game(
                this.turn,
                this.currentPlayer,
                this.blackCrossPosition
        );
    }

    private void notifyLorenzoMovement() {
        this.notifyObservers(new MSG_NOTIFICATION("Lorenzo has advanced on the Faith Track! Now at position: " + this.blackCrossPosition));
    }
}