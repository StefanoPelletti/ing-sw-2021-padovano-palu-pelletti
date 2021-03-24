package it.polimi.ingsw.Model;

public class MarketResources extends SpecialAbility {
    private Resource resource;

    public MarketResources(Resource resource) {
        this.resource = resource;
    }

    public Resource getConvertedResource() {
        return resource;
    }

}
