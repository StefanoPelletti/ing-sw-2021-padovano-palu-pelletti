package it.polimi.ingsw.Client.ModelSimplified.Middles;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.MSG_UPD_LeaderBoard;
import it.polimi.ingsw.Server.Utils.Displayer;

import java.util.Map;
import java.util.TreeMap;

public class LeaderBoard {
    private boolean enabled;
    private Map<String, Integer> leaderboard;

    public boolean isEnabled() {
        return this.enabled;
    }

    public void update(MSG_UPD_LeaderBoard message) {
        boolean newEnabled = message.getEnabled();
        Map<String, Integer> newLeaderboard = message.getLeaderboard();

        this.enabled = newEnabled;
        this.leaderboard = new TreeMap<>(newLeaderboard);
    }

    public String toResult(String thisPlayer, boolean solo) {
        return Displayer.leaderboardToResult(this.enabled, this.leaderboard, thisPlayer, solo);
    }
}
