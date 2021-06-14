package it.polimi.ingsw.client.modelSimplified.middles;

import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_LeaderBoard;
import it.polimi.ingsw.server.utils.A;

import java.util.Map;
import java.util.TreeMap;

public class LeaderboardSimplified {
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


    public Map<String, Integer> getBoard() {
        return this.board;
    }


    /**
     * Returns the Leaderboard status, with a prompt that tells a specified Player if he won, lost, or tied. <p>
     * This method also works in Solo mode, but it must be specified by parameter.
     *
     * @return a String with the result, personalized in base of the type of game (multiplayer or Solo) and the player
     */
    public String toResult(String thisPlayer, boolean solo) {
        StringBuilder result = new StringBuilder();
        if (!enabled) return result.append("Empty LeaderboardSimplified.").toString();

        result.append(A.CYAN + "      LEADERBOARD" + A.RESET).append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        boolean tie = false;
        Integer maxValue = 0;
        for (String nickname : board.keySet()) {
            if (board.get(nickname) > maxValue) maxValue = board.get(nickname);
        }
        if (solo) {
            String key = "Lorenzo";
            if (board.get(key) == 1) //lorenzo Lost
            {
                result.append(" You just won. Happy now? ").append("\n");
                result.append("\n").append(thisPlayer).append("    ");
                result.append(" - ").append(board.get(thisPlayer)).append(" points");
            }
            if (board.get(key) == 2) //lorenzo won
            {
                result.append(" I mean, you lost. GG").append("\n");
                result.append("\n").append(thisPlayer);
                result.append("\t").append(board.get(thisPlayer));
            }
        } else {
            if (board.get(thisPlayer).equals(maxValue)) {
                for (String nickname : board.keySet()) {
                    if (nickname.equals(thisPlayer))
                        continue;
                    if (maxValue.equals(board.get(nickname)))
                        tie = true;
                }
                if (tie)
                    result.append(" Extremely lucky guy, you Tied. ").append("\n");
                else
                    result.append(" You just won. Happy now? ").append("\n");
            } else
                result.append(" I mean, you lost. GG").append("\n");

            result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
            for (String nickname : board.keySet()) {
                result.append("\n").append(nickname);
                result.append("\t").append(board.get(nickname));
            }
        }
        return result.toString();

    }
}
