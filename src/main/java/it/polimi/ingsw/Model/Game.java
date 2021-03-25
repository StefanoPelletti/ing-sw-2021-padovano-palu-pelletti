package it.polimi.ingsw.Model;

import java.util.ArrayList;

public class Game {
    private Status status;
    int num_of_players;
    int turn;

    private final ArrayList<Player> playerList;
    private final LeaderCardsDeck leaderCardsDeck;
    private final Market market;
    private final FaithTrack faithTrack;
    private final DevelopmentCardsDeck developmentCardsDeck;



    public Game()
    {
        status = Status.INIT;
        num_of_players = 0;
        playerList = new ArrayList<Player>();
        leaderCardsDeck = new LeaderCardsDeck();
        market = new Market();
        faithTrack = new FaithTrack(this);
        developmentCardsDeck = new DevelopmentCardsDeck();
    }

    public void changeStatus(Status status) { this.status = status; }
    public Status getStatus() { return this.status; }

    public ArrayList<Player> getPlayerList() {
        return new ArrayList<Player>(playerList);
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
        boolean result = playerList.removeIf(x -> (x.getNickname()).equals(nickname));
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
