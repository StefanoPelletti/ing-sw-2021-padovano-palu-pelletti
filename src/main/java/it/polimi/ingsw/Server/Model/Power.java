package it.polimi.ingsw.Server.Model;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;

import java.io.Serializable;
import java.util.HashMap;

public class Power implements Serializable {
    private final HashMap<Resource, Integer> input;
    private final HashMap<Resource, Integer> output;

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

    @Override
    public boolean equals( Object obj )
    {
        if(obj == this) return true;
        if(!(obj instanceof Power)) return false;
        Power o = (Power) obj;
        return(this.input.equals(o.input) && this.output.equals(o.output));
    }

    @Override
    public String toString()
    {
        String result = "";
        result = result + "     POWER: "+"\n";
        result = result + " input:  "+input.toString()+"\n";
        result = result + " output: "+output.toString()+"\n";
        return result;
    }
}
