package it.polimi.ingsw.Server.Model.Middles;


import it.polimi.ingsw.Networking.Message.MSG_ERROR;
import it.polimi.ingsw.Server.Utils.ModelObservable;

import java.io.Serializable;

public class ErrorObject extends ModelObservable  {

    private boolean enabled;
    private String errorMessage;

    public ErrorObject()
    {
        this.enabled=false;
        this.errorMessage = "";
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if(!enabled)
            errorMessage="";
        if(enabled)
            notifyObservers();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private void notifyObservers(){
        this.notifyObservers(new MSG_ERROR(this.errorMessage));
    }
}
