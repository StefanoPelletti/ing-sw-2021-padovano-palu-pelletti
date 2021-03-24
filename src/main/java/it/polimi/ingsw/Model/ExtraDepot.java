package it.polimi.ingsw.Model;

public class ExtraDepot extends SpecialAbility {
    private Resource resource;
    private int number;

    public ExtraDepot(Resource resource, int number) {
        this.resource = resource;
        this.number = number;
    }

    public Resource getRestriction() {
        return resource;
    }

    public boolean addResource(int numRes) {
        return true;
    }

    public boolean removeResource(int numRes) {
        return true;
    }

}
