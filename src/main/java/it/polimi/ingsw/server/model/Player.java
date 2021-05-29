package it.polimi.ingsw.server.model;


import it.polimi.ingsw.networking.message.MSG_NOTIFICATION;
import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_Player;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.model.specialAbilities.ExtraDepot;
import it.polimi.ingsw.server.utils.ModelObservable;

import java.util.*;

public class Player extends ModelObservable {
    private final String nickname;
    private final Strongbox strongbox;
    private final WarehouseDepot warehouseDepot;
    private final LeaderCard[] leaderCards;
    private final DevelopmentSlot developmentSlot;
    private int vp;
    private int position;   //used for FaithTrack tracking
    private int playerNumber;
    private boolean permittedAction;

    private boolean disconnectedBeforeLeaderCard;
    private boolean disconnectedBeforeResource;

    private List<LeaderCard> startingCards;
    private int startingResources;

    /**
     * Constructor of the Player class.
     * @param nickname The nickname for the new Player.
     * @param playerNumber The number for the new Player.
     */
    public Player(String nickname, int playerNumber) {
        this.nickname = nickname;
        this.vp = 0;
        this.position = 0;
        this.playerNumber = playerNumber;
        this.leaderCards = new LeaderCard[2];
        this.strongbox = new Strongbox();
        this.warehouseDepot = new WarehouseDepot();
        this.developmentSlot = new DevelopmentSlot();
        this.permittedAction = false;
    }

    public List<LeaderCard> getStartingCards() {
        return this.startingCards;
    }

    /**
     * Sets the starting Leader Cards for the Player.
     * In the standard game rules, this method is called in initialization phase, and is given a List of cards from
     * LeaderCardsDeck.pickFourCards()
     * @param startingCards The assigned Leader Cards.
     */
    public void setStartingCards(List<LeaderCard> startingCards) {
        this.startingCards = startingCards;
    }

    public int getStartingResources() {
        return this.startingResources;
    }

    /**
     * Sets the startingResources for the player:
     *   - 1 resource if the player is the second or third
     *   - 2 resources if the player is the fourth.
     */
    public void setInitialStartingResources() {
        this.startingResources = 0;
        if (this.playerNumber == 2 || this.playerNumber == 3)
            this.startingResources = 1;
        if (this.playerNumber == 4)
            this.startingResources = 2;
    }

    /**
     * Decrements the startingResource of the Player.
     * This method should only be called in the actionManager.chooseResource() method.
     */
    public void decrementStartingResource() {
        this.startingResources--;
    }

    public String getNickname() {
        return nickname;
    }

    public int getVp() {
        return vp;
    }

    public boolean setVP(int vp) {
        if (vp < 0) return false;
        this.vp = vp;
        notifyObservers();
        return true;
    }

    public boolean addVP(int vp) {
        if (vp < 0) return false;
        this.vp = this.vp + vp;
        notifyObservers();
        return true;
    }

    /**
     * Reminds that the Player has performed a main move and cannot do anymore.
     */
    public void setAction() {
        this.permittedAction = true;
    }

    public void resetPermittedAction() {
        this.permittedAction = false;
    }

    public boolean getAction() {
        return this.permittedAction;
    }

    public int getPosition() {
        return position;
    }

