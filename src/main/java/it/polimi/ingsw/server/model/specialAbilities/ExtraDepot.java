package it.polimi.ingsw.server.model.specialAbilities;

import it.polimi.ingsw.networking.message.updateMessages.playerUpdate.MSG_UPD_Extradepot;
import it.polimi.ingsw.server.model.enumerators.Resource;
import it.polimi.ingsw.server.utils.ModelObservable;

import java.io.Serializable;
import java.util.Objects;

public class ExtraDepot extends ModelObservable implements SpecialAbility, Serializable {
    private final Resource resource;
    private int number;

    public ExtraDepot(Resource resource) {
        this.resource = resource;
        this.number = 0;
    }

    /**
     * Returns the type of Resource contained in this ExtraDepot Card.
     *
     * @return The type of Resource contained in this ExtraDepot Card.
     */
    public Resource getResourceType() {
        return resource;
    }

    /**
     * Returns the number of Resources contained in this ExtraDepot Card.
     *
     * @return The number of Resources contained in this ExtraDepot Card.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Tries to add the specified amount of Resource, compatible with the Resource that this Card operates with.
     * The type of Resource is unique to this Leader Card.
     * Also notifies observers.
     *
     * @param numAdd The amount of Resource to be add.
     * @return True if the amount of Resources were added successfully, False otherwise.
     * @see #getResourceType()
     */
    public boolean addResource(int numAdd) {
        if (numAdd > 2 || numAdd <= 0) return false;
        if (numAdd == 2 && number != 0) return false;
        if (numAdd == 1 && number == 2) return false;
        number = number + numAdd;
        notifyObservers();
        return true;
    }

    /**
     * Tries to set the specified amount of Resource, compatible with the Resource that this Card operates with.
     * The type of Resource is unique to this Leader Card.
     * Also notifies observers.
     *
     * @param num The new amount of Resource to be set.
     * @return True if the amount of Resources were set successfully, False otherwise.
     * @see #getResourceType()
     */
    public boolean setResource(int num) {
        if (num > 2 || num < 0) return false;
        number = num;
        notifyObservers();
        return true;
    }

    public boolean removeResource(int numRem) {
        if (number == 0) return false;
        if (numRem <= 0 || numRem > 2) return false;
        if (numRem == 2 && number == 1) return false;
        number = number - numRem;
        notifyObservers();
        return true;
    }

    public boolean isProduction() {
        return false;
    }

    public boolean isMarketResource() {
        return false;
    }

    public boolean isExtraDepot() {
        return true;
    }

    public boolean isDiscountResource() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ExtraDepot)) return false;
        ExtraDepot o = (ExtraDepot) obj;
        return (this.resource.equals(o.resource) && this.number == o.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.resource, this.number);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\u001B[31m" + "   SPECIAL ABILITY: " + "\u001B[0m").append("\n");
        result.append("    You have 2 more slots for ").append(resource).append("\n");
        if (number > 0) result.append("    There is/are ").append(number).append("!").append("\n");
        return result.toString();
    }

    private void notifyObservers() {
        this.notifyObservers(generateMessage());
    }

    public MSG_UPD_Extradepot generateMessage() {
        return new MSG_UPD_Extradepot(
                this.resource,
                this.number
        );
    }
}
