package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Networking.Message.MSG_ALERT;
import it.polimi.ingsw.Server.Model.Enumerators.Status;
import it.polimi.ingsw.Server.Model.FaithTrack;
import it.polimi.ingsw.Server.Model.Game;
import it.polimi.ingsw.Server.Model.Player;


import java.util.ArrayList;

public class FaithTrackManager {

    private final FaithTrack faithTrack;
    private final Game game;
    private final GameManager gameManager;

    public FaithTrackManager(Game game, GameManager gameManager)
    {
        this.faithTrack=game.getFaithTrack();
        this.gameManager = gameManager;
        this.game = game;
    }

    //??
    public void INIT()
    {

    }

    /*public FaithTrackInfo getFaithTrackStatus() {
        ArrayList<Player> players = game.getPlayerList();
        FaithTrackInfo result = new FaithTrackInfo();

        for (Player p : players)
            result.setPlayerNumberOffsetPosition(p.getPlayerNumber(), p.getPosition());
        result.setZones(faithTrack.getZones());
        return result;
    }*/

    public boolean advance(Player player)
    {
        return advance(player, false);
    }

    public boolean advance(Player player, boolean cumulative)
    {
        if ( player == null ) return false;
        ArrayList<Player> players = game.getPlayerList();
        if ( players.stream().noneMatch( x -> x.getNickname().equals(player.getNickname()))) return false;
        StringBuilder message;

        switch( faithTrack.doesActivateZone(player) )
        {
            case -1: return false;
            case 1:
                message = new StringBuilder();
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
                gameManager.addBroadcastMessage(new MSG_ALERT(message.toString()));
                return true;
            case 2:
                message = new StringBuilder();
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
                gameManager.addBroadcastMessage(new MSG_ALERT(message.toString()));
                return true;
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
                gameManager.addBroadcastMessage(new MSG_ALERT(message.toString()));
                gameManager.setStatus(Status.LAST_TURN);
                return true;
            case 0:
                faithTrack.advance(player);
                break;
        }

        if(!cumulative) {
            //notify();
        }

        return true;
    }


    public boolean advanceAllExcept(Player player)
    {
        if ( player == null ) return false;
        ArrayList<Player> players = game.getPlayerList();
        if ( players.stream().noneMatch( x -> x.getNickname().equals(player.getNickname()))) return false;

        for ( Player p : players )
        {
            if( !p.equals(player) )
            {
                advance(p, true);
            }
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
                gameManager.addBroadcastMessage(new MSG_ALERT(message.toString()));
                return true;
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
                gameManager.addBroadcastMessage(new MSG_ALERT(message.toString()));
                return true;
            case 3:
                faithTrack.advanceLorenzo();

                    if ( faithTrack.calculateVP(p) > 0 )
                    {
                        p.addVP(faithTrack.calculateVP(p));
                        message.append("\n ").append(p.getNickname());
                        message.append("  ").append(faithTrack.calculateVP(p));
                    }
                faithTrack.setZones(2, true);
                    //////////////////////////////////////add something???
                gameManager.setStatus(Status.GAME_OVER);
                return true;
            case 0:
                faithTrack.advanceLorenzo();
                break;
        }
        return true;
    }
}
