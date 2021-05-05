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

    public boolean isEnabled() {
        return enabled;
    }

    public void update( MSG_UPD_MarketHelper message )
    {
        boolean newEnabled = message.getEnabled();
        ArrayList<Resource> newResources = message.getResources();
        int newCurrentResource = message.getCurrentResource();
        boolean[] newChoices = message.getChoices();
        Resource[] newExtraResourceChoices = message.getExtraResourceChoices();

        this.enabled = newEnabled;
        this.currentResource= newCurrentResource;
        this.resources= new ArrayList<Resource>(newResources);
        this.choices = new boolean[newChoices.length];
        System.arraycopy(newChoices,0,this.choices, 0, newChoices.length );
        this.extraResourceChoices = new Resource[newExtraResourceChoices.length];
        System.arraycopy(newExtraResourceChoices, 0, this.extraResourceChoices, 0, newExtraResourceChoices.length);
    }

    public ArrayList<Resource> getResources(){
        return resources;
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

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder("");
        result.append(" MarketHelper is here to help! ");
        result.append(" Currently selected resource is a  ").append(getCurrentResource()).append("\n");
        result.append(" The Resources you gathered from the market are: ").append("\n");
        result.append(resources);
        result.append(" Please choose an option: ");
        if(getCurrentResource()!=Resource.EXTRA)
        {
            if(choices[0]) result.append(" 0 : put in depot! ").append("\n");
            if(choices[1]) result.append(" 1 : put in Extra depot!").append("\n");
        }
        else
        {
            if(choices[0]) result.append(" 0 : convert in ").append(extraResourceChoices[0]).append("\n");
            if(choices[1]) result.append(" 1 : convert in ").append(extraResourceChoices[1]).append("\n");
        }

        if(choices[2]) result.append(" 2 : discard that resource! ").append("\n");
        if(choices[3]) result.append(" 3 : swap the 1st and 2nd rows of your depot! ").append("\n");
        if(choices[4]) result.append(" 4 : swap the 1st and 3rd rows of your depot! ").append("\n");
        if(choices[5]) result.append(" 5 : swap the 2nd and 3rd rows of your depot! ").append("\n");
        if(choices[6]) result.append(" 6 : hop to the next available resource! ").append("\n");
        if(choices[7]) result.append(" 7 : hop back to the previous resource! ").append("\n");

        return result.toString();
    }

}
