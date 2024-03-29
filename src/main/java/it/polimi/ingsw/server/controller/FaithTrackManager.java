package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.FaithTrack;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enumerators.Status;

import java.util.ArrayList;
import java.util.List;

public class FaithTrackManager {

    /**
     * the reference to the faithTrack in the model
     */
    private final FaithTrack faithTrack;
    /**
     * the reference to the model entry point
     */
    private final Game game;
    /**
     * the reference to the GameManager
     */
    private final GameManager gameManager;

    /**
     * Constructs a FaithTrackManager which operates on the Player positions and the FaithTrack.
     *
     * @param game        A Game reference, extracted from the GameManager.
     * @param gameManager The GameManager reference.
     */
    public FaithTrackManager(Game game, GameManager gameManager) {
        this.faithTrack = game.getFaithTrack();
        this.gameManager = gameManager;
        this.game = game;
    }

    /**
     * Advances a specified Player on the FaithTrack.
     *
     * @param p The reference to the player.
     * @return The return value of advance() method.
     * @see #advance(Player, boolean)  the directly called method
     */
    public boolean advance(Player p) {
        return advance(p, false);
    }

    /**
     * Advances Lorenzo on the FaithTrack.
     *
     * @return The return value of advance() method.
     * @see #advance(Player, boolean) the directly called method
     */
    public boolean advanceLorenzo() {
        return advance(null, true);
    }

    /**
     * Advances a Player (or Lorenzo) on the FaithTrack.
     * The methods assigns points based on the zones activated as specified by the Game's rules.
     *
     * @param player  The reference to the Player advancing. Null if the parameter lorenzo is set to True.
     * @param lorenzo True if Lorenzo is moving, False otherwise.
     * @return True if the operation terminated correctly, False if: <ul>
     * <li> lorenzo is false and (the player reference is null or the player does not exist in this game)
     * <li> the referenced player/Lorenzo position returns an error value (-1).
     * @see it.polimi.ingsw.server.model.FaithTrack the FaithTrack
     */
    public boolean advance(Player player, boolean lorenzo) {
        Player p;
        List<Player> players;
        StringBuilder message = new StringBuilder();
        int result;

        if (lorenzo) {
            p = game.getFirstPlayer();
            result = faithTrack.doesActivateZone(game.getBlackCrossPosition() + 1);
            players = new ArrayList<>();
            players.add(p);
        } else {
            if (player == null) return false;
            players = game.getPlayerList();
            if (players.stream().noneMatch(x -> x.getNickname().equals(player.getNickname()))) return false;
            result = faithTrack.doesActivateZone(player);
        }

        switch (result) {
            case 1: //first zone being activated
            case 2: //second zone being activated
                if (lorenzo) {
                    if (result == 1)
                        message.append("Lorenzo has activated the first zone! ");
                    else
                        message = new StringBuilder("Lorenzo has activated the second zone! ");
                    message.append("\n FaithTrack Panels have been flipped ");
                    faithTrack.advanceLorenzo();
                } else {
                    if (result == 1)
                        message.append(player.getNickname()).append(" has activated the first zone! ");
                    else
                        message.append(player.getNickname()).append(" has activated the second zone! ");
                    message.append("\n FaithTrack Panels have been flipped ");
                    faithTrack.advance(player);
                }
                message.append(returnFlippedZones(players, result - 1));
                faithTrack.setZones(result - 1, true);
                break;
            case 3:
                if (lorenzo) {
                    message = new StringBuilder("Lorenzo has activated the last zone! ");
                    message.append("\n FaithTrack Panels have been flipped, but Gameover! ");
                    faithTrack.advanceLorenzo();
                    gameManager.setStatus(Status.GAME_OVER);
                    if (gameManager.getSoloWinner() == null)
                        gameManager.setSoloWinner(false);

                } else {
                    message.append(player.getNickname()).append(" has activated the third zone! ");
                    if (!gameManager.getSolo())
                        message.append("\n FaithTrack Panels have been flipped ");
                    else
                        message.append("\n It's Game Over. end the turn to finish him!").append("\n FaithTrack Panels have been flipped ");
                    faithTrack.advance(player);
                    if (gameManager.getSolo()) {
                        gameManager.setStatus(Status.GAME_OVER);
                        if (gameManager.getSoloWinner() == null)
                            gameManager.setSoloWinner(true);
                    } else {
                        gameManager.setStatus(Status.LAST_TURN);
                    }
                }
                message.append(returnFlippedZones(players, 2));
                faithTrack.setZones(2, true);
                break;
            case 0:
                if (lorenzo)
                    faithTrack.advanceLorenzo();
                else
                    faithTrack.advance(player);
                return true;
            default:
                return false;
        }

        game.broadcastMessage(message.toString());
        return true;
    }

    /**
     * Advances all players except the specified one.
     * In Solo mode advances Lorenzo.
     * It is used when a player discards a resource in the market action.
     *
     * @param player The Player reference which stays still.
     * @return True if the operation terminated correctly, False if the player reference is false or it does not exist in the game.
     */
    public boolean advanceAllExcept(Player player) {
        if (player == null) return false;
        List<Player> players = game.getPlayerList();
        if (players.stream().noneMatch(x -> x.getNickname().equals(player.getNickname()))) return false;

        if (!gameManager.getSolo()) {
            for (Player p : players) {
                if (!p.equals(player)) {
                    advance(p, false);
                }
            }
        } else {
            this.advance(null, true);
        }

        return true;
    }

    /**
     * Generates a String containing the faithTrackPanel flipped to the Players for activating a Zone.
     *
     * @param players The list of Players playing the Game.
     * @param zone    the zone being activated
     * @return A List of points awarded to the corresponding players.
     * @see it.polimi.ingsw.server.model.FaithTrack for better understanding.
     */
    private String returnFlippedZones(List<Player> players, int zone) {
        StringBuilder message = new StringBuilder();
        for (Player p : players) {
            if (faithTrack.calculateVP(p) > 0) {
                p.setFaithTrackPanels(zone);
                message.append("\n ").append(p.getNickname());
                message.append(" zone ").append(zone + 1);
                message.append(" - ").append(faithTrack.calculateVP(p)).append(" points ");
            }
        }
        return message.toString();
    }
}