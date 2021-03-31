package it.polimi.ingsw.Model;

public class Player {
    private final String nickname;
    private int VP;
    private int position;   //used for FaithTrack tracking
    private int playerNumber;

    private final Strongbox strongbox;
    private final WarehouseDepot depot;
    private LeaderCard[] leaderCards;
    private final DevelopmentSlot developmentSlot;

    public Player( String nickname, int playerNumber )
    {
        this.nickname = nickname;
        this.VP = 0;
        this.position = 1;
        this.playerNumber = playerNumber;
        LeaderCard[] leaderCards = new LeaderCard[2];
        strongbox = new Strongbox();
        depot = new WarehouseDepot();
        developmentSlot = new DevelopmentSlot();

    }

    public String getNickname() { return nickname; }
    public int getVP() { return VP; }
    public void setVP(int VP) { this.VP = VP; }
    public void addVP(int VP) { this.VP = this.VP + VP; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public int getPlayerNumber() { return playerNumber; }
    public void setPlayerNumber(int playerNumber) { this.playerNumber = playerNumber; }

    public Strongbox getStrongbox() { return this.strongbox; }
    public WarehouseDepot getDepot() { return this.depot; }
    public LeaderCard[] getLeaderCards() {
        LeaderCard[] result = new LeaderCard[2];
        result[0] = leaderCards[0];
        result[1] = leaderCards[1];
        return result;
    }
    public DevelopmentSlot getDevelopmentSlot() { return this.developmentSlot; }

    public void associateLeaderCards(LeaderCard L1, LeaderCard L2)
    {
        leaderCards[0] = L1;
        leaderCards[1] = L2;
    }
}
