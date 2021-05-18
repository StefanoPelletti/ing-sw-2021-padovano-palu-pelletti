package it.polimi.ingsw.Server.Model.Middles;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.MSG_UPD_MarketHelper;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Utils.A;
import it.polimi.ingsw.Server.Utils.ModelObservable;

import java.util.*;


public class MarketHelper extends ModelObservable {
    private boolean enabled;
    private ArrayList<Resource> resources;
    private int currentResource;
    private boolean[] choices; //represents what the player can do with the current Resource
    private Resource[] extraResourceChoices; //used only when the player has two leaderCards with MarketMarble ability
    private boolean isNormalChoice; //false if the player has two leaderCards with MarketMarble ability (must decide what he wants to do with the WhiteMarble)

    public MarketHelper() {
        resources = new ArrayList<>();
        enabled = false;
    }

    //called at the beginning of a Market Action
    public void setResources(ArrayList<Resource> newResources) {
        this.resources = newResources;
        this.currentResource = 0;
        this.choices = new boolean[8];
        this.extraResourceChoices = new Resource[2];
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        notifyObservers();
    }

    public void setNormalChoice(boolean value) {
        isNormalChoice = value;
    }

    public void setExtraResourceChoices(Resource[] extraChoices) {
        this.extraResourceChoices = extraChoices;
    }

    public void setResource(Resource resource) { //only used when player has two leaderCards with MarketMarble ability
        this.resources.set(currentResource, resource);
    }

    public void skipForward() {
        this.currentResource++;
        if (currentResource == resources.size()) currentResource = 0;
    }

    public void skipBackward() {
        this.currentResource--;
        if (currentResource == -1) currentResource = resources.size() - 1;
    }

    public void removeResource() {
        resources.remove(currentResource);
        if (currentResource == resources.size()) currentResource = 0;
    }

    public void setChoices(boolean[] choices) {
        this.choices = choices;
        if (enabled)
            notifyObservers();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public boolean isNormalChoice() {
        return isNormalChoice;
    }

    public Resource[] getExtraResources() {
        return extraResourceChoices;
    }

    public Resource getCurrentResource() {
        return resources.get(currentResource);
    }

    public int getCurrentResourceInt() {
        return this.currentResource;
    }

    public boolean[] getChoices() {
        return this.choices;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\n");
        result.append(A.CYAN + " MARKETHELPER IS HERE TO HELP! " + A.RESET).append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append(" The Resources you gathered from the market are: ").append("\n");
        result.append(" ").append(resources).append("\n");
        result.append(" Currently selected resource is a ").append(getCurrentResource()).append(". What do you want to do with it?").append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append(" Available options: ").append("\n");
        if (getCurrentResource() != Resource.EXTRA) {
            if (choices[0]) result.append("  0 : put in depot! ").append("\n");
            if (choices[1]) result.append("  1 : put in Extra depot!").append("\n");
        } else {
            if (choices[0]) result.append("  0 : convert in ").append(extraResourceChoices[0]).append("\n");
            if (choices[1]) result.append("  1 : convert in ").append(extraResourceChoices[1]).append("\n");
        }

        if (choices[2]) result.append("  2 : discard that resource! ").append("\n");
        if (choices[3]) result.append("  3 : swap the 1st and 2nd rows of your depot! ").append("\n");
        if (choices[4]) result.append("  4 : swap the 1st and 3rd rows of your depot! ").append("\n");
        if (choices[5]) result.append("  5 : swap the 2nd and 3rd rows of your depot! ").append("\n");
        if (choices[6]) result.append("  6 : hop to the next available resource! ").append("\n");
        if (choices[7]) result.append("  7 : hop back to the previous resource! ").append("\n");

        return result.toString();
    }

    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    public MSG_UPD_MarketHelper generateMessage() {
        return new MSG_UPD_MarketHelper(
                this.enabled,
                this.resources,
                this.currentResource,
                this.choices,
                this.extraResourceChoices
        );
    }
}
