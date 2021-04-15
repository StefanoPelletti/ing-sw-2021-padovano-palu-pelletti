package it.polimi.ingsw.Networking;

import it.polimi.ingsw.Server.Model.Enumerators.Resource;

import java.io.Serializable;
import java.util.ArrayList;

public class OBJ_PRODUCTION implements Serializable {

    private final ArrayList<Resource> input;
    private final Resource output;
    private final boolean basic;

    public OBJ_PRODUCTION(boolean basic, ArrayList<Resource> input, Resource output)
    {
        this.basic = basic;
        this.input = input;
        this.output = output;
    }

    public ArrayList<Resource> getProductionInput() { return input; }
    public Resource getProductionOutput() { return output; }

    public boolean isBasicProduction()
    {
        return basic;
    }
    public boolean isLeaderCardProduction()
    {
        return !basic;
    }
}
