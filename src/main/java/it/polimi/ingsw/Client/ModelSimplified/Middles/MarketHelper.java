package it.polimi.ingsw.Client.ModelSimplified.Middles;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.MSG_UPD_MarketHelper;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Utils.Displayer;

import java.util.ArrayList;

public class MarketHelper {
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
        ArrayList<Resource> newResources = message.getResources();
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

    public ArrayList<Resource> getResources() {
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

    @Override
    public String toString() {
        return Displayer.marketHelperToString(this.enabled, this.resources, this.currentResource, this.choices, this.extraResourceChoices);
    }
}
