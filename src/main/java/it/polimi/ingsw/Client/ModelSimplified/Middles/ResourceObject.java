package it.polimi.ingsw.Client.ModelSimplified.Middles;

import it.polimi.ingsw.Networking.Message.UpdateMessages.MiddlesUpdate.MSG_UPD_ResourceObject;

public class ResourceObject
{
    private boolean enabled;
    private int numOfResources;

    public boolean isEnabled() {
        return enabled;
    }

    public void update(MSG_UPD_ResourceObject message)
    {
        boolean newEnabled = message.getEnabled();
        int newNumOfResource = message.getNumOfResources();

        this.enabled=newEnabled;
        this.numOfResources=newNumOfResource;
    }

    public int getNumOfResources()
    {
        return this.numOfResources;
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        if(enabled)
        {
            result.append(" Number of resource to get: "+numOfResources).append("\n");
            result.append(" 1:  SHIELD");
            result.append(" 2:  COIN");
            result.append(" 3:  SERVANT");
            result.append(" 4:  STONE");
        }
        else
            result.append(" ResourceObject is not enabled.");
        return result.toString();
    }
}
