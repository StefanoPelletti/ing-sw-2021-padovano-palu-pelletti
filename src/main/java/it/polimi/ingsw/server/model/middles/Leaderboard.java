package it.polimi.ingsw.server.model.middles;

import it.polimi.ingsw.networking.message.MSG_Stop;
import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_LeaderBoard;
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

    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Sets the LeaderBoard enabled or disabled.
     * If the LeaderBoard is being set to enabled, it will notify its observers with two message, a MSG_UPD_LeaderBoard followed by a MSG_Stop.
     * The LeaderBoard is not thought to be used after the Game has ended.
     *
     * @param enabled The boolean value to set.
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
     * If present, the previous score of the player is summed with the given quantity.
     *
     * @param nickname the name of player associated with the score
     * @param score    the score of the player
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

    /**
     * Creates a message using generateMessage() and notifies the observers.
     *
     * @see #generateMessage()
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     * Returns a MSG_UPD_LeaderBoard representing the current state of the LeaderBoard.
     *
     * @return A MSG_UPD_LeaderBoard representing the current state of the LeaderBoard.
     */
    public MSG_UPD_LeaderBoard generateMessage() {
        return new MSG_UPD_LeaderBoard(
                this.enabled,
                this.board
        );
    }
}