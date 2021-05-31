package it.polimi.ingsw.server.utils;

import it.polimi.ingsw.networking.message.Message;

import java.util.ArrayList;
import java.util.List;

public abstract class ModelObservable {
    private final List<ModelObserver> observers;

    public ModelObservable() {
        observers = new ArrayList<>();
    }

    public void addObserver(ModelObserver observer) {
        observers.add(observer);
    }

    public List<ModelObserver> getObservers() {
        return this.observers;
    }

    public void notifyObservers(Message message) {
        for (ModelObserver o : observers) {
            o.update(message);
        }
    }
}