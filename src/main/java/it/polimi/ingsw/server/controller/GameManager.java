package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_Full;
import it.polimi.ingsw.server.model.DevelopmentCard;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enumerators.Status;
import it.polimi.ingsw.server.model.middles.Leaderboard;
import it.polimi.ingsw.server.model.specialAbilities.ExtraDepot;
import it.polimi.ingsw.server.utils.ModelObserver;

import java.util.ArrayList;
import java.util.List;

public class GameManager {

    private final Game game;
    private final FaithTrackManager faithTrackManager;
    private final ActionManager actionManager;
    private final boolean solo;
    private final List<Integer> idlePlayers;
    private final int lobbyMaxPlayers;

    private Boolean soloWinner;

    /**
     * Constructor of the GameManager Controller object.
     * Instantiating this objects consequentially instantiate the entire Controller and the entire Model.
     * The GameManager is responsible for managing the turns, the currentPlayer, the lobbySize, and the Status,
     * even thou some fields are modified by the ActionManager actions.
     * NOTE: automatically detects if the Lobby is going to be a Solo lobby.
     * NOTE: the Players must be added afterwards, as well as the Observer linkage.
     *
     * @param lobbyMaxPlayers the capacity of the Game this GameManager is managing.
     */
    public GameManager(int lobbyMaxPlayers) {
        this.game = new Game();
        this.faithTrackManager = new FaithTrackManager(this.game, this);
        this.actionManager = new ActionManager(this, this.faithTrackManager, this.game);
        this.lobbyMaxPlayers = lobbyMaxPlayers;
        this.solo = (lobbyMaxPlayers == 1);
        this.soloWinner = null;
        this.idlePlayers = new ArrayList<>();
        this.game.changeStatus(Status.INIT_1);
    }

    /**
     * @return the currentPlayer reference
     */
    public Player currentPlayer() {
        return game.getCurrentPlayer();
    }

    /**
     * Advances the Game flow by setting the nextPlayer or by ending the game.
     *
     * @return true if the Game has not ended, false otherwise
     */
    public boolean endTurn() {
        setNextPlayer();

        int firstAvailablePlayer = 1;
        for (int i = 1; i <= lobbyMaxPlayers; i++) {
            if (idlePlayers.contains(i))
                firstAvailablePlayer = i + 1;
            else
                break;
        }

        if (!solo && game.getStatus() == Status.LAST_TURN && game.getCurrentPlayerInt() == firstAvailablePlayer) {
            setStatus(Status.GAME_OVER);
            return endgame(); //which is return FALSE
        }

        return true;
    }

    /**
     * Sets the next Player and advances the turn, if a cycle has been made.
     */
    public void setNextPlayer() {
        if (solo)
            game.setTurn(game.getTurn() + 1);
        else {
            int nextPlayer = getNextPlayer(game.getCurrentPlayerInt() + 1);
            game.setCurrentPlayer(nextPlayer);
        }
    }

    /**
     * Chooses the next Player based on the Connected Player.
     * If a player is present in the IdlePlayer list, it is skipped.
     */
    private int getNextPlayer(int playerNumber) {
        if (playerNumber > lobbyMaxPlayers) {
            if (game.getStatus() == Status.INIT_1) game.changeStatus(Status.INIT_2);
            else if (game.getStatus() == Status.INIT_2) game.changeStatus(Status.STANDARD_TURN);
            game.setTurn(game.getTurn() + 1);
            return getNextPlayer(1);
        }
        if (idlePlayers.stream().anyMatch(x -> x == playerNumber)) {
            return getNextPlayer(playerNumber + 1);
        }
        return playerNumber;
    }

    /**
     * Used only in Solo mode
     *
     * @return <ul>
     * <li> null if there's no Solo mode winner yet,
     * <li> true if the Player won,
     * <li> false if Lorenzo won
     */
    public Boolean getSoloWinner() {
        return soloWinner;
    }

    /**
     * Used only in Solo mode.
     * sets the corresponding value of the Solo Winner
     * see getSoloWinner() above
     */
    public void setSoloWinner(boolean value) {
        this.soloWinner = value;
    }

    /**
     * @return the current Status of the Game
     */
    public Status getStatus() {
        return game.getStatus();
    }

    /**
     * Changes the current Status of the Game.
     *
     * @param status the new Status
     */
    public void setStatus(Status status) {
        game.changeStatus(status);
    }

    /**
     * @return true if Status is GAME_OVER, false otherwise
     */
    public boolean isGameOver() {
        return (getStatus() == Status.GAME_OVER);
    }

    /**
     * @return true if it is a Solo mode, false otherwise
     */
    public boolean getSolo() {
        return solo;
    }

    /**
     * @return the reference to the Game handled by this GameManager
     */
    public Game getGame() {
        return game;
    }

    /**
     * @return the reference to the FaithTrackManager linked with this GameManager
     */
    public FaithTrackManager getFaithTrackManager() {
        return faithTrackManager;
    }

    /**
     * @return the reference to the ActionManager linked with this GameManager
     */
    public ActionManager getActionManager() {
        return actionManager;
    }

    /**
     * Broadcasts an Error Message using the model.middles.ErrorObject.
     * Its usage is best described in the Documentation file, Actions.
     *
     * @param errorCause The cause of the error as notified by the ActionManager.
     */
    public void setErrorObject(String errorCause) {
        game.getErrorObject().setErrorMessage(errorCause);
        System.out.println(errorCause);
        game.getErrorObject().setEnabled(true);
    }

