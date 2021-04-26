package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Server.Model.Enumerators.Status;
import it.polimi.ingsw.Server.Model.*;
import it.polimi.ingsw.Server.Model.Middles.LeaderBoard;

import java.util.*;

public class GameManager {

    private Game game;
    private FaithTrackManager faithTrackManager;
    private ActionManager actionManager;
    private boolean solo;



    private Boolean soloWinner; // if null: no one, if true: the player, if false: the Lorenzo
    private int lobbyMaxPlayers;

    public GameManager(int lobbyMaxPlayers)
    {
        this.game = new Game();
        this.faithTrackManager = new FaithTrackManager(this.game, this);
        this.actionManager = new ActionManager(this, this.faithTrackManager, this.game);
        this.lobbyMaxPlayers = lobbyMaxPlayers;
        this.solo = lobbyMaxPlayers == 1;
        this.soloWinner = null;
    }

    public Player currentPlayer() {
        return game.getCurrentPlayer();
    }


    public boolean endTurn()
    {
        setNextPlayer();
        if(!solo && game.getStatus()==Status.LAST_TURN && game.getCurrentPlayerInt()==1){
            setStatus(Status.GAME_OVER);
            return endgame(); //which is return FALSE
        }
        return true;
    }

    public void setNextPlayer() {
        if (!solo) {
            game.setCurrentPlayer(game.getCurrentPlayerInt() + 1);
            if (game.getCurrentPlayerInt() > lobbyMaxPlayers) {
                game.setCurrentPlayer(1);
            }
        }
    }





    public void setStatus(Status status)
    {
        game.changeStatus(status);
    }
    public Boolean getSoloWinner() {
        return soloWinner;
    }
    public void setSoloWinner(boolean value)
    {
        this.soloWinner = value;
    }

    public Status getStatus(){return game.getStatus();}
    public boolean getSolo() { return solo; }
    public int getLobbyMaxPlayers() {
        return lobbyMaxPlayers;
    }
    public Game getGame()
    {
        return game;
    }
    public FaithTrackManager getFaithTrackManager() { return faithTrackManager; }
    public ActionManager getActionManager() { return actionManager; }
    public ArrayList<LeaderCard> pickFourLeaderCards() {

        return game.getLeaderCardsDeck().pickFourCards();
    }
    public void setLeaderCards(ArrayList<LeaderCard> cards, String nickname)
    {
        Player p = game.getPlayer(nickname);
        p.associateLeaderCards(cards);
        //notify();
    }

    public void resetErrorObject()
    {
        game.getErrorObject().setEnabled(false);
    }
    public void setErrorObject(String errorCause)
    {
        game.getErrorObject().setErrorMessage(errorCause);
        game.getErrorObject().setEnabled(true);
    }

    // if it is a NON-SOLO game, the leaderBoard will be: nickname-VP, nickname-VP (in ascending order)
    // if it is a SOLO game, the leaderBoard will contain the player-score and Lorenzo
    //       Lorenzo could be the loser (points will be 1)
    //       Lorenzo could be the winner (points will be 2)
    public boolean endgame(){
        LeaderBoard leaderBoard = game.getLeaderBoard();
        for(Player p : game.getPlayerList()){
            for(DevelopmentCard card : p.getDevelopmentSlot().getCards()){
                p.addVP(card.getVP());
            }
            for(LeaderCard leaderCard : p.getLeaderCards()){
                if(leaderCard != null) {
                    p.addVP(leaderCard.getVP());
                }
            }
            float totResources = p.getTotal();
            int points = (int) Math.floor(totResources/5);
            p.addVP(points);

            int faithPoints=0;
            int position = p.getPosition();
            if(position >= 3) faithPoints = 1;
            if(position >= 6) faithPoints = 2;
            if(position >= 9) faithPoints = 4;
            if(position >= 12) faithPoints = 6;
            if(position >= 15) faithPoints = 9;
            if(position >= 18) faithPoints = 12;
            if(position >= 21) faithPoints = 16;
            if(position == 24) faithPoints = 20;

            p.addVP(faithPoints);
            leaderBoard.addScore(p.getNickname(), p.getVP());
        }

        if(solo) {
            if(soloWinner) // if (Lorenzo has lost)
            {
                leaderBoard.addScore("Lorenzo, the Loser", 1 );
            }
            else
            {
                leaderBoard.addScore("Lorenzo, the Winner", 2);
            }
        }

        leaderBoard.setEnabled(true);
        return false;
    }
}
