package it.polimi.ingsw.client.modelSimplified;

import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_FaithTrack;
import it.polimi.ingsw.server.model.FaithTrack;

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

    public boolean[] getZones() { return this.zones; }

    public String toString(boolean solo) {
        return FaithTrack.toString(solo, this.zones, true, null, this.game);
    }
}