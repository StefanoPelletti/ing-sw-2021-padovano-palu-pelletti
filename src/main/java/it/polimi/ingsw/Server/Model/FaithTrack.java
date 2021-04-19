package it.polimi.ingsw.Server.Model;

public class FaithTrack {
    private boolean[] zones;
    private Game game;
    public FaithTrack(Game game) {
        this.game = game;
        zones = new boolean[3];
        zones[0] = zones[1] = zones[2] = false;
    }


//auxiliary method: -1 is error,
//3 would activate the last zone (zone 3)
//2 would activate the second zone (zone 2)
//1 would activate the first zone (zone 1)
//0 would not activate anything
    public int doesActivateZone( Player player )
    {
        int p = player.getPosition()+1;
        if ( p > 24 ) return -1;
        if ( p == 24  && zones[2] == false ) return 3;
        if ( p == 16 && zones[1] == false ) return 2;
        if ( p == 8 && zones[0] == false ) return 1;
        return 0;
    }

    //overload of the previous method
    //it is used to know what would happen in a specific position of the FaithTrack
    public int doesActivateZone(int p) {
        if ( p > 24 ) return -1;
        if ( p == 24  && zones[2] == false ) return 3;
        if ( p == 16 && zones[1] == false ) return 2;
        if ( p == 8 && zones[0] == false ) return 1;
        return 0;
    }

    public boolean advance( Player player )
    {
        if (player.getPosition()==24) return false;
        player.setPosition(player.getPosition()+1);
        return true;
    }

    public boolean advanceLorentz()
    {
        if(game.getBlackCrossPosition() == 24) { return false; }
        game.setBlackCrossPosition(game.getBlackCrossPosition()+1);
        return true;
    }

//result is the number of points that
    public int calculateVP( Player player )
    {
        int p = player.getPosition();
        if ( p>=5 && p<=8 && zones[0]==false ) return 2;
        if ( p>=12 && p<=16 && zones[1]==false ) return 3;
        if ( p>=19 && p<=24 && zones[2]==false ) return 4;
        return 0;
    }

    public void setZones( int pos, Boolean value )
    {
        zones[pos] = value;
    }

    public void resetZones()
    {
        zones[0] = zones[1] = zones[2] = false;
    }
    public boolean[] getZones() {
        boolean[] result = new boolean[3];
        System.arraycopy(zones, 0, result, 0, 3);
        return result;
    }

}
