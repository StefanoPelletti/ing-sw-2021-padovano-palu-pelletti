package it.polimi.ingsw.Server.Model.Middles;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.MSG_UPD_MarketHelper;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Utils.ModelObservable;

import java.util.*;


public class MarketHelper extends ModelObservable  {
    private boolean enabled;
    private ArrayList<Resource> resources;
    private int currentResource;
    private boolean[] choices; //represents what the player can do with the current Resource
    private Resource[] extraResourceChoices; //used only when the player has two leaderCards with MarketMarble ability
    private boolean isNormalChoice; //false if the player has two leaderCards with MarketMarble ability (must decide what he wants to do with the WhiteMarble)

    public MarketHelper(){
        resources = new ArrayList<>();
        enabled=false;
    }

    //called at the beginning of a Market Action
    public void setResources(ArrayList<Resource> newResources){
        this.resources = newResources;
        this.currentResource = 0;
        this.choices = new boolean[8];
        this.extraResourceChoices = new Resource[2];
    }

    public void setEnabled(boolean enabled) {
        this.enabled=enabled;
        notifyObservers();
    }

    public void setNormalChoice(boolean value){
        isNormalChoice=value;
    }

    public void setExtraResourceChoices(Resource[] extraChoices){
        this.extraResourceChoices = extraChoices;
    }

    public void setResource(Resource resource){ //only used when player has two leaderCards with MarketMarble ability
        this.resources.set(currentResource, resource);
    }

    public void skipForward(){
        this.currentResource++;
        if(currentResource == resources.size()) currentResource=0;
    }

    public void skipBackward(){
        this.currentResource--;
        if(currentResource == -1) currentResource=resources.size()-1;
    }

    public void removeResource(){
        resources.remove(currentResource);
        if(currentResource == resources.size()) currentResource=0;
    }

    public void setChoices(boolean[] choices){
        this.choices = choices;
        if(enabled)
            notifyObservers();
    }

    public boolean isEnabled() { return enabled;}

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

    private void notifyObservers(){
        this.notifyObservers(new MSG_UPD_MarketHelper(this.enabled, this.resources,
                this.currentResource, this.choices,
                this.extraResourceChoices));
    }

}
