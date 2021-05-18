package it.polimi.ingsw.Client.ModelSimplified.Middles;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.MSG_UPD_LeaderBoard;
import it.polimi.ingsw.Server.Utils.A;

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

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Leaderboard status: ");
        for (String nickname : leaderboard.keySet()) {
            result.append("\n").append(nickname);
            result.append("\t").append(leaderboard.get(nickname));
        }
        return result.toString();
    }

    public String toResult(String thisPlayer, boolean solo) {
        StringBuilder result = new StringBuilder();
        if (this.leaderboard == null) return result.append("Empty LeaderBoard.").toString();

        result.append(A.CYAN + "      LEADERBOARD" + A.RESET).append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        boolean tie = false;
        Integer maxValue = 0;
        for (String nickname : leaderboard.keySet()) {
            if (leaderboard.get(nickname) > maxValue) maxValue = leaderboard.get(nickname);
        }
        if (solo) {
            String key = "Lorenzo";
            if (leaderboard.get(key) == 1) //lorenzo Lost
            {
                result.append(" You just won. Happy now? ").append("\n");
                result.append("\n").append(thisPlayer).append("    ");
                result.append(" - ").append(leaderboard.get(thisPlayer)).append(" points");
            }
            if (leaderboard.get(key) == 2) //lorenzo won
            {
                result.append(" I mean, you lost. GG").append("\n");
                result.append("\n").append(thisPlayer);
                result.append("\t").append(leaderboard.get(thisPlayer));
            }
        } else {
            if (leaderboard.get(thisPlayer).equals(maxValue)) {
                for (String nickname : leaderboard.keySet()) {
                    if (nickname.equals(thisPlayer))
                        continue;
                    if (maxValue.equals(leaderboard.get(nickname)))
                        tie = true;
                }
                if (tie)
                    result.append(" Extremely lucky guy, you Tied. ").append("\n");
                else
                    result.append(" You just won. Happy now? ").append("\n");
            } else
                result.append(" I mean, you lost. GG").append("\n");

            result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
            for (String nickname : leaderboard.keySet()) {
                result.append("\n").append(nickname);
                result.append("\t").append(leaderboard.get(nickname));
            }
        }
        return result.toString();
    }
}
