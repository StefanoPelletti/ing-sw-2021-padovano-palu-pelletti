package it.polimi.ingsw.Model;

import java.util.HashMap;

public class DevelopmentCard {
    private int level;
    private Color color;
    private HashMap<Resource, Integer> cost;
    private int VP;
    private Power power;

    public DevelopmentCard(int level, Color color, int VP, HashMap<Resource, Integer> cost, Power power) {
        this.level = level;
        this.color = color;
        this.cost = cost;
        this.VP = VP;
        this.power = power;
    }

    //vanno ovviamente cambiati tutti i return, che sono messi solo per non avere errori
    public HashMap<Resource, Integer> getCost() {
        return cost;
    }

    public Color getColor() {
        return color;
    }

    public int getLevel() {
        return level;
    }

    public int getVP() {
        return VP;
    }

    public HashMap<Resource, Integer> getPowerInput() {
        return cost;
    }

    public HashMap<Resource, Integer> getPowerOutput() {
        return cost;
    }
}
