package it.polimi.ingsw.Model;

public class FaithTrack {
    private Boolean[] zones;
    private Game game;
    public FaithTrack(Game game) {
        this.game = game;
        zones = new Boolean[3];
        zones[0] = zones[1] = zones[2] = false;
    }

    static void advance( Player player )
    {

        if ( Game.status == Status.STANDARD_TURN )
        {
            if ( player.getPosition() == 24) return;
            player.setPosition(player.getPosition() + 1);
            if ( player.getPosition() == 8) //activate first Pope Space!
            {
                for ( Player p : Game.getPlayerList())
            }
        }

    }

   /* static void advance( Player player, int number)
    {

    } */
}
