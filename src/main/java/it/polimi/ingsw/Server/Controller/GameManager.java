package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Server.Model.Enumerators.Status;
import it.polimi.ingsw.Server.Model.*;

import java.util.*;

public class GameManager {

    private List<Message> broadcastList;
    private Game game;
    private FaithTrackManager faithTrackManager;
    private ActionManager actionManager;



    private int lobbyMaxPlayers;
    private boolean lastTurn;

    public GameManager(int lobbyMaxPlayers)
    {
        this.broadcastList = new LinkedList<>();
        this.game = new Game();
        this.faithTrackManager = new FaithTrackManager(this.game, this);
        this.actionManager = new ActionManager(this, this.faithTrackManager, this.game);
        this.lobbyMaxPlayers = lobbyMaxPlayers;
        this.lastTurn=false;
    }

    public Player currentPlayer() {
        return game.getCurrentPlayer();
    }

    public void setNextPlayer(){
        game.setCurrentPlayer(game.getCurrentPlayerInt()+1);
        if(game.getCurrentPlayerInt()>lobbyMaxPlayers){
            game.setCurrentPlayer(1);
        }
    }

    public void endTurn()
    {
        setNextPlayer();
        if(game.getStatus()!=Status.SOLO && lastTurn && game.getCurrentPlayerInt()==0) setStatus(Status.GAME_OVER);
    }

    public boolean addBroadcastMessage(Message message)
    {
        if ( message == null ) return false;
        broadcastList.add(message);
        return true;
    }

    public void setStatus(Status status)
    {
        game.changeStatus(status);
    }

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
}
