package it.polimi.ingsw.server.model.middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_MarketHelper;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.A;
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

    /**
     * The static toString() shows the user a list of possible choices to resolve a Resource grabbed from the Market.
     * A MarketHelper or a MarketHelperSimplified may use this shared method by passing their internal values.
     * @param enabled True if the MarketHelper is enabled.
     * @param resources A List of Resource that must be placed by the player, after a Market Action.
     * @param currentResource The position in resources, represents the current resource.
     * @param choices The choices valid for the current resource.
     * @param extraResourceChoices The extra choices valid for the current resource (used only when the current resource is a Resource.EXTRA).
     * @return A String representing the current state of the Market Helper.
     */
    public static String toString(boolean enabled, List<Resource> resources, int currentResource, boolean[] choices, Resource[] extraResourceChoices) {
        StringBuilder result = new StringBuilder();
        if (!enabled) return result.append(A.RED + " MARKETHELPER IS NOT ENABLED!" + A.RESET).toString();

        result.append("\n");
        result.append(A.CYAN + " MARKETHELPER IS HERE TO HELP! " + A.RESET).append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append(" The Resources you gathered from the market are: ").append("\n");
        result.append(" ").append(resources).append("\n");
        result.append(" Currently selected resource is a ").append(resources.get(currentResource)).append(". What do you want to do with it?").append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        result.append(" Available options: ").append("\n");
        if (resources.get(currentResource) != Resource.EXTRA) {
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
     * Sets the possible choices for the current resource:
     * <ul>
     * <li> 0: put in the WarehouseDepot OR convert in ExtraResource 1, if possible
     * <li> 1: put in extra depot OR convert in ExtraResource 2, if possible
     * <li> 2: discard the resource, always possible
     * <li> 3: swap rows 1 and 2 of the WarehouseDepot
     * <li> 4: swap rows 1 and 3 of the WarehouseDepot
     * <li> 5: swap rows 2 and 3 of the WarehouseDepot
     * <li> 6: skip the current resource (forward in the list)
     * <li> 7: skip the current resource (backward in the list).
     * </ul>
     * Also notifies the observers.
     * @param choices The possible choices for the current resource.
     */
    public void setChoices(boolean[] choices) {
        this.choices = choices;
        if (enabled)
            notifyObservers();
    }

    @Override
    public String toString() {
        return MarketHelper.toString(this.enabled, this.resources, this.currentResource, this.choices, this.extraResourceChoices);
    }

    /**
     * Creates a message using generateMessage() and notifies observers
     * @see #generateMessage()
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     * Returns a MSG_UPD_MarketHelper representing the current state of the MarketHelper.
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
