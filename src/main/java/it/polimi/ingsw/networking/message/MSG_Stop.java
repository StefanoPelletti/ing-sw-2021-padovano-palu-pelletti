package it.polimi.ingsw.networking.message;


public class MSG_Stop extends Message {

    /**
     * MSG_Stop is used after a MSG_UPD_LeaderBoard is sent.
     *  A ClientHandler receiving this message will result in a closeStreams() call, consequentially interrupting the ClientHandler
     */
    public MSG_Stop() {
        super(MessageType.MSG_Stop);
    }
}