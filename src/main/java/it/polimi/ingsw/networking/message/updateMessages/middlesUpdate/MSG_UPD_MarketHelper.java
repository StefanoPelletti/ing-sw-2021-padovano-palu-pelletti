package it.polimi.ingsw.networking.message.updateMessages.middlesUpdate;

import it.polimi.ingsw.client.cli.UpdateHandler;
import it.polimi.ingsw.networking.message.MessageType;
import it.polimi.ingsw.networking.message.updateMessages.UpdateMessage;
import it.polimi.ingsw.server.model.enumerators.Resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MSG_UPD_MarketHelper extends UpdateMessage implements Serializable {

    private final boolean enabled;
    private final List<Resource> resources;
    private final int currentResource;
    private final boolean[] choices; //represents what the player can do with the current Resource
    private final Resource[] extraResourceChoices; //used only when the player has two leaderCards with MarketMarble ability

    /**
     * MSG_UPD_MarkerHelper is sent by the ClientHandler to the Client.
     * It is generated by a model.middles.MarketHelper.
     * It contains the necessary information to let the Client choose what to do with a specific resource.
     * Its usage is best described in the Documentation files, Action.
     *
     * @param enabled              If true, the currentPlayer must resolve this object as a priority.
     * @param resources            A list containing all the resources left to resolve.
     * @param currentResource      An integer pointing to the currently selected resource.
     * @param choices              A 8-cell boolean array which represents the possible choices that the user can do.
     * @param extraResourceChoices A 2-cell Resource array representing the possible Resources the player can transform an Extra Resource to.
     * @see it.polimi.ingsw.server.model.middles.MarketHelper;
     */
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

    @Override
    public void executeCLI(UpdateHandler updateHandler) {
        updateHandler.updateMarketHelper(this);
    }

    @Override
    public void executeGUI() {

    }
}