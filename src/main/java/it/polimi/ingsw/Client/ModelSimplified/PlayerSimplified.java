package it.polimi.ingsw.Client.ModelSimplified;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_Player;
import it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate.MSG_UPD_DevSlot;
import it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate.MSG_UPD_Extradepot;
import it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate.MSG_UPD_Strongbox;
import it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate.MSG_UPD_WarehouseDepot;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Model.LeaderCard;
import it.polimi.ingsw.Server.Model.SpecialAbilities.ExtraDepot;

public class PlayerSimplified {
    private final StrongboxSimplified strongbox;
    private final WarehouseDepotSimplified warehouseDepot;
    private final DevelopmentSlotSimplified developmentSlot;
    private int VP;
    private int playerNumber;
    private String nickname;
    private int position;
    private LeaderCard[] leaderCards;

    public PlayerSimplified(int playerNumber) {
        this.VP = 0;
        this.playerNumber = playerNumber;
        this.nickname = "";
        this.position = 0;
        this.leaderCards = null;
        this.strongbox = new StrongboxSimplified();
        this.warehouseDepot = new WarehouseDepotSimplified();
        this.developmentSlot = new DevelopmentSlotSimplified();
    }

    public void update(MSG_UPD_Player message) {
        int newVP = message.getVP();
        int newPlayerNumber = message.getPlayerNumber();
        String newNickname = message.getNickname();
        int newPosition = message.getPosition();
        LeaderCard[] newLeaderCards = message.getLeaderCards();

        this.VP = newVP;
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

    public int getVP() {
        return VP;
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