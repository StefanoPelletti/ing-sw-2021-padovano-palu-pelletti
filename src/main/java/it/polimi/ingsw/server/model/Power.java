package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enumerators.Resource;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class Power implements Serializable {
    private final Map<Resource, Integer> input;
    private final Map<Resource, Integer> output;

    public Power(Map<Resource, Integer> input, Map<Resource, Integer> output) {
        this.input = input;
        this.output = output;
    }

    public Map<Resource, Integer> getInput() {
        return input;
    }

    public Map<Resource, Integer> getOutput() {
        return output;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Power)) return false;
        Power o = (Power) obj;
        return (this.input.equals(o.input) && this.output.equals(o.output));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.input, this.output);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("   POWER: ").append("\n");
        result.append("    input: ").append(input).append("\n");
        result.append("    output: ").append(output.toString()).append("\n");
        return result.toString();
    }
}