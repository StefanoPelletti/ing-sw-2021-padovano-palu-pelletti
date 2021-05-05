package it.polimi.ingsw.Client.ModelSimplified;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_FaithTrack;
import java.util.*;

import java.util.Arrays;

public class FaithTrackSimplified {
    private boolean[] zones;
    private GameSimplified game;

    public FaithTrackSimplified(GameSimplified game)
    {
        zones = new boolean[3];
        this.game = game;
    }

    public void update(MSG_UPD_FaithTrack message)
    {
        boolean[] newZones = message.getZones();

        for ( int i = 0 ; i < 3; i++)
            this.zones[i] = newZones[i];
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder("");
        int i = 1;
        List<PlayerSimplified> playerList = game.getPlayerList();
        result.append("  FaithTrack: ");

        for(PlayerSimplified player : playerList) {
            result.append(player.getNickname()).append(" is at position: ").append(player.getPosition());
        }
        result.append(" --------------------------- ");
        if(zones[2])
            result.append(" Third and last zone has been activated!");
        else {
            if (zones[1])
                result.append(" Second zone has been activated!");
            else {
                if (zones[0])
                    result.append(" The First zone has been activated!");
                else
                    result.append(" No zone has been activated yet!");
            }
        }
        return result.toString();
    }


}

