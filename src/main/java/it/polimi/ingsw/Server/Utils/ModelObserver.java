package it.polimi.ingsw.Server.Utils;

import it.polimi.ingsw.Networking.Message.*;

public interface ModelObserver {
   void update(Message message);
}