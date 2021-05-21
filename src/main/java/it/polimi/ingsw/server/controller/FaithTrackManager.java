package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.FaithTrack;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.model.enumerators.Status;

import java.util.ArrayList;
import java.util.List;

public class FaithTrackManager {

    private final FaithTrack faithTrack;
    private final Game game;
    private final GameManager gameManager;

    public FaithTrackManager(Game game, GameManager gameManager) {
        this.faithTrack = game.getFaithTrack();
        this.gameManager = gameManager;
        this.game = game;

    }


    public boolean advance(Player p)
    {
        return advance(p, false);
    }

    public boolean advanceLorenzo()
    {
        return advance(null, true);
    }

    public boolean advance(Player player, boolean lorenzo)
    {
        Player p;
        List<Player> players;
        StringBuilder message = new StringBuilder();
        int result;

        if(lorenzo)
        {
            p = game.getFirstPlayer();
            result = faithTrack.doesActivateZone(game.getBlackCrossPosition() + 1);
            players = new ArrayList<>();
            players.add(p);
        }
        else
        {
            if (player == null) return false;
            players = game.getPlayerList();
            if (players.stream().noneMatch(x -> x.getNickname().equals(player.getNickname()))) return false;
            result = faithTrack.doesActivateZone(player);
        }

        switch(result)
        {
            case 1:
            case 2:
                if(lorenzo)
                {
                    if(result==1)
                        message.append("Lorenzo has activated the first zone! ");
                    else
                        message = new StringBuilder("Lorenzo has activated the second zone! ");
                    message.append("\n Points have been awarded: ");
                    faithTrack.advanceLorenzo();
                }
                else
                {
                    if(result==1)
                        message.append(player.getNickname()).append(" has activated the first zone! ");
                    else
                        message.append(player.getNickname()).append(" has activated the second zone! ");
                    message.append("\n Points have been awarded: ");
                    faithTrack.advance(player);
                }
                message.append(returnAddedPoints(players));
                faithTrack.setZones(result-1, true);
                break;
            case 3:
                if(lorenzo)
                {
                    message = new StringBuilder("Lorenzo has activated the last zone! ");
                    message.append("\n Points have been awarded, but Gameover!: ");
                    faithTrack.advanceLorenzo();
                    gameManager.setStatus(Status.GAME_OVER);
                    if (gameManager.getSoloWinner() == null)
                        gameManager.setSoloWinner(false);

                }
                else
                {
                    message.append(player.getNickname()).append(" has activated the third zone! ");
                    if (!gameManager.getSolo())
                        message.append("\n Points have been awarded: ");
                    else
                        message.append("\n It's Game Over. end the turn to finish him!").append("\n Points have been awarded: ");
                    faithTrack.advance(player);
                    if (gameManager.getSolo()) {
                        gameManager.setStatus(Status.GAME_OVER);
                        if (gameManager.getSoloWinner() == null)
                            gameManager.setSoloWinner(true);
                    } else {
                        gameManager.setStatus(Status.LAST_TURN);
                    }
                }
                message.append(returnAddedPoints(players));
                faithTrack.setZones(2, true);
                break;
            case 0:
                if(lorenzo)
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


    public boolean advanceAllExcept(Player player) {
        if (player == null) return false;
        List<Player> players = game.getPlayerList();
        if (players.stream().noneMatch(x -> x.getNickname().equals(player.getNickname()))) return false;

        if (!gameManager.getSolo()) {
            for (Player p : players) {
                if (!p.equals(player)) {
                    advance(p,false);
                }
            }
        } else {
            this.advance(null, true);
        }

        return true;
    }


    private String returnAddedPoints(List<Player> players)
    {
        StringBuilder message = new StringBuilder();
        for (Player p : players) {
            if (faithTrack.calculateVP(p) > 0) {
                p.addVP(faithTrack.calculateVP(p));
                message.append("\n ").append(p.getNickname());
                message.append("  ").append(faithTrack.calculateVP(p));
            }
        }
        return message.toString();
    }
}