    public boolean setPosition(int position) {
        if (position < 0) return false;
        this.position = position;
        notifyObservers();
        notifyMovement();
        return true;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    /**
     * Sets the LeaderCard enabled field to True if it has been activated or sets the LeaderCard to null if it has been discarded.
     * @param cardNumber The number of the card.
     * @param enable The status of the LeaderCard. True if it is being enabled, False if it being discarded.
     */
    public void setLeaderCards(int cardNumber, boolean enable) {
        if (enable)
            leaderCards[cardNumber].setEnabled(true);
        else
            leaderCards[cardNumber] = null;
        notifyObservers();
    }

    public Strongbox getStrongbox() {
        return this.strongbox;
    }

    public WarehouseDepot getWarehouseDepot() {
        return this.warehouseDepot;
    }

    public LeaderCard[] getLeaderCards() {
        LeaderCard[] result = new LeaderCard[2];
        result[0] = leaderCards[0];
        result[1] = leaderCards[1];
        return result;
    }

    public DevelopmentSlot getDevelopmentSlot() {
        return this.developmentSlot;
    }

    /**
     * Associates the LeaderCards to the Player.
     * Also notifies observers.
     * @param cards The newly assigned List of Leader Cards.
     */
    public void associateLeaderCards(List<LeaderCard> cards) {
        leaderCards[0] = cards.get(0);
        leaderCards[1] = cards.get(1);
        notifyObservers();
    }

    /**
     * Returns the total amount of resources that the Player owns in his depot, extraDepots and Strongbox.
     * @return The total amount of resources that the Player owns in his depot, extraDepots and Strongbox.
     */
    public int getTotal() {
        int result = warehouseDepot.getTotal();
        result += strongbox.getTotal();
        for (LeaderCard l : getCardsWithExtraDepotAbility()) {
            ExtraDepot extraDepot = (ExtraDepot) l.getSpecialAbility();
            result += extraDepot.getNumber();
        }
        return result;
    }

    /**
     * Returns a map of the resources that the Player owns in his depot, extraDepots and Strongbox.
     * @return A map of the resources that the Player owns in his depot, extraDepots and Strongbox.
     */
    public Map<Resource, Integer> getResources() {
        Map<Resource, Integer> resources = this.warehouseDepot.getResources();
        Set<Resource> possibleResources = resources.keySet();
        for (Resource resource : possibleResources) {
            if (this.strongbox.getQuantity(resource) != null) {
                resources.merge(resource, this.strongbox.getQuantity(resource), Integer::sum);
            }
        }
        for (LeaderCard l : this.getCardsWithExtraDepotAbility()) {
            ExtraDepot depotAbility = (ExtraDepot) l.getSpecialAbility();
            Resource extraResource = depotAbility.getResourceType();
            resources.merge(extraResource, depotAbility.getNumber(), Integer::sum);
        }
        return resources;
    }

    /**
     * Returns a map of the resources that the Player owns in his depot and extraDepot. Not in his Strongbox.
     * @return A map of the resources that the Player owns in his depot and extraDepot. Not in his Strongbox.
     */
    public Map<Resource, Integer> getDepotAndExtraDepotResources() {
        Map<Resource, Integer> result;
        result = this.warehouseDepot.getResources();
        List<LeaderCard> playerLeaderCards = this.getCardsWithExtraDepotAbility();
        for (LeaderCard l : playerLeaderCards) {
            ExtraDepot ability = (ExtraDepot) l.getSpecialAbility();
            Resource resource = ability.getResourceType();
            result.merge(resource, ability.getNumber(), Integer::sum);
        }
        return result;
    }

    /**
     * Returns a List of Leader Cards whose SpecialAbility is a DiscountResourceAbility.
     * May return an empty List.
     * @return The List of Leader Cards whose SpecialAbility is a DiscountResourceAbility.
     */
    public List<LeaderCard> getCardsWithDiscountResourceAbility() {
        List<LeaderCard> result = new ArrayList<>();
        if (leaderCards[0] != null && leaderCards[0].getSpecialAbility().isDiscountResource() && leaderCards[0].isEnabled())
            result.add(leaderCards[0]);
        if (leaderCards[1] != null && leaderCards[1].getSpecialAbility().isDiscountResource() && leaderCards[1].isEnabled())
            result.add(leaderCards[1]);
        return result;
    }

    /**
     * Returns a List of Leader Cards whose SpecialAbility is a ExtraDepot.
     * May return an empty List.
     * @return The List of Leader Cards whose SpecialAbility is a ExtraDepot.
     */
    public List<LeaderCard> getCardsWithExtraDepotAbility() {
        List<LeaderCard> result = new ArrayList<>();
        if (leaderCards[0] != null && leaderCards[0].getSpecialAbility().isExtraDepot() && leaderCards[0].isEnabled())
            result.add(leaderCards[0]);
        if (leaderCards[1] != null && leaderCards[1].getSpecialAbility().isExtraDepot() && leaderCards[1].isEnabled())
            result.add(leaderCards[1]);
        return result;
    }

    /**
     * Returns a List of Leader Cards whose SpecialAbility is a ProductionAbility.
     * May return an empty List.
     * @return The List of Leader Cards whose SpecialAbility is a ProductionAbility.
     */
    public List<LeaderCard> getCardsWithProductionAbility() {
        List<LeaderCard> result = new ArrayList<>();
        if (leaderCards[0] != null && leaderCards[0].getSpecialAbility().isProduction() && leaderCards[0].isEnabled())
            result.add(leaderCards[0]);
        if (leaderCards[1] != null && leaderCards[1].getSpecialAbility().isProduction() && leaderCards[1].isEnabled())
            result.add(leaderCards[1]);
        return result;
    }

    /**
     * Returns a List of Leader Cards whose SpecialAbility is a MarketResourceAbility.
     * May return an empty List.
     * @return The List of Leader Cards whose SpecialAbility is a MarketResourceAbility.
     */
    public List<LeaderCard> getCardsWithMarketResourceAbility() {
        List<LeaderCard> result = new ArrayList<>();
        if (leaderCards[0] != null && leaderCards[0].getSpecialAbility().isMarketResource() && leaderCards[0].isEnabled())
            result.add(leaderCards[0]);
        if (leaderCards[1] != null && leaderCards[1].getSpecialAbility().isMarketResource() && leaderCards[1].isEnabled())
            result.add(leaderCards[1]);
        return result;
    }

    public boolean isDisconnectedBeforeLeaderCard() {
        return disconnectedBeforeLeaderCard;
    }

    public void setDisconnectedBeforeLeaderCard(boolean disconnectedBeforeLeaderCard) {
        this.disconnectedBeforeLeaderCard = disconnectedBeforeLeaderCard;
    }

    public boolean isDisconnectedBeforeResource() {
        return disconnectedBeforeResource;
    }

    public void setDisconnectedBeforeResource(boolean disconnectedBeforeResource) {
        this.disconnectedBeforeResource = disconnectedBeforeResource;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Player)) return false;
        Player o = (Player) obj;
        return (this.nickname.equals(o.nickname) && this.playerNumber==o.playerNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.nickname, this.playerNumber);
    }


    private void notifyMovement() {
        this.notifyObservers(new MSG_NOTIFICATION(this.nickname + " has advanced on the Faith Track! Now at position: " + this.position));
    }

    /**
     * Creates a message using generateMessage() and notifies observers.
     * @see #generateMessage()
     */
    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    /**
     * Returns a MSG_UPD_Player representing the current state of the Player.
     * @return A MSG_UPD_Player representing the current state of the Player.
     */
    public MSG_UPD_Player generateMessage() {
        return new MSG_UPD_Player(
                vp,
                playerNumber,
                nickname,
                position,
                leaderCards
        );
    }
}