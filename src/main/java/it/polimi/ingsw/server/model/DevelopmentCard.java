package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enumerators.Color;
import it.polimi.ingsw.server.model.enumerators.Resource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DevelopmentCard implements Serializable {
    private final int level;
    private final Color color;
    private final Map<Resource, Integer> cost;
    private final int vp;
    private final Power power;

    public DevelopmentCard(int level, Color color, int vp, Map<Resource, Integer> cost, Power power) {
        this.level = level;
        this.color = color;
        this.cost = cost;
        this.vp = vp;
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

    public int getVp() {
        return vp;
    }

    public Map<Resource, Integer> getPowerInput() {
        return power.getInput();
    }

    public Map<Resource, Integer> getPowerOutput() {
        return power.getOutput();
    }

    public boolean internalCheck() {
        if (level < 1 || level > 3) return false;
        if (color != Color.BLUE && color != Color.GREEN && color != Color.YELLOW && color != Color.PURPLE) return false;
        if (vp < 1 || vp > 12) return false;
        if (cost.isEmpty()) return false;
        if (power.getInput() == null) return false;
        return power.getOutput() != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof DevelopmentCard)) return false;
        DevelopmentCard o = (DevelopmentCard) obj;
        return (this.level == o.level &&
                this.color.equals(o.color) &&
                this.cost.equals(o.cost) &&
                this.vp == o.vp &&
                this.power.equals(o.power));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.level, this.color, this.cost, this.vp, this.power);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("   DEVELOPMENT CARD!   ").append("\n");
        result.append("    level : ").append(this.level).append("\n");
        result.append("    color : ").append(this.color).append("\n");
        result.append("    VP    : ").append(this.vp).append("\n");
        result.append("    cost  : ").append(this.cost).append("\n");
        result.append(this.power).append("\n");
        return result.toString();
    }
}