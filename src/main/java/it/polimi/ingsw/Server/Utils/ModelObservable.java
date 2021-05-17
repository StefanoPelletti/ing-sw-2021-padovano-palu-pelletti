package it.polimi.ingsw.Server.Utils;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Server.Utils.ModelObserver;

import java.util.*;

public abstract class ModelObservable {
    private ArrayList<ModelObserver> observers;

    public ModelObservable() {
        observers = new ArrayList<>();
    }

    public void addObserver(ModelObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ModelObserver observer) {
        observers.remove(observer);
    }

    public ArrayList<ModelObserver> getObservers() {
        return this.observers;
    }

    public void notifyObservers(Message message) {
        for (ModelObserver o : observers) {
            o.update(message);
        }
    }
}