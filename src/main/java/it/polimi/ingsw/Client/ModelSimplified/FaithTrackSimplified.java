package it.polimi.ingsw.Client.ModelSimplified;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_FaithTrack;
import it.polimi.ingsw.Server.Controller.FaithTrackInfo;

import java.util.Arrays;

public class FaithTrackSimplified {
    private Integer[] positions;
    private boolean[] zones;
    private String[] nicknames;

    public FaithTrackSimplified()
    {
        positions = new Integer[4];
        positions[0] = positions[1] = positions[2] = positions[3] = null;
        zones = new boolean[3];
        nicknames = new String[] { null, null, null, null };
    }

    public void update(MSG_UPD_FaithTrack message)
    {
        FaithTrackInfo faithTrackInfo = message.getFaithTrackInfo();
        for ( int i = 0 ; i < 3; i++)
            this.zones[i] = faithTrackInfo.getZones()[i];

        for ( int i = 0; i < 4; i++)
        {
            if(faithTrackInfo.getPresentPlayers()[i])
                positions[i] = faithTrackInfo.getPosition()[i];
        }

        ////////////////////////////// update nicknames
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder("");
        int i = 1;

        result.append("  FaithTrack: ");
        for(Integer pos : positions) {
            result.append(nicknames[pos]).append(" is at position: ").append(positions[pos]);
        }
        result.append(" --------------------------- ");
        for(boolean b : zones) {
            if(!b) break;
            result.append("La zona").append(i).append("è stata attivata");
            i++;
        }
        return result.toString();
    }


}

