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

    public void setStartingCards(List<LeaderCard> startingCards) {
        this.startingCards = startingCards;
    }

    public int getStartingResources() {
        return this.startingResources;
    }

    public void setInitialStartingResources() {
        this.startingResources = 0;
        if (this.playerNumber == 2 || this.playerNumber == 3)
            this.startingResources = 1;
        if (this.playerNumber == 4)
            this.startingResources = 2;
    }

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

    public void associateLeaderCards(List<LeaderCard> cards) {
        leaderCards[0] = cards.get(0);
        leaderCards[1] = cards.get(1);
        notifyObservers();
    }

    public int getTotal() {
        int result = warehouseDepot.getTotal();
        result += strongbox.getTotal();
        for (LeaderCard l : getCardsWithExtraDepotAbility()) {
            ExtraDepot extraDepot = (ExtraDepot) l.getSpecialAbility();
            result += extraDepot.getNumber();
        }
        return result;
    }

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

    public List<LeaderCard> getCardsWithDiscountResourceAbility() {
        List<LeaderCard> result = new ArrayList<>();
        if (leaderCards[0] != null && leaderCards[0].getSpecialAbility().isDiscountResource() && leaderCards[0].isEnabled())
            result.add(leaderCards[0]);
        if (leaderCards[1] != null && leaderCards[1].getSpecialAbility().isDiscountResource() && leaderCards[1].isEnabled())
            result.add(leaderCards[1]);
        return result;
    }

    public List<LeaderCard> getCardsWithExtraDepotAbility() {
        List<LeaderCard> result = new ArrayList<>();
        if (leaderCards[0] != null && leaderCards[0].getSpecialAbility().isExtraDepot() && leaderCards[0].isEnabled())
            result.add(leaderCards[0]);
        if (leaderCards[1] != null && leaderCards[1].getSpecialAbility().isExtraDepot() && leaderCards[1].isEnabled())
            result.add(leaderCards[1]);
        return result;
    }

    public List<LeaderCard> getCardsWithProductionAbility() {
        List<LeaderCard> result = new ArrayList<>();
        if (leaderCards[0] != null && leaderCards[0].getSpecialAbility().isProduction() && leaderCards[0].isEnabled())
            result.add(leaderCards[0]);
        if (leaderCards[1] != null && leaderCards[1].getSpecialAbility().isProduction() && leaderCards[1].isEnabled())
            result.add(leaderCards[1]);
        return result;
    }

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
        return (this.nickname.equals(o.nickname));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.nickname, this.position);
    }

    @Deprecated
    public boolean deepEquals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Player)) return false;
        Player o = (Player) obj;
        return (this.nickname.equals(o.nickname) &&
                this.vp == o.vp &&
                this.position == o.position &&
                this.playerNumber == o.playerNumber &&
                Arrays.equals(this.leaderCards, o.leaderCards) &&
                this.strongbox.equals(o.strongbox) &&
                this.warehouseDepot.equals(o.warehouseDepot) &&
                this.developmentSlot.equals(o.developmentSlot));
    }

    private void notifyMovement() {
        this.notifyObservers(new MSG_NOTIFICATION(this.nickname + " has advanced on the Faith Track! Now at position: " + this.position));
    }

    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

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