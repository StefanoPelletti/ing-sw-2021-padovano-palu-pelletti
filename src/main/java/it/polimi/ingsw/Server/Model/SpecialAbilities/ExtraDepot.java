package it.polimi.ingsw.Server.Model.SpecialAbilities;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MSG_UPD_Market;
import it.polimi.ingsw.Networking.Message.UpdateMessages.PlayerUpdate.MSG_UPD_Extradepot;
import it.polimi.ingsw.Server.Model.Enumerators.Resource;
import it.polimi.ingsw.Server.Utils.ModelObservable;

import java.io.Serializable;

public class ExtraDepot extends ModelObservable implements SpecialAbility, Serializable {
    private Resource resource;
    private int number;

    public ExtraDepot(Resource resource) {
        this.resource = resource;
        this.number = 0;
    }

    public Resource getResourceType() {
        return resource;
    }
    public int getNumber() { return number; }

    public boolean addResource(int numAdd) {
        if ( numAdd > 2 || numAdd <= 0 ) return false;
        if ( numAdd == 2 && number != 0 ) return false;
        if ( numAdd == 1 && number == 2 ) return false;
        number = number+numAdd;
        notifyObservers();
        return true;
    }

    public boolean setResource(int num){
        if ( num > 2 || num < 0 ) return false;
        number=num;
        notifyObservers();
        return true;
    }

    public boolean removeResource(int numRem) {
        if ( number == 0 ) return false;
        if ( numRem <= 0 || numRem > 2 ) return false;
        if ( numRem == 2 && number == 1 ) return false;
        number = number - numRem;
        notifyObservers();
        return true;
    }

    public boolean isProduction()
    {
        return false;
    }
    public boolean isMarketResource()
    {
        return false;
    }
    public boolean isExtraDepot()
    {
        return true;
    }
    public boolean isDiscountResource()
    {
        return false;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj == this) return true;
        if(!(obj instanceof ExtraDepot)) return false;
        ExtraDepot o = (ExtraDepot) obj;
        return (this.resource.equals(o.resource) && this.number==o.number);
    }

    @Override
    public String toString()
    {
        return "  SpecialAbility: \nI have "+getNumber()+" "+getResourceType()+ " in my belly! ";
    }

    private void notifyObservers(){
        this.notifyObservers(generateMessage());
    }

    public MSG_UPD_Extradepot generateMessage()
    {
        return new MSG_UPD_Extradepot(
                this.resource,
                this.number
        );
    }
}
