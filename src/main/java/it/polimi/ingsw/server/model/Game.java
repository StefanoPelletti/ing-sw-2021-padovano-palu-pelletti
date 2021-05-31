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
    private final List<Player> playerList;
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
     * Returns the reference of the current Player.
     * @return The reference of the current Player.
     */
    public Player getCurrentPlayer() {
        return getPlayer(currentPlayer);
    }

    /**
     * Sets the current player and notifies the observers that the current player has changed.
     * @param currentPlayer The number of the current player.
     */
    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
        notifyObservers();
    }

    /**
     * Returns the reference of a Player specified by his nickname.
     * @param nickname The nickname of a Player.
     * @return The Player reference if it was found, null otherwise.
     */
    public Player getPlayer(String nickname) {
        Optional<Player> result = playerList.stream().filter(p -> p.getNickname().equals(nickname)).findFirst();
        return result.orElse(null);
    }

    /**
     * Returns the reference of a Player specified by his player number.
     * @param playerNumber The number of a Player.
     * @return The Player reference if it was found, null otherwise.
     */
    public Player getPlayer(int playerNumber) {
        Optional<Player> result = playerList.stream().filter(p -> p.getPlayerNumber() == playerNumber).findFirst();
        return result.orElse(null);
    }

    /**
     * Changes the status of the game.
     * If the status is LAST TURN then notifies the observers.
     * @param status The new status of the Game.
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
     * Tries to add a new Player to the Game.
     * The Player is set his initial Leader Cards and initial Resources.
     * If the Player is the third or the fourth, his position will be 1 instead of 0.
     * If the Player is the first, his reference is saved in firstPlayer.
     * @param nickname The nickname for the new Player.
     * @param playerNumber The number for the new Player.
     * @return False if the nickname already exists, True if the player is added correctly.
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
     * It's a method used by the Observer pattern. It links the observer to all the objects in the Model.
     * @param observer The ModelObserver being added.
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
     * @param message The message being forwarded to the observers.
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
     * Returns True if any of the Middle Objects are enabled.
     * @return True if any of the Middle Objects are enabled, False otherwise.
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
     * Creates a message using generateMessage() and notifies observers.
     * @see #generateMessage()
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     * Returns a MSG_UPD_Game representing the current state of the Game.
     * @return A MSG_UPD_Game representing the current state of the Game.
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