package it.polimi.ingsw.server.utils;

import it.polimi.ingsw.networking.message.Message;

import java.util.ArrayList;
import java.util.List;

public abstract class ModelObservable {
    private final ArrayList<ModelObserver> observers;

    public ModelObservable() {
        observers = new ArrayList<>();
    }

    public void addObserver(ModelObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ModelObserver observer) {
        observers.remove(observer);
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