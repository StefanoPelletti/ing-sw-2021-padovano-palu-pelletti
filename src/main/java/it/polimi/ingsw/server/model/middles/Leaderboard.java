package it.polimi.ingsw.server.model.middles;

import it.polimi.ingsw.networking.message.MSG_Stop;
import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_LeaderBoard;
import it.polimi.ingsw.server.utils.A;
import it.polimi.ingsw.server.utils.ModelObservable;

import java.util.Map;
import java.util.TreeMap;

public class Leaderboard extends ModelObservable {
    private boolean enabled;
    private Map<String, Integer> board;

    /**
     * CONSTRUCTOR
     */
    public Leaderboard() {
        this.enabled = false;
        this.board = null;
    }

    /**
     *
     * @param enabled true if the LeaderBoard is enabled
     * @param leaderboard the leaderboard at the end of a game
     * @param thisPlayer the player which receives the result
     * @param solo true if the game is in solo mode
     * @return a String with the result, personalized in base of the type of game (multiplayer or solo) and the player
     */
    public static String toResult(boolean enabled, Map<String, Integer> leaderboard, String thisPlayer, boolean solo) {
        StringBuilder result = new StringBuilder();
        if (!enabled) return result.append("Empty LeaderboardSimplified.").toString();

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

    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Sets the LeaderBoard active or disabled
     * Also notifies observers
     * @param enabled the boolean value to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled)
            board = null;
        notifyObservers();
        notifyObservers(new MSG_Stop());
    }

    /**
     * If absent, the player is added with the given score.
     * If present, the previous score of the player is summed with the given quantity
     * @param nickname the name of player associated with the score
     * @param score the score of the player
     */
    public void addScore(String nickname, Integer score) {
        if (this.board == null) {
            board = new TreeMap<>();
            board.put(nickname, score);
        } else {
            if (board.containsKey(nickname)) {
                Integer ret = board.replace(nickname, score);
                board.replace(nickname, score + ret);
            } else
                board.put(nickname, score);
        }
    }

    public String toResult(String thisPlayer, boolean solo) {
        return Leaderboard.toResult(this.enabled, this.board, thisPlayer, solo);
    }

    /**
     * Creates a message and notifies the observers
     * @see #generateMessage()
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     *
     * @return a MSG_UPD_LeaderBoard representing the current state of the LeaderBoard
     */
    public MSG_UPD_LeaderBoard generateMessage() {
        return new MSG_UPD_LeaderBoard(
                this.enabled,
                this.board
        );
    }
}