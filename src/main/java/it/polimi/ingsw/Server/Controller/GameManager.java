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

    public void setNextPlayer() {
        if (game.getStatus() == Status.SOLO) {
            ////////////////////////////// QUA QUALCOSA TOMMASO SCRIVI PERFAVORE DAI SU GIACOMO, GIOVANNI, ALDO.
        } else
            game.setCurrentPlayer(game.getCurrentPlayerInt() + 1);
        if (game.getCurrentPlayerInt() > lobbyMaxPlayers) {
            game.setCurrentPlayer(1);
        }
    }

    public void setLeaderCards(){

    }

    public void endTurn()
    {
        setNextPlayer();
        if(game.getStatus()!=Status.SOLO && lastTurn && game.getCurrentPlayerInt()==0){
            setStatus(Status.GAME_OVER);
            endgame();
        }
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

    public Status getStatus(){return game.getStatus();}

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

    public void endgame(){
        for(Player p : game.getPlayerList()){
            for(DevelopmentCard card : p.getDevelopmentSlot().getCards()){
                p.addVP(card.getVP());
            }
            for(LeaderCard leaderCard : p.getLeaderCards()){
                p.addVP(leaderCard.getVP());
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
        }
    }
}
