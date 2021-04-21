package it.polimi.ingsw.Networking.Message.UpdateMessages;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;

import java.io.Serializable;

public class MSG_UPD_FaithTrack extends Message implements Serializable {

    private final boolean zones[];
    public MSG_UPD_FaithTrack(boolean[] zones)
    {
        super(MessageType.MSG_UPD_FaithTrack);
        this.zones = new boolean[3];
        System.arraycopy(zones, 0, this.zones, 0, 3);
    }

    public boolean[] getZones() {
        return zones;
    }
    public MessageType getMessageType() { return super.getMessageType();}
}
