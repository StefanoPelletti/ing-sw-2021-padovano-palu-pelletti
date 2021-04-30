package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Networking.Message.MSG_NOTIFICATION;
import it.polimi.ingsw.Server.Model.Enumerators.Status;
import it.polimi.ingsw.Server.Model.FaithTrack;
import it.polimi.ingsw.Server.Model.Game;
import it.polimi.ingsw.Server.Model.Player;
import it.polimi.ingsw.Server.Utils.ModelObservable;


import java.util.ArrayList;

public class FaithTrackManager extends ModelObservable {

    private final FaithTrack faithTrack;
    private final Game game;
    private final GameManager gameManager;

    public FaithTrackManager(Game game, GameManager gameManager)
    {
        this.faithTrack=game.getFaithTrack();
        this.gameManager = gameManager;
        this.game = game;

    }


    public boolean advance(Player player)
    {
        if ( player == null ) return false;
        ArrayList<Player> players = game.getPlayerList();
        if ( players.stream().noneMatch( x -> x.getNickname().equals(player.getNickname()))) return false;
        StringBuilder message = new StringBuilder();

        switch( faithTrack.doesActivateZone(player) )
        {
            case -1: return false;
            case 1:
                message.append( player.getNickname()).append(" has activated the first zone! ");
                message.append("\n Points have been awarded: ");
                faithTrack.advance(player);

                for ( Player p : players )
                {
                    if ( faithTrack.calculateVP(p) > 0 )
                    {
                        p.addVP(faithTrack.calculateVP(p));
                        message.append("\n ").append(p.getNickname());
                        message.append("  ").append(faithTrack.calculateVP(p));
                    }
                }
                faithTrack.setZones(0, true);
                break;
            case 2:
                message.append( player.getNickname()).append(" has activated the second zone! ");
                message.append("\n Points have been awarded: ");
                faithTrack.advance(player);

                for ( Player p : players )
                {
                    if ( faithTrack.calculateVP(p) > 0 )
                    {
                        p.addVP(faithTrack.calculateVP(p));
                        message.append("\n ").append(p.getNickname());
                        message.append("  ").append(faithTrack.calculateVP(p));
                    }
                }
                faithTrack.setZones(1, true);
                break;
            case 3:
                message = new StringBuilder();

                message.append( player.getNickname()).append(" has activated the third zone! ");
                if(!gameManager.getSolo())
                    message.append("\n Points have been awarded: ");
                else
                    message.append("\n It's Game Over. end the turn to finish him!").append("\n Points have been awarded: ");
                faithTrack.advance(player);

                for ( Player p : players )
                {
                    if ( faithTrack.calculateVP(p) > 0 )
                    {
                        p.addVP(faithTrack.calculateVP(p));
                        message.append("\n ").append(p.getNickname());
                        message.append("  ").append(faithTrack.calculateVP(p));
                    }
                }
                faithTrack.setZones(2, true);
                if(gameManager.getSolo()) {
                    gameManager.setStatus(Status.GAME_OVER);
                    if(gameManager.getSoloWinner()==null)
                        gameManager.setSoloWinner(true);
                }
                else {
                    gameManager.setStatus(Status.LAST_TURN);
                }
                break;
            case 0:
                faithTrack.advance(player);
                return true;
        }

        notifyObservers(new MSG_NOTIFICATION(message.toString()));
        return true;
    }


    public boolean advanceAllExcept(Player player)
    {
        if ( player == null ) return false;
        ArrayList<Player> players = game.getPlayerList();
        if ( players.stream().noneMatch( x -> x.getNickname().equals(player.getNickname()))) return false;

        if(!gameManager.getSolo()) {
            for (Player p : players) {
                if (!p.equals(player)) {
                    advance(p);
                }
            }
        }
        else
        {
            this.advanceLorenzo();
        }

        return true;
    }

    public boolean advanceLorenzo()
    {
        Player p = game.getFirstPlayer();
        StringBuilder message = new StringBuilder();

        switch( faithTrack.doesActivateZone(game.getBlackCrossPosition()+1) )
        {
            case -1: return false;
            case 1:
                message = new StringBuilder("Lorenzo has activated the first zone! ");
                message.append("\n Points have been awarded: ");
                faithTrack.advanceLorenzo();

                    if ( faithTrack.calculateVP(p) > 0 ) {
                        p.addVP(faithTrack.calculateVP(p));
                        message.append("\n ").append(p.getNickname());
                        message.append("  ").append(faithTrack.calculateVP(p));
                    }
                faithTrack.setZones(0, true);
                    break;
            case 2:
                message = new StringBuilder("Lorenzo has activated the second zone! ");
                message.append("\n Points have been awarded: ");
                faithTrack.advanceLorenzo();

                    if ( faithTrack.calculateVP(p) > 0 )
                    {
                        p.addVP(faithTrack.calculateVP(p));
                        message.append("\n ").append(p.getNickname());
                        message.append("  ").append(faithTrack.calculateVP(p));
                    }
                faithTrack.setZones(1, true);
                break;
            case 3:
                message = new StringBuilder("Lorenzo has activated the last zone! ");
                message.append("\n Points have been awarded, but Gameover!: ");
                faithTrack.advanceLorenzo();

                    if ( faithTrack.calculateVP(p) > 0 )
                    {
                        p.addVP(faithTrack.calculateVP(p));
                        message.append("\n ").append(p.getNickname());
                        message.append("  ").append(faithTrack.calculateVP(p));
                    }
                faithTrack.setZones(2, true);

                gameManager.setStatus(Status.GAME_OVER);
                if(gameManager.getSoloWinner()==null)
                    gameManager.setSoloWinner(false);

                break;
            case 0:
                faithTrack.advanceLorenzo();
                return true;
        }


        notifyObservers(new MSG_NOTIFICATION(message.toString()));
        return true;
    }
}
