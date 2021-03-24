package it.polimi.ingsw.Model;

public class Player {
    private final String nickname;
    private int VP;
    //private Strongbox strongbox;
    //private WarehouseDepot depot;
    private LeaderCard[] leaderCards;
    //private DevelopmentSlot developmentSlot;
    private int position;   //used for FaithTrack tracking
    private final int playerNumber;

    public Player( String nickname, int FaithPoints, int playerNumber ) //universal constructor, playerNumbers can be 1..4, FaithPoints can be 0 or 1
    {
        this.nickname = nickname;
        this.VP = 0;
        this.position = 1 + FaithPoints;
        this.playerNumber = playerNumber;
        LeaderCard[] leaderCards = new LeaderCard[2];
        /* Depot, StrongBox, LeaderCards */
    }

    public String getNickname()             { return nickname; }
    public int getVP()                      { return VP; }
    public void setVP(int VP)               { this.VP = VP; }
    public void addVP(int VP)               { this.VP = this.VP + VP; }
    public int getPlayerNumber()            { return playerNumber; }
    public int getPosition()                { return position; }
    public void setPosition(int position)   { this.position = position; }

    private void associateLeaderCards(LeaderCard L1, LeaderCard L2)
    {
        leaderCards[0] = L1;
        leaderCards[1] = L2;
    }
}
