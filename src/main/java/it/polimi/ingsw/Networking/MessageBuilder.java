package it.polimi.ingsw.Networking;

import it.polimi.ingsw.Networking.Message.MSG_UPDATE_MODEL;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Model.Game;
import it.polimi.ingsw.Server.Model.Strongbox;
import it.polimi.ingsw.Server.Utils.ModelObserver;

public class MessageBuilder implements ModelObserver {
    MSG_UPDATE_MODEL msg;
    Game game;

    public MessageType(Game game)
    {
        this.msg = null;
        this.game = game;
    }

    public void updateMarket()
    {
        if(msg==null) msg = new MSG_UPDATE_MODEL();
        msg.setMarket...
    }

    public void updateStrongbox(Strongbox o)
    {
        if(msg==null) msg = new MSG_UPDATE_MODEL();
        for (Player p : game.getPlayerList())
        {
            if (p.getStrong() == o )
            {
                msg.setPlayerStrongbox();

                break;
            }
        }
    }
}
