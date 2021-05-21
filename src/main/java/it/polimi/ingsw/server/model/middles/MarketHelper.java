package it.polimi.ingsw.server.model.middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_MarketHelper;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.Displayer;
import it.polimi.ingsw.server.utils.ModelObservable;

import java.util.ArrayList;
import java.util.List;


public class MarketHelper extends ModelObservable {
    private boolean enabled;
    private List<Resource> resources;
    private int currentResource;
    private boolean[] choices; //represents what the player can do with the current Resource
    private Resource[] extraResourceChoices; //used only when the player has two leaderCards with MarketMarble ability
    private boolean isNormalChoice; //false if the player has two leaderCards with MarketMarble ability (must decide what he wants to do with the WhiteMarble)

    public MarketHelper() {
        resources = new ArrayList<>();
        enabled = false;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        notifyObservers();
    }

    public List<Resource> getResources() {
        return resources;
    }

    //called at the beginning of a Market Action
    public void setResources(List<Resource> newResources) {
        this.resources = newResources;
        this.currentResource = 0;
        this.choices = new boolean[8];
        this.extraResourceChoices = new Resource[2];
    }

    public boolean isNormalChoice() {
        return isNormalChoice;
    }

    public void setNormalChoice(boolean value) {
        isNormalChoice = value;
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

    public void setChoices(boolean[] choices) {
        this.choices = choices;
        if (enabled)
            notifyObservers();
    }

    @Override
    public String toString() {
        return Displayer.marketHelperToString(this.enabled, this.resources, this.currentResource, this.choices, this.extraResourceChoices);
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
