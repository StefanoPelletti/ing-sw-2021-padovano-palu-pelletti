package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Model.Enumerators.Color;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.SplittableRandom;

public class DevelopmentCard implements Serializable {
    private final int level;
    private final Color color;
    private final Map<Resource, Integer> cost;
    private final int VP;
    private final Power power;

    public DevelopmentCard(int level, Color color, int VP, Map<Resource, Integer> cost, Power power) {
        this.level = level;
        this.color = color;
        this.cost = cost;
        this.VP = VP;
        this.power = power;
    }

    public Map<Resource, Integer> getCost() {
        return new HashMap<>(cost);
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

    public Map<Resource, Integer> getPowerInput() {
        return power.getInput();
    }

    public Map<Resource, Integer> getPowerOutput() {
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
        if(obj == this) return true;
        if(!(obj instanceof DevelopmentCard)) return false;
        DevelopmentCard o = (DevelopmentCard) obj;
        return(this.level == o.level &&
                this.color.equals(o.color) &&
                this.cost.equals(o.cost) &&
                this.VP==o.VP &&
                this.power.equals(o.power));

    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder("");
        result.append("   DEVELOPMENT CARD!   ").append("\n");
        result.append("    level : ").append(this.level).append("\n");
        result.append("    color : ").append(this.color).append("\n");
        result.append("    VP    : ").append(this.color).append("\n");
        result.append("    cost  : ").append(this.cost).append("\n");
        result.append(this.power).append("\n");
        return result.toString();
    }
}
