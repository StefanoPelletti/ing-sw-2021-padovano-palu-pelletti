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
        result.append("\n");
        result.append("\u001B[36m" + " RESOURCE OBJECT IS HERE TO HELP! " + "\u001B[0m").append("\n");
        if(enabled)
        {
            result.append("  Number of resource to get: ").append(numOfResources).append("\n");
            result.append("   1:  " + "\u001B[34m" + "SHIELD" + "\u001B[0m");
            result.append("   2:  " + "\u001B[33m" + "COIN" + "\u001B[0m");
            result.append("   3:  " + "\u001B[35m" + "SERVANT" + "\u001B[0m");
            result.append("   4:   STONE");
        }
        else
            result.append("  ResourceObject is not enabled.");
        return result.toString();
    }
}
