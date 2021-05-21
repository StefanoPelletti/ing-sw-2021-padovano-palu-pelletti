package it.polimi.ingsw.networking.message.updateMessages;

import it.polimi.ingsw.networking.message.Message;
import it.polimi.ingsw.networking.message.MessageType;

import java.io.Serializable;

public class MSG_UPD_End extends Message implements Serializable {

    public MSG_UPD_End() {
        super(MessageType.MSG_UPD_End);
    }
}
