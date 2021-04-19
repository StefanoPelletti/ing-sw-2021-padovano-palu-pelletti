package it.polimi.ingsw.Server.Controller;

import java.io.Serializable;

public class FaithTrackInfo implements Serializable {
    private final boolean[] presentPlayers;    // if presentPlayer[0] is true, then player #1 is present
    private final int[] position;              // check only if ^^ is true. Otherwise (false) it will be DEFAULT value (-1)
    private final boolean[] zones;


    public FaithTrackInfo()
    {
        presentPlayers = new boolean[4];
        presentPlayers[0] = presentPlayers[1] = presentPlayers[2] = presentPlayers[3] = false;
        position = new int[4];
        position[0] = position[1] = position[2] = position[3] = -1;
        zones = new boolean[3];
    }

    // if this method is called passing p.getPlayerNumber(), which returns (f.ex) a TWO
    // then presentPlayers[1] (offset) will be set to TRUE and the corresponding position[1] will be set to positionOnTrack
    // will result in a false if input values are out of range
    public boolean setPlayerNumberOffsetPosition(int playerNumberWithoutOffset, int positionOnTrack)
    {
        if ( playerNumberWithoutOffset < 1 || playerNumberWithoutOffset > 4) return false;
        if ( positionOnTrack < 1 || positionOnTrack > 24 ) return false;
        presentPlayers[playerNumberWithoutOffset-1] = true;
        position[playerNumberWithoutOffset-1] = positionOnTrack;
        return true;
    }

    //note that zones would be: Zone1 #0, Zone2 #1, Zone3 #2 (same as in faithTrack)
    public boolean setZones( int zoneNumber )
    {
        if (zoneNumber < 0 || zoneNumber > 2) return false;
        zones[zoneNumber] = true;
        return true;
    }

    public boolean setZones( boolean[] zones)
    {
        if (!( ((!zones[1])&&(!zones[2])) || ((zones[0])&&(zones[1])) )) return false;
        System.arraycopy(zones, 0, this.zones, 0, 3);
        return true;
    }

    public boolean[] getPresentPlayers() { return this.presentPlayers; }
    public int[] getPosition() { return this.position; }
    public boolean[] getZones() { return this.zones; }
}
