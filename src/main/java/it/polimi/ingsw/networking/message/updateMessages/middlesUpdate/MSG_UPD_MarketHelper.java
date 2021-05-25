package it.polimi.ingsw.networking.message.updateMessages.middlesUpdate;

import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.server.model.enumerators.Resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MSG_UPD_MarketHelper extends Message implements Serializable {

    private final boolean enabled;
    private final List<Resource> resources;
    private final int currentResource;
    private final boolean[] choices; //represents what the player can do with the current Resource
    private final Resource[] extraResourceChoices; //used only when the player has two leaderCards with MarketMarble ability

    public MSG_UPD_MarketHelper(boolean enabled, List<Resource> resources,
                                int currentResource, boolean[] choices,
                                Resource[] extraResourceChoices) {
        super(MessageType.MSG_UPD_MarketHelper);

        this.enabled = enabled;
        this.resources = new ArrayList<>(resources);
        this.currentResource = currentResource;

        if (choices == null)
            this.choices = null;
        else {
            this.choices = new boolean[8];
            System.arraycopy(choices, 0, this.choices, 0, choices.length);
        }

        if (extraResourceChoices == null)
            this.extraResourceChoices = null;
        else {
            this.extraResourceChoices = new Resource[extraResourceChoices.length];
            System.arraycopy(extraResourceChoices, 0, this.extraResourceChoices, 0, extraResourceChoices.length);
        }
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public List<Resource> getResources() {
        return this.resources;
    }

    public int getCurrentResource() {
        return this.currentResource;
    }

    public boolean[] getChoices() {
        return this.choices;
    }

    public Resource[] getExtraResourceChoices() {
        return this.extraResourceChoices;
    }
}