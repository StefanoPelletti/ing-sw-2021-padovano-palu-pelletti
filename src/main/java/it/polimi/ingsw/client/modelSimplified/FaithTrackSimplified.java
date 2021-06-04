package it.polimi.ingsw.client.modelSimplified;

import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_FaithTrack;
import it.polimi.ingsw.server.model.FaithTrack;
import it.polimi.ingsw.server.model.Player;
import it.polimi.ingsw.server.utils.A;

import java.util.List;

public class FaithTrackSimplified {
    private final boolean[] zones;
    private final GameSimplified game;

    public FaithTrackSimplified(GameSimplified game) {
        zones = new boolean[3];
        this.game = game;
    }

    public void update(MSG_UPD_FaithTrack message) {
        boolean[] newZones = message.getZones();

        System.arraycopy(newZones, 0, this.zones, 0, 3);
    }

    /**
     * Returns a String that represents the current status of FaithTrack, with the zones and the position of the Players.
     * Can work in Solo mode.
     * @return A String representing the current status of the FaithTrack.
     */
    public boolean[] getZones() { return this.zones; }

    public String toString(boolean solo) {
        StringBuilder result = new StringBuilder();
        List<PlayerSimplified> playerListSimplified;

        result.append("                  FAITH TRACK: ").append("\n");
        result.append("\n").append(A.CYAN + "=====X=====X=====X=====X=====X=====X=====X=====" + A.RESET).append("\n");
        playerListSimplified = game.getPlayerList();
        for (PlayerSimplified player : playerListSimplified) {
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