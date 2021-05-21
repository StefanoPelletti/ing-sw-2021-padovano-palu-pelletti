package it.polimi.ingsw.server.utils;

import it.polimi.ingsw.networking.message.Message;

public interface ModelObserver {
    void update(Message message);
}