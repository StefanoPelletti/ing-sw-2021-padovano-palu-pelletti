package it.polimi.ingsw.Networking.Message.UpdateMessages;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;
import it.polimi.ingsw.Server.Controller.FaithTrackInfo;

import java.io.Serializable;

public class MSG_UPD_FaithTrack extends Message implements Serializable {

    private FaithTrackInfo faithTrackInfo;
    public MSG_UPD_FaithTrack(FaithTrackInfo faithTrackInfo)
    {
        super(MessageType.MSG_UPD_FaithTrack);
        this.faithTrackInfo = faithTrackInfo;
    }

    public FaithTrackInfo getFaithTrackInfo() {
        return faithTrackInfo;
    }
    public MessageType getMessageType() { return super.getMessageType();}
}
