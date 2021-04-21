package it.polimi.ingsw.Client.ModelSimplified.Middles;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.MSG_UPD_MarketHelper;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;

import java.util.ArrayList;

public class MarketHelper
{
    private boolean enabled;
    private ArrayList<Resource> resources;
    private int currentResource;
    private boolean[] choices; //represents what the player can do with the current Resource
    private Resource[] extraResourceChoices; //used only when the player has two leaderCards with MarketMarble ability
    private boolean isNormalChoice; //false if the player has two leaderCards with MarketMarble ability (must decide what he wants to do with the WhiteMarble)

    public boolean isEnabled() {
        return enabled;
    }

    public void update(MSG_UPD_MarketHelper message )
    {
        boolean newEnabled = message.getEnabled();
        ArrayList<Resource> newResources = message.getResources();
        int newCurrentResource = message.getCurrentResource();
        boolean[] newChoices = message.getChoices();
        Resource[] newExtraResourceChoices = message.getExtraResourceChoices();
        boolean newNormalChoice = message.getIsNormalChoice();

        this.enabled = newEnabled;
        this.currentResource= newCurrentResource;
        this.isNormalChoice=newNormalChoice;
        this.resources= new ArrayList<Resource>(newResources);
        this.choices = new boolean[newChoices.length];
        System.arraycopy(newChoices,0,this.choices, 0, newChoices.length );
        this.extraResourceChoices = new Resource[newExtraResourceChoices.length];
        System.arraycopy(newExtraResourceChoices, 0, this.extraResourceChoices, 0, newExtraResourceChoices.length);
    }

    public ArrayList<Resource> getResources(){
        return resources;
    }

    public boolean isNormalChoice(){
        return isNormalChoice;
    }

    public Resource[] getExtraResources(){
        return extraResourceChoices;
    }

    public Resource getCurrentResource(){
        return resources.get(currentResource);
    }

    public boolean[] getChoices(){
        return this.choices;
    }

}
