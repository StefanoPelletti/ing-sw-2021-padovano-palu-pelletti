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
    private final boolean solo;

    public FaithTrackManager(Game game, GameManager gameManager)
    {
        this.faithTrack=game.getFaithTrack();
        this.gameManager = gameManager;
        this.game = game;
        solo= (gameManager.getLobbyMaxPlayers() == 1);
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
                message.append("\n Last turn begins!").append("\n Points have been awarded: ");
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
                if(solo)
                {
                    gameManager.setStatus(Status.GAME_OVER);
                }
                else {
                    gameManager.setStatus(Status.LAST_TURN);
                }
                break;
            case 0:
                faithTrack.advance(player);
                return true;
        }
        if(!(solo && gameManager.getStatus()==Status.GAME_OVER)) {
            notifyObservers(new MSG_NOTIFICATION(message.toString()));
        }
        return true;
    }


    public boolean advanceAllExcept(Player player)
    {
        if ( player == null ) return false;
        ArrayList<Player> players = game.getPlayerList();
        if ( players.stream().noneMatch( x -> x.getNickname().equals(player.getNickname()))) return false;

        if(!solo) {
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

        switch( faithTrack.doesActivateZone(game.getBlackCrossPosition()) )
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
                faithTrack.advanceLorenzo();

                    if ( faithTrack.calculateVP(p) > 0 )
                    {
                        p.addVP(faithTrack.calculateVP(p));
                        message.append("\n ").append(p.getNickname());
                        message.append("  ").append(faithTrack.calculateVP(p));
                    }
                faithTrack.setZones(2, true);
                    //////////////////////////////////////add something??? IT'S OVER!
                gameManager.setStatus(Status.GAME_OVER);
                return true;
            case 0:
                faithTrack.advanceLorenzo();
                break;
        }

        notifyObservers(new MSG_NOTIFICATION(message.toString()));
        return true;
    }
}
