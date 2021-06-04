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
     * <ul>
     * <li> 0 would not activate anything
     * <li> 1 would activate the first zone (zone 1)
     * <li> 2 would activate the second zone (zone 2)
     * <li> 3 would activate the third zone (zone 3).
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
     * <ul>
     * <li> 0 if the player was outside the activated zone
     * <li> 2 if the player was inside the first zone
     * <li> 3 if the player was inside the second zone.
     * <li> 4 if the player was inside the third zone.
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