package it.polimi.ingsw.Client.ModelSimplified;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_FaithTrack;
import it.polimi.ingsw.Server.Utils.A;

import java.util.*;

import java.util.Arrays;

public class FaithTrackSimplified {
    private boolean[] zones;
    private GameSimplified game;

    public FaithTrackSimplified(GameSimplified game) {
        zones = new boolean[3];
        this.game = game;
    }

    public void update(MSG_UPD_FaithTrack message) {
        boolean[] newZones = message.getZones();

        System.arraycopy(newZones, 0, this.zones, 0, 3);
    }


    public String toString(boolean solo) {
        StringBuilder result = new StringBuilder();
        List<PlayerSimplified> playerList = game.getPlayerList();

        result.append("                  FAITH TRACK: ").append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        for (PlayerSimplified player : playerList) {
            result.append("   ").append(player.getNickname()).append(" is at position: ").append(player.getPosition()).append("\n");
        }
        if (solo)
            result.append("\n").append(" Lorenzo is at position: ").append(game.getBlackCrossPosition()).append("\n");
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
}