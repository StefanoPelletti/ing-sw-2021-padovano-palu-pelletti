package it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate;

import it.polimi.ingsw.Networking.Message.Message;
import it.polimi.ingsw.Networking.Message.MessageType;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class MSG_UPD_LeaderBoard extends Message implements Serializable {

    private final boolean enabled;
    private final Map<String, Integer> leaderboard;

    public MSG_UPD_LeaderBoard(boolean enabled, Map<String, Integer> leaderboard) {
        super(MessageType.MSG_UPD_LeaderBoard);
        this.enabled=enabled;
        this.leaderboard = new TreeMap<>(leaderboard);
    }

    public boolean getEnabled() { return this.enabled; }
    public Map<String, Integer> getLeaderboard() { return this.leaderboard;}

    public MessageType getMessageType() { return super.getMessageType();}

}