    // if it is a NON-SOLO game, the leaderBoard will be: nickname-VP, nickname-VP (in ascending order)
    // if it is a SOLO game, the leaderBoard will contain the player-score and Lorenzo
    //       Lorenzo could be the loser (points will be 1)
    //       Lorenzo could be the winner (points will be 2)

    /**
     * Calculates the scores of the Players at the end of the game,
     * loading the model.middles.LeaderBoard object with that data.
     * If it a Solo game, the leaderBoard will contain the Player-score and Lorenzo:
     * Lorenzo could be the loser (his score will be 1) or
     * Lorenzo could be the winner (his score will be 2)
     *
     * @return false, always
     */
    public boolean endgame() {
        Leaderboard leaderBoard = game.getLeaderBoard();
        for (Player p : game.getPlayerList()) {
            int points = 0;

            for (int i = 0; i < p.getFaithTrackPanels().length; i++) {
                if (p.getFaithTrackPanels()[0]) points += 2;
                if (p.getFaithTrackPanels()[1]) points += 3;
                if (p.getFaithTrackPanels()[2]) points += 4;
            }

            for (DevelopmentCard card : p.getDevelopmentSlot().getCards()) {
                points += card.getVp();
            }
            for (LeaderCard leaderCard : p.getLeaderCards()) {
                if (leaderCard != null && leaderCard.isEnabled()) {
                    points += leaderCard.getVp();
                }
            }
            float totResources = p.getTotal();
            points += (int) Math.floor(totResources / 5);


            int faithPoints = 0;
            int position = p.getPosition();
            if (position >= 3) faithPoints = 1;
            if (position >= 6) faithPoints = 2;
            if (position >= 9) faithPoints = 4;
            if (position >= 12) faithPoints = 6;
            if (position >= 15) faithPoints = 9;
            if (position >= 18) faithPoints = 12;
            if (position >= 21) faithPoints = 16;
            if (position == 24) faithPoints = 20;

            points += faithPoints;
            leaderBoard.addScore(p.getNickname(), points);
        }

        if (solo) {
            if (soloWinner) // if (Lorenzo has lost)
            {
                leaderBoard.addScore("Lorenzo", 1);
            } else {
                leaderBoard.addScore("Lorenzo", 2);
            }
        }

        leaderBoard.setEnabled(true);
        return false;
    }


    /**
     * @param observer the Observer that is being linked to all model objects
     * @see #addAllObserver(ModelObserver)
     */
    public void addAllObserver(ModelObserver observer) {
        game.addAllObservers(observer);
    }

    /**
     * Resets the model.middles.ErrorObject from its error-state.
     * It is always called before doing an action.
     */
    public void resetErrorObject() {
        game.getErrorObject().setEnabled(false);
    }

    //used in lobby or for disconnection

    /**
     * Adds a player, specified by his playerNumber, to the IdlePlayers list.
     * See Documentation file, DisconnectionReconnection, for better understanding.
     * The players present in this list will be skipped when choosing the next player.
     *
     * @param playerNumber The number of the Player who's being disconnected.
     */
    public void addIdlePlayer(Integer playerNumber) {
        this.idlePlayers.add(playerNumber);
    }

    /**
     * Removes a player, specified by his playerNumber, from the IdlePlayers list.
     * see Documentation file, DisconnectionReconnection, for better understanding.
     * Doing so will allow the specified player to be selected while choosing the next player.
     *
     * @param playerNumber The number of the Player who's being reconnected.
     */
    public void removeIdlePlayer(Integer playerNumber) {
        this.idlePlayers.remove(playerNumber);
    }

    /**
     * @return true if all Players are Idle, false if at least one Player is still connected
     */
    public boolean areAllPlayersIdle() {
        return (lobbyMaxPlayers == idlePlayers.size());
    }

    /**
     * generates a MSG_UPD_Full message containing messages from every possible model objects.
     * It is used to update the Client Model to the latest status.
     * Works by calling every model object generateMessage() method.
     *
     * @return The MSG_UPD_Full message.
     * @see it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_Full
     */
    public MSG_UPD_Full getFullModel() {
        MSG_UPD_Full result = new MSG_UPD_Full();

        result.setDevCardsVendor(game.getDevelopmentCardsVendor().generateMessage());

        result.setLeaderCardsPicker(game.getLeaderCardsPicker().generateMessage());

        result.setMarketHelper(game.getMarketHelper().generateMessage());

        result.setResourcePicker(game.getResourcePicker().generateMessage());

        result.setGame(game.generateMessage());

        result.setDevDeck(game.getDevelopmentCardsDeck().generateMessage());

        result.setFaithTrack(game.getFaithTrack().generateMessage());

        result.setMarket(game.getMarket().generateMessage());

        for (Player p : game.getPlayerList()) {
            result.addPlayerUpdate(p.getPlayerNumber(), p.generateMessage());

            result.addPlayerUpdate(p.getPlayerNumber(), p.getWarehouseDepot().generateMessage());

            result.addPlayerUpdate(p.getPlayerNumber(), p.getDevelopmentSlot().generateMessage());

            result.addPlayerUpdate(p.getPlayerNumber(), p.getStrongbox().generateMessage());

            if (!p.getCardsWithExtraDepotAbility().isEmpty()) {
                for (LeaderCard card : p.getCardsWithExtraDepotAbility()) {
                    ExtraDepot depot = (ExtraDepot) card.getSpecialAbility();
                    result.addPlayerUpdate(p.getPlayerNumber(), depot.generateMessage());
                }
            }
        }

        return result;
    }
}