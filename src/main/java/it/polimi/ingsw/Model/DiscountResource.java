package it.polimi.ingsw.Model;

public class DiscountResource extends SpecialAbility {
    private Resource resource;

    public DiscountResource(Resource resource) {
        this.resource = resource;
    }

    public Resource getDiscountedResource() {
        return resource;
    }
}
