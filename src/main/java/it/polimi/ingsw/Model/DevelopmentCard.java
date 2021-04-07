package it.polimi.ingsw.Model;

import it.polimi.ingsw.Model.Enumerators.Color;
import it.polimi.ingsw.Model.Enumerators.Resource;

import java.util.HashMap;

public class DevelopmentCard {
    private final int level;
    private final Color color;
    private final HashMap<Resource, Integer> cost;
    private final int VP;
    private final Power power;

    public DevelopmentCard(int level, Color color, int VP, HashMap<Resource, Integer> cost, Power power) {
        this.level = level;
        this.color = color;
        this.cost = cost;
        this.VP = VP;
        this.power = power;
    }

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
        return power.getInput();
    }

    public HashMap<Resource, Integer> getPowerOutput() {
        return power.getOutput();
    }

    public boolean internalCheck()
    {
        if(level<1 || level>3) return false;
        if(color!=Color.BLUE && color!=Color.GREEN && color!=Color.YELLOW && color!=Color.PURPLE) return false;
        if(VP<1 || VP>12) return false;
        if(cost.isEmpty()) return false;
        if(power.getInput() == null ) return false;
        if(power.getOutput() == null ) return false;
        return true;
    }

    @Override
    public boolean equals( Object obj )
    {
        if(obj == this) return false;
        if(!(obj instanceof DevelopmentCard)) return false;
        DevelopmentCard o = (DevelopmentCard) obj;
        return(this.level == o.level &&
                this.color.equals(o.color) &&
                this.cost.equals(o.cost) &&
                this.VP==o.VP &&
                this.power.equals(o.power));

    }
}
