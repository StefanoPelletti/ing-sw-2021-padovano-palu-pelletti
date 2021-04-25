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
        result.append(" MarketHelper è qui ad aiutarti! ");
        result.append(" la risorsa attualmente selezionata è un ").append(getCurrentResource()).append("\n");
        result.append(" le risorse che hai recuperato dal mercato, sono: ").append("\n");
        result.append(resources);
        result.append(" Perfavore seleziona una delle seguenti possibili opzioni");
        if(getCurrentResource()!=Resource.EXTRA)
        {
            if(choices[0]) result.append(" 0 : inserisci nel deposito").append("\n");
            if(choices[1]) result.append(" 1 : inserisci nel deposito extra").append("\n");
        }
        else
        {
            if(choices[0]) result.append(" 0 : scambia con un ").append(extraResourceChoices[0]).append("\n");
            if(choices[1]) result.append(" 1 : scambia con un ").append(extraResourceChoices[1]).append("\n");
        }

        if(choices[2]) result.append(" 2 : scarta la risorsa").append("\n");
        if(choices[3]) result.append(" 3 : scambia le righe 1 e 2").append("\n");
        if(choices[4]) result.append(" 4 : scambia le righe 1 e 3").append("\n");
        if(choices[5]) result.append(" 5 : scambia le righe 2 e 3").append("\n");
        if(choices[6]) result.append(" 6 : vai alla prossima risorsa").append("\n");
        if(choices[7]) result.append(" 7 : vai alla risorsa precedente").append("\n");

        return result.toString();
    }

}
