package it.polimi.ingsw.client.modelSimplified.middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_LeaderBoard;
import it.polimi.ingsw.server.utils.Displayer;

import java.util.Map;
import java.util.TreeMap;

public class LeaderBoard {
    private boolean enabled;
    private Map<String, Integer> board;

    public boolean isEnabled() {
        return this.enabled;
    }

    public void update(MSG_UPD_LeaderBoard message) {
        boolean newEnabled = message.getEnabled();
        Map<String, Integer> newLeaderboard = message.getLeaderboard();

        this.enabled = newEnabled;
        this.board = new TreeMap<>(newLeaderboard);
    }

    public String toResult(String thisPlayer, boolean solo) {
        return Displayer.leaderboardToResult(this.enabled, this.board, thisPlayer, solo);
    }
}
