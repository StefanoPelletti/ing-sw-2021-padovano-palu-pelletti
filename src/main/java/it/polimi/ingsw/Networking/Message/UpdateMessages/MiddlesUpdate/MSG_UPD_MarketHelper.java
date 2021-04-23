package it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;

import java.io.Serializable;
import java.util.ArrayList;

public class MSG_UPD_MarketHelper extends Message implements Serializable {

    private final boolean enabled;
    private final ArrayList<Resource> resources;
    private final int currentResource;
    private final boolean[] choices; //represents what the player can do with the current Resource
    private final Resource[] extraResourceChoices; //used only when the player has two leaderCards with MarketMarble ability
    private final boolean isNormalChoice; //false if the player has two leaderCards with MarketMarble ability (must decide what he wants to do with the WhiteMarble)

    public MSG_UPD_MarketHelper(boolean enabled, ArrayList<Resource> resources,
                                int currentResource, boolean[] choices,
                                Resource[] extraResourceChoices, boolean isNormalChoice)
    {
        super(MessageType.MSG_UPD_MarketHelper);

        this.enabled=enabled;
        this.resources = new ArrayList<>(resources);
        this.currentResource=currentResource;
        this.choices = new boolean[choices.length];
        System.arraycopy(choices,0,this.choices,0,choices.length);
        this.extraResourceChoices = new Resource[extraResourceChoices.length];
        System.arraycopy(extraResourceChoices,0,this.extraResourceChoices,0,extraResourceChoices.length);
        this.isNormalChoice=isNormalChoice;
    }

    public boolean getEnabled() { return this.enabled;}
    public ArrayList<Resource> getResources() { return this.resources;}
    public int getCurrentResource() { return this.currentResource;}
    public boolean[] getChoices() { return this.choices;}
    public Resource[] getExtraResourceChoices() { return this.extraResourceChoices;}
    public boolean getIsNormalChoice() { return this.isNormalChoice;}

    public MessageType getMessageType() { return super.getMessageType();}
}
