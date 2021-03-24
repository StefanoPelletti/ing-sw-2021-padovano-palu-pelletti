package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Game {
    private Status status;
    int num_of_players;

    private ArrayList<Player> playerList;
    private LeaderCardsDeck leaderCardsDeck;
    private Market market;
    private FaithTrack faithTrack;



    public Game()
    {
        status = Status.INIT;
        num_of_players = 0;
        playerList = new ArrayList<Player>();
        leaderCardsDeck = new LeaderCardsDeck();
        market = new Market();
        faithTrack = new FaithTrack(this);

    }

    public void changeStatus(Status status) { this.status = status; }
    public Status getStatus() { return this.status; }

    public ArrayList<Player> getPlayerList() {
        ArrayList<Player> newList = new ArrayList<Player>(playerList);
        return newList;
    }

    public void addPlayer( String nickname )
    {
        if(num_of_players < 4) {
            playerList.add(new Player(nickname, num_of_players));
            num_of_players++;
        }
        else
        {
            System.out.println("Max number of player reached!");
        }
    }

    public void removePlayer( String nickname )
    {
        Boolean result = playerList.removeIf(x -> (x.getNickname()).equals(nickname));
        if ( result )
        {
            for ( int i = 0; i < playerList.size(); i++)
                playerList.get(i).setPlayerNumber(i);
        }
        else
        {
            System.out.println("No player with such nickname found!");
        }
    }

    public void advanceOnTrack( Player player )
    {
          switch ( faithTrack.doesActivateZone(player) )
          {
                case 0:
                    faithTrack.advance( player );
                    break;
                case 1:
                    faithTrack.advance( player );
                    for ( Player p : playerList )
                        p.addVP(faithTrack.calculateVP(p));
                    faithTrack.setZones(0, true);
                    break;
                case 2:
                    faithTrack.advance( player );
                    for ( Player p : playerList )
                        p.addVP(faithTrack.calculateVP(p));
                    faithTrack.setZones(1, true);
                    break;
                case 3:
                    faithTrack.advance( player );
                    for ( Player p : playerList )
                        p.addVP(faithTrack.calculateVP(p));
                    faithTrack.setZones(2, true);
                    changeStatus(Status.LAST_TURN);
                    break;
                case -1:
                    return;
            }
        }


}
