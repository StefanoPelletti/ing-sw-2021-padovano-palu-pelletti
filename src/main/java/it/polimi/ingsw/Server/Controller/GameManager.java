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
    public GameManager()
    {
        broadcastList = new LinkedList<>();
        game = new Game();
        faithTrackManager = new FaithTrackManager(this.game, this);
        actionManager = new ActionManager(this, this.faithTrackManager, this.game);
    }

    public boolean addBroadcastMessage(Message message)
    {
        if ( message == null ) return false;
        broadcastList.add(message);
        return true;
    }

    public boolean INIT()
    {
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
}
