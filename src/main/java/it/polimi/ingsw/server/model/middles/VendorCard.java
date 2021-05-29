package it.polimi.ingsw.server.model.middles;

import it.polimi.ingsw.server.model.DevelopmentCard;
import java.io.Serializable;

public class VendorCard implements Serializable {
    private final DevelopmentCard card;
    private final boolean slot1;
    private final boolean slot2;
    private final boolean slot3;

    /**
     * CONSTRUCTOR
     * @param card The card eligible for a sell to the player who asked to buy a card.
     * @param slot1 True if and only if card can be placed in slot 1 of the player.
     * @param slot2 True if and only if card can be placed in slot 2 of the player.
     * @param slot3 True if and only if card can be placed in slot 3 of the player.
     */
    public VendorCard(DevelopmentCard card, boolean slot1, boolean slot2, boolean slot3) {
        this.card = card;
        this.slot1 = slot1;
        this.slot2 = slot2;
        this.slot3 = slot3;
    }

    public DevelopmentCard getCard() {
        return card;
    }

    public boolean isSlot1() {
        return slot1;
    }

    public boolean isSlot2() {
        return slot2;
    }

    public boolean isSlot3() {
        return slot3;
    }

    public boolean isSlot(int slot){
        switch (slot) {
            case 0:
                return slot1;
            case 1:
                return slot2;
            case 2:
                return slot3;
            default:
                return false;
        }
    }
}
