package it.polimi.ingsw.Model;

import java.util.HashMap;

public class Power {
    private HashMap<Resource, Integer> input;
    private HashMap<Resource, Integer> output;

    public Power(HashMap<Resource, Integer> input, HashMap<Resource, Integer> output) {
        this.input = input;
        this.output = output;
    }

    public HashMap<Resource, Integer> getInput() {
        return input;
    }

    public HashMap<Resource, Integer> getOutput() {
        return output;
    }
}
