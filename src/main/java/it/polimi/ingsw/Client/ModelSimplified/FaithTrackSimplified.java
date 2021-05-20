package it.polimi.ingsw.Client.ModelSimplified;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_FaithTrack;
import it.polimi.ingsw.Server.Utils.Displayer;

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
        return Displayer.faithTrackToString(solo, this.zones, true, null, this.game);
    }
}