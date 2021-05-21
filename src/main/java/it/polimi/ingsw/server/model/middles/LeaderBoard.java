package it.polimi.ingsw.server.model.middles;

import it.polimi.ingsw.networking.message.MSG_Stop;
import it.polimi.ingsw.networking.message.updateMessages.middlesUpdate.MSG_UPD_LeaderBoard;
import it.polimi.ingsw.server.utils.Displayer;
import it.polimi.ingsw.server.utils.ModelObservable;

import java.util.Map;
import java.util.TreeMap;

public class LeaderBoard extends ModelObservable {
    private boolean enabled;
    private Map<String, Integer> board;

    public LeaderBoard() {
        this.enabled = false;
        this.board = null;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled)
            board = null;
        notifyObservers();
        notifyObservers(new MSG_Stop());
    }

    public Map<String, Integer> getLeaderboard() {
        if (this.board == null)
            return null;
        else
            return new TreeMap<>(this.board);
    }

    public void putScore(String nickname, Integer score) {
        if (this.board == null)
            board = new TreeMap<>();
        board.put(nickname, score);
    }

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
        return Displayer.leaderboardToResult(this.enabled, this.board, thisPlayer, solo);
    }

    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    public MSG_UPD_LeaderBoard generateMessage() {
        return new MSG_UPD_LeaderBoard(
                this.enabled,
                this.board
        );
    }
}