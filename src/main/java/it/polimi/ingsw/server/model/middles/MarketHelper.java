package it.polimi.ingsw.server.model.middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_MarketHelper;
import it.polimi.ingsw.server.model.enumerators.Resource;
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

    /**
     * CONSTRUCTOR
     */
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

    /**
     * Sets the MerketHelper active or disabled.
     * Also notifies observers.
     *
     * @param enabled The boolean value to set.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        notifyObservers();
    }

    public List<Resource> getResources() {
        return resources;
    }

    /**
     * Called at the beginning of a Market Action.
     * Also notifies the observers.
     *
     * @param newResources The resources that must be placed or discarded by the player.
     */
    public void setResources(List<Resource> newResources) {
        this.resources = newResources;
        this.currentResource = 0;
        this.choices = new boolean[8];
        this.extraResourceChoices = new Resource[2];
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

    public boolean[] getChoices() {
        return this.choices;
    }

    /**
     * Sets the possible choices for the current resource.
     * If the current resource is a normal resource (not Resource.EXTRA):
     * <ul>
     * <li> 0: put in the WarehouseDepot OR convert in ExtraResource 1, if possible.</li>
     * <li> 1: put in extra depot OR convert in ExtraResource 2, if possible.</li>
     * <li> 2: discard the resource, always possible.</li>
     * <li> 3: swap rows 1 and 2 of the WarehouseDepot.</li>
     * <li> 4: swap rows 1 and 3 of the WarehouseDepot.</li>
     * <li> 5: swap rows 2 and 3 of the WarehouseDepot.</li>
     * <li> 6: skip the current resource (forward in the list).</li>
     * <li> 7: skip the current resource (backward in the list).</li>
     * </ul>
     * If the current resource is a Resource.EXTRA, possible if the player has 2 leader cards with the MarketResources ability, the choices 0 and 1 are different:
     * <ul>
     * <li> 0: convert the resource in the resource given by the player's Leader Card 1.</li>
     * <li> 1: convert the resource in the resource given by the player's Leader Card 2.</li>
     * <li> the other choices remain the same as above</li>
     * </ul>
     * Also notifies the observers.
     *
     * @param choices The possible choices for the current resource.
     */
    public void setChoices(boolean[] choices) {
        this.choices = choices;
        if (enabled)
            notifyObservers();
    }

    /**
     * Creates a message using generateMessage() and notifies observers
     *
     * @see #generateMessage()
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     * Returns a MSG_UPD_MarketHelper representing the current state of the MarketHelper.
     *
     * @return a MSG_UPD_MarketHelper representing the current state of the MarketHelper
     */
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
