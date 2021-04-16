package it.polimi.ingsw.Server.Controller;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Server.Model.Enumerators.Status;
import it.polimi.ingsw.Server.Model.Game;

import java.util.*;

public class GameManager {

    private List<Message> broadcastList;
    private Game game;
    private FaithTrackManager faithTrackManager;
    private ActionManager actionManager;
    private int lobbyMaxPlayers;
    private int currentPlayer;

    public GameManager(int lobbyMaxPlayers)
    {
        this.broadcastList = new LinkedList<>();
        this.game = new Game();
        this.faithTrackManager = new FaithTrackManager(this.game, this);
        this.actionManager = new ActionManager(this, this.faithTrackManager, this.game);
        this.lobbyMaxPlayers = lobbyMaxPlayers;
        this.currentPlayer = 1;
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
    }
}
