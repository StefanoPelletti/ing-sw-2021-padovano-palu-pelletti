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

    /**
     * Constructor of the FaithTrack.
     * @param game game reference
     */
    public FaithTrack(Game game) {
        this.game = game;
        zones = new boolean[3];
    }

    /**
     * Gets the position of the player and calls the other method.
     * @param player player reference
     * @return a number for all the zones:
     *   - 0 would not activate anything
     *   - 1 would activate the first zone (zone 1)
     *   - 2 would activate the second zone (zone 2)
     *   - 3 would activate the third zone (zone 3)
     */
    public int doesActivateZone(Player player) {
        int p = player.getPosition() + 1;
        return doesActivateZone(p);
    }

    /**
     * Overload of the previous method. It is used to know what would happen in a specific position of the FaithTrack
     * @param p position of the player
     * @return the number for the zone
     */
    public int doesActivateZone(int p) {
        if (p > 24) return -1;
        if (p == 24 && !zones[2]) return 3;
        if (p == 16 && !zones[1]) return 2;
        if (p == 8 && !zones[0]) return 1;
        return 0;
    }

    /**
     * Increments the position of the player by 1.
     * @param player player reference
     * @return true if the player advanced, false if the position of the player is the last one.
     */
    public boolean advance(Player player) {
        if (player.getPosition() == 24) return false;
        player.setPosition(player.getPosition() + 1);
        return true;
    }

    /**
     * Increments the position of Lorenzo by 1.
     * @return true if Lorenzo advanced, false if its position is the last one.
     */
    public boolean advanceLorenzo() {
        if (game.getBlackCrossPosition() == 24) {
            return false;
        }
        game.setBlackCrossPosition(game.getBlackCrossPosition() + 1);
        return true;
    }

    /**
     * When a zone has been activated this method calculates the Victory Points.
     * @param player player reference
     * @return the number of points for each zone:
     *   - 0 if it has not been activated any zone
     *   - 2 if the first zone has been activated
     *   - 3 if the second zone has been activated
     *   - 4 if the third zone has been activated.
     */
    public int calculateVP(Player player) {
        int p = player.getPosition();
        if (p >= 5 && p <= 8 && !zones[0]) return 2;
        if (p >= 12 && p <= 16 && !zones[1]) return 3;
        if (p >= 19 && p <= 24 && !zones[2]) return 4;
        return 0;
    }

    /**
     * Given:
     * @param pos a zone
     * @param activated true or false
     * Sets the zone to true or false, according to the value of activated
     */
    public void setZones(int pos, Boolean activated) {
        zones[pos] = activated;
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

    /**
     * Receives:
     * @param solo
     * @param zones
     * @param simplified
     * @param game
     * @param gameSimplified
     * @return a string that represents the FaithTrack (the position of all the players). It can be the FaithTrackSimplified, the normal FaithTrack (called in the local mode) or the FaithTrack for single player.
     */
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

    /**
     * notifies the observers by sending a message that contains the actual internal status of the FaithTrack.
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     *
     * @return the actual message passed by the notifyObservers() method that contains the status of the FaithTrack.
     */
    public MSG_UPD_FaithTrack generateMessage() {
        return new MSG_UPD_FaithTrack(zones);
    }


}