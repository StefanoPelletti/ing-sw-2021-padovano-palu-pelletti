package it.polimi.ingsw.Server.Model.Middles;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.MSG_UPD_LeaderBoard;
import it.polimi.ingsw.Server.Utils.ModelObservable;

import java.util.Map;
import java.util.TreeMap;

public class LeaderBoard extends ModelObservable  {
    private boolean enabled;
    private Map<String, Integer> leaderboard;

    public LeaderBoard()
    {
        this.enabled=false;
        this.leaderboard = null;
    }

    public boolean isEnabled() { return this.enabled; }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
        if(!enabled)
            leaderboard = null;
        notifyObservers();
    }

    public Map<String, Integer> getLeaderboard() {
        if (this.leaderboard==null)
            return null;
        else
            return new TreeMap<>(this.leaderboard);
    }

    public void putScore( String nickname, Integer score )
    {
        if(this.leaderboard==null)
            leaderboard = new TreeMap<>();
        leaderboard.put(nickname, score);
    }

    public void addScore(String nickname, Integer score)
    {
        if(this.leaderboard==null) {
            leaderboard = new TreeMap<>();
            leaderboard.put(nickname, score);
        }
        else
        {
            if(leaderboard.containsKey(nickname))
            {
                Integer ret = leaderboard.replace(nickname, score);
                leaderboard.replace(nickname, score+ret);
            }
            else
                leaderboard.put(nickname, score);
        }
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        result.append("Leaderboard status: ");
        for( String nickname : leaderboard.keySet())
        {
            result.append("\n").append(nickname);
            result.append("\t").append(leaderboard.get(nickname));
        }
        return result.toString();
    }

    private void notifyObservers(){
        this.notifyObservers(generateMessage());
    }

    public MSG_UPD_LeaderBoard generateMessage()
    {
        return new MSG_UPD_LeaderBoard(
                this.enabled,
                this.leaderboard
        );
    }
}
