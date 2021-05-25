package it.polimi.ingsw.client.modelSimplified.middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_MarketHelper;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.model.middles.MarketHelper;

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

    @Override
    public String toString() {
        return MarketHelper.toString(this.enabled, this.resources, this.currentResource, this.choices, this.extraResourceChoices);
    }
}