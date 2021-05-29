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
     * @param game The Game reference.
     */
    public FaithTrack(Game game) {
        this.game = game;
        zones = new boolean[3];
    }

    /**
     * Gets the position of the player and calls the other method.
     * @param player The Player reference.
     * @return A number for all the zones:
     *   - 0 would not activate anything
     *   - 1 would activate the first zone (zone 1)
     *   - 2 would activate the second zone (zone 2)
     *   - 3 would activate the third zone (zone 3).
     * @see #doesActivateZone(int)
     */
    public int doesActivateZone(Player player) {
        int p = player.getPosition() + 1;
        return doesActivateZone(p);
    }

    /**
     * This method is used to know what would happen in a specific position of the FaithTrack if a Player advances.
     * @param p A specific position of a Player (or even Lorenzo).
     * @return The number for the zone if one would activate, 0 if no zone would be activated, -1 if the position is greater than 24 (impossible).
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
     * @param player The Player reference.
     * @return True if the player advanced, False if the position of the player is the last one.
     */
    public boolean advance(Player player) {
        if (player.getPosition() == 24) return false;
        player.setPosition(player.getPosition() + 1);
        return true;
    }

    /**
     * Increments the position of Lorenzo by 1.
     * @return True if Lorenzo advanced, False if its position is the last one.
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
     * @param player The Player reference.
     * @return The number of points given by each zone:
     *   - 0 if the player was outside the activated zone
     *   - 2 if the player was inside the first zone
     *   - 3 if the player was inside the second zone.
     *   - 4 if the player was inside the third zone.
     */
    public int calculateVP(Player player) {
        int p = player.getPosition();
        if (p >= 5 && p <= 8 && !zones[0]) return 2;
        if (p >= 12 && p <= 16 && !zones[1]) return 3;
        if (p >= 19 && p <= 24 && !zones[2]) return 4;
        return 0;
    }

    /**
     * Sets the specified zone to True or False, according to the value of activated.
     * Also notifies its observers.
     * @param zoneNumber The number of the zone.
     * @param activated True if the zone if being activated.
     */
    public void setZones(int zoneNumber, Boolean activated) {
        zones[zoneNumber] = activated;
        notifyObservers();
    }

    /**
     * Sets all the zones back to False.
     * Also notifies observers.
     */
    public void resetZones() {
        zones[0] = zones[1] = zones[2] = false;
        notifyObservers();
    }

    /**
     * Returns a new 3-cell boolean array containing the current status of the zones.
     * @return A new 3-cell boolean array containing the current status of the zones.
     */
    public boolean[] getZones() {
        boolean[] result = new boolean[3];
        System.arraycopy(zones, 0, result, 0, 3);
        return result;
    }

    public String toString(boolean solo) {
        return FaithTrack.toString(solo, this.zones, false, this.game, null);
    }

    /**
     * Returns a String that represents the current status of FaithTrack, with the zones and the position of the Players.
     * Can work in Solo mode, if specified by the solo parameter.
     * A FaithTrack or a FaithTrackSimplified may use this shared method by passing their internal values.
     * @param solo True if the Game is in Solo mode.
     * @param zones The internal values of the FaithTrack zones.
     * @param simplified True if this method is called by a FaithTrackSimplified, False if this method is called by a FaithTrack.
     * @param game The reference to the Game, if simplified was False.
     * @param gameSimplified The reference to the GameSimplified, if simplified was True.
     * @return A String representing the current status of the FaithTrack.
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