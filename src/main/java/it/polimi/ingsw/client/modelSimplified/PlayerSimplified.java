package it.polimi.ingsw.client.modelSimplified;

import it.polimi.ingsw.networking.message.updateMessages.MSG_UPD_Player;
import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_DevSlot;
import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_Extradepot;
import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_Strongbox;
import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_WarehouseDepot;
import it.polimi.ingsw.server.model.LeaderCard;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.model.specialAbilities.ExtraDepot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlayerSimplified {
    private final StrongboxSimplified strongbox;
    private final WarehouseDepotSimplified warehouseDepot;
    private final DevelopmentSlotSimplified developmentSlot;
    private final boolean[] faithTrackPanels;
    private int playerNumber;
    private String nickname;
    private int position;
    private LeaderCard[] leaderCards;


    public PlayerSimplified(int playerNumber) {
        this.faithTrackPanels = new boolean[3];
        this.playerNumber = playerNumber;
        this.nickname = "";
        this.position = 0;
        this.leaderCards = null;
        this.strongbox = new StrongboxSimplified();
        this.warehouseDepot = new WarehouseDepotSimplified();
        this.developmentSlot = new DevelopmentSlotSimplified();
    }

    /**
     * Updates this Player to reflect the same status inside the server model.
     *
     * @param message the UpdateMessage that will update this Object internal status.
     */
    public void update(MSG_UPD_Player message) {
        boolean[] newFaithTrackPanels = message.getFaithTrackPanels();
        int newPlayerNumber = message.getPlayerNumber();
        String newNickname = message.getNickname();
        int newPosition = message.getPosition();
        LeaderCard[] newLeaderCards = message.getLeaderCards();

        System.arraycopy(newFaithTrackPanels, 0, this.faithTrackPanels, 0, 3);
        this.playerNumber = newPlayerNumber;
        this.nickname = newNickname;
        this.position = newPosition;
        this.leaderCards = new LeaderCard[2];
        this.leaderCards[0] = newLeaderCards[0];
        this.leaderCards[1] = newLeaderCards[1];
    }

    public void updateExtradepot(MSG_UPD_Extradepot message) {
        Resource resource = message.getResource();
        int quantity = message.getNumber();
        for (int i = 0; i < 2; i++) {
            if (leaderCards[i] != null) {
                if (leaderCards[i].getSpecialAbility().isExtraDepot()) {
                    ExtraDepot depot = (ExtraDepot) leaderCards[i].getSpecialAbility();
                    if (depot.getResourceType() == resource)
                        depot.setResource(quantity);
                }
            }
        }
    }

    public void updateStrongbox(MSG_UPD_Strongbox message) {
        this.strongbox.update(message);
    }

    public void updateWarehouseDepot(MSG_UPD_WarehouseDepot message) {
        this.warehouseDepot.update(message);
    }

    public void updateDevelopmentSlot(MSG_UPD_DevSlot message) {
        this.developmentSlot.update(message);
    }

    public String getNickname() {
        return this.nickname;
    }

    public int getPosition() {
        return this.position;
    }

    public int getPlayerNumber() {
        return this.playerNumber;
    }

    public boolean[] getFaithTrackPanels() {
        return this.faithTrackPanels;
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

    public List<LeaderCard> getCardsWithExtraDepotAbility() {
        List<LeaderCard> result = new ArrayList<>();
        if (leaderCards[0] != null && leaderCards[0].getSpecialAbility().isExtraDepot() && leaderCards[0].isEnabled())
            result.add(leaderCards[0]);
        if (leaderCards[1] != null && leaderCards[1].getSpecialAbility().isExtraDepot() && leaderCards[1].isEnabled())
            result.add(leaderCards[1]);
        return result;
    }

    public LeaderCard[] getLeaderCards() {
        return leaderCards;
    }

    public StrongboxSimplified getStrongbox() {
        return strongbox;
    }

    public WarehouseDepotSimplified getWarehouseDepot() {
        return warehouseDepot;
    }

    public DevelopmentSlotSimplified getDevelopmentSlot() {
        return developmentSlot;
    }
}