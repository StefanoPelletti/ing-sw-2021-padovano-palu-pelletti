package it.polimi.ingsw.server.model;

import it.polimi.ingsw.client.modelSimplified.GameSimplified;
import it.polimi.ingsw.client.modelSimplified.PlayerSimplified;
import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_FaithTrack;
import it.polimi.ingsw.server.utils.A;
import it.polimi.ingsw.server.utils.ModelObservable;

import java.util.List;

public class FaithTrack extends ModelObservable {
    private final Game game;
    private final boolean[] zones;

    public FaithTrack(Game game) {
        this.game = game;
        zones = new boolean[3];
    }

    //auxiliary method: -1 is error,
//3 would activate the last zone (zone 3)
//2 would activate the second zone (zone 2)
//1 would activate the first zone (zone 1)
//0 would not activate anything
    public int doesActivateZone(Player player) {
        int p = player.getPosition() + 1;
        return doesActivateZone(p);
    }

    //overload of the previous method
    //it is used to know what would happen in a specific position of the FaithTrack
    public int doesActivateZone(int p) {
        if (p > 24) return -1;
        if (p == 24 && !zones[2]) return 3;
        if (p == 16 && !zones[1]) return 2;
        if (p == 8 && !zones[0]) return 1;
        return 0;
    }

    public boolean advance(Player player) {
        if (player.getPosition() == 24) return false;
        player.setPosition(player.getPosition() + 1);
        return true;
    }

    public boolean advanceLorenzo() {
        if (game.getBlackCrossPosition() == 24) {
            return false;
        }
        game.setBlackCrossPosition(game.getBlackCrossPosition() + 1);
        return true;
    }

    //result is the number of points that
    public int calculateVP(Player player) {
        int p = player.getPosition();
        if (p >= 5 && p <= 8 && !zones[0]) return 2;
        if (p >= 12 && p <= 16 && !zones[1]) return 3;
        if (p >= 19 && p <= 24 && !zones[2]) return 4;
        return 0;
    }

    public void setZones(int pos, Boolean value) {
        zones[pos] = value;
        notifyObservers();
    }

    public void resetZones() {
        zones[0] = zones[1] = zones[2] = false;
        notifyObservers();
    }

    public boolean[] getZones() {
        boolean[] result = new boolean[3];
        System.arraycopy(zones, 0, result, 0, 3);
        return result;
    }

    public String toString(boolean solo) {
        return FaithTrack.toString(solo, this.zones, false, this.game, null);
    }

    public static String toString(boolean solo, boolean[] zones, boolean simplified, Game game, GameSimplified gameSimplified) {
        StringBuilder result = new StringBuilder();
        List<PlayerSimplified> playerListSimplified;
        List<Player> playerList;

        result.append("                  FAITH TRACK: ").append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        if (simplified) {
            playerListSimplified = gameSimplified.getPlayerList();
            for (PlayerSimplified player : playerListSimplified) {
                result.append("   ").append(player.getNickname()).append(" is at position: ").append(player.getPosition()).append("\n");
            }
        } else {
            playerList = game.getPlayerList();
            for (Player player : playerList) {
                result.append("   ").append(player.getNickname()).append(" is at position: ").append(player.getPosition()).append("\n");
            }
        }

        if (solo) {
            if (simplified)
                result.append("\n").append(" Lorenzo is at position: ").append(gameSimplified.getBlackCrossPosition()).append("\n");
            else
                result.append("\n").append(" Lorenzo is at position: ").append(game.getBlackCrossPosition()).append("\n");
        }

        result.append("\n");
        if (zones[2])
            result.append("   Third and last zone has been activated!");
        else {
            if (zones[1])
                result.append("   Second zone has been activated!");
            else {
                if (zones[0])
                    result.append("   The First zone has been activated!");
                else
                    result.append("   No zone has been activated yet!");
            }
        }
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        return result.toString();
    }

    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    public MSG_UPD_FaithTrack generateMessage() {
        return new MSG_UPD_FaithTrack(zones);
    }


}