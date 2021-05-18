package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Networking.Message.UpdateMessages.*;
import it.polimi.ingsw.Server.Model.Enumerators.Status;
import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.Model.Middles.LeaderBoard;
import it.polimi.ingsw.Server.Model.SpecialAbilities.ExtraDepot;
import it.polimi.ingsw.Server.Utils.ModelObserver;

import java.util.*;

public class GameManager {

    private final Game game;
    private final FaithTrackManager faithTrackManager;
    private final ActionManager actionManager;
    private final boolean solo;
    private final ArrayList<Integer> idlePlayers;
    private final int lobbyMaxPlayers;

    private Boolean soloWinner; // if null: no one, if true: the player, if false: the Lorenzo


    public GameManager(int lobbyMaxPlayers) {
        this.game = new Game();
        this.faithTrackManager = new FaithTrackManager(this.game, this);
        this.actionManager = new ActionManager(this, this.faithTrackManager, this.game);
        this.lobbyMaxPlayers = lobbyMaxPlayers;
        this.solo = (lobbyMaxPlayers == 1);
        this.soloWinner = null;
        this.idlePlayers = new ArrayList<>();
    }

    public Player currentPlayer() {
        return game.getCurrentPlayer();
    }


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

    public void setNextPlayer() {
        if (solo)
            game.setTurn(game.getTurn() + 1);
        else {
            int nextPlayer = getNextPlayer(game.getCurrentPlayerInt() + 1);
            game.setCurrentPlayer(nextPlayer);
        }
    }

    private int getNextPlayer(int playerNumber) {
        if (playerNumber > lobbyMaxPlayers) {
            if(game.getStatus()==Status.INIT_1) game.changeStatus(Status.INIT_2);
            else if(game.getStatus()==Status.INIT_2) game.changeStatus(Status.STANDARD_TURN);
            game.setTurn(game.getTurn() + 1);
            return getNextPlayer(1);
        }
        if (idlePlayers.stream().anyMatch(x -> x == playerNumber)) {
            return getNextPlayer(playerNumber + 1);
        }
        return playerNumber;
    }

    public void addIdlePlayer(Integer playerNumber) {
        this.idlePlayers.add(playerNumber);
    }

    public void removeIdlePlayer(Integer playerNumber) {
        this.idlePlayers.remove(playerNumber);
    }

    public boolean areAllPlayersIdle() {
        return (lobbyMaxPlayers == idlePlayers.size());
    }

    public void setStatus(Status status) {
        game.changeStatus(status);
    }

    public Boolean getSoloWinner() {
        return soloWinner;
    }

    public void setSoloWinner(boolean value) {
        this.soloWinner = value;
    }

    public Status getStatus() {
        return game.getStatus();
    }

    public boolean getSolo() {
        return solo;
    }

    public int getLobbyMaxPlayers() {
        return lobbyMaxPlayers;
    }

    public Game getGame() {
        return game;
    }

    public FaithTrackManager getFaithTrackManager() {
        return faithTrackManager;
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public void resetErrorObject() {
        game.getErrorObject().setEnabled(false);
    }

    public void setErrorObject(String errorCause) {
        game.getErrorObject().setErrorMessage(errorCause);
        game.getErrorObject().setEnabled(true);
    }

    // if it is a NON-SOLO game, the leaderBoard will be: nickname-VP, nickname-VP (in ascending order)
    // if it is a SOLO game, the leaderBoard will contain the player-score and Lorenzo
    //       Lorenzo could be the loser (points will be 1)
    //       Lorenzo could be the winner (points will be 2)
    public boolean endgame() {
        LeaderBoard leaderBoard = game.getLeaderBoard();
        for (Player p : game.getPlayerList()) {
            int points = p.getVP();

            for (DevelopmentCard card : p.getDevelopmentSlot().getCards()) {
                points += card.getVP();
            }
            for (LeaderCard leaderCard : p.getLeaderCards()) {
                if (leaderCard != null) {
                    points += leaderCard.getVP();
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

    public MSG_UPD_Full getFullModel() {
        MSG_UPD_Full result = new MSG_UPD_Full();

        result.setDevCardsVendor(game.getDevelopmentCardsVendor().generateMessage());

        result.setLeaderCardsObject(game.getLeaderCardsObject().generateMessage());

        result.setMarketHelper(game.getMarketHelper().generateMessage());

        result.setResourceObject(game.getResourceObject().generateMessage());

        result.setGame(game.generateMessage());

        result.setDevDeck(game.getDevelopmentCardsDeck().generateMessage());

        result.setFaithTrack(game.getFaithTrack().generateMessage());

        result.setMarket(game.getMarket().generateMessage());

        for (Player p : game.getPlayerList()) {
            result.addPlayerUpdate(p.getPlayerNumber(), p.generateMessage());

            result.addPlayerUpdate(p.getPlayerNumber(), p.getWarehouseDepot().generateMessage());

            result.addPlayerUpdate(p.getPlayerNumber(), p.getDevelopmentSlot().generateMessage());

            result.addPlayerUpdate(p.getPlayerNumber(), p.getStrongbox().generateMessage());

            if (p.getCardsWithExtraDepotAbility().size() > 0) {
                for (LeaderCard card : p.getCardsWithExtraDepotAbility()) {
                    ExtraDepot depot = (ExtraDepot) card.getSpecialAbility();
                    result.addPlayerUpdate(p.getPlayerNumber(), depot.generateMessage());
                }
            }
        }

        return result;
    }

    public void addAllObserver(ModelObserver observer) {
        game.addAllObservers(observer);
    }

    public boolean isGameOver() {
        return (getStatus() == Status.GAME_OVER);
    }
}