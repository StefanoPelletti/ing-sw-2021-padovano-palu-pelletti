package it.polimi.ingsw.Server.Model.Middles;


import java.io.Serializable;

public class ErrorObject implements Serializable {

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
        //notify();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
