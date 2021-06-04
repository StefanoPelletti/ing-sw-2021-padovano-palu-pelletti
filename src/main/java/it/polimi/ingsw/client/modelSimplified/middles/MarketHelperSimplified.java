package it.polimi.ingsw.client.modelSimplified.middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_MarketHelper;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.A;

import java.util.ArrayList;
import java.util.List;

public class MarketHelperSimplified {
    private boolean enabled;
    private ArrayList<Resource> resources;
    private int currentResource;
    private boolean[] choices; //represents what the player can do with the current Resource
    private Resource[] extraResourceChoices; //used only when the player has two leaderCards with MarketMarble ability

    public boolean isEnabled() {
        return enabled;
    }

    public void update(MSG_UPD_MarketHelper message) {
        boolean newEnabled = message.getEnabled();
        List<Resource> newResources = message.getResources();
        int newCurrentResource = message.getCurrentResource();
        boolean[] newChoices = message.getChoices();
        Resource[] newExtraResourceChoices = message.getExtraResourceChoices();

        this.enabled = newEnabled;
        this.currentResource = newCurrentResource;
        this.resources = new ArrayList<>(newResources);
        if (newChoices == null)
            this.choices = null;
        else {
            this.choices = new boolean[newChoices.length];
            System.arraycopy(newChoices, 0, this.choices, 0, newChoices.length);
        }
        if (newExtraResourceChoices == null)
            this.extraResourceChoices = null;
        else {
            this.extraResourceChoices = new Resource[newExtraResourceChoices.length];
            System.arraycopy(newExtraResourceChoices, 0, this.extraResourceChoices, 0, newExtraResourceChoices.length);
        }
    }

    public List<Resource> getResources() {
        return resources;
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
     * The static toString() shows the user a list of possible choices to resolve a Resource grabbed from the Market.
     * @return A String representing the current state of the Market Helper.
     */
    @Override
    public String toString() {
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
}
